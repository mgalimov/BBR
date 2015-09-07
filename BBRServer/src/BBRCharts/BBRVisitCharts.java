package BBRCharts;

import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRChartCell;
import BBR.BBRChartData;
import BBR.BBRChartData.BBRChartDataTypes;
import BBR.BBRChartPeriods;
import BBR.BBRChartPeriods.BBRChartDetail;
import BBR.BBRChartRow;
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
			BBRChartData data = new BBRChartData();
			
			String[][] cols = {
					{"Date", BBRChartDataTypes.BBR_CHART_STRING},
					{"Visits", BBRChartDataTypes.BBR_CHART_NUMBER}
			};
			data.addCols(cols);
			
			BBRVisitManager mgr = new BBRVisitManager();
			List<Object[]> list = mgr.getVisitsByPeriod(period);
			
			String[] delim = {"-", "-", "-"};
			
			if (period.detail == BBRChartDetail.BBR_CHART_DETAIL_DAY)
				delim[0] = "";
			if (period.detail == BBRChartDetail.BBR_CHART_DETAIL_MONTH) {
				delim[0] = "";
				delim[1] = "";
			}
			if (period.detail == BBRChartDetail.BBR_CHART_DETAIL_YEAR) {
				delim[0] = "";
				delim[1] = "";
				delim[2] = "";
			}
			
			for(Object[] line: list) {
				data.addRow(line[3].toString() + delim[2] + 
							line[2].toString() + delim[1] +
							line[1].toString() + delim[0] +
							line[0].toString(), line[4]);
			}
			
			return data.toJson();
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
