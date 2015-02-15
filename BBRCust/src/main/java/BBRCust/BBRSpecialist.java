package BBRCust;

import BBR.BBRDataElement;

public class BBRSpecialist extends BBRDataElement {
	private Long id;
	private String name;
	private String position; 
	private Long userId;
	
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

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Long getUserId() {
		return userId;
	}
}
