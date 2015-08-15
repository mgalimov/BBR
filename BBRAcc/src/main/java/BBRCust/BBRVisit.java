package BBRCust;

import java.util.Date;

import javax.persistence.*;

import BBR.BBRDataElement;
import BBRAcc.BBRPoS;
import BBRAcc.BBRUser;

@Entity
@Table(name="visits")
public class BBRVisit extends BBRDataElement {
	@Id
	@GeneratedValue
	@Column(name="VIS_ID")
	private Long id;
	
	@Column(name="TIME_SCHEDULED")
	private Date timeScheduled;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private BBRPoS pos;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private BBRUser user;
	
	@Column(name="USER_NAME")
	private String userName;
	
	@Column(name="USER_CONTACTS")
	private String userContacts;

	private String posTitle;
	
	@Column(name="LENGTH")
	private Float length = 0F;

	@ManyToOne(fetch=FetchType.EAGER)
	private BBRSpecialist spec;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private BBRProcedure procedure;
	
	public class BBRVisitStatus {
		public static final int VISSTATUS_INITIALIZED = 0;
		public static final int VISSTATUS_APPROVED = 1;
		public static final int VISSTATUS_CANCELLED = 2;
		public static final int VISSTATUS_PERFORMED = 3;
		public static final int VISSTATUS_DISAPPROVED = 4;
	}
	
	@Column(name="STATUS")
	private int status = 0;

	@Column(name="FINAL_PRICE")
	private float finalPrice = 0;

	public BBRVisit() {}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	public void setTimeScheduled(Date timeScheduled) {
		this.timeScheduled = timeScheduled;
	}
	
	public Date getTimeScheduled() {
		return timeScheduled;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserContacts(String userContacts) {
		this.userContacts = userContacts;
	}
	
	public String getUserContacts() {
		return userContacts;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getStatus() {
		return status;
	}

	public String getPosTitle() {
		return posTitle;
	}

	public void setPosTitle(String posTitle) {
		this.posTitle = posTitle;
	}

	public BBRSpecialist getSpec() {
		return spec;
	}

	public void setSpec(BBRSpecialist spec) {
		this.spec = spec;
	}

	public BBRPoS getPos() {
		return pos;
	}

	public void setPos(BBRPoS pos) {
		this.pos = pos;
	}

	public BBRUser getUser() {
		return user;
	}

	public void setUser(BBRUser user) {
		this.user = user;
	}

	public BBRProcedure getProcedure() {
		return procedure;
	}

	public void setProcedure(BBRProcedure procedure) {
		this.procedure = procedure;
	}

	public Float getLength() {
		return length;
	}

	public void setLength(Float length) {
		this.length = length;
	}
	
	public Float getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(Float finalPrice) {
		this.finalPrice = finalPrice;
	}

}
