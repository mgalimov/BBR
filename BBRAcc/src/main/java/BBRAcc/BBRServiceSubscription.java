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
@Table(name="servicesubscriptions")
public class BBRServiceSubscription extends BBRDataElement {
	@Id
	@GeneratedValue
	@Column(name="SS_ID")
	private Long id;
	
	@Column(name="START_DATE")
	private Date startDate;
	
	@Column(name="END_DATE")
	private Date endDate;
		
	@ManyToOne(fetch=FetchType.EAGER)
	private BBRShop shop;

	@ManyToOne(fetch=FetchType.EAGER)
	private BBRService service;
	
	@Column(name="BALANCE")
	private Float balance;

	@Column(name="CREDIT_LIMIT")
	private Float creditLimit;
	
	@Column(name="CURRENCY")
	private String currency;

	@Column(name="STATUS")
	private int status;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public BBRShop getShop() {
		return shop;
	}

	public void setShop(BBRShop shop) {
		this.shop = shop;
	}

	public BBRService getService() {
		return service;
	}

	public void setService(BBRService service) {
		this.service = service;
	}

	public Float getBalance() {
		return balance;
	}

	public void setBalance(Float balance) {
		this.balance = balance;
	}

	public Float getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(Float creditLimit) {
		this.creditLimit = creditLimit;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public class BBRServiceSubscriptionStatuses {
		public static final int SUBSCRIPTION_REQUESTED = 0;
		public static final int SUBSCRIPTION_ACTIVE = 1;
		public static final int SUBSCRIPTION_CANCELLED = 2;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
