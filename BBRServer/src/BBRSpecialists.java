import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRErrors;
import BBRAcc.BBRPoS;
import BBRAcc.BBRPoSManager;
import BBRAcc.BBRUser;
import BBRAcc.BBRUserManager;
import BBRClientApp.BBRManagementApplication;

/**
 * Servlet implementation class BBRBackend
 */
@WebServlet("/BBRSpecialists")
public class BBRSpecialists extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BBRSpecialists() {
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
				respText = app.getSpecData(Long.parseLong(id));
			} else
			if (operation.equals("delete")) {
				respText = app.deleteSpec(Long.parseLong(id));
			} else
			if (operation.equals("update")) {
				String name = params.get("name");
				String position = params.get("position");
				String posId = params.get("pos");
				String usrId = params.get("usr");
				BBRPoSManager mgrPos = new BBRPoSManager();
				BBRPoS pos = mgrPos.findById(Long.parseLong(posId));
				BBRUserManager mgrUsr = new BBRUserManager();
				BBRUser usr = mgrUsr.findById(Long.parseLong(usrId));
				if (pos != null && usr != null) {						
					respText = app.updateSpec(Long.parseLong(id), name, position, usr, pos);
				} else {
					respText = BBRErrors.ERR_POS_NOTFOUND;
					response.setStatus(700);
				}
						
			} else
			if (operation.equals("create")) {
				String name = params.get("name");
				String position = params.get("position");
				String posId = params.get("pos");
				String usrId = params.get("usr");
				BBRPoSManager mgrPos = new BBRPoSManager();
				BBRPoS pos = mgrPos.findById(Long.parseLong(posId));
				BBRUserManager mgrUsr = new BBRUserManager();
				BBRUser usr = mgrUsr.findById(Long.parseLong(usrId));
				if (pos != null && usr != null) {						
					respText = app.createSpec(name, position, usr, pos);
				} else {
					respText = BBRErrors.ERR_POS_NOTFOUND;
					response.setStatus(700);
				}
			} else
			if (operation.equals("reference")) {
				String q = params.get("q");
				respText = app.getSpecs(q);
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
			
			respText = app.getSpecs(Integer.parseInt(pageNum), Integer.parseInt(rowsPerPage), sortingFields);
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
			response.setStatus(700);
		}
		
		response.setContentType("text/plain");  
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().write(respText); 
}

}
