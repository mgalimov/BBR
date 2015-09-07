package BBRCharts;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRChartPeriods;
import BBR.BBRUtil;
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
			
			BBRChartPeriods periods = new BBRChartPeriods();
			SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
			if (!params.get("periods[startDate]").equals(""));
				periods.startDate = df.parse(params.get("periods[startDate]"));
			if (!params.get("periods[endDate]").equals(""));
				periods.endDate = df.parse(params.get("periods[endDate]"));
			if (!params.get("periods[detail]").equals(""));
				periods.detail = Integer.parseInt(params.get("periods[detail]"));
			if (!params.get("periods[compareToStartDate]").equals(""))
				periods.compareToStartDate = df.parse(params.get("periods[compareToStartDate]"));
			if (!params.get("periods[compareToEndDate]").equals(""))
				periods.compareToEndDate = df.parse(params.get("periods[compareToEndDate]"));
		
			respText = getChartData(indicator, type, options, periods, params, request, response);
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
								  BBRParams params, HttpServletRequest request, 
								  HttpServletResponse response) {
		return "";
	}
}
