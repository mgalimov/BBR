package BBRAcc;

import BBR.BBRDataElement;
import BBR.BBRGPS;

public class BBRPoS extends BBRDataElement {
	private Long id;
	private String title;
	private String locationDescription;
	private BBRGPS locationGPS;
	private BBRShop shop;
	
	public BBRPoS() {}
	
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

	public void setLocationDescription(String locationDescription) {
		this.locationDescription = locationDescription;
	}
	
	public String getLocationDescription() {
		return locationDescription;
	}

	public void setLocationGPS(BBRGPS locationGPS) {
		this.locationGPS = locationGPS;
	}
	
	public BBRGPS getLocationGPS() {
		return locationGPS;
	}

	public void setShop(BBRShop shop) {
		this.shop = shop;
	}
	
	public BBRShop getShop() {
		return shop;
	}

}
