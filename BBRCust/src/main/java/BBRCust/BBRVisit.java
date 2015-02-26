package BBRCust;

import java.util.Date;

import BBR.BBRDataElement;

public class BBRVisit extends BBRDataElement {
	private Long id;
	private Date timeScheduled;
	private Long posId;
	private Long userId;
	private String userName;
	private String userContacts;
	private String procedure;
	private String posTitle;
	private BBRSpecialist spec;
	
	public class BBRVisitStatus {
		public static final int VISSTATUS_INITIALIZED = 0;
		public static final int VISSTATUS_APPROVED = 1;
		public static final int VISSTATUS_CANCELLED = 3;
		public static final int VISSTATUS_PERFORMED = 4;
	}
	
	private int status = 0;
	
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

	public void setPosId(Long posId) {
		this.posId = posId;
	}
	
	public Long getPosId() {
		return posId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Long getUserId() {
		return userId;
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

	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}
	
	public String getProcedure() {
		return procedure;
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
}
