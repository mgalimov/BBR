package BBRCust;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRUtil;
import BBRCust.BBRCustReg;

public class BBRTurnManager extends BBRDataManager<BBRTurn>{
	public BBRTurnManager() {
		super();
		sessionIndex = BBRCustReg.sessionIndex;
		titleField = "title";
		classTitle = "Turn";	
	}

	public String createAndStoreTurn(BBRSpecialist specialist, Date startTime, Date endTime) {
        try {
	        if (checkNoCross(specialist, startTime, endTime, null)) {
				boolean tr = BBRUtil.beginTran(sessionIndex);
		        Session session = BBRUtil.getSession(sessionIndex);
		        
		        SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat);

		        BBRTurn turn = new BBRTurn();
		        turn.setSpecialist(specialist);
		        turn.setStartTime(startTime);
		        turn.setEndTime(endTime);
		        turn.setTitle(specialist.getName() + ": " + df.format(startTime) + " - " + df.format(endTime));
		        session.save(turn);
		        
		        BBRUtil.commitTran(sessionIndex, tr);
		        return turn.getId().toString();
	        } else 
	        	return null;
        } catch (Exception ex) {
        	return null;
        }
    }

	public boolean checkNoCross(BBRSpecialist spec, Date startTime, Date endTime, Long currentId) {
		if (spec == null || startTime == null || endTime == null)
			return false;
		
		boolean tr = BBRUtil.beginTran(sessionIndex);
        
		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat); 
		
		Session session = BBRUtil.getSession(sessionIndex);
		String where = "where spec.id = " + spec.getId() + 
						" and startTime <= '" + df.format(endTime) + "'" + 
						" and endTime >= '" + df.format(startTime) + "'" ;
		
		if (currentId != null)
			where += " and id <> " + currentId; 
			
		String qry = "select count(*) from BBRTurn " + where;

		Long count = (Long)session.createQuery(qry).uniqueResult();
		BBRUtil.commitTran(sessionIndex, tr);
       
		return (count > 0);
	}
	
	@Override
    public String wherePos(Long posId) {
    	return "specialist.pos.id = " + posId;
    };
    
	@Override
    public String whereShop(Long shopId) {
    	return "specialist.pos.shop.id = " + shopId;
    };
}
