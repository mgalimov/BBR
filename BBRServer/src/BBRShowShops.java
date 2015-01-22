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
@WebServlet("/BBRShowShops")
public class BBRShowShops extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BBRShowShops() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BBRShopManager mgr = new BBRShopManager();
		String json = mgr.listShops().toJson();
		
//		String json = "{\"total_rows\":\"2\",\"page_data\":[{\"id\":\"1\",\"title\":\"My Shop\"},{\"id\":\"2\",\"title\":\"My Shop\"}]}";
		
		response.setContentType("text/plain");  
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().write(json); 
}

}
