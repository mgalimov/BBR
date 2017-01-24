package BBRCharts;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRChartPeriods;
import BBR.BBRUtil;
import BBRAcc.BBRPoS;
import BBRAcc.BBRPoSManager;
import BBRAcc.BBRShop;
import BBRAcc.BBRShopManager;
import BBRAcc.BBRUser.BBRUserRole;
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
			String options = params.get("options");
			String shopId = params.get("shopId");
			String posId = params.get("posId");
			
			BBRChartPeriods periods = new BBRChartPeriods();
			SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
			if (params.get("periods[startDate]") != null)
				if (!params.get("periods[startDate]").isEmpty());
				periods.startDate = df.parse(params.get("periods[startDate]"));
			if (params.get("periods[endDate]") != null && !params.get("periods[endDate]").isEmpty());
				periods.endDate = df.parse(params.get("periods[endDate]"));
			if (params.get("periods[detail]") != null && !params.get("periods[detail]").isEmpty());
				periods.detail = Integer.parseInt(params.get("periods[detail]"));
			if (params.get("periods[compareToStartDate]") != null && !params.get("periods[compareToStartDate]").isEmpty())
				periods.compareToStartDate = df.parse(params.get("periods[compareToStartDate]"));
			if (params.get("periods[compareToEndDate]") != null && !params.get("periods[compareToEndDate]").isEmpty())
				periods.compareToEndDate = df.parse(params.get("periods[compareToEndDate]"));
			periods.alignDates();
		
			BBRPoS pos = null;
			BBRShop shop = null;

			if (posId != null && !posId.isEmpty()) {
				BBRPoSManager pmgr = new BBRPoSManager();
				pos = pmgr.findById(Long.parseLong(posId));
			} else
				if (shopId != null && !shopId.isEmpty()) {
					BBRShopManager smgr = new BBRShopManager();
					shop = smgr.findById(Long.parseLong(shopId));
				}

			if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN && pos == null)
				pos = context.user.getPos();
			if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN && pos == null && shop == null)
				shop = context.user.getShop();
			
			if (pos == null && shop == null)
				respText = "";
			else
				respText = getChartData(indicator, type, options, periods, shop, pos, params, request, response);
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
	
	protected String getChartData(String indicator, String type, String options, BBRChartPeriods periods,
								  BBRShop shop, BBRPoS pos,
								  BBRParams params, HttpServletRequest request, 
								  HttpServletResponse response) {
		return "";
	}
}
