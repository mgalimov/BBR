package BBRAcc;


import java.util.Date;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRErrors;
import BBR.BBRUtil;

public class BBRTransactionManager extends BBRDataManager<BBRTransaction> {

	public BBRTransactionManager() {
		super();
		titleField = "id";
		classTitle = "transaction";	
	}

	public BBRTransaction create(BBRServiceSubscription serviceSubscription,
											 char type, Date date, Float amount, String currency) throws Exception {
		if (serviceSubscription == null) return null;
		if (serviceSubscription.getShop() == null) return null;
		if (!isTypeAllowed(type)) return null;
		if (!currency.equals(serviceSubscription.getCurrency()))
			throw new Exception(BBRErrors.ERR_CURRENCIES_DONT_MATCH);
		
		boolean tr = BBRUtil.beginTran();
        Session session = BBRUtil.getSession();
        
        BBRTransaction tran = new BBRTransaction();
        tran.setServiceSubscription(serviceSubscription);
        tran.setType(type);
        tran.setDate(date);
        tran.setCurrency(currency);
        tran.setAmount(amount);
        session.save(tran);

        BBRUtil.commitTran(tr);
        return tran;
    }
    
	public boolean isTypeAllowed(char type) {
		if (type == 'A' || type == 'S') return true;
		
		return false;
	}

	public BBRTransaction add(BBRServiceSubscription serviceSubscription, Float amount, String currency) throws Exception {
		return create(serviceSubscription, 'A', new Date(), amount, currency);
	}

	public BBRTransaction add(BBRServiceSubscription serviceSubscription, Float amount) throws Exception {
		return add(serviceSubscription, amount, serviceSubscription.getCurrency());
	}
	
	public BBRTransaction subtract(BBRServiceSubscription serviceSubscription, Float amount, String currency) throws Exception {
		return create(serviceSubscription, 'S', new Date(), amount, currency);
	}

	public BBRTransaction subtract(BBRServiceSubscription serviceSubscription, Float amount) throws Exception {
		return subtract(serviceSubscription, amount, serviceSubscription.getCurrency());
	}
}
