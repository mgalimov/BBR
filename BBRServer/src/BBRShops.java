import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBRClientApp.BBRApplication;

/**
 * Servlet implementation class BBRBackend
 */
@WebServlet("/BBRShops")
public class BBRShops extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BBRShops() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BBRApplication app = BBRApplication.getApp(request);
		BBRParams params = new BBRParams(request.getQueryString());
		String id = params.get("id");
		String operation = params.get("operation");
		String respText = "";
		
		if (operation.equals("getdata")) {
			respText = app.getShopData(Long.parseLong(id));
		} else
		if (operation.equals("delete")) {
			respText = app.deleteShop(Long.parseLong(id));
		} else
		if (operation.equals("update")) {
			String title = params.get("title");
			respText = app.updateShop(Long.parseLong(id), title);
		} else
		if (operation.equals("create")) {
			String title = params.get("title");
			respText = app.createShop(title);
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
		BBRApplication app = BBRApplication.getApp(request);
		String respText = app.getShops();
		
		response.setContentType("text/plain");  
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().write(respText); 
}

}
