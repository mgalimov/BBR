package BBRCust;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import BBR.BBRChartPeriods;
import BBR.BBRDataElement;
import BBR.BBRDataManager;
import BBR.BBRDataSet;
import BBR.BBRUtil;
import BBRAcc.BBRPoS;
import BBRAcc.BBRShop;
import BBRAcc.BBRUser;
import BBRCust.BBRSpecialist.BBRSpecialistState;
import BBRCust.BBRVisit.BBRVisitStatus;
import BBRCust.BBRVisit;

public class BBRVisitManager extends BBRDataManager<BBRVisit>{
	private final float minimalLength = (float) 0.5;
	
	public BBRVisitManager() {
		super();
		titleField = "timeScheduled";
		classTitle = "Visit";	
	}

	public BBRVisit createAndStoreVisit(BBRPoS pos, BBRUser user, Date timeScheduled, BBRProcedure procedure, BBRSpecialist spec, String userName, String userContacts) {
        try {
			boolean tr = BBRUtil.beginTran();
	        Session session = BBRUtil.getSession();
	        BBRVisit visit = new BBRVisit();
	        visit.setPos(pos);
	        visit.setUser(user);
	        visit.setTimeScheduled(timeScheduled);
	        visit.setProcedure(procedure);
	        visit.setSpec(spec);
	        visit.setUserName(userName);
	        visit.setUserContacts(userContacts);
	        visit.setStatus(BBRVisitStatus.VISSTATUS_INITIALIZED);
	        if (procedure != null) {
	        	visit.setLength(procedure.getLength());
		        visit.setFinalPrice(procedure.getPrice());
	        }
	        if (visit.getLength() < minimalLength)
	        	visit.setLength(minimalLength);
	        session.save(visit);
	        
	        SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat);
	        
	        BBRTaskManager tmgr = new BBRTaskManager();
	        tmgr.createAndStoreTask("Approve visit", null, pos, new Date(), new Date(), 
	        						df.format(visit.getTimeScheduled()) + " --> " + visit.getPos().getTitle() + 
	        						", " + visit.getUserName() + ", " + visit.getUserContacts(), 
	        						BBRVisit.class.getName(), visit.getId());

	        BBRUtil.commitTran(tr);
	        return visit;
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
		
		boolean tr = BBRUtil.beginTran();
        Session session = BBRUtil.getSession();
        Date startOfDay = BBRUtil.getStartOfDay(date);
        Date endOfDay = BBRUtil.getEndOfDay(date);
        DateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormatWithSecs);
        DateFormat sdf = new SimpleDateFormat(BBRUtil.fullDateFormat);
        
        String select = "select visit.timeScheduled as timeScheduled, visit.spec.id as spec, visit.length as length, "+
        				"visit.userName as userName, case when trim(visit.userContacts) = '' then '–' else visit.userContacts end as userContacts, " + 
        				"visit.id, visit.status";
        String from = " from BBRVisit visit";
        String where = " where visit.timeScheduled >= '" + df.format(startOfDay) + "' and "
        			  + " visit.timeScheduled <= '" + df.format(endOfDay) + "'";
        where = where + " and visit.pos.id = " + posId;
        where = where + " and visit.spec.status = " + BBRSpecialistState.SPECSTATE_ACTIVE;
        
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
		
		query = session.createQuery("select spec.id, turn.startTime, turn.endTime, " + selProc + 
								    "  from BBRTurn turn right join turn.specialist as spec"+ 
								    " where spec.status = " + BBRSpecialistState.SPECSTATE_ACTIVE +  
									"   and spec.pos.id = " + posId +
									"   and turn.date = '" + sdf.format(date) + "'");
		List<Object[]> specs = query.list();
		
		query = session.createQuery("select spec.id, 0, 0, 0" + 
								    "  from BBRSpecialist as spec"+ 
								    " where spec.status = " + BBRSpecialistState.SPECSTATE_ACTIVE +  
									"   and spec.pos.id = " + posId +
									"   and spec.id not in (select turn.specialist.id from BBRTurn as turn where turn.date = '" + sdf.format(date) + "')");
		List<Object[]> specsNoTurns = query.list();

        BBRUtil.commitTran(tr);
        
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

