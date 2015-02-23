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
import BBRClientApp.BBRContext;
import BBRClientApp.BBRManagementApplication;

/**
 * Servlet implementation class BBRBackend
 */
@WebServlet("/BBRVisit")
public class BBRVisit extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BBRVisit() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String respText = "";
		try {
			BBRManagementApplication app = BBRManagementApplication.getApp(request);
			BBRParams params = new BBRParams(request.getQueryString());
			String id = params.get("id");
			String operation = params.get("operation");
			
			if (operation.equals("getdata")) {
				respText = app.getVisitData(Long.parseLong(id));
			} else
			if (operation.equals("delete") || operation.equals("cancel")) {
				respText = app.cancelVisit(Long.parseLong(id));
			} else
			if (operation.equals("update")  || operation.equals("approve")) {
				respText = app.approveVisit(Long.parseLong(id));
			} else
			if (operation.equals("create")) {
				BBRContext context = BBRContext.getContext(request);
				String userName = params.get("userName");
				String userContacts = params.get("userContacts");
				String procedure = params.get("procedure");
				try {
					BBRPoSManager posmgr = new BBRPoSManager();
					Long posId = Long.parseLong(params.get("timeScheduled"));
					BBRPoS pos = posmgr.findById(posId);
					if (pos == null)
						throw new Exception(BBRErrors.ERR_POS_NOTFOUND);
					
					DateFormat df = new SimpleDateFormat("YYYY-MM-DD kk:mm");
					Date timeScheduled = df.parse(params.get("timeScheduled"));
					Long userId = null;
					if (context.user != null)
						userId = context.user.getId();
					
					respText = app.createVisit(posId, timeScheduled, procedure, userName, userContacts, userId);
				} catch (Exception ex) {
					respText = ex.getLocalizedMessage();
					response.setStatus(700);
				}
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
		String respText = "";
		try {
			BBRManagementApplication app = BBRManagementApplication.getApp(request);
			
			BBRParams params = new BBRParams(request.getReader());
			String pageNum = params.get("page_num");
			String rowsPerPage = params.get("rows_per_page");
			Hashtable<Integer, Hashtable<String, String>> sortingFields = params.getArray("sorting");
			
			respText = app.getVisits(Integer.parseInt(pageNum), Integer.parseInt(rowsPerPage), sortingFields);
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
			response.setStatus(700);
		}
		
		response.setContentType("text/plain");  
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().write(respText); 
	}

}
