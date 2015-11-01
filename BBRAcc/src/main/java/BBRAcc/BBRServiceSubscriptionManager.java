package BBRAcc;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRErrors;
import BBR.BBRUtil;
import BBRAcc.BBRServiceSubscription.BBRServiceSbscriptionStatuses;

public class BBRServiceSubscriptionManager extends BBRDataManager<BBRServiceSubscription> {

	public BBRServiceSubscriptionManager() {
		super();
		sessionIndex = BBRAccReg.sessionIndex;
		titleField = "id";
		classTitle = "service subscription";	
	}

	public BBRServiceSubscription createAndStoreServiceSubscription(BBRService service, BBRShop shop, String currency,
																	Date startDate, Date endDate) throws Exception {
		if (shop == null) return null;
		
		boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);
        
        SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
        String sd = df.format(startDate);
        String ed = df.format(endDate);
        
		if (count("startDate <= '" + sd + "' and endDate >= '" + ed + "' and status=" + BBRServiceSbscriptionStatuses.SUBSCRIPTION_ACTIVE) > 0)
			throw new Exception(BBRErrors.ERR_CANT_CREATE_DUPLICATE_SERVICE);
		
        BBRServiceSubscription ss = new BBRServiceSubscription();
        ss.setService(service);
        ss.setShop(shop);
        ss.setCurrency(currency);
        ss.setStartDate(startDate);
        ss.setEndDate(endDate);
        ss.setCreditLimit(service.getCreditLimit());
        ss.setBalance(0F);
        ss.setStatus(BBRServiceSbscriptionStatuses.SUBSCRIPTION_REQUESTED);
        session.save(ss);

        BBRUtil.commitTran(sessionIndex, tr);
        return ss;
    }
	
	public Float getBalance(Date date) {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);
        
        SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
        String d = df.format(date);
        
     	String where = " where date <= '" + d + "' and status=" + BBRServiceSbscriptionStatuses.SUBSCRIPTION_ACTIVE;
        
        Float sum = (Float)session.createQuery("Select sum(isNull(amount, 0)) from BBRTransaction " + where).uniqueResult();
        BBRUtil.commitTran(sessionIndex, tr);
 	
        return sum;
	}
  }
