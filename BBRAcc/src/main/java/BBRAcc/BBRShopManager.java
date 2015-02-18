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
	}
	public void createAndStoreShop(String title) {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);

        BBRShop shop = new BBRShop();
        shop.setTitle(title);
        session.save(shop);

        BBRUtil.commitTran(sessionIndex, tr);
    }
	
    @SuppressWarnings("unchecked")
	public BBRDataSet<BBRShop> list(BBRUser user, String queryTerm, String sortBy) {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        
        Session session = BBRUtil.getSession(sessionIndex);
   		String orderBy = " order by " + sortBy;
   		String where = "";
   		
   		if (queryTerm != null && !queryTerm.equals("")) {
   			queryTerm.replaceAll(" ", "%");
   			where = " where title like '%" + queryTerm + "%'";
   		}
   			
        Long count = (Long)session.createQuery("Select count(*) from " + typeName + where).uniqueResult();
        Query query = session.createQuery("from " + typeName + where + orderBy);
        query.setMaxResults(maxRowsToReturn);
        
        List<BBRShop> list = query.list();
        BBRUtil.commitTran(sessionIndex, tr);

        return new BBRDataSet<BBRShop>(list, count);
    }
}
