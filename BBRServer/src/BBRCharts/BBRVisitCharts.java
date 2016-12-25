package BBRCharts;

import java.text.SimpleDateFormat;
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

@WebServlet("/BBRVisitCharts")
public class BBRVisitCharts extends BBRBasicChartServlet {
	private static final long serialVersionUID = 1L;

	protected String getChartData(String indicator, String type, String options,
				BBRChartPeriods period, BBRShop shop, BBRPoS pos,
			  	BBRParams params, HttpServletRequest request, 
			  	HttpServletResponse response) {

		if (indicator.equals("newVisits")) {
			BBRContext context = BBRContext.getContext(request);
			BBRVisitManager manager = new BBRVisitManager();
			String where = "";
			if (context.user.getRole() == BBRUserRole.ROLE_BBR_OWNER)
				return "0";
			
			if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN)
				if (context.user.getPos() != null)
					where = manager.wherePos(context.user.getPos().getId());
			if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
				if (context.user.getShop() != null)
					where = manager.whereShop(context.user.getShop().getId());
			if (!where.equals("")) 
				where = "(" + where +") and";
			Date dt = BBRUtil.now(context.getTimeZone());
			SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat);
			where += "(status = " + BBRVisitStatus.VISSTATUS_INITIALIZED + ") and (timeScheduled >= '" + df.format(BBRUtil.getStartOfDay(dt)) + "')";

			String count1 = manager.count(where).toString();
			
	        where += " and (timeScheduled <= '" + df.format(BBRUtil.getEndOfDay(dt)) + "')";
			String count2 = manager.count(where).toString();
		
			return count2 + " на сегодня и " + count1 + " всего";
		}
		
		if (indicator.equals("todayVisits")) {
			BBRContext context = BBRContext.getContext(request);
			BBRVisitManager manager = new BBRVisitManager();
			String where = "";
			if (context.user.getRole() == BBRUserRole.ROLE_BBR_OWNER)
				return "0";
			
			if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN)
				if (context.user.getPos() != null)
					where = manager.wherePos(context.user.getPos().getId());
			if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
				if (context.user.getShop() != null)
					where = manager.whereShop(context.user.getShop().getId());
			if (!where.equals("")) 
				where = "(" + where +") and";
			Date dt = BBRUtil.now(context.getTimeZone());
			SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat);
			where += "(status in (" + BBRVisitStatus.VISSTATUS_APPROVED +", " + 
					                  BBRVisitStatus.VISSTATUS_PERFORMED + "))" + 
			         " and (coalesce(realTime, timeScheduled) >= '" + df.format(BBRUtil.getStartOfDay(dt)) + "')" + 
					 " and (coalesce(realTime, timeScheduled) <= '" + df.format(BBRUtil.getEndOfDay(dt)) + "')";
			String count = manager.count(where).toString();
		
			return count;
		}

		if (indicator.equals("tomorrowVisits")) {
			BBRContext context = BBRContext.getContext(request);
			BBRVisitManager manager = new BBRVisitManager();
			String where = "";
			if (context.user.getRole() == BBRUserRole.ROLE_BBR_OWNER)
				return "0";
			
			if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN)
				if (context.user.getPos() != null)
					where = manager.wherePos(context.user.getPos().getId());
			if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
				if (context.user.getShop() != null)
					where = manager.whereShop(context.user.getShop().getId());
			if (!where.equals("")) 
				where = "(" + where +") and";
			Date dt = BBRUtil.now(context.getTimeZone());
			Calendar c = Calendar.getInstance();
			c.setTime(dt);
			c.add(Calendar.DAY_OF_YEAR, 1);
			dt = c.getTime();
			SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat);
			where += "(status in (" + BBRVisitStatus.VISSTATUS_APPROVED +", " + 
					                  BBRVisitStatus.VISSTATUS_PERFORMED + "))" + 
			         " and (coalesce(realTime, timeScheduled) >= '" + df.format(BBRUtil.getStartOfDay(dt)) + "')" + 
					 " and (coalesce(realTime, timeScheduled) <= '" + df.format(BBRUtil.getEndOfDay(dt)) + "')";
			String count = manager.count(where).toString();
		
			return count;
		}

		
		if (indicator.equals("visitsByPeriod")) {
			return visitsByPeriod(type, options, period, shop, pos);
		}

		if (indicator.equals("incomeByPeriod")) {
			return incomeByPeriod(type, options, period, shop, pos);
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

	protected String visitsByPeriod(String type, String options,
			BBRChartPeriods period, BBRShop shop, BBRPoS pos) {
		try {
			BBRChartData data = new BBRChartData();
			
			String[][] cols = {
					{"Date", BBRChartDataTypes.BBR_CHART_STRING},
					{"Visits", BBRChartDataTypes.BBR_CHART_NUMBER}
			};
			data.addCols(cols);
			
			BBRVisitManager mgr = new BBRVisitManager();
			List<Object[]> list = mgr.getVisitsByPeriod(period.startDate, period.endDate, period.detail, pos, shop);
			list = BBRChartData.enrichDateList(list, period.startDate, period.endDate, period.detail);
			
			List<Object[]> listComp = null;
			if (period.compareToEndDate != null) {
				data.addCol("Visits to compare", BBRChartDataTypes.BBR_CHART_NUMBER);
				listComp = mgr.getVisitsByPeriod(period.compareToStartDate, period.compareToEndDate, period.detail, pos, shop);
				listComp = BBRChartData.enrichDateList(listComp, period.compareToStartDate, period.compareToEndDate, period.detail);
			}
			
			data.importList(list, listComp, period);
			
			return data.toJson();
		} catch (Exception ex) {
			return "";
		}
	}

	protected String incomeByPeriod(String type, String options,
			BBRChartPeriods period, BBRShop shop, BBRPoS pos) {
		try {
			BBRChartData data = new BBRChartData();
			
			String[][] cols = {
					{"Date", BBRChartDataTypes.BBR_CHART_STRING},
					{"Income", BBRChartDataTypes.BBR_CHART_NUMBER}
			};
			data.addCols(cols);
			
			BBRVisitManager mgr = new BBRVisitManager();
			List<Object[]> list = mgr.getIncomeByPeriod(period.startDate, period.endDate, period.detail, pos, shop);
			list = BBRChartData.enrichDateList(list, period.startDate, period.endDate, period.detail);
			
			List<Object[]> listComp = null;
			if (period.compareToEndDate != null) {
				data.addCol("Income to compare", BBRChartDataTypes.BBR_CHART_NUMBER);
				listComp = mgr.getIncomeByPeriod(period.compareToStartDate, period.compareToEndDate, period.detail, pos, shop);
				listComp = BBRChartData.enrichDateList(listComp, period.compareToStartDate, period.compareToEndDate, period.detail);
			}
			
			data.importList(list, listComp, period);
			
			return data.toJson();
		} catch (Exception ex) {
			return "";
		}
	}

}

