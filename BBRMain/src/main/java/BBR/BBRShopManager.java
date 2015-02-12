package BBR;

import org.hibernate.Query;
import org.hibernate.Session;

import java.util.*;

public class BBRShopManager extends BBRDataManager<BBRShop>{
	public BBRShopManager(Class<BBRShop> type) {
		super(type);
	}

	public void createAndStoreShop(String title) {
        Session session = BBRUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        BBRShop theShop = new BBRShop();
        theShop.setTitle(title);
        session.save(theShop);

        session.getTransaction().commit();
    }
    
    @SuppressWarnings("unchecked")
	public BBRDataSet<BBRShop> listShops() {
        boolean tr = BBRUtil.beginTran();
        
        Session session = BBRUtil.getSession();
        session.beginTransaction();
        List<BBRShop> list = session.createQuery("from BBRShop").list();
        BBRUtil.commitTran(tr);

        return new BBRDataSet<BBRShop>(list);
    }

    @SuppressWarnings("unchecked")
	public BBRDataSet<BBRShop> listShops(int pageNumber, int pageSize) {
        boolean tr = BBRUtil.beginTran();
        
        Session session = BBRUtil.getSession();
        Long count = (Long)session.createQuery("Select count(*) from BBRShop s").uniqueResult();
        Query query = session.createQuery("from BBRShop");
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        List<BBRShop> list = query.list();
        BBRUtil.commitTran(tr);

        return new BBRDataSet<BBRShop>(list, count);
    }
    
	public BBRShop findShopById(Long id) {
        boolean tr = BBRUtil.beginTran();
        BBRShop result = (BBRShop) BBRUtil.getSession().createQuery("from BBRShop as shop where shop.id = '" + id.toString() + "'").uniqueResult();
        BBRUtil.commitTran(tr);
        return result;
    }
	
	public void deleteShop(BBRShop shop){
        boolean tr = BBRUtil.beginTran();
        BBRUtil.getSession().delete(shop);
        BBRUtil.commitTran(tr);
    }

	public void updateShop(BBRShop shop) {
        boolean tr = BBRUtil.beginTran();
        BBRUtil.getSession().update(shop);
        BBRUtil.commitTran(tr);	
	}
}
