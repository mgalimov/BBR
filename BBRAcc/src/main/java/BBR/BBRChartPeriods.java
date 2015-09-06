package BBR;

import java.util.Date;

public class BBRChartPeriods {
	Date startDate = null;
	Date endDate = null;
	int detail = 3; 
	Date compareToStartDate = null;
	Date compareToEndDate = null;
	
	public class BBRChartDetail {
		final static int BBR_CHART_DETAIL_HOUR = 1;
		final static int BBR_CHART_DETAIL_DAY = 2;
		final static int BBR_CHART_DETAIL_MONTH = 3;
		final static int BBR_CHART_DETAIL_YEAR = 4;
	}
	
	public BBRChartPeriods (Date startDate, Date endDate, int detail, Date compareToStartDate, Date compareToEndDate) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.detail = detail;
		this.compareToStartDate = compareToStartDate;
		this.compareToEndDate = compareToEndDate;
	}
	
	public BBRChartPeriods (Date startDate, Date endDate, int detail) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.detail = detail;
	}

	public BBRChartPeriods (Date startDate, Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public BBRChartPeriods (String periods) {
		if (periods == null) return;
		
		BBRChartPeriods p = BBRUtil.gson().fromJson(periods, BBRChartPeriods.class);
		this.startDate = p.startDate;
		this.endDate = p.endDate;
		this.detail = p.detail;
		this.compareToStartDate = p.compareToStartDate;
		this.compareToEndDate = p.compareToEndDate;
	}
	
	public BBRChartPeriods () {
	}
}
