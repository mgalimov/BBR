import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRErrors;
import BBR.BBRUtil;
import BBRAcc.BBRPoS;
import BBRAcc.BBRPoSManager;
import BBRAcc.BBRShop;
import BBRAcc.BBRUser.BBRUserRole;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;
import BBRCust.BBRSpecialist;
import BBRCust.BBRSpecialistManager;
import BBRCust.BBRProcedure;
import BBRCust.BBRProcedureGroup;
import BBRCust.BBRProcedureGroupManager;
import BBRCust.BBRProcedureManager;

@WebServlet("/BBRSpecialists")
@MultipartConfig
public class BBRSpecialists extends BBRBasicServlet<BBRSpecialist, BBRSpecialistManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRSpecialists() throws InstantiationException, IllegalAccessException {
        super(BBRSpecialistManager.class);
    }

	@Override
	protected String create(BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String name = params.get("name");
		String position = params.get("position");
		String dailyAmountS = params.get("dailyAmount");
		String procedurePercentS = params.get("procedurePercent");
		Float dailyAmount = null;
		Float procedurePercent = null;
		try {
			dailyAmount = BBRUtil.convertF(dailyAmountS);
			procedurePercent = BBRUtil.convertF(procedurePercentS);
			if (procedurePercent < 0 || procedurePercent > 100 || dailyAmount < 0)
				throw new Exception(BBRErrors.ERR_WRONG_INPUT_FORMAT);
		} catch (Exception ex) {
			throw new Exception(BBRErrors.ERR_WRONG_INPUT_FORMAT);
		}
		String posId = params.get("pos");
		String startWorkHour = params.get("startWorkHour");
		String endWorkHour = params.get("endWorkHour");
		String procs = params.get("procedures");
		String procGroups = params.get("procedureGroups");
		Date startWH = null;
		Date endWH = null;
		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullTimeFormat);
		int status = Integer.parseInt(params.get("status"));
		BBRPoSManager mgrPos = new BBRPoSManager();
		BBRPoS pos = mgrPos.findById(Long.parseLong(posId));
		if (pos != null) {	
			if (!startWorkHour.equals(""))
				startWH = df.parse(startWorkHour);
			if (!endWorkHour.equals(""))
				endWH = df.parse(endWorkHour);
			
			Set<BBRProcedure> procedures = new HashSet<BBRProcedure>();
			BBRProcedureManager pmgr = new BBRProcedureManager();
			if (!procs.equals("")) {
				for (String proc : procs.split(",")) {
					BBRProcedure procedure = pmgr.findById(Long.parseLong(proc));
					if (procedure != null) 
						procedures.add(procedure);
				}
			}

			Set<BBRProcedureGroup> procedureGroups = new HashSet<BBRProcedureGroup>();
			BBRProcedureGroupManager pgmgr = new BBRProcedureGroupManager();
			if (!procGroups.equals("")) {
				for (String procGroup : procGroups.split(",")) {
					BBRProcedureGroup procedureGroup = pgmgr.findById(Long.parseLong(procGroup));
					if (procedureGroups != null) 
						procedureGroups.add(procedureGroup);
				}
			}

			if (procGroups.equals("") && procs.equals(""))
				throw new Exception(BBRErrors.ERR_PROC_NOTSPECIFIED);

			BBRSpecialist spec = manager.create(name, position, dailyAmount, procedurePercent, 
												null, pos, status, startWH, endWH, procedures, procedureGroups);
			return spec.getId().toString();
		}
		return "";
	}

	@Override
	protected BBRSpecialist beforeUpdate(BBRSpecialist spec, BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String name = params.get("name");
		String position = params.get("position");
		String posId = params.get("pos");
		String startWorkHour = params.get("startWorkHour");
		String endWorkHour = params.get("endWorkHour");
		String procs = params.get("procedures");
		String procGroups = params.get("procedureGroups");
		int status = Integer.parseInt(params.get("status"));
		String dailyAmountS = params.get("dailyAmount");
		String procedurePercentS = params.get("procedurePercent");
		Float dailyAmount = null;
		Float procedurePercent = null;
		try {
			dailyAmount = BBRUtil.convertF(dailyAmountS);
			procedurePercent = BBRUtil.convertF(procedurePercentS);
			if (procedurePercent < 0 || procedurePercent > 100 || dailyAmount < 0)
				throw new Exception(BBRErrors.ERR_WRONG_INPUT_FORMAT);
		} catch (Exception ex) {
			throw new Exception(BBRErrors.ERR_WRONG_INPUT_FORMAT);
		}
		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullTimeFormat);
		BBRPoSManager mgrPos = new BBRPoSManager();
		BBRPoS pos = mgrPos.findById(Long.parseLong(posId));
		if (pos != null) {						
			spec.setName(name);
			spec.setPosition(position);
			spec.setPos(pos);
			spec.setUser(null);
			spec.setDailyAmount(dailyAmount);
			spec.setProcedurePercent(procedurePercent);
			if (!startWorkHour.equals(""))
				spec.setStartWorkHour(df.parse(startWorkHour));
			if (!endWorkHour.equals(""))
				spec.setEndWorkHour(df.parse(endWorkHour));
			spec.setStatus(status);
			
			Set<BBRProcedure> procedures = new HashSet<BBRProcedure>();
			BBRProcedureManager pmgr = new BBRProcedureManager();
			if (!procs.equals("")) {
				for (String proc : procs.split(",")) {
					BBRProcedure procedure = pmgr.findById(Long.parseLong(proc));
					if (procedure != null) 
						procedures.add(procedure);
				}
			}

			Set<BBRProcedureGroup> procedureGroups = new HashSet<BBRProcedureGroup>();
			BBRProcedureGroupManager pgmgr = new BBRProcedureGroupManager();
			if (!procGroups.equals("")) {
				for (String procGroup : procGroups.split(",")) {
					BBRProcedureGroup procedureGroup = pgmgr.findById(Long.parseLong(procGroup));
					if (procedureGroups != null) 
						procedureGroups.add(procedureGroup);
				}
			}

			if (procGroups.equals("") && procs.equals(""))
				throw new Exception(BBRErrors.ERR_PROC_NOTSPECIFIED);
			
			spec.setProcedures(procedures);
			spec.setProcedureGroups(procedureGroups);
			
			manager.update(spec);
		}
		return null;		
	}
	
	@Override
	protected String getReferenceData(String query, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		String constrains = params.get("constrains");
		BBRPoS pos = null;
		BBRShop shop = null;

		if (context.planningVisit != null)
			pos = context.planningVisit.getPos();
		else
			if (constrains != null && !constrains.isEmpty()) {
				BBRPoSManager pmgr = new BBRPoSManager();
				pos = pmgr.findById(Long.parseLong(constrains));
			}
		if (pos == null)
			if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN || context.user.getRole() == BBRUserRole.ROLE_POS_SPECIALIST)
				pos = context.user.getPos();
			else
				if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
					shop = context.user.getShop();
		
		return manager.list(query, manager.getTitleField(), pos, shop, true).toJson();
	}
}
