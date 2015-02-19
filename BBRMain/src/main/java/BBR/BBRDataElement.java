package BBR;

public class BBRDataElement {
	public String toJson() {
		return BBRUtil.gson.toJson(this);
	}
	
}
