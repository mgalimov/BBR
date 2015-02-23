package BBRAcc;

import org.hibernate.Session;
import BBR.BBRDataManager;
import BBR.BBRErrors;
import BBR.BBRGPS;
import BBR.BBRUtil;

public class BBRPoSManager extends BBRDataManager<BBRPoS>{
	
	public BBRPoSManager() {
		super();
		sessionIndex = BBRAccReg.sessionIndex;
	}
	
	public void createAndStorePoS(BBRShop shop, String title, String locationDescription, BBRGPS locationGPS) throws Exception {
		if (shop == null)
			throw new Exception(BBRErrors.ERR_SHOP_MUST_BE_SPECIFIED);
		
		if (findByTitle(title, shop) != null)
			throw new Exception(BBRErrors.ERR_TITLE_MUST_BE_UNIQUE);
		
		boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);

        BBRPoS pos = new BBRPoS();
        pos.setShop(shop);
        pos.setTitle(title);
        pos.setLocationDescription(locationDescription);
        pos.setLocationGPS(locationGPS);
        session.save(pos);

        BBRUtil.commitTran(sessionIndex, tr);
    }
	
	public BBRPoS findByTitle(String title, BBRShop shop) {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        BBRPoS result = (BBRPoS) BBRUtil.getSession(sessionIndex).createQuery("from BBRPoS as pos where pos.title = '" + title + "' and pos.shop = " + shop.getId()).uniqueResult();
        BBRUtil.commitTran(sessionIndex, tr);
        return result;
    }

}
