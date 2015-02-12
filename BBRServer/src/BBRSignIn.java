import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import BBRClientApp.BBRApplication;

/**
 * Servlet implementation class BBRSignIn
 */
@WebServlet("/BBRSignIn")
public class BBRSignIn extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BBRSignIn() {
        super();
    }

    // Signing in
    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BBRApplication app = BBRApplication.getApp(request);
		BBRParams params = new BBRParams(request.getQueryString());
		String email = params.get("email");
		String password = params.get("password");
		String rememberme = params.get("rememberme");
		app.SignIn(email, password);
		
		if (app.user != null) {
			if (rememberme != null)
				if (!rememberme.equals("")) {
					Cookie c = new Cookie("email", email);
					c.setMaxAge(24*60*30);
					response.addCookie(c);
					
					c = new Cookie("pwdhash", app.user.getEncodedPassword());
					c.setMaxAge(24*60*30);
					response.addCookie(c);
				}
		}
		response.sendRedirect(request.getContextPath() + "/" + app.getWelcomePage());
	}

	// Signing out
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BBRApplication app = BBRApplication.getApp(request);
		app.SignOut(response);
		response.sendRedirect(request.getContextPath() + "/" + app.getWelcomePage());
	}

}
