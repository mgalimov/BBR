package BBRAcc;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRDataSet;
import BBR.BBRErrors;
import BBR.BBRGPS;
import BBR.BBRUtil;

public class BBRPoSManager extends BBRDataManager<BBRPoS>{
	
	public BBRPoSManager() {
		super();
		sessionIndex = BBRAccReg.sessionIndex;
		classTitle = "Point of service";	
	}
	
	public void createAndStorePoS(BBRShop shop, String title, String locationDescription, BBRGPS locationGPS, Date startWorkHour, Date endWorkHour) throws Exception {
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
        pos.setStartWorkHour(startWorkHour);
        pos.setEndWorkHour(endWorkHour);
        session.save(pos);

        BBRUtil.commitTran(sessionIndex, tr);
    }
	
	public BBRPoS findByTitle(String title, BBRShop shop) {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        BBRPoS result = (BBRPoS) BBRUtil.getSession(sessionIndex).createQuery("from BBRPoS as pos where pos.title = '" + title + "' and pos.shop = " + shop.getId()).uniqueResult();
        BBRUtil.commitTran(sessionIndex, tr);
        return result;
    }

	@SuppressWarnings({ "unchecked", "unused" })
	public BBRDataSet<BBRPoS> listLocal(BBRGPS locationGPS, Double radius) {
		//TODO: throw Exceptions
		if (radius > 100.0 || radius <= 0.5) return null;
		
		if (locationGPS == null) {
			locationGPS = new BBRGPS(37.620842, 55.754113);
			radius = 10000.0;
		}
			
        boolean tr = BBRUtil.beginTran(sessionIndex);
        
        Session session = BBRUtil.getSession(sessionIndex);
 		String where = " where (" 
 							+ "(locationGPS.lat - " + locationGPS.lat + ")*(locationGPS.lat - " + locationGPS.lat + ")"
 							+ "+"
 							+ "(locationGPS.lng - " + locationGPS.lng + ")*(locationGPS.lng - " + locationGPS.lng + ")"
 							+ ") <= " + radius;

 		Long count = (Long)session.createQuery("Select count(*) from " + typeName + where).uniqueResult();
        Query query = session.createQuery("from " + typeName + where);
        
        if (maxRowsToReturn > 0)
        	query.setMaxResults(maxRowsToReturn);
        
        List<BBRPoS> list = query.list();
        BBRUtil.commitTran(sessionIndex, tr);

        return new BBRDataSet<BBRPoS>(list, count);
	}
	
	@Override
    public String wherePos(Long posId) {
    	return "id = " + posId;
    };
    
    @Override
    public String whereShop(Long shopId) {
    	return "shop.id = " + shopId;
    };
}
