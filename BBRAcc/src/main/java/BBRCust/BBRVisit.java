package BBRCust;

import java.text.SimpleDateFormat;
import java.util.Date;

import BBR.BBRDataElement;
import BBR.BBRUtil;
import BBRAcc.BBRPoS;
import BBRAcc.BBRUser;

public class BBRVisit extends BBRDataElement {
	private Long id;
	private Date timeScheduled;
	private BBRPoS pos;
	private BBRUser user;
	private String userName;
	private String userContacts;
	private String posTitle;
	private Float length = 0F;
	private BBRSpecialist spec;
	private BBRProcedure procedure;
	
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
	
	@Override
	public String toJson() {
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		BBRVisitString vs = new BBRVisitString();
		vs.id = id;
		if (timeScheduled != null)
			vs.timeScheduled = df.format(timeScheduled);
		else
			vs.timeScheduled = "08:00";
		vs.pos = pos;
		vs.user = user;
		vs.userName = userName;
		vs.userContacts = userContacts;
		vs.length = length;
		vs.spec = spec;
		vs.procedure = procedure;
		vs.posTitle = posTitle;
		String s = BBRUtil.gson.toJson(vs); 
		return s;
	}
	
	@SuppressWarnings("unused")
	private class BBRVisitString {
		private Long id;
		private String timeScheduled;
		private BBRPoS pos;
		private BBRUser user;
		private String userName;
		private String userContacts;
		private Float length = 0F;
		private BBRSpecialist spec;
		private BBRProcedure procedure;
		private String posTitle;
		
		BBRVisitString() {}
	}
}
