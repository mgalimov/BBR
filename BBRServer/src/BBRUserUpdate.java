

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
@WebServlet("/BBRUserUpdate")
public class BBRUserUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BBRUserUpdate() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BBRApplication app = BBRApplication.GetApp(request);
		String id = request.getParameter("id");
		String operation = request.getParameter("operation");
		String respText = "";
		
		if (operation.equals("getdata")) {
			respText = app.getUserData(Long.parseLong(id));
		} else
		if (operation.equals("delete")) {
			respText = app.deleteUser(Long.parseLong(id));
		} else
		if (operation.equals("update")) {
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String approved = request.getParameter("approved");
			respText = app.updateUser(Long.parseLong(id), firstName, lastName, Boolean.parseBoolean(approved));
		} else
				
			// TODO: rework!
				respText = "Unknown operation";
						
		response.setContentType("text/plain");  
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().write(respText); 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
