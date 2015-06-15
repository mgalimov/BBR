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
        
        String select = "select visit.timeScheduled as timeScheduled, visit.spec.id as spec, visit.length as length";
        String from = " from BBRVisit visit";
        String where = " where timeScheduled >= '" + df.format(startOfDay) + "' and "
        			 + "timeScheduled <= '" + df.format(endOfDay) + "'";
        where = where + " and visit.pos.id = " + posId;
        
        String orderBy = " order by visit.timeScheduled ASC";
        
        Query query = session.createQuery(select + from + where + orderBy);
		List<Object[]> list = query.list();
		
		query = session.createQuery("select spec.id, spec.name from BBRSpecialist spec where spec.pos.id = " + posId);
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

}
