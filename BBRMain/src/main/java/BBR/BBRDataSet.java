package BBR;

import java.util.*;

public class BBRDataSet<T extends BBRDataElement> {
	public String total_rows;
	public List<T> page_data;
	
	public BBRDataSet (List<T> rows) {
		Integer i = rows.size();
		total_rows = i.toString();
		this.page_data = rows;
	}

	public BBRDataSet (List<T> rows, Long realSize) {
		total_rows = realSize.toString();
		this.page_data = rows;
	}

	public String toJson() {
		String s = "{\"total_rows\":\"" + total_rows + "\",\"page_data\":[";
		for (T el: page_data) {
			s += el.toJson() + ",";
		}
		s = s.substring(0, s.length() - 1);
		s += "]}";
		return s;
		//return BBRUtil.gson.toJson(this);
	}

}
