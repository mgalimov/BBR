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
import BBR.BBRDataSet;
import BBR.BBRUtil;
import BBR.BBRChartData.BBRChartDataTypes;
import BBR.BBRChartPeriods;
import BBRAcc.BBRPoS;
import BBRAcc.BBRShop;
import BBRAcc.BBRUser.BBRUserRole;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;
import BBRCust.BBRSpecialist;
import BBRCust.BBRSpecialistManager;
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
		
			return count2 + " �� ������� � " + count1 + " �����";
		}
		
		if (indicator.equals("todayVisits") || indicator.equals("tomorrowVisits")) {
			BBRContext context = BBRContext.getContext(request);
			if (context.user.getRole() == BBRUserRole.ROLE_BBR_OWNER)
				return "0";
			BBRVisitManager manager = new BBRVisitManager();
			
			Long posId = null;
			Long shopId = null;
			
			if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN)
				if (context.user.getPos() != null)
					posId = context.user.getPos().getId();
			if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
				if (context.user.getShop() != null)
					shopId = context.user.getShop().getId();

			Date dt = BBRUtil.now(context.getTimeZone());

			if (indicator.equals("tomorrowVisits")) {
				Calendar c = Calendar.getInstance();
				c.setTime(dt);
				c.add(Calendar.DAY_OF_YEAR, 1);
				dt = c.getTime();
			}

			String count = manager.getVisitsNumber(dt, posId, shopId).toString();
		
			return count;
		}
		
		if (indicator.equals("visitsByPeriod")) {
			return visitsByPeriod(type, options, period, shop, pos);
		}

		if (indicator.equals("incomeByPeriod")) {
			return incomeByPeriod(type, options, period, shop, pos);
		}

		if (indicator.equals("visitsByWeekDays")) {
			return visitsByWeekDays(type, options, period, shop, pos);
		}

		if (indicator.equals("visitsBySpecialists")) {
			return visitsBySpecialists(type, options, period, shop, pos);
		}

		if (indicator.equals("visitsBySources")) {
			return visitsBySources(type, options, period, shop, pos);
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
					{"����", BBRChartDataTypes.BBR_CHART_STRING},
					{"���������", BBRChartDataTypes.BBR_CHART_NUMBER}
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
					{"����", BBRChartDataTypes.BBR_CHART_STRING},
					{"�����", BBRChartDataTypes.BBR_CHART_NUMBER}
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

	protected String visitsByWeekDays(String type, String options,
			BBRChartPeriods period, BBRShop shop, BBRPoS pos) {
		try {
			BBRChartData data = new BBRChartData();
			
			String[][] cols = {
					{"����", BBRChartDataTypes.BBR_CHART_STRING},
					{"���������", BBRChartDataTypes.BBR_CHART_NUMBER}
			};
			data.addCols(cols);
			
			String[] weekdays = {"�����������", "�������", "�����", "�������", "�������", "�������", "�����������"};
			BBRVisitManager mgr = new BBRVisitManager();
			List<Object[]> list = mgr.getVisitsByWeekDays(period.startDate, period.endDate, period.detail, pos, shop);
			
			List<Object[]> rlist = new ArrayList<Object[]>(7);
			for (int i = 0; i < 7; i++) {
				Object[] o = {weekdays[i], 0};
				rlist.add(o);
			}
			for (int i = 0; i < list.size(); i++)
			{
				Object[] o = (Object[])list.get(i);
				int wd = i - 1;
				if (wd < 0)
					wd = 6;
				Object[] o1 = rlist.get(wd);
				o1[1] = o[1];
				rlist.set(wd, o1);
			}
			list = rlist;
			
			List<Object[]> listComp = null;
			if (period.compareToEndDate != null) {
				data.addCol("Visits to compare", BBRChartDataTypes.BBR_CHART_NUMBER);
				listComp = mgr.getVisitsByWeekDays(period.compareToStartDate, period.compareToEndDate, period.detail, pos, shop);
				List<Object[]> nlist = new ArrayList<Object[]>(7);
				for (int i = 0; i < 7; i++) {
					Object[] o = {"", 0};
					nlist.add(o);
				}
				for (int i = 0; i < listComp.size(); i++)
				{
					Object[] o = (Object[])listComp.get(i);
					int wd = i - 1;
					if (wd < 0)
						wd = 6;
					Object[] o1 = nlist.get(wd);
					o1[1] = o[1];
					nlist.set(wd, o1);
				}
								
				listComp = nlist;
			}
			
			data.importList(list, listComp, period);
			
			return data.toJson();
		} catch (Exception ex) {
			return "";
		}
	}
	
	protected String visitsBySpecialists(String type, String options,
			BBRChartPeriods period, BBRShop shop, BBRPoS pos) {
		try {
			BBRChartData data = new BBRChartData();
			
			data.addCol("����", BBRChartDataTypes.BBR_CHART_STRING);
			BBRSpecialistManager spec = new BBRSpecialistManager();
			BBRDataSet<BBRSpecialist> slist = spec.list();

			BBRVisitManager mgr = new BBRVisitManager();
		
			List<Object[]> blist = new ArrayList<Object[]>();
			for (BBRSpecialist s : slist.data) {
				data.addCol(s.getName(), BBRChartDataTypes.BBR_CHART_NUMBER);
				List<Object[]> list = mgr.getVisitsBySpecialist(period.startDate, period.endDate, period.detail, s.getId(), pos, shop);
				list = BBRChartData.enrichDateList(list, period.startDate, period.endDate, period.detail);
				
				boolean found = false;
				for (Object[] line : list) {
					for (int i = 0; i < blist.size(); i++) {
						Object[] o = blist.get(i);
						if (o[0].toString().equals(line[0].toString())) {
							Object[] bline = new Object[o.length + line.length - 1];
							for (int j = 0; j < o.length; j++)
								bline[j] = o[j];
							for (int j = 1; j < line.length; j++)
								bline[o.length + j - 1] = line[j];
							blist.set(i, bline);
							found = true;
							break;
						}
					}
					if (!found) {
						blist.add(line);
					}
				}
			}
			data.importList(blist, null, period);
			
			return data.toJson();
		} catch (Exception ex) {
			return "";
		}
	}

	protected String visitsBySources(String type, String options,
			BBRChartPeriods period, BBRShop shop, BBRPoS pos) {
		try {
			BBRChartData data = new BBRChartData();
			
			String[][] cols = {
					{"��������", BBRChartDataTypes.BBR_CHART_STRING},
					{"���������", BBRChartDataTypes.BBR_CHART_NUMBER}
			};
			data.addCols(cols);
			
			BBRVisitManager mgr = new BBRVisitManager();
			List<Object[]> list = mgr.getVisitsBySources(period.startDate, period.endDate, pos, shop);
	
			for (Object[] el : list) {
				switch ((int)el[0]) {
				case 0: {
					el[0] = "���������";
					break;
				}
				case 10: {
					el[0] = "��������";
					break;
				}
				}
			}
			
			data.importList(list, null, period);
			
			return data.toJson();
		} catch (Exception ex) {
			return "";
		}
	}

}

