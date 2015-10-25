package BBRAcc;


import java.util.Date;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRErrors;
import BBR.BBRUtil;

public class BBRTransactionManager extends BBRDataManager<BBRTransaction> {

	public BBRTransactionManager() {
		super();
		sessionIndex = BBRAccReg.sessionIndex;
		titleField = "id";
		classTitle = "transaction";	
	}

	public BBRTransaction createAndStoreTransaction(BBRServiceSubscription serviceSubscription,
											 char type, Date date, Float amount, String currency) throws Exception {
		if (serviceSubscription == null) return null;
		if (serviceSubscription.getShop() == null) return null;
		if (!isTypeAllowed(type)) return null;
		if (!currency.equals(serviceSubscription.getCurrency()))
			throw new Exception(BBRErrors.ERR_CURRENCIES_DONT_MATCH);
		
		boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);
        
        BBRTransaction tran = new BBRTransaction();
        tran.setServiceSubscription(serviceSubscription);
        tran.setType(type);
        tran.setDate(date);
        tran.setCurrency(currency);
        tran.setAmount(amount);
        session.save(tran);

        BBRUtil.commitTran(sessionIndex, tr);
        return tran;
    }
    
	public boolean isTypeAllowed(char type) {
		if (type == 'A' || type == 'S') return true;
		
		return false;
	}

	public BBRTransaction add(BBRServiceSubscription serviceSubscription, Float amount, String currency) throws Exception {
		return createAndStoreTransaction(serviceSubscription, 'A', new Date(), amount, currency);
	}

	public BBRTransaction add(BBRServiceSubscription serviceSubscription, Float amount) throws Exception {
		return add(serviceSubscription, amount, serviceSubscription.getCurrency());
	}
	
	public BBRTransaction subtract(BBRServiceSubscription serviceSubscription, Float amount, String currency) throws Exception {
		return createAndStoreTransaction(serviceSubscription, 'S', new Date(), amount, currency);
	}

	public BBRTransaction subtract(BBRServiceSubscription serviceSubscription, Float amount) throws Exception {
		return subtract(serviceSubscription, amount, serviceSubscription.getCurrency());
	}
}
