import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;

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
		try {
			BBRContext context = BBRContext.getContext(request);
			BBRParams params = new BBRParams(request.getQueryString());
			String email = params.get("email");
			String password = params.get("password");
			String rememberme = params.get("rememberme");
			String timezone = params.get("timezone");
			for (int i = 1; i <= 5; i++) {
				try {
					context.SignIn(email, password, timezone);
					break;
				} catch (Exception ex) {
					Thread.sleep(5000);
				}
			}
			
			if (context.user != null) {
				if (rememberme != null)
					if (!rememberme.equals("")) {
						Cookie c = new Cookie("email", email);
						c.setMaxAge(24*60*30);
						response.addCookie(c);
						
						c = new Cookie("pwdhash", context.user.getEncodedPassword());
						c.setMaxAge(24*60*30);
						response.addCookie(c);
						
						c = new Cookie("timezone", context.getTimeZone());
						c.setMaxAge(24*60*30);
						response.addCookie(c);
					}
			}
			response.sendRedirect(request.getContextPath() + "/" + context.getWelcomePage());
		} catch (Exception ex) {
			String respText = ex.getLocalizedMessage();
			if (respText == null)
				respText = "";
			response.setStatus(700);
			response.setContentType("text/plain");  
			response.setCharacterEncoding("UTF-8"); 
			response.getWriter().write(respText);
		}
	}

	// Signing out
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BBRContext context = BBRContext.getContext(request);
		context.SignOut(request, response);
		response.sendRedirect(request.getContextPath() + "/" + context.getWelcomePage());
	}

}
