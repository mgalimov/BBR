package BBR;

import java.util.*;

public class BBRDataSet<T extends BBRDataElement> {
	public String totalRecords;
	public List<T> data;
	
	public BBRDataSet (List<T> rows) {
		if (rows != null) {
			Integer i = rows.size();
			totalRecords = i.toString();
		} else
			totalRecords = "0";
		this.data = rows;
	}

	public BBRDataSet (List<T> rows, Long realSize) {
		totalRecords = realSize.toString();
		this.data = rows;
	}

	public String toJson() {
		String s = "{\"recordsTotal\":\"" + totalRecords + "\",\"recordsFiltered\":\"" + totalRecords + "\",\"data\":[";
		
		if (data != null) {
			for (BBRDataElement el: data) {
				s += BBRUtil.gson().toJson(el) + ",";
			}
			if (data.size() > 0)
				s = s.substring(0, s.length() - 1);
		}
		s += "]}";
		return s;
	}

}
