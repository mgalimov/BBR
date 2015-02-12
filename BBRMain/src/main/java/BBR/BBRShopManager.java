package BBR;

import org.hibernate.Session;

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
}
