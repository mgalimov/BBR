package BBRCust;

import org.hibernate.Session;
import BBR.BBRDataManager;
import BBR.BBRUtil;
import BBRCust.BBRCustReg;

public class BBRProcedureManager extends BBRDataManager<BBRProcedure>{
	
	public BBRProcedureManager() {
		super();
		sessionIndex = BBRCustReg.sessionIndex;
	}

	public void createAndStoreProcedure(String title, Long posId, String posTitle, float length, float price, String currency, int status) {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);

        BBRProcedure proc = new BBRProcedure();
        proc.setTitle(title);
        proc.setPosId(posId);
        proc.setPosTitle(posTitle);
        proc.setLength(length);
        proc.setPrice(price);
        proc.setCurrency(currency);
        proc.setStatus(status);
        session.save(proc);

        BBRUtil.commitTran(sessionIndex, tr);
    }
}
