package BBRAcc;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRUtil;

public class BBRServiceManager extends BBRDataManager<BBRService> {

	public BBRServiceManager() {
		super();
		sessionIndex = BBRAccReg.sessionIndex;
		titleField = "title";
		classTitle = "service";	
	}

	public BBRService createAndStoreService(String title, int status, Float price, String currency, Float creditLimit) throws Exception {
		boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);
        
        BBRService service = new BBRService();
        service.setTitle(title);
        service.setStatus(status);
        service.setPrice(price);
        service.setCurrency(currency);
        service.setCreditLimit(creditLimit);
        session.save(service);

        BBRUtil.commitTran(sessionIndex, tr);
        return service;
    }
  }
