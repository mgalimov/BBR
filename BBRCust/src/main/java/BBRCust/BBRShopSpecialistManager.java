package BBRCust;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRUtil;
import BBRCust.BBRCustReg;

public class BBRShopSpecialistManager extends BBRDataManager<BBRShopSpecialist>{
	
	public BBRShopSpecialistManager() {
		super();
		sessionIndex = BBRCustReg.sessionIndex;
	}

	public void createAndStoreShopSpecialist(String title) {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);

        BBRShopSpecialist spec = new BBRShopSpecialist();
        spec.setTitle(title);
        session.save(spec);

        BBRUtil.commitTran(sessionIndex, tr);
    }
}
