package BBR;

import com.google.gson.Gson;

public class BBRDataElement {
	public String toJson() {
		return new Gson().toJson(this);
	}
}
