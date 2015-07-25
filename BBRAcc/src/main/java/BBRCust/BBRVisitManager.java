package BBRCust;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import BBR.BBRDataElement;
import BBR.BBRDataManager;
import BBR.BBRDataSet;
import BBR.BBRUtil;
import BBRAcc.BBRPoS;
import BBRAcc.BBRShop;
import BBRAcc.BBRUser;
import BBRCust.BBRCustReg;
import BBRCust.BBRSpecialist.BBRSpecialistState;
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

	public String createAndStoreVisit(BBRPoS pos, BBRUser user, Date timeScheduled, BBRProcedure procedure, BBRSpecialist spec, String userName, String userContacts) {
        try {
			boolean tr = BBRUtil.beginTran(sessionIndex);
	        Session session = BBRUtil.getSession(sessionIndex);
	        BBRVisit visit = new BBRVisit();
	        visit.setPos(pos);
	        visit.setUser(user);
	        visit.setTimeScheduled(timeScheduled);
	        visit.setProcedure(procedure);
	        visit.setSpec(spec);
	        visit.setUserName(userName);
	        visit.setUserContacts(userContacts);
	        visit.setStatus(BBRVisitStatus.VISSTATUS_INITIALIZED);
	        if (procedure != null)
	        	visit.setLength(procedure.getLength());
	        if (visit.getLength() < minimalLength)
	        	visit.setLength(minimalLength);
	        session.save(visit);
	        
	        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        
	        BBRTaskManager tmgr = new BBRTaskManager();
	        tmgr.createAndStoreTask("Approve visit", null, pos, new Date(), new Date(), 
	        						df.format(visit.getTimeScheduled()) + " --> " + visit.getPos().getTitle() + 
	        						", " + visit.getUserName() + ", " + visit.getUserContacts(), 
	        						BBRVisit.class.getName(), visit.getId());

	        BBRUtil.commitTran(sessionIndex, tr);
	        return visit.getId().toString();
        } catch (Exception ex) {
        	return null;
        }
    }

	public BBRDataSet<BBRVisit> list(Long userId, int pageNumber, int pageSize, String orderBy) {
   		if (userId == null)
   			return null;
   			
        String where = " where user.id=" + userId.toString() + "";
        return list(pageNumber, pageSize, where, orderBy);
	}
	
	public class BBRScheduleList {
		public List<Object[]> list;
		public List<Object[]> specs;
		public int procLength;
				
		BBRScheduleList(List<Object[]> list, int procLength, List<Object[]> specs) {
			this.list = list;
			this.specs = specs;
			this.procLength = procLength;
		}
	}
	
	@SuppressWarnings("unchecked")
	public BBRScheduleList getSchedule(Date date, String posId, String procedureId) {
		if (posId == null) return null;
		if (posId == "") return null;
		
		boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);
        Date startOfDay = BBRUtil.getStartOfDay(date);
        Date endOfDay = BBRUtil.getEndOfDay(date);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        String select = "select visit.timeScheduled as timeScheduled, visit.spec.id as spec, visit.length as length, "+
        				"visit.userName as userName, case when trim(visit.userContacts) = '' then '–' else visit.userContacts end as userContacts, " + 
        				"task.id as taskId";
        String from = " from BBRVisit visit, BBRTask task";
        String where = " where visit.timeScheduled >= '" + df.format(startOfDay) + "' and "
        			  + " visit.timeScheduled <= '" + df.format(endOfDay) + "'";
        where = where + " and visit.pos.id = " + posId;
        where = where + " and visit.spec.status = " + BBRSpecialistState.SPECSTATE_ACTIVE;
        where = where + " and task.objectId=visit.id";
        where = where + " and task.objectType = 'BBRCust.BBRVisit'";
        
        String orderBy = " order by visit.timeScheduled ASC";
        
        Query query = session.createQuery(select + from + where + orderBy);
		List<Object[]> list = query.list();
		
		String selProc = "";
		
		if (procedureId != null && !procedureId.isEmpty()) {
			selProc = "select spc.id " + 
	                  "  from BBRSpecialist spc" + 
			          " where spc.status = " + BBRSpecialistState.SPECSTATE_ACTIVE +
			          "   and spc.pos.id = " + posId + 
			          "   and (" + procedureId + " member of spc.procedures)";
		
			selProc = " case when (spec.id in (" + selProc + ")) then 1 else 0 end";
		} else
			selProc = "1";
		
		query = session.createQuery("select spec.id, spec.startWorkHour, spec.endWorkHour, " + selProc + 
								    "  from BBRSpecialist spec "+ 
								    " where spec.status = " + BBRSpecialistState.SPECSTATE_ACTIVE +  
									"   and spec.pos.id = " + posId);
		List<Object[]> specs = query.list();
        
		DateFormat hf = new SimpleDateFormat("HH");
		DateFormat mf = new SimpleDateFormat("mm");
		
		for(Object[] line: list) {
			if (mf.format((Date)line[0]).equals("00"))
				line[0] = Long.parseLong(hf.format((Date)line[0])) * 2;
			else
				line[0] = Long.parseLong(hf.format((Date)line[0])) * 2 + 1;
			line[2] = Math.round(((Float) line[2]) * 2);
		}
		
		for(Object[] line: specs) {
			for (int i = 1; i <= 2; i++) {
				if (line[i] != null) {
					if (mf.format((Date)line[i]).equals("00"))
						line[i] = Long.parseLong(hf.format((Date)line[i])) * 2;
					else
						line[i] = Long.parseLong(hf.format((Date)line[i])) * 2 + 1;
				} else
					line[i] = (i == 1) ? 0L : 48L;
			}
		}
		
        BBRUtil.commitTran(sessionIndex, tr);
		
        Integer procLength = 1;
		if (procedureId != null && !procedureId.equals("")) {
			BBRProcedureManager pmgr = new BBRProcedureManager();
			BBRProcedure proc = pmgr.findById(Long.parseLong(procedureId));
			procLength = Math.round(proc.getLength() * 2);
		}

		return new BBRScheduleList(list, procLength, specs);
	}
	

	public void approve(BBRVisit visit) {
		if (visit != null) {
			visit.setStatus(BBRVisitStatus.VISSTATUS_APPROVED);
			update(visit);
		}
	}

	public void disapprove(BBRVisit visit) {
		if (visit != null) {
			visit.setStatus(BBRVisitStatus.VISSTATUS_DISAPPROVED);
			update(visit);
		}		
	}

	public class BBRVisitor extends BBRDataElement{
		public String id;
		public String userName;
		public String userContacts;
		public Date lastVisitDate;
		
		BBRVisitor() {
		}
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public BBRDataSet<BBRVisitor> listVisitors(int pageNumber, int pageSize, String orderBy, BBRPoS pos, BBRShop shop) {
		boolean tr = BBRUtil.beginTran(sessionIndex);
        
		Session session = BBRUtil.getSession(sessionIndex);
		if (orderBy == null)
    	   orderBy = "";
		if (orderBy.length() > 0) {
       		orderBy = orderBy.trim();
       		if (!orderBy.startsWith("order by"))
       			orderBy = "order by " + orderBy.trim();
		}
       
		String where = "";
		
		if (pos != null)
			where += "where pos.id = " + pos.getId() + " ";
		else
			if (shop != null)
				where += "where pos.shop.id = " + shop.getId() + " ";
		
		String groupBy = " group by userName, userContacts ";

		int count = session.createQuery("select distinct userName, userContacts from BBRVisit " + where).list().size();
		
		Query query = session.createQuery("select userName, userContacts, max(timeScheduled) as lastVisitDate from BBRVisit " + where + groupBy + orderBy);
       
		if (pageNumber >= 0) {
    	   query.setFirstResult(pageNumber * pageSize);
       
    	   if (pageSize > maxRowsToReturn && maxRowsToReturn > 0)
    		   pageSize = maxRowsToReturn;
           query.setMaxResults(pageSize);
		}
       
        List<Object[]> list = query.list();
		BBRUtil.commitTran(sessionIndex, tr);
       
       List<BBRVisitor> listVisitors = new ArrayList<BBRVisitor>();
       
       for(Object[] line: list) {
    	   BBRVisitor visitor = new BBRVisitor();
    	   visitor.userName = (String) line[0];
    	   visitor.userContacts = (String) line[1];
    	   visitor.lastVisitDate = (Date) line[2];
    	   visitor.id = visitor.userName + BBRUtil.recordDivider + visitor.userContacts; 
    	   listVisitors.add(visitor);
       }
       
		BBRDataSet<BBRVisitor> ds = new BBRDataSet<BBRVisitor>(listVisitors, Long.valueOf(count));
		return ds;
  }

	public BBRVisitor findVisitor(String userName, String userContacts) {
		boolean tr = BBRUtil.beginTran(sessionIndex);
        
		Session session = BBRUtil.getSession(sessionIndex);
		
		if (userName == null || userName.isEmpty() || userContacts == null || userContacts.isEmpty())
			return null;

		Query query = session.createQuery("select userName, userContacts, max(timeScheduled) as lastVisitDate"+ 
		                                  "  from BBRVisit " +  
										  " where userName = :userName" + 
		                                  "   and userContacts = :userContacts" +
										  " group by userName, userContacts").
		                                  setParameter("userName", userName).
		                                  setParameter("userContacts", userContacts);
       
        Object[] line = (Object[])query.uniqueResult();
		BBRUtil.commitTran(sessionIndex, tr);
       
  	    BBRVisitor visitor = new BBRVisitor();
		visitor.userName = (String) line[0];
		visitor.userContacts = (String) line[1];
		visitor.lastVisitDate = (Date) line[2];
		visitor.id = visitor.userName + BBRUtil.recordDivider + visitor.userContacts; 
       
		return visitor;
	}

	public BBRDataSet<BBRVisit> listVisitsByNameAndContacts(String userName, String userContacts, int pageNumber, int pageSize, String orderBy) {
		String where = "userName = '" + userName + "' and userContacts='" + userContacts + "'";
		return list(pageNumber, pageSize, where, orderBy);
	}

}
