package BBRAcc;

import javax.persistence.*;

import BBR.BBRDataElement;

@Entity
@Table(name="shops")
public class BBRShop extends BBRDataElement {
	@Id
	@GeneratedValue
	@Column(name="SHOP_ID")
	private Long id;
	
	@Column(name="TITLE")
	private String title;
	
	@Column(name="COUNTRY")
	private String country;

	@Column(name="TIMEZONE")
	private String timeZone;
	
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
}
