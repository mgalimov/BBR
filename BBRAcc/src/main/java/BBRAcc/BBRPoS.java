package BBRAcc;

import java.util.Date;

import BBR.BBRDataElement;
import BBR.BBRGPS;
import BBR.JsonFormat;

public class BBRPoS extends BBRDataElement {
	private Long id;
	private String title;
	private String locationDescription;
	private BBRGPS locationGPS;

	@JsonFormat(format="HH:mm")
	private Date startWorkHour;

	@JsonFormat(format="HH:mm")
	private Date endWorkHour;

	public BBRShop shop;
	
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

	public Date getStartWorkHour() {
		return startWorkHour;
	}

	public void setStartWorkHour(Date startWorkHour) {
		this.startWorkHour = startWorkHour;
	}

	public Date getEndWorkHour() {
		return endWorkHour;
	}

	public void setEndWorkHour(Date endWorkHour) {
		this.endWorkHour = endWorkHour;
	}

	public String getMapHref() {
		if (locationGPS != null)
			return "https://maps.yandex.ru/?ll=" + locationGPS.lng + "," + locationGPS.lat;
		else
			return "#";
	}
}
