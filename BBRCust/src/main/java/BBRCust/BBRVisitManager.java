package BBRCust;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import BBR.BBRDataManager;
import BBR.BBRDataSet;
import BBR.BBRUtil;
import BBRCust.BBRCustReg;
import BBRCust.BBRVisit.BBRVisitStatus;

public class BBRVisitManager extends BBRDataManager<BBRVisit>{
	
	public BBRVisitManager() {
		super();
		sessionIndex = BBRCustReg.sessionIndex;
	}

	public String createAndStoreVisit(Long posId, String posTitle, Date timeScheduled, String procedure, String userName, String userContacts, Long userId) {
        try {
			boolean tr = BBRUtil.beginTran(sessionIndex);
	        Session session = BBRUtil.getSession(sessionIndex);
	
	        BBRVisit visit = new BBRVisit();
	        visit.setPosId(posId);
	        visit.setPosTitle(posTitle);
	        visit.setTimeScheduled(timeScheduled);
	        visit.setProcedure(procedure);
	        visit.setUserName(userName);
	        visit.setUserContacts(userContacts);
	        visit.setUserId(userId);
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

        String where = " where userId=" + userId.toString() + "";
        
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
