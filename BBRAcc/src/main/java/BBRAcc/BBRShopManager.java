package BBRAcc;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRDataSet;
import BBR.BBRUtil;

public class BBRShopManager extends BBRDataManager<BBRShop>{
	
	public BBRShopManager() {
		super();
		sessionIndex = BBRAccReg.sessionIndex;
		classTitle = "Shop";	
	}
	
	public void createAndStoreShop(String title, String country) {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);

        BBRShop shop = new BBRShop();
        shop.setTitle(title);
        shop.setCountry(country);
        session.save(shop);

        BBRUtil.commitTran(sessionIndex, tr);
    }
	
    @SuppressWarnings("unchecked")
	public BBRDataSet<BBRShop> list(BBRUser user, String queryTerm, String sortBy, BBRShop shop) {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        
        Session session = BBRUtil.getSession(sessionIndex);
   		String orderBy = " order by " + sortBy;
   		String where = "";
   		
   		if (queryTerm != null && !queryTerm.equals("")) {
   			queryTerm.replaceAll("\\s", "%");
   			where = " where title like '%" + queryTerm + "%'";
   		}
   		
   		if (shop != null) {
   			if (where.equals(""))
   				where = " where";
   			else
   				where += " and";	
   			where += " id = " + shop.getId();		
   		}
        Long count = (Long)session.createQuery("Select count(*) from " + typeName + where).uniqueResult();
        Query query = session.createQuery("from " + typeName + where + orderBy);
        query.setMaxResults(maxRowsToReturn);
        
        List<BBRShop> list = query.list();
        BBRUtil.commitTran(sessionIndex, tr);

        return new BBRDataSet<BBRShop>(list, count);
    }
    
    @Override
    public String wherePos(Long posId) {
    	BBRPoSManager mgr = new BBRPoSManager();
    	BBRPoS pos = mgr.findById(posId);
    	if (pos != null)
    		if (pos.getShop() != null)
    			return "id = " + pos.getShop().getId();
    	
    	return "1=0";
    };
    
    @Override
    public String whereShop(Long shopId) {
    	return "id = " + shopId;
    };
    
}
