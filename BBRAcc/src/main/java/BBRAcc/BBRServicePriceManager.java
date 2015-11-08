package BBRAcc;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.Query;
import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRErrors;
import BBR.BBRUtil;


public class BBRServicePriceManager extends BBRDataManager<BBRServicePrice> {

	public BBRServicePriceManager() {
		super();
		sessionIndex = BBRAccReg.sessionIndex;
		titleField = "id";
		classTitle = "service subscription";	
	}

	public BBRServicePrice createAndStoreServicePrice(BBRService service, String country, Date startDate, Date endDate,
														Float price, String currency, Float creditLimit) throws Exception {
		if (service == null) return null;
		
		boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);
        
        SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
        String sd = df.format(startDate);
        String ed = df.format(endDate);
        
		if (count("service.id = " + service.getId().toString() + " and country='" + country + "' and startDate <= '" + sd + "' and ((endDate is null) or (endDate >= '" + ed + "'))") > 0)
			throw new Exception(BBRErrors.ERR_CANT_CREATE_DUPLICATE_PRICE);
		
        BBRServicePrice sp = new BBRServicePrice();
        sp.setService(service);
        sp.setCurrency(currency);
        sp.setStartDate(startDate);
        sp.setEndDate(endDate);
        sp.setCreditLimit(creditLimit);
        session.save(sp);

        BBRUtil.commitTran(sessionIndex, tr);
        return sp;
    }
	
	public BBRServicePrice getCurrentPrice(BBRService service, String country, Date date) {
		boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);

        SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
        String d = df.format(date);

        Query query = session.createQuery("from " + typeName + " where country = '" + country + "' and startDate <= '" + d + "' and (endDate is null or endDate >='" + d + "'");
        BBRServicePrice price = (BBRServicePrice)query.uniqueResult();
        
        BBRUtil.commitTran(sessionIndex, tr);
		return price;
	}


}
