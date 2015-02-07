package BBR;

import org.hibernate.Session;

import java.util.*;

public class BBRShopManager {

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
        Session session = BBRUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<BBRShop> list = session.createQuery("from BBRShop").list();
        session.getTransaction().commit();
        return new BBRDataSet<BBRShop>(list);
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
