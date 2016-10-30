package BBRCust;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

import BBR.BBRChartPeriods;
import BBR.BBRDataElement;
import BBR.BBRDataManager;
import BBR.BBRDataSet;
import BBR.BBRErrors;
import BBR.BBRMailer;
import BBR.BBRUtil;
import BBRAcc.BBRPoS;
import BBRAcc.BBRPoSManager;
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

	public BBRVisit scheduleVisit(boolean notification, BBRPoS pos, BBRUser user, Date timeScheduled, BBRProcedure procedure, BBRSpecialist spec, String userName, String userContacts, String comment) {
		boolean tr = BBRUtil.beginTran();
        try {
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
			visit.setDiscountPercent(0);
			visit.setDiscountAmount(0);
			visit.setPricePaid(0);
			visit.setAmountToSpecialist(0);
			visit.setAmountToMaterials(0);
			visit.setComment(comment);
			visit.setBookingCode(generateNewBookingCode());
	        if (procedure != null) {
	        	visit.setLength(procedure.getLength());
		        visit.setFinalPrice(procedure.getPrice());
	        }
	        if (visit.getLength() < minimalLength)
	        	visit.setLength(minimalLength);
	        session.save(visit);
	        BBRUtil.commitTran(tr);
	        
	        if (notification) {
	        	boolean t = BBRUtil.beginTran();
	        	SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat);
		        try {
			        BBRTaskManager tmgr = new BBRTaskManager();
			        tmgr.createAndStoreTask("Подтвердите запись!", null, pos, new Date(), new Date(), 
			        						df.format(visit.getTimeScheduled()) + " --> " + visit.getPos().getTitle() + 
			        						", " + visit.getUserName() + ", " + visit.getUserContacts(), 
			        						BBRVisit.class.getName(), visit.getId());
			        BBRUtil.commitTran(t);
		        } catch (Exception ex) {
		        	BBRUtil.rollbackTran(t);
		        }
		        
		        try{
		        	String p = "не указана";
		        	String s = "не указан";
		        	if (visit.getProcedure() != null && visit.getProcedure().getTitle() != "")
		        		p = visit.getProcedure().getTitle();
		        	if (visit.getSpec() != null && visit.getSpec().getName() != "")
		        		s = visit.getSpec().getName();
		        	
		        	BBRMailer.send(visit.getPos().getEmail(), 
		        			"Barbiny: Новая запись в " + visit.getPos().getTitle(), 
		        			"Время: " + df.format(visit.getTimeScheduled()) + "\n" +
		        			"Имя: " + visit.getUserName() + "\n" +
		        			"Контакты: " + visit.getUserContacts() + "\n" +
		        			"Услуга: " + p + "\n" +
		        			"Мастер: " + s + "\n" + "\n" + 
		        			"http://www.barbiny.ru/manager-visit-edit.jsp?id=" + visit.getId());
		        }catch (Exception ex) {
		        }
	        }
	        return visit;
        } catch (Exception ex) {
        	BBRUtil.rollbackTran(tr);
        	return null;
        }
    }

	private String generateNewBookingCode() {
		String code="";
		int s = BBRUtil.codeAlphabet.length();
		boolean found = true;
		
		while (found) {
			for (int i = 1; i <= BBRUtil.codeLength; i++) {
				code += BBRUtil.codeAlphabet.charAt((int)(Math.random() * s));
			}
			BBRVisit v = findByBookingCode(code);
			found = (v != null);
		}
		return code;
	}

	public BBRVisit createAndStoreVisit(BBRPoS pos, BBRUser user, Date realTime, BBRProcedure procedure, BBRSpecialist spec, String userName, String userContacts,
									 float length, float discountPercent, float discountAmount, float pricePaid, float amountToSpecialist, float amountToMaterials,
									 String comment, Set<BBRProcedure> procedures) {
		boolean tr = BBRUtil.beginTran();
        try {
	        Session session = BBRUtil.getSession();
	        BBRVisit visit = new BBRVisit();
	        
			if (userName.isEmpty())
				userName = "Anonymous";
			if (userContacts.isEmpty())
				userContacts = "999999999999";
				
	        visit.setPos(pos);
	        visit.setUser(user);
	        visit.setTimeScheduled(null);
	        visit.setRealTime(realTime);
	        visit.setProcedure(procedure);
	        visit.setSpec(spec);
	        visit.setUserName(userName);
	        visit.setUserContacts(userContacts);
	        visit.setStatus(BBRVisitStatus.VISSTATUS_PERFORMED);
	        visit.setLength(length);
			visit.setDiscountPercent(discountPercent);
			visit.setDiscountAmount(discountAmount);
			visit.setPricePaid(pricePaid);
			visit.setAmountToSpecialist(amountToSpecialist);
			visit.setAmountToMaterials(amountToMaterials);
			visit.setComment(comment);
			visit.setProcedures(procedures);
			
			if (procedure != null) {
				if (visit.getLength() == null || visit.getLength() <= 0.1)
					visit.setLength(procedure.getLength());
		        visit.setFinalPrice(procedure.getPrice());
	        }
	        if (visit.getLength() < minimalLength)
	        	visit.setLength(minimalLength);
	        session.save(visit);
	        
	        BBRUtil.commitTran(tr);
	        return visit;
        } catch (Exception ex) {
        	BBRUtil.rollbackTran(tr);
        	return null;
        }
    }

	public BBRDataSet<BBRVisit> list(Long userId,  BBRShop shop, BBRPoS pos, Date startDate, Date endDate, int pageNumber, int pageSize, String orderBy) {
   		if (userId == null)
   			return null;
   			
        String where = " where user.id=" + userId.toString() + "";
        
		String whereF = getFilterWhere(shop, pos, startDate, endDate, pageNumber);
		if (!whereF.isEmpty())
			where += "(" + where + ") and " + whereF;
		
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
		if (posId == null || posId.isEmpty()) return null;
		
		boolean tr = BBRUtil.beginTran();
        Session session = BBRUtil.getSession();
        Date startOfDay = BBRUtil.getStartOfDay(date);
        Date endOfDay = BBRUtil.getEndOfDay(date);
        DateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormatWithSecs);
        DateFormat sdf = new SimpleDateFormat(BBRUtil.fullDateFormat);
        
        String select = "select coalesce(visit.realTime, visit.timeScheduled, visit.timeScheduled, visit.timeScheduled) as timeScheduled, visit.spec.id as spec, visit.length as length, "+
        				"visit.userName as userName, case when trim(visit.userContacts) = '' then '–' else visit.userContacts end as userContacts, " + 
        				"visit.id, visit.status";
        String from = " from BBRVisit visit";
        String where = " where coalesce(visit.realTime, visit.timeScheduled) >= '" + df.format(startOfDay) + "' and "
        			  + " coalesce(visit.realTime, visit.timeScheduled) <= '" + df.format(endOfDay) + "'";
        where = where + " and visit.pos.id = " + posId;
        where = where + " and visit.status in (" + BBRVisitStatus.VISSTATUS_APPROVED + ", " + BBRVisitStatus.VISSTATUS_INITIALIZED + ", " + BBRVisitStatus.VISSTATUS_PERFORMED + ")";
        where = where + " and visit.spec.status = " + BBRSpecialistState.SPECSTATE_ACTIVE;
        
        String orderBy = " order by coalesce(visit.realTime, visit.timeScheduled) ASC";
        
        Query query = session.createQuery(select + from + where + orderBy);
		List<Object[]> list = query.list();
		BBRUtil.log.info(list.size());
		
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
	
	@SuppressWarnings("unchecked")
	public List<String> getFreeTimesBySpec(Date date, String posId, String specId) throws Exception {
		if (posId == null || posId.isEmpty()) return null;
		if (specId == null || specId.isEmpty()) return null;

		List<String> freeTimes = new ArrayList<String>();
		List<Object[]> visitList = null;
        BBRPoSManager pmgr = new BBRPoSManager();
        BBRSpecialistManager smgr = new BBRSpecialistManager();
        BBRPoS pos;
        BBRSpecialist spec;
        try {
        	pos = pmgr.findById(Long.parseLong(posId));
        	spec = smgr.findById(Long.parseLong(specId));
        	if (pos == null)
        		throw new Exception(BBRErrors.ERR_POS_NOTFOUND);
        	if (spec == null)
        		throw new Exception(BBRErrors.ERR_SPEC_MUST_BE_SPECIFIED);
        } catch (Exception ex) {
        	throw new Exception(BBRErrors.ERR_WRONG_INPUT_FORMAT);
        }
        
		boolean tr = BBRUtil.beginTran();
		try {
	        Session session = BBRUtil.getSession();
	        Date startOfDay = BBRUtil.getStartOfDay(date);
	        Date endOfDay = BBRUtil.getEndOfDay(date);
	        DateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormatWithSecs);
	        
	        String select = "select coalesce(visit.realTime, visit.timeScheduled) as timeScheduled, visit.length as length";
	        String from = " from BBRVisit visit";
	        String where = " where coalesce(visit.realTime, visit.timeScheduled) >= '" + df.format(startOfDay) + "' and "
	        			       + " coalesce(visit.realTime, visit.timeScheduled) <= '" + df.format(endOfDay) + "'";
	        where += " and visit.pos.id = " + posId;
	        where += " and visit.status in (" + BBRVisitStatus.VISSTATUS_APPROVED + ", " + BBRVisitStatus.VISSTATUS_INITIALIZED + ", " + BBRVisitStatus.VISSTATUS_PERFORMED + ")";
	        if (spec != null)
	        	where += " and visit.spec.id = " + specId;
	        String orderBy = " order by coalesce(visit.realTime, visit.timeScheduled) ASC";
	        
	        Query query = session.createQuery(select + from + where + orderBy);
			visitList = query.list();
			BBRUtil.commitTran(tr);
		} catch (Exception ex) {
			BBRUtil.rollbackTran(tr);
		}

        BBRTurnManager tmgr = new BBRTurnManager();
        BBRTurn turn = tmgr.findByDate(spec, date);
        if (turn == null) {
        	return null;
        }

        int startHalfHour;
        
        Calendar c = Calendar.getInstance();
