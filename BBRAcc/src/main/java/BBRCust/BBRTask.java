package BBRCust;

import java.util.Date;

import BBR.BBRDataElement;
import BBRAcc.BBRPoS;
import BBRAcc.BBRUser;

public class BBRTask extends BBRDataElement {
	private Long id;
	private String title;
	private BBRPoS pos;
	private BBRUser performer;
	private Date deadline;
	private Date createdAt;
	private String text;
	private int state;
	private String objectType;
	private Long objectId;
	
	BBRTask() {}

	public class BBRTaskState {
		public static final int TASKSTATE_INITIALIZED = 0;
		public static final int TASKSTATE_READ = 1;
		public static final int TASKSTATE_COMPLETED = 2;
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

	public BBRUser getPerformer() {
		return performer;
	}

	public void setPerformer(BBRUser performer) {
		this.performer = performer;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public BBRPoS getPos() {
		return pos;
	}

	public void setPos(BBRPoS pos) {
		this.pos = pos;
	}
	
}
