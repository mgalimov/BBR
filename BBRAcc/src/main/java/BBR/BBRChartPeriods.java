package BBR;

import java.util.Date;

public class BBRChartPeriods {
	public Date startDate = null;
	public Date endDate = null;
	public Integer detail = 2; 
	public Date compareToStartDate = null;
	public Date compareToEndDate = null;
	
	public class BBRChartDetail {
		public final static int BBR_CHART_DETAIL_HOUR = 1;
		public final static int BBR_CHART_DETAIL_DAY = 2;
		public final static int BBR_CHART_DETAIL_MONTH = 3;
		public final static int BBR_CHART_DETAIL_YEAR = 4;
	}
	
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
}
