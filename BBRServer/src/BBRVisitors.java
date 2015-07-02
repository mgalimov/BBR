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
import BBRCust.BBRSpecialist;
import BBRCust.BBRSpecialistManager;
import BBRCust.BBRVisitManager;
import BBRCust.BBRVisit;
import BBRCust.BBRVisit.BBRVisitStatus;

@WebServlet("/BBRVisits")
public class BBRVisitors extends BBRBasicServlet<BBRVisit, BBRVisitManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRVisitors() throws InstantiationException, IllegalAccessException {
        super(BBRVisitManager.class);
    }

	@Override
	protected String getData(int pageNumber, int pageSize, 
								Hashtable<Integer, Hashtable<String, String>> columns, 
								Hashtable<Integer, Hashtable<String, String>> sortingFields, 
								BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		if (context.user != null)
			return manager.listVisitors(pageNumber, pageSize, BBRContext.getOrderBy(sortingFields, columns)).toJson();
		else
			return "";
	}

}
