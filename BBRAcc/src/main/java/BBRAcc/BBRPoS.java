package BBRAcc;

//import java.text.SimpleDateFormat;
import java.util.Date;

import BBR.BBRDataElement;
import BBR.BBRGPS;
//import BBR.BBRUtil;

public class BBRPoS extends BBRDataElement {
	private Long id;
	private String title;
	private String locationDescription;
	private BBRGPS locationGPS;
	private Date startWorkHour;
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
/*
	@Override
	public String toJson() {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		BBRPoSString ps = new BBRPoSString();
		ps.id = id;
		ps.title = title;
		ps.locationDescription = locationDescription;
		ps.locationGPS = locationGPS;
		if (startWorkHour != null)
			ps.startWorkHour = df.format(startWorkHour);
		else
			ps.startWorkHour = "08:00";
		
		if (endWorkHour != null)
			ps.endWorkHour = df.format(endWorkHour);
		else
			ps.endWorkHour = "21:00";
		ps.shop = shop;
		String s = BBRUtil.gson.toJson(ps);
		return s;
	}
	
	@SuppressWarnings("unused")
	private class BBRPoSString {
		public Long id;
		public String title;
		public String locationDescription;
		public BBRGPS locationGPS;
		public String startWorkHour;
		public String endWorkHour;
		public BBRShop shop;
		
		BBRPoSString() {}
	}
 */
	
	public String getMapHref() {
		if (locationGPS != null)
			return "https://maps.yandex.ru/?ll=" + locationGPS.lng + "," + locationGPS.lat;
		else
			return "#";
	}
}
