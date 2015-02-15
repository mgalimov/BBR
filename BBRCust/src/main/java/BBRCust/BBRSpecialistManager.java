package BBRCust;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRUtil;
import BBRCust.BBRCustReg;

public class BBRSpecialistManager extends BBRDataManager<BBRSpecialist>{
	
	public BBRSpecialistManager() {
		super();
		sessionIndex = BBRCustReg.sessionIndex;
	}

	public void createAndStoreSpecialist(String name, String position) {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);

        BBRSpecialist spec = new BBRSpecialist();
        spec.setName(name);
        spec.setPosition(position);
        session.save(spec);

        BBRUtil.commitTran(sessionIndex, tr);
    }
}
