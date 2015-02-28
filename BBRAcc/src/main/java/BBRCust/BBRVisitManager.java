package BBRCust;

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

        String where = " where user.id=" + userId.toString() + "";
        
        Long count = (Long)session.createQuery("Select count(*) from " + typeName + where).uniqueResult();
        Query query = session.createQuery("from " + typeName + where + orderBy);
        
        query.setFirstResult((pageNumber - 1) * pageSize);
        if (pageSize > maxRowsToReturn)
        	pageSize = maxRowsToReturn;
        query.setMaxResults(pageSize);
        
        @SuppressWarnings("unchecked")
		List<BBRVisit> list = (List<BBRVisit>)query.list();
        BBRDataSet<BBRVisit> ds = new BBRDataSet<BBRVisit>(list, count);
        BBRUtil.commitTran(sessionIndex, tr);
		
        return ds;
	}
}
