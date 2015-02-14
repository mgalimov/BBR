package BBR;


import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

//http://stackoverflow.com/questions/3403909/get-generic-type-of-class-at-runtime
public class BBRDataManager<T> {
	private final String typeName;

	public BBRDataManager() {
		typeName = BBRUtil.getGenericParameterClass(this.getClass(), 0).getName();
	}
	 
    @SuppressWarnings("unchecked")
	public BBRDataSet<T> list() {
        boolean tr = BBRUtil.beginTran();
        List<T> list = BBRUtil.getSession().createQuery("from " + typeName).list();
        BBRUtil.commitTran(tr);
        return new BBRDataSet<T>(list);
    }

    @SuppressWarnings("unchecked")
	public BBRDataSet<T> list(int pageNumber, int pageSize, String orderBy) {
        boolean tr = BBRUtil.beginTran();
        
        Session session = BBRUtil.getSession();
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
        query.setMaxResults(pageSize);
        List<T> list = query.list();
        BBRUtil.commitTran(tr);

        return new BBRDataSet<T>(list, count);
    }

	public void delete(T record){
        boolean tr = BBRUtil.beginTran();
        BBRUtil.getSession().delete(record);
        BBRUtil.commitTran(tr);
    }

	public void update(T record) {
        boolean tr = BBRUtil.beginTran();
        BBRUtil.getSession().update(record);
        BBRUtil.commitTran(tr);	
	}

    @SuppressWarnings("unchecked")
	public T findById(Long id) {
        boolean tr = BBRUtil.beginTran();
       		
        T result = (T) BBRUtil.getSession().createQuery("from " + typeName + " as t where t.id = '" + id.toString() + "'").uniqueResult();
        BBRUtil.commitTran(tr);
        return result;
    }
}
