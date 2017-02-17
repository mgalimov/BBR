package BBRCharts;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRChartData;
import BBR.BBRChartData.BBRChartDataTypes;
import BBR.BBRChartPeriods;
import BBRAcc.BBRPoS;
import BBRAcc.BBRShop;
import BBRClientApp.BBRParams;
import BBRCust.BBRVisitManager;

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
					{"Типы", BBRChartDataTypes.BBR_CHART_STRING},
					{"Посетители", BBRChartDataTypes.BBR_CHART_NUMBER}
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
			
			Object[][] o = {{"Новые", arr[0]}, {"Повторные", arr[1]}};
			
			data.addRow(o[0]);
			data.addRow(o[1]);
			
			return data.toJson();
		} catch (Exception ex) {
			return "";
		}
	}
}

