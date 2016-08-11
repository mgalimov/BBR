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

	@Column(name="REAL_TIME")
	private Date realTime;

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
		public static final int VISSTATUS_INITIALIZED = 0; // yellow
		public static final int VISSTATUS_APPROVED = 1;    // green
		public static final int VISSTATUS_CANCELLED = 2;   // gray
		public static final int VISSTATUS_PERFORMED = 3;   // blue
		public static final int VISSTATUS_DISAPPROVED = 4; // red
	}
	
	@Column(name="STATUS")
	private int status = 0;

	@Column(name="FINAL_PRICE")
	private float finalPrice = 0;

	@Column(name="DISCOUNT_PERCENT")
	private float discountPercent = 0;

	@Column(name="DISCOUNT_AMOUNT")
	private float discountAmount = 0;

	@Column(name="PRICE_PAID")
	private float pricePaid = 0;

	@Column(name="AMOUNT_TO_SPEC")
	private float amountToSpecialist = 0;

	@Column(name="AMOUNT_TO_MATERIALS")
	private float amountToMaterials = 0;

	@Column(name="COMMENT")
	private String comment = "";
	
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

	public Date getRealTime() {
		return realTime;
	}

	public void setRealTime(Date realTime) {
		this.realTime = realTime;
	}

	public float getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(float discountPercent) {
		this.discountPercent = discountPercent;
	}

	public float getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(float discountAmount) {
		this.discountAmount = discountAmount;
	}

	public float getPricePaid() {
		return pricePaid;
	}

	public void setPricePaid(float pricePaid) {
		this.pricePaid = pricePaid;
	}

	public float getAmountToSpecialist() {
		return amountToSpecialist;
	}

	public void setAmountToSpecialist(float amountToSpecialist) {
		this.amountToSpecialist = amountToSpecialist;
	}

	public float getAmountToMaterials() {
		return amountToMaterials;
	}

	public void setAmountToMaterials(float amountToMaterials) {
		this.amountToMaterials = amountToMaterials;
	}

	public void setFinalPrice(float finalPrice) {
		this.finalPrice = finalPrice;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
