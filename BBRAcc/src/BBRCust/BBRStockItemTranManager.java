package BBRCust;


import java.util.Date;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRUtil;
import BBRAcc.BBRPoS;

public class BBRStockItemTranManager extends BBRDataManager<BBRStockItemTran> {

	public BBRStockItemTranManager() {
		super();
		titleField = "id";
		classTitle = "StockItemTransaction";	
	}

	public BBRStockItemTran create(BBRStockItem item, BBRPoS pos, BBRSpecialist specialist, 
								   char type, Date date, Float qty) throws Exception {
		if (item == null || pos == null || specialist == null || !isTypeAllowed(type))
			return null;
		
		boolean tr = BBRUtil.beginTran();
        Session session = BBRUtil.getSession();
        
        BBRStockItemTran tran = new BBRStockItemTran();
        tran.setSpecialist(specialist);
        tran.setItem(item);
        tran.setPos(pos);
        tran.setType(type);
        tran.setDate(date);
        session.save(tran);

        BBRUtil.commitTran(tr);
        return tran;
    }
    
	public boolean isTypeAllowed(char type) {
		if (type == 'A' || type == 'S') return true;
		return false;
	}

	public BBRStockItemTran add(BBRStockItem item, BBRPoS pos, BBRSpecialist specialist, Float qty) throws Exception {
		return create(item, pos, specialist, 'A', new Date(), qty);
	}

	public BBRStockItemTran subtract(BBRStockItem item, BBRPoS pos, BBRSpecialist specialist, Float qty) throws Exception {
		return create(item, pos, specialist, 'S', new Date(), qty);
	}
	
    public String whereShop(Long shopId) {
    	return "pos.shop.id = " + shopId;
    };

}