import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import BBR.*;

/**
 * Servlet implementation class BBRBackend
 */
@WebServlet("/BBRBackend")
public class BBRBackend extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BBRBackend() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BBRUserManager mgr = new BBRUserManager();
		String email = request.getParameter("email");;
		String respText = "";
		
		if (email == null || email == "") {
			respText = BBRErrors.ERR_EMPTY_EMAIL;
		} else {
			BBRUser user = mgr.findUserByEmail(email);
			if (user == null) {
				respText = BBRErrors.ERR_USER_NOTFOUND;
			} else {
				respText = BBRErrors.MSG_USER_FOUND;
			}
		}
		
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
