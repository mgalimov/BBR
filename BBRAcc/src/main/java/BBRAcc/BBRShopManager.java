package BBRAcc;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRUtil;

public class BBRShopManager extends BBRDataManager<BBRShop>{
	public void createAndStoreShop(String title) {
        Session session = BBRUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        BBRShop theShop = new BBRShop();
        theShop.setTitle(title);
        session.save(theShop);

        session.getTransaction().commit();
    }
}
