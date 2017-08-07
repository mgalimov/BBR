package BBR;

public class BBRChartCell extends BBRDataElement {
	Object v;
	String f;
	
	public BBRChartCell(Object value, String formattedValue) {
		this.v = value;
		this.f = formattedValue;
	}
	
	public BBRChartCell(Object value) {
		this.v = value;
		this.f = null;
	}
}
