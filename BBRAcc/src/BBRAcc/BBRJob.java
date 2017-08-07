package BBRAcc;

import javax.persistence.*;
import java.util.Date;

import BBR.BBRDataElement;

@Entity
@Table(name="jobs")
public class BBRJob extends BBRDataElement {
	@Id
	@GeneratedValue
	@Column(name="JOB_ID")
	private Long id;
	
	@Column(name="TITLE")
	private String title;

	@Column(name="LAST_RUN")
	private Date lastRun;

	@Column(name="NEXT_RUN")
	private Date nextRun;

	@Column(name="RUN_CONDITIONS")
	private String runConditions;

	@Column(name="RUN_METHOD")
	private String runMethod;

	@Column(name="LAST_RUN_STATUS")
	private String lastRunStatus;
	
	public BBRJob() {}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}

	public Date getLastRun() {
		return lastRun;
	}

	public void setLastRun(Date lastRun) {
		this.lastRun = lastRun;
	}

	public Date getNextRun() {
		return nextRun;
	}

	public void setNextRun(Date nextRun) {
		this.nextRun = nextRun;
	}

	public String getRunConditions() {
		return runConditions;
	}

	public void setRunConditions(String runConditions) {
		this.runConditions = runConditions;
	}

	public String getRunMethod() {
		return runMethod;
	}

	public void setRunMethod(String runMethod) {
		this.runMethod = runMethod;
	}

	public String getLastRunStatus() {
		return lastRunStatus;
	}

	public void setLastRunStatus(String lastRunStatus) {
		this.lastRunStatus = lastRunStatus;
	}

}
