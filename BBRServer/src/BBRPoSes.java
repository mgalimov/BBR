import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBRClientApp.BBRAdminApplication;

/**
 * Servlet implementation class BBRBackend
 */
@WebServlet("/BBRPoSes")
public class BBRPoSes extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BBRPoSes() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String respText = "";
		try {
			BBRAdminApplication app = BBRAdminApplication.getApp(request);
			BBRParams params = new BBRParams(request.getQueryString());
			String id = params.get("id");
			String operation = params.get("operation");
			
			if (operation.equals("getdata")) {
				respText = app.getPoSData(Long.parseLong(id));
			} else
			if (operation.equals("delete")) {
				respText = app.deletePoS(Long.parseLong(id));
			} else
			if (operation.equals("update")) {
				String shopId = params.get("shop");
				String title = params.get("title");
				String locationDescription = params.get("locationDescription");
				respText = app.updatePoS(Long.parseLong(id), Long.parseLong(shopId), title, locationDescription);
			} else
			if (operation.equals("create")) {
				String shopId = params.get("shop");
				String title = params.get("title");
				String locationDescription = params.get("locationDescription");
					respText = app.createPoS(Long.parseLong(shopId), title, locationDescription);
			} else
			if (operation.equals("reference")) {
				String q = params.get("q");
				respText = app.getPoSes(q);
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
			BBRAdminApplication app = BBRAdminApplication.getApp(request);
			
			BBRParams params = new BBRParams(request.getReader());
			String pageNum = params.get("page_num");
			String rowsPerPage = params.get("rows_per_page");
			Hashtable<Integer, Hashtable<String, String>> sortingFields = params.getArray("sorting");
			
			respText = app.getPoSes(Integer.parseInt(pageNum), Integer.parseInt(rowsPerPage), sortingFields);
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
			response.setStatus(700);
		}
		
		response.setContentType("text/plain");  
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().write(respText); 
	}

}
