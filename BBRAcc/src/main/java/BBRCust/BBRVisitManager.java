package BBRCust;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import BBR.BBRDataManager;
import BBR.BBRDataSet;
import BBR.BBRUtil;
import BBRAcc.BBRPoS;
import BBRAcc.BBRUser;
import BBRCust.BBRCustReg;
import BBRCust.BBRVisit.BBRVisitStatus;
import BBRCust.BBRVisit;

public class BBRVisitManager extends BBRDataManager<BBRVisit>{
	private final float minimalLength = (float) 0.5;
	
	public BBRVisitManager() {
		super();
		sessionIndex = BBRCustReg.sessionIndex;
		titleField = "timeScheduled";
		classTitle = "Visit";	
	}

	public String createAndStoreVisit(BBRPoS pos, BBRUser user, Date timeScheduled, BBRProcedure procedure, String userName, String userContacts) {
        try {
			boolean tr = BBRUtil.beginTran(sessionIndex);
	        Session session = BBRUtil.getSession(sessionIndex);
	        BBRVisit visit = new BBRVisit();
	        visit.setPos(pos);
	        visit.setUser(user);
	        visit.setTimeScheduled(timeScheduled);
	        visit.setProcedure(procedure);
	        visit.setUserName(userName);
	        visit.setUserContacts(userContacts);
	        visit.setStatus(BBRVisitStatus.VISSTATUS_INITIALIZED);
	        visit.setLength(procedure.getLength());
	        if (visit.getLength() < minimalLength)
	        	visit.setLength(minimalLength);
	        session.save(visit);
	
	        BBRUtil.commitTran(sessionIndex, tr);
	        return visit.getId().toString();
        } catch (Exception ex) {
        	return null;
        }
    }

	@SuppressWarnings("unused")
	public BBRDataSet<BBRVisit> list(Long userId, int pageNumber, int pageSize, String orderBy) {
   		if (userId == null) {
   			return null;
   		}

		boolean tr = BBRUtil.beginTran(sessionIndex);
        
        Session session = BBRUtil.getSession(sessionIndex);
        if (orderBy == null)
        	orderBy = "";
        if (orderBy.length() > 0) {
        	orderBy = orderBy.trim();
        	if (!orderBy.startsWith("order by"))
        		orderBy = "order by " + orderBy.trim();
        	orderBy = " " + orderBy;
        }

        String where = " where visit.user.id=" + userId.toString() + "";
        //join visit.user user 
        Long count = (Long)session.createQuery("Select count(*) from BBRVisit visit join visit.user user " + where).uniqueResult();
        Query query = session.createQuery("from BBRVisit visit " + where + orderBy);
        
        query.setFirstResult((pageNumber - 1) * pageSize);
        if (pageSize > maxRowsToReturn && maxRowsToReturn > 0)
        	pageSize = maxRowsToReturn;
        query.setMaxResults(pageSize);
        
        @SuppressWarnings("unchecked")
		List<BBRVisit> list = (List<BBRVisit>)query.list();
        BBRDataSet<BBRVisit> ds = new BBRDataSet<BBRVisit>(list, count);
        BBRUtil.commitTran(sessionIndex, tr);
		
        return ds;
	}
	
	public List<Object[]> getSchedule(Date date, String posId, String specialistId) {
		if (posId == null) return null;
		if (posId == "") return null;
		
		boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);
        Date startOfDay = BBRUtil.getStartOfDay(date);
        Date endOfDay = BBRUtil.getEndOfDay(date);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        String select = "select visit.timeScheduled as timeScheduled, visit.length as length";
        String from = " from BBRVisit visit";
        String where = " where timeScheduled >= '" + df.format(startOfDay) + "' and "
        			 + "timeScheduled <= '" + df.format(endOfDay) + "'";
        where = where + " and visit.pos.id = " + posId;
        
        if (specialistId != null)
        	if (!specialistId.equals(""))
        		where = where + " and visit.specialist.id = " + specialistId;
        String orderBy = " order by visit.timeScheduled ASC";
        
        Query query = session.createQuery(select + from + where + orderBy);
        
		@SuppressWarnings("unchecked")
		List<Object[]> list = query.list();
		DateFormat tf = new SimpleDateFormat("HHmm");
		
		for(Object[] line: list) {
			line[0] = tf.format((Date)line[0]);
		}
			
        BBRUtil.commitTran(sessionIndex, tr);
		
		return list;
	}
}
