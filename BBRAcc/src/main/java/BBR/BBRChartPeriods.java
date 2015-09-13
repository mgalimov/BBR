package BBR;

import java.util.Calendar;
import java.util.Date;

public class BBRChartPeriods {
	public Date startDate = null;
	public Date endDate = null;
	public Integer detail = 2; 
	public Date compareToStartDate = null;
	public Date compareToEndDate = null;
	
	public BBRChartPeriods (Date startDate, Date endDate, Integer detail, Date compareToStartDate, Date compareToEndDate) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.detail = detail;
		this.compareToStartDate = compareToStartDate;
		this.compareToEndDate = compareToEndDate;
	}
	
	public BBRChartPeriods (Date startDate, Date endDate, Integer detail) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.detail = detail;
	}

	public BBRChartPeriods (Date startDate, Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public BBRChartPeriods () {
	}
	
	
	public class BBRChartDetail {
		public final static int BBR_CHART_DETAIL_DATE = 0;
		public final static int BBR_CHART_DETAIL_HOUR = 1;
		public final static int BBR_CHART_DETAIL_DAY = 2;
		public final static int BBR_CHART_DETAIL_MONTH = 3;
		public final static int BBR_CHART_DETAIL_YEAR = 4;
	}
	
    public static String periodFunction(String field, int detail) {
		String[] functions = {field, 
				"CONCAT(STR(YEAR(" + field + ")), '-', STR(MONTH(" + field + ")), '-', STR(DAY(" + field + ")), ' ', STR(HOUR(" + field + ")), ':00')", 
				"CONCAT(STR(YEAR(" + field + ")), '-', STR(MONTH(" + field + ")), '-', STR(DAY(" + field + ")))", 
				"CONCAT(STR(YEAR(" + field + ")), '-', STR(MONTH(" + field + ")), '-01')", 
				"STR(YEAR(timeScheduled))"};
		return functions[detail];
    }
    
	private static String indftms[] = {"yyyy-M-dd HH:mm", "yyyy-M-dd HH:mm", "yyyy-M-dd", "yyyy-M-dd", "yyyy"};
	private static String outdftms[] = {"yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm", "yyyy-MM-dd", "yyyy-MM", "yyyy"};
	private static int delta[] = {0, Calendar.HOUR, Calendar.DAY_OF_MONTH, Calendar.MONTH, Calendar.YEAR};

    public static String dateInFormat(int detail) {
		return indftms[detail];
    }

    public static String dateOutFormat(int detail) {
		return outdftms[detail];
    }

    public static int getDelta(int detail) {
    	return delta[detail];
    }
}
