

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import BBRClientApp.BBRApplication;

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
		BBRApplication app = BBRApplication.GetApp(request);
		BBRParams params = new BBRParams(request.getQueryString());
		String id = params.get("id");
		String operation = params.get("operation");
		String respText = "";
		
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
			respText = app.updateUser(Long.parseLong(id), firstName, lastName, Boolean.parseBoolean(approved));
		} else
		if (operation.equals("create")) {
			String email = params.get("email");
			String firstName = params.get("firstName");
			String lastName = params.get("lastName");
			respText = app.createUser(email, firstName, lastName);
		} else
			respText = "Unknown operation";
						
		response.setContentType("text/plain");  
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().write(respText); 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BBRApplication app = BBRApplication.GetApp(request);
		String respText = app.getUsers();
		
		response.setContentType("text/plain");  
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().write(respText); 
	}

}
