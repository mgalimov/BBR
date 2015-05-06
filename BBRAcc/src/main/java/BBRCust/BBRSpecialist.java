package BBRCust;

import BBR.BBRDataElement;
import BBRAcc.BBRPoS;
import BBRAcc.BBRUser;

public class BBRSpecialist extends BBRDataElement {
	private Long id;
	private String name;
	private String position; 
	private BBRUser user;
	private BBRPoS pos;
	
	public class BBRSpecialistState {
		public static final int SPECSTATE_ACTIVE = 1;
		public static final int SPECSTATE_INACTIVE = 2;
	}
	
	private int status;
	
	public BBRSpecialist() {}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
	public String getPosition() {
		return position;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
