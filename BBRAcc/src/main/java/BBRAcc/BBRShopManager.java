package BBRAcc;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRUtil;

public class BBRShopManager extends BBRDataManager<BBRShop>{
	
	public BBRShopManager() {
		super();
		sessionIndex = BBRAccReg.sessionIndex;
	}
	public void createAndStoreShop(String title) {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);

        BBRShop shop = new BBRShop();
        shop.setTitle(title);
        session.save(shop);

        BBRUtil.commitTran(sessionIndex, tr);
    }
}
