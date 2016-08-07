package BBR;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

//http://stackoverflow.com/questions/3403909/get-generic-type-of-class-at-runtime
public class BBRDataManager<T extends BBRDataElement> {
	protected final String typeName;
	protected final Class<?> type;
	protected final int maxRowsToReturn = -100;
	protected String titleField = "title";
	protected String classTitle = "abstract Data Element";

	public BBRDataManager() {
		type = BBRUtil.getGenericParameterClass(this.getClass(), 0);
		typeName = type.getName();
	}
	 
    @SuppressWarnings("unchecked")
	public BBRDataSet<T> list() {
        boolean tr = BBRUtil.beginTran();
        List<T> list = BBRUtil.getSession().createQuery("from " + typeName).list();
        BBRUtil.commitTran(tr);
        return new BBRDataSet<T>(list);
    }

    
    @SuppressWarnings({ "unchecked", "unused" })
   	public BBRDataSet<T> list(int pageNumber, int pageSize, String where, String orderBy) {
        boolean tr = BBRUtil.beginTran();
           
       Session session = BBRUtil.getSession();
       if (orderBy == null)
       	orderBy = "";
       if (orderBy.length() > 0) {
       	orderBy = orderBy.trim();
       	if (!orderBy.startsWith("order by"))
       		orderBy = "order by " + orderBy.trim();
       }
       
       if (!where.equals("") && !where.trim().startsWith("where"))
    	   where = " where " + where;
       
       List<T> list;
       Long count = (Long)session.createQuery("Select count(*) from " + typeName + " " + where).uniqueResult();
       
       if (count == null) {
    	   list = null;
    	   count = 0L;
       }  else { 
	       Query query = session.createQuery( " from " + typeName + " " + where + " " + orderBy);
	       
	       if (pageNumber >= 0) {
	    	   query.setFirstResult(pageNumber * pageSize);
	       
	    	   if (pageSize > maxRowsToReturn && maxRowsToReturn > 0)
	    		   pageSize = maxRowsToReturn;
	           query.setMaxResults(pageSize);
	       }
	       
	       list = query.list();
       }
       BBRDataSet<T> ds = new BBRDataSet<T>(list, count);
       BBRUtil.commitTran(tr);
	
       return ds;
    }

   	public Long count(String where) {
       boolean tr = BBRUtil.beginTran();
           
       Session session = BBRUtil.getSession();
       
       if (!where.equals("") && !where.trim().startsWith("where"))
    	   where = " where " + where;
       
       Long count = (Long)session.createQuery("Select count(*) from " + typeName + " " + where).uniqueResult();
       BBRUtil.commitTran(tr);
	
       return count;
    }
   	
   	public BBRDataSet<T> list(int pageNumber, int pageSize, String orderBy) {
       return list(pageNumber, pageSize, "", orderBy);
    }
   	
	public void delete(T record) throws Exception {
        boolean tr = BBRUtil.beginTran();
        try {
        	BBRUtil.getSession().delete(record);
            BBRUtil.commitTran(tr);
        } catch (Exception ex) {
        	BBRUtil.rollbackTran(tr);
        	throw ex;
        }
    }

	public void update(T record) throws Exception {
		checkBeforeUpdate(record);
        boolean tr = BBRUtil.beginTran();
        try {
        	BBRUtil.getSession().update(record);
        	BBRUtil.commitTran(tr);	
        } catch (Exception ex) {
        	BBRUtil.rollbackTran(tr);
        	throw ex;
        }
	}

	public boolean checkBeforeUpdate(T record) throws Exception {
		return true;
	}
	
    @SuppressWarnings("unchecked")
	public T findById(Long id) {
    	boolean tr = BBRUtil.beginTran();
       	Session session = BBRUtil.getSession(); 	
        T result = (T) session.createQuery("from " + typeName + " as t where t.id = '" + id.toString() + "'").uniqueResult();
        BBRUtil.commitTran(tr);
        return result;
    }
    
    @SuppressWarnings({ "unchecked", "unused" })
	public BBRDataSet<T> list(String queryTerm, String sortBy, String where) {
        boolean tr = BBRUtil.beginTran();
        
        Session session = BBRUtil.getSession();
   		String orderBy = " order by " + sortBy;
   		
   		if (queryTerm != null && !queryTerm.equals("")) {
   			queryTerm.replaceAll("\\s", "%");
   			where = titleField + "like '%" + queryTerm + "%' " + where;
   		}
   		
        if (!where.equals("") && !where.trim().startsWith("where"))
     	   where = " where " + where;
   		
        Long count = (Long)session.createQuery("Select count(*) from " + typeName + where).uniqueResult();
        Query query = session.createQuery("from " + typeName + where + orderBy);
        
        if (maxRowsToReturn > 0)
        	query.setMaxResults(maxRowsToReturn);
        
        List<T> list = query.list();
        BBRUtil.commitTran(tr);

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
    
    public String wherePos(Long posId) {
    	return "pos.id = " + posId;
    };
    
    public String whereShop(Long shopId) {
    	return "shop.id = " + shopId;
    };
}
