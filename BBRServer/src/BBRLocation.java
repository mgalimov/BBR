

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRGPS;
import BBRClientApp.BBRContext;

/**
 * Servlet implementation class BBRLocation
 */
@WebServlet("/BBRLocation")
public class BBRLocation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BBRLocation() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String respText = "";
		try {
			BBRContext context = BBRContext.getContext(request);
			BBRParams params = new BBRParams(request.getQueryString());
			float lat = Float.parseFloat(params.get("lat"));
			float lng = Float.parseFloat(params.get("lng"));
			context.setLocation(new BBRGPS(lat, lng));
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
	}

}
