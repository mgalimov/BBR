import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRErrors;
import BBRAcc.BBRPoS;
import BBRAcc.BBRPoSManager;
import BBRClientApp.BBRAdminApplication;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRManagementApplication;

/**
 * Servlet implementation class BBRBackend
 */
@WebServlet("/BBRVisit")
public class BBRProcedures extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BBRProcedures() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String respText = "";
		try {
			BBRManagementApplication app = BBRManagementApplication.getApp(request);
			BBRAdminApplication appAdmin = BBRAdminApplication.getApp(request);
			BBRParams params = new BBRParams(request.getQueryString());
			String id = params.get("id");
			String operation = params.get("operation");
			
			if (operation.equals("getdata")) {
				respText = app.getProcedureData(Long.parseLong(id));
			} else
			if (operation.equals("delete")) {
				respText = app.deleteProcedure(Long.parseLong(id));
			} else
			if (operation.equals("update")) {
				String title = params.get("title");
				String posId = params.get("pos");
				appAdmin.
				
				String length = params.get("length");
				String price = params.get("price");
				String currency = params.get("currency");
				String status = params.get("status");
				respText = app.updateProcedure(Long.parseLong(id));
			} else
			if (operation.equals("create")) {
				String title = params.get("title");
				String posId = params.get("pos");
				String length = params.get("length");
				String price = params.get("price");
				String currency = params.get("currency");
				String status = params.get("status");
				respText = app.createProcedure(pos, timeScheduled, procedure, userName, userContacts, userId);
			} else {
				respText = "Unknown operation";
				response.setStatus(700);
			}
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
			response.setStatus(700);
		}
		response.setContentType("text/plain");  
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().write(respText); 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BBRContext context = BBRContext.getContext(request);
		String respText = "";
		try {
			BBRManagementApplication app = BBRManagementApplication.getApp(request);
			
			BBRParams params = new BBRParams(request.getReader());
			String pageNum = params.get("page_num");
			String rowsPerPage = params.get("rows_per_page");
			Hashtable<Integer, Hashtable<String, String>> sortingFields = params.getArray("sorting");
			
			if (context.user != null)
				respText = app.getVisits(context.user.getId(), Integer.parseInt(pageNum), Integer.parseInt(rowsPerPage), sortingFields);
			else
				throw new Exception(BBRErrors.ERR_USER_NOTSPECIFIED);
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
			response.setStatus(700);
		}
		
		response.setContentType("text/plain");  
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().write(respText); 
	}

}
