package BBR;

public class BBRGPS {
	public float lat;
	public float lng;
	
	public BBRGPS(float lat, float lng) {
		this.lat = lat;
		this.lng = lng;
	}
	
	public String toString() {
		return String.format("%.3f, %.3f", lat, lng);
	}

	public void setLat(float lat) {
		this.lat = lat;
	}
	
	public float getLat() {
		return lat;
	}

	public void setLng(float lng) {
		this.lng = lng;
	}
	
	public float getLng() {
		return lng;
	}
}
