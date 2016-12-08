package BBRAcc;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import BBR.BBRDataElement;
import BBR.BBRGPS;
import BBR.JsonFormat;

@Entity
@Table(name="poses")
public class BBRPoS extends BBRDataElement {
	@Id
	@GeneratedValue
	@Column(name="POS_ID")
	private Long id;
	
	@Column(name="TITLE")
	private String title;
	
	@Column(name="LOC_DESC")
	private String locationDescription;
	
	@Embedded
	private BBRGPS locationGPS;

	@JsonFormat(format="HH:mm")
	@Column(name="START_WORK_HOUR")
	private Date startWorkHour;

	@JsonFormat(format="HH:mm")
	@Column(name="END_WORK_HOUR")
	private Date endWorkHour;

	@ManyToOne(fetch=FetchType.EAGER)
	public BBRShop shop;
	
	@Column(name="CURRENCY")
	private String currency;
	
	@Column(name="TIMEZONE")
	private String timeZone;

	@Column(name="URL_ID")
	private String urlID;
	
	@Column(name="EMAIL_NOTIFICATION")
	private String email;

	@Column(name="SMS_NOTIFICATION")
	private String sms;
	
	@Column(name="PRIZE_VISIT_NUMBER")
	private Long prizeVisitNumber;

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
			return "https://maps.yandex.ru/?ll=" + locationGPS.lng + "," + locationGPS.lat + "&z=16";
		else
			return "#";
	}
	
	
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getUrlID() {
		return urlID;
	}

	public void setUrlID(String urlID) {
		this.urlID = urlID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSms() {
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}

	public Long getPrizeVisitNumber() {
		return prizeVisitNumber;
	}

	public void setPrizeVisitNumber(Long prizeVisitNumber) {
		this.prizeVisitNumber = prizeVisitNumber;
	}
}
