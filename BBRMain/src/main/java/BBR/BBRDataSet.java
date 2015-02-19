package BBR;

import java.util.*;

public class BBRDataSet<T> {
	public String total_rows;
	public	List<T> page_data;
	
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
		return BBRUtil.gson.toJson(this);
	}

}
