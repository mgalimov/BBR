package BBRCust;

import BBR.BBRDataElement;

public class BBRProcedure extends BBRDataElement {
	private Long id;
	private String title;
	private Long posId;
	private String posTitle;
	private float length;
	private float price;
	private String currency;
	
	public class BBRProcedureStatus {
		public static final int PROCSTATUS_INITIALIZED = 0;
		public static final int PROCSTATUS_APPROVED = 1;
		public static final int PROCSTATUS_INACTIVE = 2;
	}
	
	private BBRProcedureStatus status;
	
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

	public Long getPosId() {
		return posId;
	}

	public void setPosId(Long posId) {
		this.posId = posId;
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

	public String getPosTitle() {
		return posTitle;
	}

	public void setPosTitle(String posTitle) {
		this.posTitle = posTitle;
	}

	public BBRProcedureStatus getStatus() {
		return status;
	}

	public void setStatus(BBRProcedureStatus status) {
		this.status = status;
	}
}
