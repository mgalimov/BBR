package BBRAcc;

import java.util.Set;
import BBR.BBRDataElement;

public class BBRShop extends BBRDataElement {
	private Long id;
	private String title;
	
	public BBRShop() {}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
}