//        c.setTime(pos.getStartWorkHour());
//        startHalfHour = c.get(Calendar.HOUR_OF_DAY) * 2;
//        if (c.get(Calendar.MINUTE) != 0)
//        	startHalfHour++;
        
        c.setTime(turn.getStartTime());
        int specStartHalfHour = c.get(Calendar.HOUR_OF_DAY) * 2;
        if (c.get(Calendar.MINUTE) != 0)
        	specStartHalfHour++;
        
//        if (specStartHalfHour < startHalfHour)
        	startHalfHour = specStartHalfHour;

        int endHalfHour;
        
//        c.setTime(pos.getEndWorkHour());
//        endHalfHour = c.get(Calendar.HOUR_OF_DAY) * 2;
//        if (c.get(Calendar.MINUTE) != 0)
//        	endHalfHour++;
        
        c.setTime(turn.getEndTime());
        int specEndHalfHour = c.get(Calendar.HOUR_OF_DAY) * 2;
        if (c.get(Calendar.MINUTE) != 0)
        	specEndHalfHour++;
        
//        if (specEndHalfHour < endHalfHour)
        	endHalfHour = specEndHalfHour;

        int[] hours = new int[48];
        for (int h = 0; h < startHalfHour; h++)
        	hours[h] = 1;
        for (int h = startHalfHour; h < endHalfHour; h++)
        	hours[h] = 0;
        for (int h = endHalfHour; h < 48; h++)
        	hours[h] = 1;
        
        for (Object[] v : visitList) {
        	c.setTime((Date)v[0]);
            int halfHour = c.get(Calendar.HOUR_OF_DAY) * 2;
            if (c.get(Calendar.MINUTE) != 0)
            	halfHour++;
            for (int h = halfHour; h < halfHour + (int)((Float)v[1] * 2); h++)
            	hours[h] = 1;
        }
        
        for (int h = 0; h < 48; h++) {
        	if (hours[h] == 0) {
        		String freeTime = "" + (int)Math.floor(h / 2);
        		if ((int)(Math.floor(h / 2) * 2) != h)
        			freeTime += ":30";
        		else {
        			freeTime += ":00";
        			freeTimes.add(freeTime);
        		}
        	}
        }
        
        return freeTimes;
	}

	@SuppressWarnings("unchecked")
	public List<String> getFreeTimesByProc(Date date, String posId, String procId) throws Exception {
		if (posId == null || posId.isEmpty()) return null;
		if (procId == null || procId.isEmpty()) return null;

		List<String> freeTimes = new ArrayList<String>();
		List<Object[]> specList = null;
		List<Object[]> visitList = null;
		String specIds = "";
		
        BBRPoSManager pmgr = new BBRPoSManager();
        BBRProcedureManager prmgr = new BBRProcedureManager();
        BBRPoS pos;
        BBRProcedure proc;
        try {
        	pos = pmgr.findById(Long.parseLong(posId));
        	proc = prmgr.findById(Long.parseLong(procId));
        	if (pos == null)
        		throw new Exception(BBRErrors.ERR_POS_NOTFOUND);
        	if (proc == null)
        		throw new Exception(BBRErrors.ERR_PROC_NOTFOUND);
        } catch (Exception ex) {
        	throw new Exception(BBRErrors.ERR_WRONG_INPUT_FORMAT);
        }
        
        Session session = BBRUtil.getSession();
        Date startOfDay = BBRUtil.getStartOfDay(date);
        Date endOfDay = BBRUtil.getEndOfDay(date);
        DateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormatWithSecs);

        boolean tr = BBRUtil.beginTran();
		try {
	        String select = "select turn.specialist.id, turn.startTime, turn.endTime";
	        String from = " from BBRTurn turn";
	        String where = " where turn.date >= '" + df.format(startOfDay) + "' and "
	        			       + " turn.date <= '" + df.format(endOfDay) + "'";
	        where += " and turn.specialist.pos.id = " + posId;
	        where += " and (" + procId + " member of turn.specialist.procedures)";
	        
	        Query query = session.createQuery(select + from + where);
			specList = query.list();
			BBRUtil.commitTran(tr);
		} catch (Exception ex) {
			BBRUtil.rollbackTran(tr);
		}

		int startHalfHour = 48;
		int endHalfHour = 0;
		
		Calendar c = Calendar.getInstance();
		
		for (Object[] s : specList) {
			specIds += ", " + (Long)(s[0]);
			
			c.setTime((Date)s[1]);
	        int specStartHalfHour = c.get(Calendar.HOUR_OF_DAY) * 2;
	        
			c.setTime((Date)s[2]);
	        int specEndHalfHour = c.get(Calendar.HOUR_OF_DAY) * 2;
	        
	        if (specStartHalfHour < startHalfHour)
	        	startHalfHour = specStartHalfHour;
	        if (specEndHalfHour > endHalfHour)
	        	endHalfHour = specEndHalfHour;
		}
		specIds = specIds.substring(1);
		
		tr = BBRUtil.beginTran();
		session = BBRUtil.getSession();
		try {
	        String select = "select coalesce(visit.realTime, visit.timeScheduled) as timeScheduled, visit.length as length";
	        String from = " from BBRVisit visit";
	        String where = " where coalesce(visit.realTime, visit.timeScheduled) >= '" + df.format(startOfDay) + "' and "
	        			       + " coalesce(visit.realTime, visit.timeScheduled) <= '" + df.format(endOfDay) + "'";
	        where += " and visit.pos.id = " + posId;
	        where += " and visit.status in (" + BBRVisitStatus.VISSTATUS_APPROVED + ", " + BBRVisitStatus.VISSTATUS_INITIALIZED + ", " + BBRVisitStatus.VISSTATUS_PERFORMED + ")";
	        where += " and visit.spec.id in (" + specIds + ")";
	        String orderBy = " order by coalesce(visit.realTime, visit.timeScheduled) ASC";
	        
	        Query query = session.createQuery(select + from + where + orderBy);
			visitList = query.list();
			BBRUtil.commitTran(tr);
		} catch (Exception ex) {
			BBRUtil.rollbackTran(tr);
		}
		
