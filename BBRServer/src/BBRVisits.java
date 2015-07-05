import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRErrors;
import BBRAcc.BBRPoS;
import BBRAcc.BBRPoSManager;
import BBRAcc.BBRUser.BBRUserRole;
import BBRClientApp.BBRContext;
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
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd H:mm");
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

			Long id = Long.parseLong(manager.createAndStoreVisit(
					context.planningVisit.getPos(), 
					context.user, 
					context.planningVisit.getTimeScheduled(), 
					context.planningVisit.getProcedure(), 
					context.planningVisit.getSpec(), 
					context.planningVisit.getUserName(),
					context.planningVisit.getUserContacts()));
			
			context.planningVisit = manager.findById(id);
		}

		context.setLastVisitStep(++visitStep);
		return "";
	}

	@Override
	protected BBRVisit beforeUpdate(BBRVisit visit, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		visit.setStatus(BBRVisitStatus.VISSTATUS_APPROVED);
		manager.update(visit);
		return null;		
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
		String[] userNC = (String[])request.getAttribute("userNC"); 
		
		if (userNC != null) {
			return manager.listVisitsByNameAndContacts(userNC[0], userNC[1], pageNumber, pageSize, BBRContext.getOrderBy(sortingFields, columns)).toJson();
		}
		else
			if (context.user != null)
				return manager.list(context.user.getId(), pageNumber, pageSize, BBRContext.getOrderBy(sortingFields, columns)).toJson();
			else
				return "";
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
}
