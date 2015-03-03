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
import BBRClientApp.BBRContext;
import BBRCust.BBRProcedure;
import BBRCust.BBRProcedureManager;
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
	String create(BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		BBRContext context = BBRContext.getContext(request);
		String userName = params.get("userName");
		String userContacts = params.get("userContacts");

		BBRProcedure proc = null;
		
		if (!params.get("procedure").isEmpty()) {
			Long procedureId = Long.parseLong(params.get("procedure"));

			BBRProcedureManager mgrProc = new BBRProcedureManager();
			proc = mgrProc.findById(procedureId);						
		}
		
		Long posId = Long.parseLong(params.get("pos"));

		BBRPoSManager mgrPoS = new BBRPoSManager();
		BBRPoS pos = mgrPoS.findById(posId);
		if (pos == null)
			throw new Exception(BBRErrors.ERR_POS_NOTFOUND);

		DateFormat df = new SimpleDateFormat("y-M-d H:mm");
		Date timeScheduled = df.parse(params.get("timeScheduled"));
		
		context.setLastVisitScheduled(manager.createAndStoreVisit(pos, context.user, timeScheduled, proc, userName, userContacts));
		
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
	protected String getData(int pageNumber, int pageSize, Hashtable<Integer, Hashtable<String, String>> sortingFields, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		if (context.user != null)
			return manager.list(context.user.getId(), pageNumber, pageSize, BBRContext.getOrderBy(sortingFields)).toJson();
		else
			return "";
	}
}
