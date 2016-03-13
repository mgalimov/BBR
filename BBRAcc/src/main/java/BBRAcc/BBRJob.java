package BBRAcc;

import javax.persistence.*;

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

}
