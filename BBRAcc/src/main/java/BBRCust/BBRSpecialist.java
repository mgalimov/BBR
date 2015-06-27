package BBRCust;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import BBR.BBRDataElement;
import BBR.JsonFormat;
import BBRAcc.BBRPoS;
import BBRAcc.BBRUser;

@Entity
@Table(name="specialists")
public class BBRSpecialist extends BBRDataElement {
	@Id
	@GeneratedValue
	@Column(name="SPEC_ID")
	private Long id;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="POSITION")
	private String position;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private BBRUser user;

	@ManyToOne(fetch=FetchType.EAGER)
	private BBRPoS pos;
	
	public class BBRSpecialistState {
		public static final int SPECSTATE_ACTIVE = 1;
		public static final int SPECSTATE_INACTIVE = 2;
	}
	
	@Column(name="STATUS")
	private int status;

	@JsonFormat(format="HH:mm")
	@Column(name="START_WORK_HOUR")
	private Date startWorkHour;

	@JsonFormat(format="HH:mm")
	@Column(name="END_WORK_HOUR")
	private Date endWorkHour;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "SPEC_PROC", joinColumns = {
			@JoinColumn(name = "SPEC_ID")
	}, inverseJoinColumns = {
			@JoinColumn(name = "PROC_ID")
	})
	private Set<BBRProcedure> procedures;
	
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

	public Date getStartWorkHour() {
		return startWorkHour;
	}

	public void setStartWorkHour(Date startWorkHour) {
		this.startWorkHour = startWorkHour;
	}

	public Date getEndWorkHour() {
		return endWorkHour;
	}

	public void setEndWorkHour(Date endWorkHour) {
		this.endWorkHour = endWorkHour;
	}

	public Set<BBRProcedure> getProcedures() {
		return procedures;
	}

	public void setProcedures(Set<BBRProcedure> procedures) {
		this.procedures = procedures;
	}

}
