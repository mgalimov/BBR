import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRErrors;
import BBR.BBRUtil;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;
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
			BBRParams params = new BBRParams(request.getQueryString());

			respText = "";
			
			String operation = params.get("operation");
			BBRVisitManager mgr = new BBRVisitManager();
			Date dateSelected;
			DateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
			try {
				dateSelected = df.parse(params.get("date"));
			} catch (Throwable ex) {
				throw new Exception(BBRErrors.ERR_DATE_INCORRECT);
			}
			String posId = params.get("pos");
			
			if (operation == null || operation.isEmpty() || operation.equals("schedule")) {
				String procId = params.get("proc");
				if (posId == null || posId.isEmpty()) {
					if (context.planningVisit != null)
						posId = context.planningVisit.getPos().getId().toString();
				}
				
				BBRScheduleList list = mgr.getSchedule(dateSelected, posId, procId);
				respText = BBRUtil.gson().toJson(list);
			} else
			if (operation.equals("freetimes")) {
				String specId = params.get("spec");
				String procId = params.get("proc");
				List<String> freeTimes = null;
				if (specId != null && !specId.isEmpty()) {
					freeTimes = mgr.getFreeTimesBySpec(dateSelected, posId, specId);
				} else
					if (procId != null && !procId.isEmpty()) {
						freeTimes = mgr.getFreeTimesByProc(dateSelected, posId, procId);
					}
				if (freeTimes != null && freeTimes.size() > 0) {
					for (String s : freeTimes) {
						respText += "*" + s;
					}
					respText = respText.substring(1);
				} else 
					respText = "";

			}
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
