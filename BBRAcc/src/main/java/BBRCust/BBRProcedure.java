package BBRCust;

import java.sql.Blob;

import javax.persistence.*;

import BBR.BBRDataElement;

@Entity
@Table(name="procedures")
public class BBRProcedure extends BBRDataElement {
	@Id
	@GeneratedValue
	@Column(name="PROC_ID")
	private Long id;
	
	@Column(name="TITLE")
	private String title;

	@Column(name="LENGTH")
	private float length;
	
	@Column(name="PRICE")
	private float price;
	
	public class BBRProcedureStatus {
		public static final int PROCSTATUS_INITIALIZED = 0;
		public static final int PROCSTATUS_APPROVED = 1;
		public static final int PROCSTATUS_APPROVED_LOCAL = 3;
		public static final int PROCSTATUS_INACTIVE = 2;
	}
	
	@Column(name="STATUS")
	private int status;
	
	@Column(name="PHOTO")
	private Blob photo;
	
	@ManyToOne(fetch=FetchType.EAGER)
	BBRProcedureGroup procedureGroup;
	
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

	public Blob getPhoto() {
		return photo;
	}

	public void setPhoto(Blob photo) {
		this.photo = photo;
	}

	public BBRProcedureGroup getProcedureGroup() {
		return procedureGroup;
	}

	public void setProcedureGroup(BBRProcedureGroup procedureGroup) {
		this.procedureGroup = procedureGroup;
	}
}
