package BBRCust;

import java.util.Date;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRUtil;
import BBRCust.BBRCustReg;
import BBRCust.BBRVisit.BBRVisitStatus;

public class BBRVisitManager extends BBRDataManager<BBRVisit>{
	
	public BBRVisitManager() {
		super();
		sessionIndex = BBRCustReg.sessionIndex;
	}

	public void createAndStoreVisit(Long posId, Date timeScheduled, String procedure, String userName, String userContacts, Long userId) {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);

        BBRVisit visit = new BBRVisit();
        visit.setPosId(posId);
        visit.setTimeScheduled(timeScheduled);
        visit.setProcedure(procedure);
        visit.setUserName(userName);
        visit.setUserContacts(userContacts);
        visit.setUserId(userId);
        visit.setStatus(BBRVisitStatus.VISSTATUS_INITIALIZED);
        session.save(visit);

        BBRUtil.commitTran(sessionIndex, tr);
    }
}
