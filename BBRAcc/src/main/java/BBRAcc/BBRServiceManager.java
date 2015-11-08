package BBRAcc;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRErrors;
import BBR.BBRUtil;
import BBRAcc.BBRService.BBRServiceStatus;

public class BBRServiceManager extends BBRDataManager<BBRService> {

	public BBRServiceManager() {
		super();
		sessionIndex = BBRAccReg.sessionIndex;
		titleField = "title";
		classTitle = "service";	
	}

	public BBRService createAndStoreService(String title, int status, Boolean demo, Boolean basic) throws Exception {
		boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);

		if (count("demo=true and status=" + BBRServiceStatus.SERVICE_ACTIVE) > 0)
			throw new Exception(BBRErrors.ERR_CANT_CREATE_DUPLICATE_BASIC_OR_DEMO_SERVICE);

		if (count("basic=true and status=" + BBRServiceStatus.SERVICE_ACTIVE) > 0)
			throw new Exception(BBRErrors.ERR_CANT_CREATE_DUPLICATE_BASIC_OR_DEMO_SERVICE);

        BBRService service = new BBRService();
        service.setTitle(title);
        service.setStatus(status);
        service.setDemo(demo);
        service.setBasic(basic);
        session.save(service);

        BBRUtil.commitTran(sessionIndex, tr);
        return service;
    }
  }
