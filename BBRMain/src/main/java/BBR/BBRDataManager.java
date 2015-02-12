package BBR;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
//http://stackoverflow.com/questions/3403909/get-generic-type-of-class-at-runtime
public class BBRDataManager<T> {
	private final Class<T> type;
	
	public BBRDataManager(Class<T> type) {
        this.type = type;
	}

	public Class<T> getMyType() {
       return this.type;
	}
	
    @SuppressWarnings("unchecked")
	public BBRDataSet<T> list() {
        boolean tr = BBRUtil.beginTran();
        List<T> list = BBRUtil.getSession().createQuery("from BBRUser").list();
        BBRUtil.commitTran(tr);
        return new BBRDataSet<T>(list);
    }

    @SuppressWarnings("unchecked")
	public BBRDataSet<T> list(int pageNumber, int pageSize) {
        boolean tr = BBRUtil.beginTran();
        
        Session session = BBRUtil.getSession();
        Long count = (Long)session.createQuery("Select count(*) from BBRUser u").uniqueResult();
        Query query = session.createQuery("from BBRUser");
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
       		
        T result = (T) BBRUtil.getSession().createQuery("from " + type.getName() + " as user where user.id = '" + id.toString() + "'").uniqueResult();
        BBRUtil.commitTran(tr);
        return result;
    }
}
