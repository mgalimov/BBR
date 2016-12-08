import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRErrors;
import BBR.BBRUtil;
import BBRAcc.BBRPoS;
import BBRAcc.BBRPoSManager;
import BBRAcc.BBRUser.BBRUserRole;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;
import BBRCust.BBRProcedure;
import BBRCust.BBRProcedureManager;
import BBRCust.BBRSpecialist;
import BBRCust.BBRSpecialistManager;
import BBRCust.BBRVisitManager;
import BBRCust.BBRVisit;
import BBRCust.BBRVisit.BBRVisitStatus;

@WebServlet("/BBRVisits")
public class BBRVisits extends BBRBasicServlet<BBRVisit, BBRVisitManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRVisits() throws InstantiationException, IllegalAccessException {
        super(BBRVisitManager.class);
    }

	@Override
	protected String create(BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		BBRContext context = BBRContext.getContext(request);
		int visitStep = context.getLastVisitStep();
		
		String formMode = "";
		try {
			formMode = (String)context.get("newVisitMode");
			if (formMode == null)
				formMode = "";
		} catch (Exception ex) {
		}

		
		if (formMode != null && formMode.equals("manager-edit")) {
			try {
				String userName = params.get("userName");
				String userContacts = params.get("userContacts");
				String comment = params.get("comment");
				
				Long posId = Long.parseLong(params.get("pos"));
				BBRPoSManager mgrPoS = new BBRPoSManager();
				BBRPoS pos = mgrPoS.findById(posId);
				
				Long procedureId = Long.parseLong(params.get("procedure"));
				BBRProcedureManager mgrProc = new BBRProcedureManager();
				BBRProcedure proc = mgrProc.findById(procedureId);						
	
				Long specId = Long.parseLong(params.get("spec"));
				BBRSpecialistManager mgrSpec = new BBRSpecialistManager();
				BBRSpecialist spec = mgrSpec.findById(specId);						
	
				if (pos == null)
					throw new Exception(BBRErrors.ERR_POS_MUST_BE_SPECIFIED);

				if (proc == null)
					throw new Exception(BBRErrors.ERR_PROC_MUST_BE_SPECIFIED);

				if (spec == null)
					throw new Exception(BBRErrors.ERR_SPEC_MUST_BE_SPECIFIED);

				DateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat);
				Date timeScheduled = df.parse(params.get("timeScheduled"));
				
				context.planningVisit = manager.scheduleVisit(false,
													pos, 
													null, 
													timeScheduled, 
													proc, 
													spec, 
													userName,
													userContacts,
													comment);
				
				context.planningVisit.setStatus(BBRVisitStatus.VISSTATUS_APPROVED);
				manager.update(context.planningVisit);
				
			} catch (Throwable ex) {
				throw new Exception(BBRErrors.ERR_FILL_REQUIRED_FIELDS);
			}
		} else 
			if (formMode != null && formMode.equals("general-edit")) 
			{
				if (visitStep == 1) {
					context.planningVisit = new BBRVisit();
					
					Long posId = Long.parseLong(params.get("pos"));
	
					BBRPoSManager mgrPoS = new BBRPoSManager();
					BBRPoS pos = mgrPoS.findById(posId);
					if (pos == null) {
						throw new Exception(BBRErrors.ERR_POS_NOTFOUND);
					}
					
					context.planningVisit.setPos(pos);
				}
					
				if (visitStep == 2) {
					if (context.planningVisit == null)
						throw new Exception(BBRErrors.ERR_VISIT_NOTFOUND);
					
					if (!params.get("procedure").isEmpty()) {
						Long procedureId = Long.parseLong(params.get("procedure"));
	
						BBRProcedureManager mgrProc = new BBRProcedureManager();
						BBRProcedure proc = mgrProc.findById(procedureId);						
						context.planningVisit.setProcedure(proc);
					}
					
					if (!params.get("spec").isEmpty()) {
						Long specId = Long.parseLong(params.get("spec"));
	
						BBRSpecialistManager mgrSpec = new BBRSpecialistManager();
						BBRSpecialist spec = mgrSpec.findById(specId);						
						context.planningVisit.setSpec(spec);
					}
					
					DateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat);
					try {
						Date timeScheduled = df.parse(params.get("timeScheduled"));
						context.planningVisit.setTimeScheduled(timeScheduled);
					} catch (Throwable ex) {
						throw new Exception(BBRErrors.ERR_DATE_INCORRECT);
					}
				}
				
				if (visitStep == 3) {
					if (context.planningVisit == null)
						throw new Exception(BBRErrors.ERR_VISIT_NOTFOUND);
					
					String userName = params.get("userName");
					String userContacts = params.get("userContacts");
					
					context.planningVisit.setUserName(userName);
					context.planningVisit.setUserContacts(userContacts);
	
					context.planningVisit = manager.scheduleVisit(true,
							context.planningVisit.getPos(), 
							context.user, 
							context.planningVisit.getTimeScheduled(), 
							context.planningVisit.getProcedure(), 
							context.planningVisit.getSpec(), 
							context.planningVisit.getUserName(),
							context.planningVisit.getUserContacts(),
							"");
				}
	
				context.setLastVisitStep(++visitStep);
			}
			else {
				try {
					String userName = params.get("userName");
					String userContacts = params.get("userContacts");					
					Long posId = Long.parseLong(params.get("pos"));
					
					BBRPoSManager mgrPoS = new BBRPoSManager();
					BBRPoS pos = mgrPoS.findById(posId);
				
					Long procedureId = Long.parseLong(params.get("procedure"));
					BBRProcedureManager mgrProc = new BBRProcedureManager();
					BBRProcedure proc = mgrProc.findById(procedureId);	
	
					Long specId = Long.parseLong(params.get("spec"));
					BBRSpecialistManager mgrSpec = new BBRSpecialistManager();
					BBRSpecialist spec = mgrSpec.findById(specId);						
					
					Set<BBRProcedure> procedures = new HashSet<BBRProcedure>();
					String procs = params.get("procedures");
					if (!procs.equals("")) {
						for (String prc : procs.split(",")) {
							BBRProcedure procedure = mgrProc.findById(Long.parseLong(prc));
							if (procedure != null) 
								procedures.add(procedure);
						}
					}
					
					try {
						Date realTime = BBRUtil.convertDT(params.get("realTime"));
						float length = BBRUtil.convertF(params.get("length"));
						float discountPercent = BBRUtil.convertF(params.get("discountPercent"));
						float discountAmount = BBRUtil.convertF(params.get("discountAmount"));
						float pricePaid = BBRUtil.convertF(params.get("pricePaid"));
						float amountToSpecialist = BBRUtil.convertF(params.get("amountToSpecialist"));
						float amountToMaterials = BBRUtil.convertF(params.get("amountToMaterials"));
						String comment = params.get("comment");
						context.planningVisit = manager.createAndStoreVisit(pos, 
								null, 
								realTime, 
								proc, 
								spec, 
								userName, 
								userContacts,
								length,
								discountPercent, 
								discountAmount, 
								pricePaid, 
								amountToSpecialist, 
								amountToMaterials, 
								comment,
								procedures);
					} catch (Exception ex) {
						throw new Exception(BBRErrors.ERR_WRONG_INPUT_FORMAT);
					}
				} catch (Exception ex) {
					throw new Exception(BBRErrors.ERR_FILL_REQUIRED_FIELDS);
				}
			}
				
		
		return "";
	}

	@Override
	protected BBRVisit beforeUpdate(BBRVisit visit, BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String userName = params.get("userName");
			String userContacts = params.get("userContacts");
	
			BBRProcedureManager pmgr = new BBRProcedureManager();
			BBRProcedure proc = pmgr.findById(Long.parseLong(params.get("procedure")));
	
			BBRSpecialistManager smgr = new BBRSpecialistManager();
			BBRSpecialist spec = smgr.findById(Long.parseLong(params.get("spec")));

			Set<BBRProcedure> procedures = new HashSet<BBRProcedure>();
			String procs = params.get("procedures");
			if (!procs.equals("")) {
				for (String prc : procs.split(",")) {
					BBRProcedure procedure = pmgr.findById(Long.parseLong(prc));
					if (procedure != null) 
						procedures.add(procedure);
				}
			}

			try {
				float finalPrice = Float.parseFloat(params.get("finalPrice"));
				Date timeScheduled = BBRUtil.convertDT(params.get("timeScheduled"));
				Date realTime = BBRUtil.convertDT(params.get("realTime"));
				float length = BBRUtil.convertF(params.get("length"));
				float discountPercent = BBRUtil.convertF(params.get("discountPercent"));
				float discountAmount = BBRUtil.convertF(params.get("discountAmount"));
				float pricePaid = BBRUtil.convertF(params.get("pricePaid"));
				float amountToSpecialist = BBRUtil.convertF(params.get("amountToSpecialist"));
				float amountToMaterials = BBRUtil.convertF(params.get("amountToMaterials"));
				String comment = params.get("comment");
		
				visit.setUserName(userName);
				visit.setUserContacts(userContacts);
				visit.setFinalPrice(finalPrice);
				visit.setProcedure(proc);
				visit.setSpec(spec);
				visit.setTimeScheduled(timeScheduled);
				visit.setRealTime(realTime);
				visit.setLength(length);
				visit.setDiscountPercent(discountPercent);
				visit.setDiscountAmount(discountAmount);
				visit.setPricePaid(pricePaid);
				visit.setAmountToSpecialist(amountToSpecialist);
				visit.setAmountToMaterials(amountToMaterials);
				visit.setComment(comment);
				visit.setProcedures(procedures);
			} catch (Exception ex) {
				throw new Exception(BBRErrors.ERR_WRONG_INPUT_FORMAT);
			}
		} catch (Exception ex) {
			throw new Exception(BBRErrors.ERR_FILL_REQUIRED_FIELDS);
		}
		return visit;		
	}
	
	@Override
	protected String delete (BBRVisit visit, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		try {
			visit.setStatus(BBRVisitStatus.VISSTATUS_CANCELLED);
			manager.update(visit);
			return "";
		} catch (Exception ex) {
			return ex.getLocalizedMessage();
		}
	}
	
	@Override
	protected String getData(int pageNumber, int pageSize, 
								Hashtable<Integer, Hashtable<String, String>> columns, 
								Hashtable<Integer, Hashtable<String, String>> sortingFields, 
								BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		String[] userNC = (String[])context.get("visitsUserNC"); 
		if (userNC != null) {
			String userN = userNC[0];
			String userC = "";
			
			if (userNC.length == 2)
				userC = userNC[1];
			
			return manager.listVisitsByNameAndContacts(userN, userC, context.filterShop, context.filterPoS, context.filterStartDate, context.filterEndDate, 
													   pageNumber, pageSize, BBRContext.getOrderBy(sortingFields, columns)).toJson();
		}

		String[] datePos = (String[])context.get("visitsDatePos");
		if (datePos != null) {
			try {
				SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
				Date dt = df.parse(datePos[0]);
				
				BBRPoSManager pmgr = new BBRPoSManager();
				BBRPoS pos = pmgr.findById(Long.parseLong(datePos[1]));
				return manager.listVisitsByDateAndPos(dt, pos, pageNumber, pageSize, BBRContext.getOrderBy(sortingFields, columns)).toJson();
			} catch (Exception ex) {
				return "";
			}
		}

		String poss = (String)context.get("visitsPosId");
		if (poss != null) {
			try {
				BBRPoSManager pmgr = new BBRPoSManager();
				BBRPoS pos = pmgr.findById(Long.parseLong(poss));
				return manager.listUnapprovedVisitsByPos(pos, context.filterStartDate, context.filterEndDate, 
														 pageNumber, pageSize, BBRContext.getOrderBy(sortingFields, columns)).toJson();
			} catch (Exception ex) {
				return "";
			}
		}
		
		String myVisits = (String)context.get("visitsMy");
		if (myVisits != null && !myVisits.isEmpty() && context.user != null)
			return manager.list(context.user.getId(), context.filterShop, context.filterPoS, context.filterStartDate, context.filterEndDate,
							    pageNumber, pageSize, BBRContext.getOrderBy(sortingFields, columns)).toJson();
		
		try {
			if (context.filterPoS == null && context.filterShop == null) {
				if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
					context.filterShop = context.user.getShop();
				else
					if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN)
						context.filterPoS = context.user.getPos();
			}
				
			if (context.filterPoS != null || context.filterShop != null)
				return manager.listAllVisitsByFilter(context.filterShop, context.filterPoS, context.filterStartDate, context.filterEndDate, 
												 pageNumber, pageSize, BBRContext.getOrderBy(sortingFields, columns)).toJson();
			else
				return null;
		} catch (Exception ex) {
			return "";
		}
}
	
	@Override
	protected String cancel(String id, BBRParams params,	HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		context.setLastVisitStep(1);
		return "";
	}
	
	protected String getRecordData(String id, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		
		BBRVisit obj = (BBRVisit)manager.findById(Long.parseLong(id));
		if (obj != null) {
			if ((context.user.getRole() == BBRUserRole.ROLE_BBR_OWNER) ||
				((context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN) && (obj.getPos().getShop().getId() == context.user.getShop().getId())) ||
				((context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN) && (obj.getPos().getId() == context.user.getPos().getId())) ||
				((context.user.getRole() == BBRUserRole.ROLE_POS_SPECIALIST) && (obj.getPos().getId() == context.user.getPos().getId())) ||
				((context.user.getRole() == BBRUserRole.ROLE_VISITOR) && (obj.getUser().getId() == context.user.getId())))
				return obj.toJson();
			else
				return context.gs(BBRErrors.ERR_RECORD_NOTFOUND, manager.getClassTitle());
		}
		else
			return context.gs(BBRErrors.ERR_RECORD_NOTFOUND, manager.getClassTitle());
	}
	
	@Override
	protected String processOperation(String operation, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		if (operation.equals("approve")) {
			Long visitId = Long.parseLong(params.get("visitId"));
			BBRVisit visit = manager.findById(visitId);
			manager.approve(visit);
		} else
		if (operation.equals("disapprove")) {
			Long visitId = Long.parseLong(params.get("visitId"));
			BBRVisit visit = manager.findById(visitId);
			manager.disapprove(visit);			
		} else
		if (operation.equals("close")) {
			Long visitId = Long.parseLong(params.get("visitId"));
			BBRVisit visit = manager.findById(visitId);
			manager.close(visit);
		} else
		if (operation.equals("cancelVisit")) {
			Long visitId = Long.parseLong(params.get("visitId"));
			BBRVisit visit = manager.findById(visitId);
			manager.cancel(visit);
		} else
		if (operation.equals("createWizard")) {
			String userName = params.get("userName");
			String userContacts = params.get("userContacts");
			String comment = params.get("comment");
			String timeScheduledS = params.get("timeScheduled");
			try {
				SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat);
				Date timeScheduled = df.parse(timeScheduledS);

				Long posId = Long.parseLong(params.get("pos"));
				BBRPoSManager mgrPoS = new BBRPoSManager();
				BBRPoS pos = mgrPoS.findById(posId);
				if (pos == null)
					throw new Exception(BBRErrors.ERR_POS_NOTFOUND);
			
				BBRSpecialistManager mgrSpec;
				BBRSpecialist spec = null;
				BBRProcedureManager mgrProc;
				BBRProcedure proc = null;
				
				String specIdS = params.get("spec");
				String procIdS = params.get("proc");
				if (specIdS != null && !specIdS.isEmpty()) {
					Long specId = Long.parseLong(specIdS);
					mgrSpec = new BBRSpecialistManager();
					spec = mgrSpec.findById(specId);
				}
				
				if (procIdS != null && !procIdS.isEmpty()) {
					Long procId = Long.parseLong(params.get("proc"));
					mgrProc = new BBRProcedureManager();
					proc = mgrProc.findById(procId);
				}

				if (spec == null && proc == null)
					throw new Exception(BBRErrors.ERR_RECORD_NOTFOUND);

				if (proc != null) {
					spec = manager.findSpecByTimeAndProc(timeScheduled, proc, pos);
				}
				
				BBRVisit visit = manager.scheduleVisit(true, pos, null, timeScheduled, 
						         proc, spec, userName, userContacts, comment);
				return visit.toJson();
						
			} catch (Exception ex) {
				return "";
			}
		} else
		if (operation.equals("checkBookingCode")) {
			BBRVisit visit;
			if (params.get("code").equals(""))
				visit = null;
			visit = manager.findByBookingCode(params.get("code"));
			if (visit != null)
				return visit.toJson();
			else
				return "";
		} else
		if (operation.equals("getVisitsNumber")) {
			String userContacts = params.get("userContacts").trim(); 
			String posId = params.get("posId").trim();
			
			BBRContext context = BBRContext.getContext(request);
			if (userContacts.equals(""))
				return "";
			else {
				Long l = manager.getVisitsNumber(userContacts);
				String prizeString = "";
				try {
					if (manager.isPrizeVisit(l, Long.parseLong(posId)))
						prizeString = context.gs("MSG_PRIZE_VISIT");
					String d[] = new String[2];
					d[0] = l.toString();
					d[1] = prizeString;
					return BBRUtil.gson().toJson(d);
				} catch (Exception ex) {
					return "";
				}
			}
		}
		return "";
	};

	@Override
	protected String getBadgeNumber(BBRParams params, HttpServletRequest request,
			HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		String where = "";
		if (context.user.getRole() == BBRUserRole.ROLE_BBR_OWNER)
			return "0";
		
		if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN)
			if (context.user.getPos() != null)
				where = manager.wherePos(context.user.getPos().getId());
		if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
			if (context.user.getShop() != null)
				where = manager.whereShop(context.user.getShop().getId());
		if (!where.equals("")) 
			where = "(" + where +") and";
		Date dt = BBRUtil.now(context.getTimeZone());
		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat);
		where += "(status = " + BBRVisitStatus.VISSTATUS_INITIALIZED + ") and (timeScheduled >= '" + df.format(BBRUtil.getStartOfDay(dt)) + "')";

		String count1 = manager.count(where).toString();
		
        where += " and (timeScheduled <= '" + df.format(BBRUtil.getEndOfDay(dt)) + "')";
		String count2 = manager.count(where).toString();
	
		return count2 + " / " + count1;
	}

}
