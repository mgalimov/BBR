import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.JDBCException;

import BBRAcc.BBRPoS;
import BBRAcc.BBRPoSManager;
import BBRAcc.BBRShop;
import BBRAcc.BBRShopManager;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;
import BBR.BBRUtil;

@WebServlet("/BBRBase")
public class BBRBase extends HttpServlet  {
	private static final long serialVersionUID = 1L;
	private static final int errorResponseCode = 700;
	
    // Performing operations
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BBRContext context = BBRContext.getContext(request); 

		String respText = "";
		try {
			BBRParams params = new BBRParams(request.getQueryString());
			String operation = params.get("operation");
			
			if (operation.equals("setFilters")) {
				SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
				Date startDate = df.parse(params.get("filterStartDate"));
				Date endDate = df.parse(params.get("filterEndDate"));
				
				BBRShopManager smgr = new BBRShopManager();
				BBRShop shop = null;
				if (params.get("filterShop") != null && !params.get("filterShop").isEmpty())
					shop = smgr.findById(Long.parseLong(params.get("filterShop")));

				BBRPoSManager pmgr = new BBRPoSManager();
				BBRPoS pos = null;
				if (params.get("filterPoS") != null && !params.get("filterPoS").isEmpty())
					pos = pmgr.findById(Long.parseLong(params.get("filterPoS")));

				context.filterStartDate = startDate;
				context.filterEndDate = endDate;
				context.filterShop = shop;
				context.filterPoS = pos;
			} else
			if (operation.equals("setGridFilter")) {
				Hashtable<String, String> filter = params.getList("filter");
				context.set("filter", filter);
			} else
				respText = processOperation(operation, params, request, response);
		} catch (JDBCException ex) {
			BBRUtil.log.error(ex.getMessage());
			BBRUtil.log.error(ex.getSQL());
			BBRUtil.log.error(ex.getStackTrace());
			respText = ex.getMessage();
			if (context != null)
				respText = context.gs(respText);
			response.setStatus(errorResponseCode);
		} catch (Exception ex) {
			BBRUtil.log.error(ex.getMessage());
			BBRUtil.log.error(ex.getStackTrace());
			respText = ex.getMessage();
			if (context != null)
				respText = context.gs(respText);
			response.setStatus(errorResponseCode);
		}
		response.setContentType("text/plain");  
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().write(respText); 

	}

	// Getting data
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BBRContext context = BBRContext.getContext(request); 

		String respText = "";
		try {
			BBRParams params = new BBRParams(request.getQueryString());
			String operation = params.get("operation");
			respText = processOperation(operation, params, request, response);
		} catch (JDBCException ex) {
			BBRUtil.log.error(ex.getMessage());
			BBRUtil.log.error(ex.getSQL());
			BBRUtil.log.error(ex.getStackTrace());
			respText = ex.getMessage();
			if (context != null)
				respText = context.gs(respText);
			response.setStatus(errorResponseCode);
		} catch (Exception ex) {
			BBRUtil.log.error(ex.getMessage());
			BBRUtil.log.error(ex.getStackTrace());
			respText = ex.getMessage();
			if (context != null)
				respText = context.gs(respText);
			response.setStatus(errorResponseCode);
		}
		response.setContentType("text/plain");  
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().write(respText); 
	}

	protected String processOperation(String operation, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		return "";
	};

}
