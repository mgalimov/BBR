package BBRCust;

import java.util.Calendar;
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
	
	@SuppressWarnings("unused")
	public BBRDataSet<BBRVisit> getSchedule(Date date, String specialistId) {
		boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);

        String where = "visit.timeScheduled >= '" + BBRUtil.getStartOfDay(date).toString() + "' and "
        			 + "visit.timeScheduled <= '" + BBRUtil.getEndOfDay(date).toString() + "'";
        if (!specialistId.equals(""))
         where = where + " and visit.procedure.id = " + specialistId;
        
        String orderBy = "visit.timeScheduled ASC";
        
        Long count = (Long)session.createQuery("Select count(*) from BBRVisit visit " + where).uniqueResult();
        Query query = session.createQuery("from BBRVisit visit " + where + orderBy);
        
		@SuppressWarnings("unchecked")
		List<BBRVisit> list = (List<BBRVisit>)query.list();
        BBRDataSet<BBRVisit> ds = new BBRDataSet<BBRVisit>(list, count);
        BBRUtil.commitTran(sessionIndex, tr);
		
		return null;
	}
}
