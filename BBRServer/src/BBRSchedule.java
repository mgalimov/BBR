import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRErrors;
import BBR.BBRUtil;
import BBRClientApp.BBRContext;
import BBRCust.BBRVisitManager;
import BBRCust.BBRVisitManager.BBRScheduleList;

@WebServlet("/BBRSchedule")
public class BBRSchedule extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public BBRSchedule() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String respText = "";
		try {
			BBRContext context = BBRContext.getContext(request);
			BBRVisitManager mgr = new BBRVisitManager();
			BBRParams params = new BBRParams(request.getQueryString());
			Date dateSelected;
			DateFormat df = new SimpleDateFormat("MM/dd/yy");
			respText = "";
			
			try {
				dateSelected = df.parse(params.get("date"));
			} catch (Throwable ex) {
				throw new Exception(BBRErrors.ERR_DATE_INCORRECT);
			}
			
			String specId = params.get("spec");
			BBRScheduleList list = mgr.getSchedule(dateSelected, context.planningVisit.getPos().getId().toString(), specId);
			respText = BBRUtil.gson.toJson(list);
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
			response.setStatus(700);
		}
		response.setContentType("text/plain");  
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().write(respText);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
