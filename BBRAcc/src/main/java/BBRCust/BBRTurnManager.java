package BBRCust;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRUtil;

public class BBRTurnManager extends BBRDataManager<BBRTurn>{
	public BBRTurnManager() {
		super();
		titleField = "title";
		classTitle = "Turn";	
	}

	public String createAndStoreTurn(BBRSpecialist specialist, Date date, Date startTime, Date endTime) {
        try {
        	BBRTurn turn = new BBRTurn();
        	turn.setId(null);
        	turn = setTurnFields(turn, specialist, date, startTime, endTime);
	        if (turn != null) {
				boolean tr = BBRUtil.beginTran();
		        Session session = BBRUtil.getSession();
		        session.save(turn);
		        BBRUtil.commitTran(tr);
		        return turn.getId().toString();
	        } else 
	        	return null;
        } catch (Exception ex) {
        	return null;
        }
    }
	
	public BBRTurn setTurnFields(BBRTurn turn, BBRSpecialist specialist, Date date, Date startTime, Date endTime) {
        try {
        	Calendar cd = Calendar.getInstance();
        	cd.setTime(date);
            int year = cd.get(Calendar.YEAR);
            int month = cd.get(Calendar.MONTH);
            int day = cd.get(Calendar.DATE);
            
        	Calendar cs = Calendar.getInstance();
        	Calendar ce = Calendar.getInstance();
        	cs.setTime(startTime);
        	ce.setTime(endTime);
        	cs.set(year, month, day);
        	ce.set(year, month, day);
        	
        	startTime = cs.getTime();
        	endTime = ce.getTime();
        	
	        if (checkNoCross(specialist, startTime, endTime, turn.getId())) {
		        SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat);
		        turn.setDate(date);
		        turn.setSpecialist(specialist);
		        turn.setStartTime(startTime);
		        turn.setEndTime(endTime);
		        turn.setTitle(specialist.getName() + ": " + df.format(startTime) + " - " + df.format(endTime));
		        return turn;
	        } else 
	        	return null;
        } catch (Exception ex) {
        	return null;
        }
    }

	public boolean checkNoCross(BBRSpecialist spec, Date startTime, Date endTime, Long currentId) {
		if (spec == null || startTime == null || endTime == null)
			return false;
		
		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat); 
		
		boolean tr = BBRUtil.beginTran();
		Session session = BBRUtil.getSession();
		String where = " where specialist.id = " + spec.getId() + 
						" and startTime <= '" + df.format(endTime) + "'" + 
						" and endTime >= '" + df.format(startTime) + "'" ;
		
		if (currentId != null)
			where += " and id <> " + currentId; 
			
		String qry = "select count(*) from BBRTurn" + where;

		Long count = (Long)session.createQuery(qry).uniqueResult();
		BBRUtil.commitTran(tr);
       
		return (count == 0);
	}
	
	@Override
    public String wherePos(Long posId) {
    	return "specialist.pos.id = " + posId;
    };
    
	@Override
    public String whereShop(Long shopId) {
    	return "specialist.pos.shop.id = " + shopId;
    };
    
    public BBRTurn guessNextTurn(BBRSpecialist spec) {
    	if (spec == null) return null;
    	
		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat); 
    	
    	BBRTurn turn = new BBRTurn(); 
    			
		boolean tr = BBRUtil.beginTran();
		Session session = BBRUtil.getSession();
		String where = " where specialist.id = " + spec.getId() + 
						" and date >= '" + df.format(new Date()) + "'" ;
		String qry = "select max(date) from BBRTurn" + where;
		Date dt = (Date)session.createQuery(qry).uniqueResult();
		BBRUtil.commitTran(tr);
		
		if (dt == null)
			dt = new Date();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		turn.setDate(calendar.getTime());
		
		calendar.setTime(spec.getStartWorkHour());
		turn.setStartTime(calendar.getTime());
		
		calendar.setTime(spec.getEndWorkHour());
		turn.setEndTime(calendar.getTime());
		
		turn.setSpecialist(spec);

		return turn;
    }
}