		for(Object[] line: specsNoTurns)
			specs.add(line);
		
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
			try {
				update(visit);
			} catch (Exception e) {
			}
		}
	}

	public void disapprove(BBRVisit visit) {
		if (visit != null) {
			visit.setStatus(BBRVisitStatus.VISSTATUS_DISAPPROVED);
			try {
				update(visit);
			} catch (Exception e) {
			}
		}		
	}

	public void close(BBRVisit visit) {
		if (visit != null) {
			visit.setStatus(BBRVisitStatus.VISSTATUS_PERFORMED);
			try {
				update(visit);
			} catch (Exception e) {
			}
		}		
	}

	public void cancel(BBRVisit visit) {
		if (visit != null) {
			visit.setStatus(BBRVisitStatus.VISSTATUS_CANCELLED);
			try {
				update(visit);
			} catch (Exception e) {
			}
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
	public BBRDataSet<BBRVisitor> listVisitors(int pageNumber, int pageSize, String orderBy, BBRPoS pos, BBRShop shop, Integer days) {
		boolean tr = BBRUtil.beginTran();
        
		Session session = BBRUtil.getSession();
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
		
		String having = "";
		if (days != null && days > 0) {
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DATE, -days);
			SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
			having = " having max(timeScheduled) > '" + df.format(c.getTime()) + "' ";
		}
		
		String groupBy = " group by userName, userContacts ";
		
		String qry = "select userName, userContacts, max(timeScheduled) as lastVisitDate from BBRVisit " + 
		             where + groupBy + having + orderBy;

		int count = session.createQuery(qry).list().size();
		Query query = session.createQuery(qry);
       
		if (pageNumber >= 0) {
    	   query.setFirstResult(pageNumber * pageSize);
       
    	   if (pageSize > maxRowsToReturn && maxRowsToReturn > 0)
    		   pageSize = maxRowsToReturn;
           query.setMaxResults(pageSize);
		}
       
        List<Object[]> list = query.list();
		BBRUtil.commitTran(tr);
       
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
		boolean tr = BBRUtil.beginTran();
        
		Session session = BBRUtil.getSession();
		
		if (userName == null || userName.isEmpty())
			return null;

		Query query = session.createQuery("select userName, userContacts, max(timeScheduled) as lastVisitDate"+ 
		                                  "  from BBRVisit " +  
										  " where userName = :userName" + 
		                                  "   and userContacts = :userContacts" +
										  " group by userName, userContacts").
		                                  setParameter("userName", userName).
		                                  setParameter("userContacts", userContacts);
       
        Object[] line = (Object[])query.uniqueResult();
		BBRUtil.commitTran(tr);
       
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

	public BBRDataSet<BBRVisit> listVisitsByDateAndPos(Date date, BBRPoS pos, int pageNumber, int pageSize, String orderBy) {
		if (date == null || pos == null)
			return null;
		
		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat);
		
		String where = "pos.id = " + pos.getId() + 
				" and timeScheduled >='" + df.format(BBRUtil.getStartOfDay(date)) + 
				"' and timeScheduled <='" + df.format(BBRUtil.getEndOfDay(date)) + "'";
		return list(pageNumber, pageSize, where, orderBy);
	}

	public BBRDataSet<BBRVisit> listUnapprovedVisitsByPos(BBRPoS pos, int pageNumber, int pageSize, String orderBy) {
		if (pos == null)
			return null;
		
		String where = "pos.id = " + pos.getId() + " and status = " + BBRVisitStatus.VISSTATUS_INITIALIZED;
		
		return list(pageNumber, pageSize, where, orderBy);
	}

	public BBRDataSet<BBRVisit> listAllVisitsByFilter(BBRShop shop, BBRPoS pos, Date startDate, Date endDate, int pageNumber, int pageSize, String orderBy) {
		String where = "";
		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
		
		if (shop != null)
			where += " pos.shop.id = " + shop.getId();
		else
			if (pos != null)
				where += " pos.id = " + pos.getId();
		
		if (!where.equals(""))
			where += " and ";
		
		if (startDate != null)
			where += " timeScheduled >= '" + df.format(startDate) + "'";

		if (!where.equals(""))
			where += " and ";
		
		if (endDate != null)
			where += " timeScheduled <= '" + df.format(endDate) + "'";

		return list(pageNumber, pageSize, where, orderBy);
	}

	// Charts
	@SuppressWarnings("unchecked")
	public List<Object[]> getVisitsByPeriod(Date startDate, Date endDate, int detail, BBRPoS pos, BBRShop shop) {
		boolean tr = BBRUtil.beginTran();
		Session session = BBRUtil.getSession();
		
		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
		String pf = BBRChartPeriods.periodFunction("timeScheduled", detail);
		
		String where = "";
		if (pos != null)
			where = " and pos.id = " + pos.getId();
		else
			if (shop != null)
				where = "and pos.shop.id = " + shop.getId();
		
		Query query = session.createQuery("select " + pf + ", count(*) as visits" + 
		                                  "  from BBRVisit " +  
										  " where timeScheduled >= '"+df.format(startDate)+"'" + 
		                                  "   and timeScheduled <= '"+df.format(endDate)+"'" +
										  "   and status = " + BBRVisitStatus.VISSTATUS_PERFORMED + 
										  where +
										  " group by " + pf + 
										  " order by timeScheduled asc");

		List<Object[]> list = query.list();
		BBRUtil.commitTran(tr);
		
		return list;
	}

    public String whereShop(Long shopId) {
    	return "pos.shop.id = " + shopId;
    }

	@SuppressWarnings("unchecked")
	public List<Object[]> getIncomeByPeriod(Date startDate, Date endDate,
			Integer detail, BBRPoS pos, BBRShop shop) {
		boolean tr = BBRUtil.beginTran();
		Session session = BBRUtil.getSession();
		
		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
		String pf = BBRChartPeriods.periodFunction("timeScheduled", detail);
		
		String where = "";
		if (pos != null)
			where = " and pos.id = " + pos.getId();
		else
			if (shop != null)
				where = "and pos.shop.id = " + shop.getId();
		
		Query query = session.createQuery("select " + pf + ", sum(finalPrice) as income" + 
		                                  "  from BBRVisit " +  
										  " where timeScheduled >= '"+df.format(startDate)+"'" + 
		                                  "   and timeScheduled <= '"+df.format(endDate)+"'" +
										  "   and status = " + BBRVisitStatus.VISSTATUS_PERFORMED + 
										  where +
										  " group by " + pf + 
										  " order by timeScheduled asc");

		List<Object[]> list = query.list();
		BBRUtil.commitTran(tr);
		
		return list;
	};
}
