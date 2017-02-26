package BBRCust;

import javax.persistence.*;

import BBR.BBRDataElement;
import BBRAcc.BBRPoS;

@Entity
@Table(name="proceduregroups")
public class BBRProcedureGroup extends BBRDataElement {
	@Id
	@GeneratedValue
	@Column(name="GROUP_ID")
	private Long id;
	
	@Column(name="TITLE")
	private String title;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private BBRPoS pos;

	@Column(name="DESCRIPTION")
	private String description;

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

	public BBRPoS getPos() {
		return pos;
	}

	public void setPos(BBRPoS pos) {
		this.pos = pos;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
