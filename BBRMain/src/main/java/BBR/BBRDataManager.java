package BBR;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

//http://stackoverflow.com/questions/3403909/get-generic-type-of-class-at-runtime
public class BBRDataManager<T extends BBRDataElement> {
	protected final String typeName;
	protected final Class<?> type;
	protected int sessionIndex = -1;
	protected final int maxRowsToReturn = -100;
	protected String titleField = "title";
	protected String classTitle = "abstract Data Element";

	public BBRDataManager() {
		type = BBRUtil.getGenericParameterClass(this.getClass(), 0);
		typeName = type.getName();
	}
	 
    @SuppressWarnings("unchecked")
	public BBRDataSet<T> list() {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        List<T> list = BBRUtil.getSession(sessionIndex).createQuery("from " + typeName).list();
        BBRUtil.commitTran(sessionIndex, tr);
        return new BBRDataSet<T>(list);
    }

    
    @SuppressWarnings({ "unchecked", "unused" })
   	public BBRDataSet<T> list(int pageNumber, int pageSize, String where, String orderBy) {
        boolean tr = BBRUtil.beginTran(sessionIndex);
           
       Session session = BBRUtil.getSession(sessionIndex);
       if (orderBy == null)
       	orderBy = "";
       if (orderBy.length() > 0) {
       	orderBy = orderBy.trim();
       	if (!orderBy.startsWith("order by"))
       		orderBy = "order by " + orderBy.trim();
       }
       
       Long count = (Long)session.createQuery("Select count(*) from " + typeName + " " + where).uniqueResult();
       
       Query query = session.createQuery( " from " + typeName + " " + where + " " + orderBy);
       query.setFirstResult(pageNumber * pageSize);
       if (pageSize > maxRowsToReturn && maxRowsToReturn > 0)
       	 pageSize = maxRowsToReturn;
       query.setMaxResults(pageSize);
       List<T> list = query.list();
       BBRDataSet<T> ds = new BBRDataSet<T>(list, count);
       BBRUtil.commitTran(sessionIndex, tr);
	
       return ds;
    }
    
   	public BBRDataSet<T> list(int pageNumber, int pageSize, String orderBy) {
       return list(pageNumber, pageSize, "", orderBy);
    }
   	
	public void delete(T record){
        boolean tr = BBRUtil.beginTran(sessionIndex);
        BBRUtil.getSession(sessionIndex).delete(record);
        BBRUtil.commitTran(sessionIndex, tr);
    }

	public void update(T record) {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        BBRUtil.getSession(sessionIndex).update(record);
        BBRUtil.commitTran(sessionIndex, tr);	
	}

    @SuppressWarnings("unchecked")
	public T findById(Long id) {
    	boolean tr = BBRUtil.beginTran(sessionIndex);
       		
        T result = (T) BBRUtil.getSession(sessionIndex).createQuery("from " + typeName + " as t where t.id = '" + id.toString() + "'").uniqueResult();
        BBRUtil.commitTran(sessionIndex, tr);
        return result;
    }
    
    public int getSessionIndex() {
    	return sessionIndex;
    }
    
    @SuppressWarnings({ "unchecked", "unused" })
	public BBRDataSet<T> list(String queryTerm, String sortBy, String where) {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        
        Session session = BBRUtil.getSession(sessionIndex);
   		String orderBy = " order by " + sortBy;
   		
   		if (queryTerm != null && !queryTerm.equals("")) {
   			queryTerm.replaceAll("\\s", "%");
   			where = titleField + "like '%" + queryTerm + "%' " + where;
   		}
   		
   		if (!where.equals(""))
   			where = " where " + where;
   		
        Long count = (Long)session.createQuery("Select count(*) from " + typeName + where).uniqueResult();
        Query query = session.createQuery("from " + typeName + where + orderBy);
        
        if (maxRowsToReturn > 0)
        	query.setMaxResults(maxRowsToReturn);
        
        List<T> list = query.list();
        BBRUtil.commitTran(sessionIndex, tr);

        return new BBRDataSet<T>(list, count);
    }

	public BBRDataSet<T> list(String queryTerm, String sortBy) {
		return list(queryTerm, sortBy, "");
	}

    
    public String getTitleField() {
    	return titleField;
    }
    
    public String getClassTitle() {
    	return classTitle;
    }
}
