package BBRCharts;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;

public abstract class BBRBasicChartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int errorResponseCode = 700;
       
    public BBRBasicChartServlet() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BBRContext context = BBRContext.getContext(request); 

		String respText = "";
		try {
			BBRParams params = new BBRParams(request.getQueryString());
			String type = params.get("type");
			String indicator = params.get("indicator");
			respText = getChartData(indicator, type, params, request, response);
		} catch (Exception ex) {
			respText = ex.getMessage();
			if (context != null)
				respText = context.gs(respText);
			response.setStatus(errorResponseCode);
		}
		response.setContentType("text/plain");  
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().write(respText); 

	}
	
	protected String getChartData(String indicator, String type,
								  BBRParams params, HttpServletRequest request, 
								  HttpServletResponse response) {
		return "";
	}
}
