package BBRCust;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRUtil;

public class BBRShopSpecialistManager extends BBRDataManager<BBRShopSpecialist>{
	public void createAndStoreShop(String title) {
        Session session = BBRUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        BBRShopSpecialist theShop = new BBRShopSpecialist();
        theShop.setTitle(title);
        session.save(theShop);

        session.getTransaction().commit();
    }
}
