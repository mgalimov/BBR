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
		titleField = "id";
		classTitle = "service subscription";	
	}

	public BBRServicePrice create(BBRService service, String country, Date startDate, Date endDate,
														Float price, String currency, Float creditLimit) throws Exception {
		if (service == null) return null;
        if (startDate == null) return null;
        if (endDate != null && startDate.after(endDate))
        	throw new Exception(BBRErrors.ERR_WRONG_INPUT_FORMAT);
        
        if (country == null) country = "";
        if (currency == null) currency = "";
        if (price == null) price = 0F;
        if (creditLimit == null) creditLimit = 0F;
		
		boolean tr = BBRUtil.beginTran();
        Session session = BBRUtil.getSession();
        
        SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
        String sd = df.format(startDate);
        String wed = "";
        if (endDate != null)
        	wed = " and (startDate <= '" + df.format(endDate) + "'))";  
        
        
		if (count("service.id = " + service.getId().toString() + " and country='" + country + "' and ((endDate is null) or (endDate >= '" + sd + "'))" + wed) > 0) {
			BBRUtil.rollbackTran(tr);
			throw new Exception(BBRErrors.ERR_CANT_CREATE_DUPLICATE_PRICE);
		}
		
        BBRServicePrice sp = new BBRServicePrice();
        sp.setService(service);
        sp.setCountry(country);
        sp.setCurrency(currency);
        sp.setStartDate(startDate);
        sp.setEndDate(endDate);
        sp.setCreditLimit(creditLimit);
        sp.setPrice(price);
        session.save(sp);

        BBRUtil.commitTran(tr);
        return sp;
    }
	
	public BBRServicePrice getCurrentPrice(BBRService service, String country, Date date) {
		boolean tr = BBRUtil.beginTran();
        Session session = BBRUtil.getSession();

        SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
        String d = df.format(date);

        Query query = session.createQuery("from " + typeName + " where country = '" + country + "' and startDate <= '" + d + "' and ((endDate is null) or endDate >='" + d + "')");
        BBRServicePrice price = (BBRServicePrice)query.uniqueResult();
        
        BBRUtil.commitTran(tr);
		return price;
	}


}
