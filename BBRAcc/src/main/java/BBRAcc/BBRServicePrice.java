package BBRAcc;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.persistence.*;

import BBR.BBRDataElement;
import BBR.BBRDataSet;

@SuppressWarnings("unused")
@Entity
@Table(name="services")
public class BBRServicePrice extends BBRDataElement {
	@Id
	@GeneratedValue
	@Column(name="SERVICEPRICE_ID")
	private Long id;

	@Column(name="COUNTRY")
	private String country;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private BBRService service;
	
	@Column(name="START_DATE")
	private Date startDate;

	@Column(name="END_DATE")
	private Date endDate;

	@Column(name="CREDIT_LIMIT")
	private Float creditLimit;

	@Column(name="PRICE")
	private Float price;

	@Column(name="CURRENCY")
	private String currency;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Float getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(Float creditLimit) {
		this.creditLimit = creditLimit;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BBRService getService() {
		return service;
	}

	public void setService(BBRService service) {
		this.service = service;
	}


}
