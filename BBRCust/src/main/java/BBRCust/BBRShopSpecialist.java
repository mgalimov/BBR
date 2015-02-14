package BBRCust;

import BBR.BBRDataElement;

public class BBRShopSpecialist extends BBRDataElement {
	private Long id;
	
	private String title;
	
	public BBRShopSpecialist() {}
	
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
