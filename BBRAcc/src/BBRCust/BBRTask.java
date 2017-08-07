package BBRCust;

import java.util.Date;

import javax.persistence.*;

import BBR.BBRDataElement;
import BBRAcc.BBRPoS;
import BBRAcc.BBRUser;

@Entity
@Table(name="tasks")
public class BBRTask extends BBRDataElement {
	
	@Id
	@GeneratedValue
	@Column(name="TASK_ID")
	private Long id;
	
	@Column(name="TITLE")
	private String title;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private BBRPoS pos;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private BBRUser performer;
	
	@Column(name="DEADLINE")
	private Date deadline;
	
	@Column(name="CREATEDAT")
	private Date createdAt;
	
	@Column(name="TEXT")
	private String text;
	
	@Column(name="STATE")
	private int state;
	
	@Column(name="OBJECT_TYPE")
	private String objectType;
	
	@Column(name="OBJECT_ID")
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
