package BBRCharts;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBRClientApp.BBRParams;

@WebServlet("/BBRVisitCharts")
public class BBRVisitCharts extends BBRBasicChartServlet {
	private static final long serialVersionUID = 1L;

	protected String getChartData(String indicator, String type,
			  BBRParams params, HttpServletRequest request, 
			  HttpServletResponse response) {
		return "[[\"Employee Name\", \"Salary\"],"+
				"[\"Mike\", {\"v\":22500, \"f\":\"22,500\"}],"+
				"[\"Bob\", 35000],"+
				"[\"Alice\", 44000],"+
				"[\"Frank\", 27000],"+
				"[\"Floyd\", 92000],"+
				"[\"Fritz\", 18500]"+
			   "]";
	}
}