//        Calendar c = Calendar.getInstance();
//        c.setTime(pos.getStartWorkHour());
//        int startHalfHour = c.get(Calendar.HOUR_OF_DAY) * 2;
//        if (c.get(Calendar.MINUTE) != 0)
//        	startHalfHour++;
//
//        c.setTime(pos.getEndWorkHour());
//        int endHalfHour = c.get(Calendar.HOUR_OF_DAY) * 2;
//        if (c.get(Calendar.MINUTE) != 0)
//        	endHalfHour++;
        
        int[] hours = new int[48];
        for (int h = 0; h < startHalfHour; h++)
        	hours[h] = specList.size();
        for (int h = startHalfHour; h < endHalfHour; h++)
        	hours[h] = 0;
        for (int h = endHalfHour; h < 48; h++)
        	hours[h] = specList.size();
        
        for (Object[] v : visitList) {
        	c.setTime((Date)v[0]);
            int halfHour = c.get(Calendar.HOUR_OF_DAY) * 2;
            if (c.get(Calendar.MINUTE) != 0)
            	halfHour++;
            for (int h = halfHour; h < halfHour + (int)((Float)v[1] * 2); h++)
            	hours[h]++;
        }
        
        for (int h = 0; h < 48; h++) {
        	if (hours[h] < specList.size()) {
        		String freeTime = "" + (int)Math.floor(h / 2);
        		if ((int)(Math.floor(h / 2) * 2) != h)
        			freeTime += ":30";
        		else {
        			freeTime += ":00";
        			freeTimes.add(freeTime);
        		}
        	}
        }
        
        return freeTimes;
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
			if (visit.getRealTime() == null) {
				visit.setRealTime(new Date()); 
			}
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
		
		String qry = "select userName, userContacts, max(realTime) as lastVisitDate from BBRVisit " + 
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

		Query query = session.createQuery("select userName, userContacts, max(coalesce(visit.realTime, visit.timeScheduled)) as lastVisitDate"+ 
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

	public BBRDataSet<BBRVisit> listVisitsByNameAndContacts(String userName, String userContacts, BBRShop shop, BBRPoS pos, Date startDate, Date endDate, int pageNumber, int pageSize, String orderBy) {
		String where = "userName = '" + userName + "' and userContacts='" + userContacts + "'";
		String whereF = getFilterWhere(shop, pos, startDate, endDate, pageNumber);
		if (!whereF.isEmpty())
			where = "(" + where + ") and " + whereF;
		return list(pageNumber, pageSize, where, orderBy);
	}

	public BBRDataSet<BBRVisit> listVisitsByDateAndPos(Date date, BBRPoS pos, int pageNumber, int pageSize, String orderBy) {
		if (date == null || pos == null)
			return null;
		
		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat);
		
		String where = "pos.id = " + pos.getId() + 
				"  and ((timeScheduled >='" + df.format(BBRUtil.getStartOfDay(date)) + 
				"' and timeScheduled <='" + df.format(BBRUtil.getEndOfDay(date)) + "')" + 
				"   or (realTime >='" + df.format(BBRUtil.getStartOfDay(date)) + 
				"' and realTime <='" + df.format(BBRUtil.getEndOfDay(date)) + "'))";
		return list(pageNumber, pageSize, where, orderBy);
	}

	public BBRDataSet<BBRVisit> listUnapprovedVisitsByPos(BBRPoS pos, Date startDate, Date endDate, int pageNumber, int pageSize, String orderBy) {
		if (pos == null)
			return null;
		
		String where = "pos.id = " + pos.getId() + " and status = " + BBRVisitStatus.VISSTATUS_INITIALIZED;
		
		String whereF = getFilterWhere(null, pos, startDate, endDate, pageNumber);
		if (!whereF.isEmpty())
			where += "(" + where + ") and " + whereF;

		return list(pageNumber, pageSize, where, orderBy);
	}

	private String getFilterWhere(BBRShop shop, BBRPoS pos, Date startDate, Date endDate, int pageNumber) {
		String where = "";
		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat);
		
		if (shop != null)
			where += " pos.shop.id = " + shop.getId();
		else
			if (pos != null)
				where += " pos.id = " + pos.getId();
		
		
		String whereTS = "";
		String whereRT = "";
		
		if (startDate != null) {
			whereTS = " timeScheduled >= '" + df.format(BBRUtil.getStartOfDay(startDate)) + "'";
			whereRT = " realTime >= '" + df.format(BBRUtil.getStartOfDay(startDate)) + "'";
		}

		if (!whereTS.equals(""))
			whereTS += " and ";
		
		if (!whereRT.equals(""))
			whereRT += " and ";
		
		if (endDate != null) {
			whereTS += " timeScheduled <= '" + df.format(BBRUtil.getEndOfDay(endDate)) + "'";
			whereRT += " realTime <= '" + df.format(BBRUtil.getEndOfDay(endDate)) + "'";
		}

		if (!whereTS.isEmpty() && !whereRT.isEmpty()) {
			if (!where.equals(""))
				where += " and ";
			where += "((" + whereTS + ") or (" + whereRT + "))"; 
		}	
		return where;
	}
	
	public BBRDataSet<BBRVisit> listAllVisitsByFilter(BBRShop shop, BBRPoS pos, Date startDate, Date endDate, int pageNumber, int pageSize, String orderBy) {
		return list(pageNumber, pageSize, getFilterWhere(shop, pos, startDate, endDate, pageNumber), orderBy);
	}

	// Charts
	@SuppressWarnings("unchecked")
	public List<Object[]> getVisitsByPeriod(Date startDate, Date endDate, int detail, BBRPoS pos, BBRShop shop) {
		boolean tr = BBRUtil.beginTran();
		Session session = BBRUtil.getSession();
		
		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
		String pf = BBRChartPeriods.periodFunction("timeScheduled", "realTime", detail);
	
		String where = "";
		if (pos != null)
			where = " and pos.id = " + pos.getId();
		else
			if (shop != null)
				where = "and pos.shop.id = " + shop.getId();
		
		Query query = session.createQuery("select " + pf + ", count(*) as visits" + 
		                                  "  from BBRVisit " +  
										  " where ((timeScheduled >= '"+df.format(BBRUtil.getStartOfDay(startDate))+"' " + 
		                                  "     and timeScheduled <= '"+df.format(BBRUtil.getEndOfDay(endDate))+"') or " +
										  "        (realTime >= '"+df.format(BBRUtil.getStartOfDay(startDate))+"' " + 
		                                  "     and realTime <= '"+df.format(BBRUtil.getEndOfDay(endDate))+"'))" +
										  "   and (status = " + BBRVisitStatus.VISSTATUS_PERFORMED + 
										  "        or status = " + BBRVisitStatus.VISSTATUS_APPROVED + ")" + 
										  where +
										  " group by " + pf + 
										  " order by coalesce(visit.realTime, visit.timeScheduled) asc");

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
		String pf = BBRChartPeriods.periodFunction("timeScheduled", "realTime", detail);
		
		String where = "";
		if (pos != null)
			where = " and pos.id = " + pos.getId();
		else
			if (shop != null)
				where = "and pos.shop.id = " + shop.getId();
		
		Query query = session.createQuery("select " + pf + ", sum(finalPrice) as income" + 
		                                  "  from BBRVisit " +  
										  " where ((timeScheduled >= '"+df.format(BBRUtil.getStartOfDay(startDate))+"' " + 
		                                  "     and timeScheduled <= '"+df.format(BBRUtil.getEndOfDay(endDate))+"') or " +
										  "        (realTime >= '"+df.format(BBRUtil.getStartOfDay(startDate))+"' " + 
		                                  "     and realTime <= '"+df.format(BBRUtil.getEndOfDay(endDate))+"'))" +
										  "   and (status = " + BBRVisitStatus.VISSTATUS_PERFORMED + 
										  "        or status = " + BBRVisitStatus.VISSTATUS_APPROVED + ")" + 
										  where +
										  " group by " + pf + 
										  " order by coalesce(visit.realTime, visit.timeScheduled) asc");

		List<Object[]> list = query.list();
		BBRUtil.commitTran(tr);
		
		return list;
	}

	@SuppressWarnings("unchecked")
	public BBRSpecialist findSpecByTimeAndProc(Date timeScheduled, BBRProcedure proc, BBRPoS pos) {
		if (pos == null) return null;
		if (proc == null) return null;

		List<Object[]> specList = null;
		List<Object[]> visitList = null;
		String specIds = "";
		
        Session session = BBRUtil.getSession();
        Date startOfDay = BBRUtil.getStartOfDay(timeScheduled);
        Date endOfDay = BBRUtil.getEndOfDay(timeScheduled);
        DateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormatWithSecs);
        
        boolean tr = BBRUtil.beginTran();
		try {
	        String select = "select turn.specialist.id, turn.startTime, turn.endTime";
	        String from = " from BBRTurn turn";
	        String where = " where turn.date >= '" + df.format(startOfDay) + "' and "
	        			       + " turn.date <= '" + df.format(endOfDay) + "'";
	        where += " and turn.specialist.pos.id = " + pos.getId();
	        where += " and (" + proc.getId() + " member of turn.specialist.procedures)";
	        
	        Query query = session.createQuery(select + from + where);
			specList = query.list();
			BBRUtil.commitTran(tr);
		} catch (Exception ex) {
			BBRUtil.rollbackTran(tr);
		}

		for (Object[] s : specList) {
			specIds += ", " + ((Long)s[0]).toString();
		}
		specIds = specIds.substring(1);
		
		tr = BBRUtil.beginTran();
		session = BBRUtil.getSession();
		try {
	        String select = "select visit.spec.id, coalesce(visit.realTime, visit.timeScheduled) as timeScheduled, visit.length";
	        String from = " from BBRVisit visit";
	        String where = " where coalesce(visit.realTime, visit.timeScheduled) >= '" + df.format(startOfDay) + "' and "
	        			       + " coalesce(visit.realTime, visit.timeScheduled) <= '" + df.format(endOfDay) + "'";
	        where += " and visit.pos.id = " + pos.getId();
	        where += " and visit.status in (" + BBRVisitStatus.VISSTATUS_APPROVED + ", " + BBRVisitStatus.VISSTATUS_INITIALIZED + ", " + BBRVisitStatus.VISSTATUS_PERFORMED + ")";
	        where += " and visit.spec.id in (" + specIds + ")";
	        String orderBy = " order by coalesce(visit.realTime, visit.timeScheduled) ASC";
	        
	        Query query = session.createQuery(select + from + where + orderBy);
			visitList = query.list();
			BBRUtil.commitTran(tr);
		} catch (Exception ex) {
			BBRUtil.rollbackTran(tr);
		}
		
		ArrayList<Long> specs = new ArrayList<Long>();
		Calendar c = Calendar.getInstance();
		
        for (Object[] s : specList) {
        	boolean found = false;
        	Long spec = (Long)s[0];
        	Date st = (Date)s[1];
        	Date et = (Date)s[2];

        	c.setTime(et);
        	if (proc != null)
        		c.add(Calendar.MINUTE, -(int)(proc.getLength()));
        	else 
        		c.add(Calendar.MINUTE, -30);
        	
        	if (timeScheduled.compareTo(st) < 0 || timeScheduled.compareTo(c.getTime()) > 0)
        		found = true;
        	
        	for (Object[] v : visitList) {
				c.setTime((Date)(v[1]));
        		c.add(Calendar.MINUTE, (int)((Float)(v[2])*60));
        		if ((Long)v[0] == spec && c.getTime().compareTo(timeScheduled) > 0 && ((Date)(v[1])).compareTo(timeScheduled) <= 0) {
        			found = true;
        			break;
        		}
        	}
        	if (!found)
        		specs.add(spec);
        }
        
        if (specs.size() > 0) {
        	int index = (int) Math.floor(Math.random() * (float)specs.size());
        	BBRSpecialistManager smgr = new BBRSpecialistManager();
        	BBRSpecialist spec = smgr.findById(specs.get(index));
        	return spec; 
        } else return null;
	}
	
	public BBRVisit findByBookingCode(String code) {
    	boolean tr = BBRUtil.beginTran();
    	try {
    		Session session = BBRUtil.getSession(); 	
    		BBRVisit result = (BBRVisit) session.createQuery("from " + typeName + " as t where t.bookingCode = '" + code + "'").uniqueResult();
    		BBRUtil.commitTran(tr);
    		return result;
    	} catch (Exception ex) {
    		BBRUtil.rollbackTran(tr);
    		return null;
    	}
    }

	public boolean isTimeAvailable(Date time, Long posId, Long procId) {
		try {
			BBRProcedureManager prmgr = new BBRProcedureManager();
			BBRPoSManager pmgr = new BBRPoSManager();
			BBRPoS pos = pmgr.findById(posId);
			BBRProcedure proc = prmgr.findById(procId); 
			BBRSpecialist spec = findSpecByTimeAndProc(time, proc, pos);
			if (spec != null)
				return true;
			return false;
		} catch (Exception ex) {
			return false;
		}
	}
}
