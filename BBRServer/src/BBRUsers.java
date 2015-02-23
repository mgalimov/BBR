import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBRClientApp.BBRAdminApplication;

/**
 * Servlet implementation class BBRUserUpdate
 */
@WebServlet("/BBRUsers")
public class BBRUsers extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BBRUsers() {
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
				respText = app.getUserData(Long.parseLong(id));
			} else
			if (operation.equals("delete")) {
				respText = app.deleteUser(Long.parseLong(id));
			} else
			if (operation.equals("update")) {
				String firstName = params.get("firstName");
				String lastName = params.get("lastName");
				String approved = params.get("approved");
				String role = params.get("role");
				respText = app.updateUser(Long.parseLong(id), firstName, lastName, Boolean.parseBoolean(approved), Integer.valueOf(role));
			} else
			if (operation.equals("create")) {
				String email = params.get("email");
				String firstName = params.get("firstName");
				String lastName = params.get("lastName");
				String password = params.get("password");
				respText = app.createUser(email, firstName, lastName, password);
			} else
				respText = "Unknown operation";
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
	        
			respText = app.getUsers(Integer.parseInt(pageNum), Integer.parseInt(rowsPerPage), sortingFields);
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
			response.setStatus(700);
		}
		
		response.setContentType("text/plain");  
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().write(respText); 
	}

}
