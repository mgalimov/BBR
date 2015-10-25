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
@Table(name="transactions")
public class BBRTransaction extends BBRDataElement {
	@Id
	@GeneratedValue
	@Column(name="TRAN_ID")
	private Long id;
	
	@Column(name="TRAN_DATE")
	private Date date;
	
	@Column(name="amount")
	private Float amount;

	@Column(name="curency")
	private String currency;
	
	@Column(name="type")
	private char type;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private BBRServiceSubscription service;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public BBRServiceSubscription getServiceSubscription() {
		return service;
	}

	public void setServiceSubscription(BBRServiceSubscription service) {
		this.service = service;
	}
	

}
