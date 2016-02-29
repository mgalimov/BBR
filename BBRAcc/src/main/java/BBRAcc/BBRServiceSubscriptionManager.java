package BBRAcc;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRErrors;
import BBR.BBRUtil;
import BBRAcc.BBRServiceSubscription.BBRServiceSubscriptionStatuses;
import BBRAcc.BBRUser.BBRUserRole;
import BBRCust.BBRTaskManager;

public class BBRServiceSubscriptionManager extends BBRDataManager<BBRServiceSubscription> {

	public BBRServiceSubscriptionManager() {
		super();
		sessionIndex = BBRAccReg.sessionIndex;
		titleField = "id";
		classTitle = "service subscription";	
	}

	public BBRServiceSubscription createAndStoreServiceSubscription(BBRService service, BBRShop shop, Date startDate) throws Exception {
		if (shop == null) return null;
		
		boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);
        
        SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
        String sd = df.format(startDate);
        
		if (count("shop.id = " + shop.getId().toString() + " and startDate <= '" + sd + "' and ((endDate is null) or (endDate >= '" + sd + "')) and status=" + BBRServiceSubscriptionStatuses.SUBSCRIPTION_ACTIVE) > 0)
			throw new Exception(BBRErrors.ERR_CANT_CREATE_DUPLICATE_SERVICE);
		
		BBRServicePriceManager spmgr = new BBRServicePriceManager(); 
		BBRServicePrice price = spmgr.getCurrentPrice(service, shop.getCountry(), startDate);
		
		BBRServiceSubscription ss = null;
		
		if (price != null) {
	        ss = new BBRServiceSubscription();
	        ss.setService(service);
	        ss.setShop(shop);
        	ss.setCurrency(price.getCurrency());
    		ss.setCreditLimit(price.getCreditLimit());
	        ss.setStartDate(startDate);
	        ss.setEndDate(null);
	        ss.setBalance(0F);
	        ss.setStatus(BBRServiceSubscriptionStatuses.SUBSCRIPTION_REQUESTED);
	        session.save(ss);
	        
	        BBRTaskManager tmgr = new BBRTaskManager();
	        tmgr.createAndStoreTask("Approve new subscription", null, null, new Date(), new Date(), 
	        		"Approve new subscription", BBRServiceSubscription.class.getName(), ss.getId());	
		} else {
	        BBRUtil.commitTran(sessionIndex, tr);
			throw new Exception(BBRErrors.ERR_NO_PRICE_SPECIFIED);
		}

        BBRUtil.commitTran(sessionIndex, tr);
        return ss;
    }
	
	public Float getBalance(Date date) {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);
        
        SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
        String d = df.format(date);
        
     	String where = " where date <= '" + d + "' and status=" + BBRServiceSubscriptionStatuses.SUBSCRIPTION_ACTIVE;
        
        Float sum = (Float)session.createQuery("Select sum(isNull(amount, 0)) from BBRTransaction " + where).uniqueResult();
        BBRUtil.commitTran(sessionIndex, tr);
 	
        return sum;
	}

	public void cancelSubscriptionByShopAdmin(BBRUser user, BBRServiceSubscription subscr) {
		if (user != null && user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN && subscr != null) {
			subscr.setStatus(BBRServiceSubscriptionStatuses.SUBSCRIPTION_CANCELLED);
			subscr.setEndDate(new Date());
			update(subscr);
		}
	}
  }
