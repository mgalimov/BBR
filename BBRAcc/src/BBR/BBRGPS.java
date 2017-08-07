package BBR;

import javax.persistence.*;

@Embeddable
public class BBRGPS {
	@Column(name="LOC_LAT")
	public double lat;
	
	@Column(name="LOC_LNG")
	public double lng;

	public BBRGPS() {
		this.lat = 0;
		this.lng = 0;
	}

	public BBRGPS(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}

	public BBRGPS(String gps) {
		String[] s = gps.split(",");
		this.lat = Double.parseDouble(s[0]);
		this.lng = Double.parseDouble(s[1]);
	}

	public String toString() {
		return String.format("%.3f, %.3f", lat, lng);
	}

	public void setLat(double lat) {
		this.lat = lat;
	}
	
	public double getLat() {
		return lat;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}
	
	public double getLng() {
		return lng;
	}
}
