package BBRCharts;

import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRChartData;
import BBR.BBRChartData.BBRChartDataTypes;
import BBR.BBRChartPeriods;
import BBRClientApp.BBRParams;
import BBRCust.BBRVisitManager;

@WebServlet("/BBRVisitCharts")
public class BBRVisitCharts extends BBRBasicChartServlet {
	private static final long serialVersionUID = 1L;

	protected String getChartData(String indicator, String type, String options,
				BBRChartPeriods period,
			  	BBRParams params, HttpServletRequest request, 
			  	HttpServletResponse response) {
		
		if (indicator.equals("visitsByPeriod")) {
			try {
				BBRChartData data = new BBRChartData();
				
				String[][] cols = {
						{"Date", BBRChartDataTypes.BBR_CHART_STRING},
						{"Visits", BBRChartDataTypes.BBR_CHART_NUMBER}
				};
				data.addCols(cols);
				
				BBRVisitManager mgr = new BBRVisitManager();
				List<Object[]> list = mgr.getVisitsByPeriod(period.startDate, period.endDate, period.detail);
				list = BBRChartData.enrichDateList(list, period.startDate, period.endDate, period.detail);
				
				List<Object[]> listComp = null;
				if (period.compareToEndDate != null) {
					data.addCol("Visits to compare", BBRChartDataTypes.BBR_CHART_NUMBER);
					listComp = mgr.getVisitsByPeriod(period.compareToStartDate, period.compareToEndDate, period.detail);
					listComp = BBRChartData.enrichDateList(listComp, period.compareToStartDate, period.compareToEndDate, period.detail);
				}
				
				data.importList(list, listComp, period);
				
				return data.toJson();
			} catch (Exception ex) {
				return "";
			}
		}
		
		if (indicator.equals("test")) {
			String[][] cols = {
					{"Employee Name"},
					{"Salary", BBRChartDataTypes.BBR_CHART_NUMBER},
					{"Taxes", BBRChartDataTypes.BBR_CHART_NUMBER}
			};
			Object[][][] rows = {
					{{"Mike"}, {22500, "22,500"}, {10000}},
					{{"Bob"}, {35000, "35,000"}, {10000}},
					{{"Alice"}, {44000}, {10000}},
					{{"Frank"}, {27000}, {10000}},
					{{"Floyd"}, {92000}, {10000}},
					{{"Fritz"}, {18500}, {10000}}
			};
			BBRChartData data = new BBRChartData(cols, rows);
			return data.toJson();
		}
		return "";
	}
}
