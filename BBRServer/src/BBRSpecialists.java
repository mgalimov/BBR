import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
import BBRCust.BBRProcedureManager;

@WebServlet("/BBRSpecialists")
public class BBRSpecialists extends BBRBasicServlet<BBRSpecialist, BBRSpecialistManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRSpecialists() throws InstantiationException, IllegalAccessException {
        super(BBRSpecialistManager.class);
    }

	@Override
	protected String create(BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String name = params.get("name");
		String position = params.get("position");
		String posId = params.get("pos");
		String startWorkHour = params.get("startWorkHour");
		String endWorkHour = params.get("endWorkHour");
		String procs = params.get("procedures");
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
			} else
				throw new Exception(BBRErrors.ERR_PROC_NOTSPECIFIED);
			
			manager.createAndStoreSpecialist(name, position, null, pos, status, startWH, endWH, procedures);
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
		int status = Integer.parseInt(params.get("status"));
		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullTimeFormat);
		BBRPoSManager mgrPos = new BBRPoSManager();
		BBRPoS pos = mgrPos.findById(Long.parseLong(posId));
		if (pos != null) {						
			spec.setName(name);
			spec.setPosition(position);
			spec.setPos(pos);
			spec.setUser(null);
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
			} else
				throw new Exception(BBRErrors.ERR_PROC_NOTSPECIFIED);
			spec.setProcedures(procedures);
			
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
			if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN || context.user.getRole() == BBRUserRole.ROLE_POS_SPECIALIST )
				pos = context.user.getPos();
			else
				if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
					shop = context.user.getShop();
		
		return manager.list(query, manager.getTitleField(), pos, shop).toJson();
	}
}
