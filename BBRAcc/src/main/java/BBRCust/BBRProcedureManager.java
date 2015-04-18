package BBRCust;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRDataSet;
import BBR.BBRUtil;
import BBRAcc.BBRPoS;
import BBRCust.BBRCustReg;

public class BBRProcedureManager extends BBRDataManager<BBRProcedure>{
	
	public BBRProcedureManager() {
		super();
		sessionIndex = BBRCustReg.sessionIndex;
		classTitle = "Procedure";	
	}

	public void createAndStoreProcedure(String title, BBRPoS pos, float length, float price, String currency, int status) {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);

        BBRProcedure proc = new BBRProcedure();
        proc.setTitle(title);
        proc.setPos(pos);
        proc.setLength(length);
        proc.setPrice(price);
        proc.setCurrency(currency);
        proc.setStatus(status);
        session.save(proc);

        BBRUtil.commitTran(sessionIndex, tr);
    }

	public BBRDataSet<BBRProcedure> list(String query, String titleField, BBRVisit visit) {
		if (visit == null)
			return null;
		
		String where = "visit.id = " + visit.getId();
		return (BBRDataSet<BBRProcedure>)list(query, titleField, where);
	}
	
	@Override
    public String whereShop(Long shopId) {
    	return "pos.shop.id = " + shopId;
    };
}
