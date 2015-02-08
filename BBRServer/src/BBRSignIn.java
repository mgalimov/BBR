import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.*;
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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BBRApplication app = BBRApplication.getApp(request);
		BBRParams params = new BBRParams(request.getQueryString());
		String email = params.get("email");
		String password = params.get("password");
		String respText = app.SignIn(email, password);
		
		if (app.user != null) {
			response.setContentType("text/plain; charset=utf-8");  
			response.setCharacterEncoding("UTF-8"); 
			response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
			response.setHeader("Location", request.getPathInfo() + "/" + app.getWelcomePage());
			
			//response.sendRedirect(request.getContextPath() + "/" + app.getWelcomePage());
		}
		else
		{
			response.setContentType("text/plain; charset=utf-8");  
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
