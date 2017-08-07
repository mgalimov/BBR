package BBRCust;

import java.util.Date;

import javax.persistence.*;

import BBR.BBRDataElement;
import BBR.JsonFormat;

@Entity
@Table(name="turns")
public class BBRTurn extends BBRDataElement {
	@Id
	@GeneratedValue
	@Column(name="TURN_ID")
	private Long id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private BBRSpecialist specialist;

	@JsonFormat(format="yyyy-MM-dd")
	@Column(name="TURN_DATE")
	private Date date;

	@JsonFormat(format="HH:mm")
	@Column(name="START_TIME")
	private Date startTime;
	
	@JsonFormat(format="HH:mm")
	@Column(name="END_TIME")
	private Date endTime;
	
	@Column(name="TITLE")
	private String title;
	
	public BBRTurn() {}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	
	public BBRSpecialist getSpecialist() {
		return specialist;
	}

	public void setSpecialist(BBRSpecialist specialist) {
		this.specialist = specialist;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
