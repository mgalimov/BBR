package BBRCust;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRUtil;
import BBRAcc.BBRPoS;
import BBRAcc.BBRUser;
import BBRCust.BBRCustReg;

public class BBRSpecialistManager extends BBRDataManager<BBRSpecialist>{
	
	public BBRSpecialistManager() {
		super();
		sessionIndex = BBRCustReg.sessionIndex;
		titleField = "name";
		classTitle = "Specialist";	
	}

	public void createAndStoreSpecialist(String name, String position, BBRUser user, BBRPoS pos) {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);

        BBRSpecialist spec = new BBRSpecialist();
        spec.setName(name);
        spec.setPosition(position);
        spec.setUser(user);
        spec.setPos(pos);
        session.save(spec);

        BBRUtil.commitTran(sessionIndex, tr);
    }
}
