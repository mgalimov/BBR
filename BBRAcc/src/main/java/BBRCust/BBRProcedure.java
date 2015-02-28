package BBRCust;

import BBR.BBRDataElement;
import BBRAcc.BBRPoS;

public class BBRProcedure extends BBRDataElement {
	private Long id;
	private String title;
	private BBRPoS pos;
	private float length;
	private float price;
	private String currency;
	
	public class BBRProcedureStatus {
		public static final int PROCSTATUS_INITIALIZED = 0;
		public static final int PROCSTATUS_APPROVED = 1;
		public static final int PROCSTATUS_INACTIVE = 2;
	}
	
	private int status;
	
	BBRProcedure() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public BBRPoS getPos() {
		return pos;
	}

	public void setPos(BBRPoS pos) {
		this.pos = pos;
	}
}
