package BBR;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

//http://stackoverflow.com/questions/3403909/get-generic-type-of-class-at-runtime
public class BBRDataManager<T> {
	protected final String typeName;
	protected int sessionIndex = -1;
	protected final int maxRowsToReturn = 100;

	public BBRDataManager() {
		typeName = BBRUtil.getGenericParameterClass(this.getClass(), 0).getName();
	//	get session index in all ascentors!
	}
	 
    @SuppressWarnings("unchecked")
	public BBRDataSet<T> list() {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        List<T> list = BBRUtil.getSession(sessionIndex).createQuery("from " + typeName).list();
        BBRUtil.commitTran(sessionIndex, tr);
        return new BBRDataSet<T>(list);
    }

    @SuppressWarnings("unchecked")
	public BBRDataSet<T> list(int pageNumber, int pageSize, String orderBy) {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        
        Session session = BBRUtil.getSession(sessionIndex);
        if (orderBy == null)
        	orderBy = "";
        if (orderBy != "") {
        	orderBy = orderBy.trim();
        	if (!orderBy.startsWith("order by"))
        		orderBy = "order by " + orderBy.trim();
        }
        Long count = (Long)session.createQuery("Select count(*) from " + typeName).uniqueResult();
        Query query = session.createQuery("from " + typeName + " " + orderBy);
        query.setFirstResult((pageNumber - 1) * pageSize);
        if (pageSize > maxRowsToReturn)
        	pageSize = maxRowsToReturn;
        query.setMaxResults(pageSize);
        List<T> list = query.list();
        BBRDataSet<T> ds = new BBRDataSet<T>(list, count);
        BBRUtil.commitTran(sessionIndex, tr);

        return ds;
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
}
