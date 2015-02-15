

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBRClientApp.BBRAdminApplication;

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
		BBRAdminApplication app = BBRAdminApplication.getApp(request);
		BBRParams params = new BBRParams(request.getQueryString());
		String email = params.get("email");
		String password = params.get("password");
		String passwordCopy = params.get("passwordCopy");
		String firstName = params.get("firstName");
		String lastName = params.get("lastName");

		app.SignUp(email, firstName, lastName, password, passwordCopy);
		
		response.sendRedirect(request.getContextPath() + "/" + app.getWelcomePage());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
