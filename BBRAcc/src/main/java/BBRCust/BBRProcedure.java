package BBRCust;

import javax.persistence.*;

import BBR.BBRDataElement;
import BBRAcc.BBRPoS;

@Entity
@Table(name="procedures")
public class BBRProcedure extends BBRDataElement {
	@Id
	@GeneratedValue
	@Column(name="PROC_ID")
	private Long id;
	
	@Column(name="TITLE")
	private String title;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private BBRPoS pos;
	
	@Column(name="LENGTH")
	private float length;
	
	@Column(name="PRICE")
	private float price;
	
	public class BBRProcedureStatus {
		public static final int PROCSTATUS_INITIALIZED = 0;
		public static final int PROCSTATUS_APPROVED = 1;
		public static final int PROCSTATUS_INACTIVE = 2;
	}
	
	@Column(name="STATUS")
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
