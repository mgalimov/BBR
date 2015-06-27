import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBRAcc.BBRPoS;
import BBRAcc.BBRPoSManager;
import BBRClientApp.BBRContext;
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
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
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
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
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
			}
			spec.setProcedures(procedures);
			
			manager.update(spec);
		}
		return null;		
	}
	
	@Override
	protected String getReferenceData(String query, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);

		if (context.planningVisit != null)
			return manager.list(query, manager.getTitleField(), context.planningVisit.getPos()).toJson();
		else
			return manager.list(query, manager.getTitleField()).toJson();
	}
}
