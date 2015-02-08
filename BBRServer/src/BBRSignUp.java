

import java.io.IOException;
import java.net.URLDecoder;
import java.security.Policy.Parameters;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBRClientApp.BBRApplication;

/**
 * Servlet implementation class BBRSignUp
 */
@WebServlet("/BBRSignUp")
public class BBRSignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BBRSignUp() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BBRApplication app = BBRApplication.getApp(request);
		BBRParams params = new BBRParams(request.getQueryString());
		String email = params.get("email");
		String password = params.get("password");
		String firstName = params.get("firstName");
		String lastName = params.get("lastName");
		String respText = app.SignUp(email, firstName, lastName, password);
		
		if (app.user != null) {
			response.sendRedirect(request.getContextPath() + "/" + app.getWelcomePage());
		} else
		{
			response.setContentType("text/plain");  
			response.setCharacterEncoding("UTF-8"); 
			response.getWriter().write(respText); 	
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
