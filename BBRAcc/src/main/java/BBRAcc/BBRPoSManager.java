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
import BBRAcc.BBRPoS;

public class BBRPoSManager extends BBRDataManager<BBRPoS>{
	
	public BBRPoSManager() {
		super();
		classTitle = "Point of service";	
	}
	
	public BBRPoS createAndStorePoS(BBRShop shop, String title, String locationDescription, 
								  BBRGPS locationGPS, Date startWorkHour, Date endWorkHour,
								  String currency, String timeZone, String urlID, String email, String sms) throws Exception {
		boolean tr = BBRUtil.beginTran();
        Session session = BBRUtil.getSession();

        BBRPoS pos = new BBRPoS();
        pos.setShop(shop);
        pos.setTitle(title);
        pos.setLocationDescription(locationDescription);
        pos.setLocationGPS(locationGPS);
        pos.setStartWorkHour(startWorkHour);
        pos.setEndWorkHour(endWorkHour);
        pos.setCurrency(currency);
        pos.setTimeZone(timeZone);
        pos.setUrlID(urlID);
        pos.setEmail(email);
        pos.setSms(sms);

        checkBeforeUpdate(pos);
        session.save(pos);

        BBRUtil.commitTran(tr);
        
        return pos;
    }
	
	@Override
	public boolean checkBeforeUpdate(BBRPoS pos) throws Exception {
		if (pos.getShop() == null)
			throw new Exception(BBRErrors.ERR_SHOP_MUST_BE_SPECIFIED);
		
		BBRPoS foundPoS = findByTitle(pos.getTitle(), pos.getShop()); 
		if (foundPoS != null && foundPoS.getId() != pos.getId())
			throw new Exception(BBRErrors.ERR_TITLE_MUST_BE_UNIQUE);

		foundPoS = findByUrlId(pos.getUrlID());
		if (foundPoS != null && foundPoS.getId() != pos.getId())
			throw new Exception(BBRErrors.ERR_URLID_MUST_BE_UNIQUE);

		return true;
	}
	
	public BBRPoS findByTitle(String title, BBRShop shop) {
        boolean tr = BBRUtil.beginTran();
        BBRPoS result = (BBRPoS) BBRUtil.getSession().createQuery("from BBRPoS as pos where pos.title = '" + title + "' and pos.shop = " + shop.getId()).uniqueResult();
        BBRUtil.commitTran(tr);
        return result;
    }
	
	@SuppressWarnings("unchecked")
	public List<BBRPoS> findByTitleLike(String title) {
        boolean tr = BBRUtil.beginTran();
        title = title.replace(" ", "%").replace("\t", "%").replace("\n", "%");
        List<BBRPoS> list = BBRUtil.getSession().createQuery("from BBRPoS as pos where pos.title like '%" + title + "%'").list();
        BBRUtil.commitTran(tr);
        return list;
    }

	@SuppressWarnings({ "unchecked", "unused" })
	public BBRDataSet<BBRPoS> listLocal(BBRGPS locationGPS, Double radius) {
		if (radius > 100.0 || radius <= 0.5) return null;
		
		if (locationGPS == null) {
			locationGPS = new BBRGPS(37.620842, 55.754113);
			radius = 10000.0;
		}
			
        boolean tr = BBRUtil.beginTran();
        
        Session session = BBRUtil.getSession();
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
        BBRUtil.commitTran(tr);

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
        
	@SuppressWarnings("unchecked")
	public List<Object[]> listSpecialWithShops(BBRShop shop, BBRPoS pos) {
        boolean tr = BBRUtil.beginTran();
        Session session = BBRUtil.getSession();
        
		String where = "";
		if (pos != null)
			where = " where pos.id = " + pos.getId();
		else
			if (shop != null)
				where = " where pos.shop.id = " + shop.getId();
		
		Query query = session.createQuery("select pos.shop.id, pos.shop.title, pos.id, pos.title" + 
										  "  from BBRPoS pos" +  
										  	where + 
										  " order by pos.shop.title, pos.title asc");

		List<Object[]> list = query.list();
		
		BBRUtil.commitTran(tr);

        return list;
	}

	public BBRPoS findByUrlId(String posUrlID) {
        boolean tr = BBRUtil.beginTran();
        BBRPoS result = (BBRPoS) BBRUtil.getSession().createQuery("from BBRPoS as pos where pos.urlID = '" + posUrlID + "'").uniqueResult();
        BBRUtil.commitTran(tr);
        return result;
	}
}
