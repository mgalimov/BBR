package BBRCust;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRUtil;

public class BBRStockItemManager extends BBRDataManager<BBRStockItem>{
	
	public BBRStockItemManager() {
		super();
		titleField = "title";
		classTitle = "StockItem";	
	}

	public BBRStockItem create(String title, BBRStockItemGroup group, String description, int state) {
        boolean tr = BBRUtil.beginTran();
        Session session = BBRUtil.getSession();

        BBRStockItem item = new BBRStockItem();
    	item.setTitle(title);
    	item.setGroup(group);
    	item.setDescription(description);
    	item.setState(state);

    	session.save(item);

        BBRUtil.commitTran(tr);
        return item;
    }
}