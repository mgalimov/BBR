package BBRCharts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRChartData;
import BBR.BBRUtil;
import BBR.BBRChartData.BBRChartDataTypes;
import BBR.BBRChartPeriods;
import BBRAcc.BBRPoS;
import BBRAcc.BBRShop;
import BBRAcc.BBRUser.BBRUserRole;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;
import BBRCust.BBRVisitManager;
import BBRCust.BBRVisit.BBRVisitStatus;

@WebServlet("/BBRVisitorCharts")
public class BBRVisitorCharts extends BBRBasicChartServlet {
	private static final long serialVersionUID = 1L;

	protected String getChartData(String indicator, String type, String options,
				BBRChartPeriods period, BBRShop shop, BBRPoS pos,
			  	BBRParams params, HttpServletRequest request, 
			  	HttpServletResponse response) {

		if (indicator.equals("visitorsNewVsReturned")) {
			return visitorsByPeriod(type, options, period, shop, pos);
		}

		return "";
	}

	protected String visitorsByPeriod(String type, String options,
			BBRChartPeriods period, BBRShop shop, BBRPoS pos) {
		try {
			BBRChartData data = new BBRChartData();
			
			String[][] cols = {
					{"Types", BBRChartDataTypes.BBR_CHART_STRING},
					{"Visitors", BBRChartDataTypes.BBR_CHART_NUMBER}
			};
			data.addCols(cols);
			
			BBRVisitManager mgr = new BBRVisitManager();
			
			Long posId = null;
			Long shopId = null;
			
			if (pos != null)
				posId = pos.getId();
			if (shop != null)
				shopId = shop.getId();
			
				
			Long[] arr = mgr.getVisitorsNewVsReturned(period.startDate, period.endDate, posId, shopId);
			
			data.addRow((Object[])arr);
			
			return data.toJson();
		} catch (Exception ex) {
			return "";
		}
	}
}

