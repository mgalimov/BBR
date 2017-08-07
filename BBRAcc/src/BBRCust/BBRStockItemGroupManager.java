package BBRCust;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRUtil;
import BBRAcc.BBRShop;

public class BBRStockItemGroupManager extends BBRDataManager<BBRStockItemGroup>{
	
	public BBRStockItemGroupManager() {
		super();
		titleField = "title";
		classTitle = "StockItemGroup";	
	}

	public BBRStockItemGroup create(String title, BBRShop shop, String description) {
        boolean tr = BBRUtil.beginTran();
        Session session = BBRUtil.getSession();

        BBRStockItemGroup group = new BBRStockItemGroup();
    	group.setTitle(title);
    	group.setDescription(description);
    	group.setShop(shop);

    	session.save(group);

        BBRUtil.commitTran(tr);
        return group;
    }
}
