package BBRCust;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import BBR.BBRDataElement;
import BBRAcc.BBRPoS;
import BBRAcc.BBRShop;

@Entity
@Table(name="promos")
public class BBRPromo extends BBRDataElement {
	@Id
	@GeneratedValue
	@Column(name="PROMO_ID")
	private Long id;
	
	@Column(name="TITLE")
	private String title;
	
	@ManyToOne(fetch = FetchType.EAGER)
	BBRShop shop;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "PROMO_POS", joinColumns = {
			@JoinColumn(name = "PROMO_ID")
	}, inverseJoinColumns = {
			@JoinColumn(name = "POS_ID")
	})
	private Set<BBRPoS> poses;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "PROMO_PROC", joinColumns = {
			@JoinColumn(name = "PROMO_ID")
	}, inverseJoinColumns = {
			@JoinColumn(name = "PROC_ID")
	})
	private Set<BBRProcedure> procedures;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name="PROMO_SRC", joinColumns=@JoinColumn(name="PROMO_ID"))
	@Column(name="SOURCE")
	private Set<Integer> sources;
	
	public class BBRPromoStatus {
		public static final int PROMOSTATUS_INITIALIZED = 0;
		public static final int PROMOSTATUS_APPROVED = 1;
		public static final int PROMOSTATUS_INACTIVE = 2;
	}
	
	@Column(name="STATUS")
	private int status;

	@Column(name="START_DATE")
	private Date startDate;

	@Column(name="END_DATE")
	private Date endDate;

	@Column(name="DISCOOUNT")
	private float discount;

	@Column(name="VISITS_NUMBER")
	private int visitsNumber;

	public class BBRPromoType {
		public static final int PROMOTYPE_DISCOUNT = 0;
		public static final int PROMOTYPE_FREE_VISIT = 1;
	}
	
	@Column(name="PROMO_TYPE")
	private int promoType;

	public BBRPromo() {
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

	public BBRShop getShop() {
		return shop;
	}

	public void setShop(BBRShop shop) {
		this.shop = shop;
	}

	public Set<BBRPoS> getPoses() {
		return poses;
	}

	public void setPoses(Set<BBRPoS> poses) {
		this.poses = poses;
	}

	public Set<BBRProcedure> getProcedures() {
		return procedures;
	}

	public void setProcedures(Set<BBRProcedure> procedures) {
		this.procedures = procedures;
	}

	public Set<Integer> getSources() {
		return sources;
	}

	public void setSources(Set<Integer> sources) {
		this.sources = sources;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public int getVisitsNumber() {
		return visitsNumber;
	}

	public void setVisitsNumber(int visitsNumber) {
		this.visitsNumber = visitsNumber;
	}

	public int getPromoType() {
		return promoType;
	}

	public void setPromoType(int promoType) {
		this.promoType = promoType;
	}

}
