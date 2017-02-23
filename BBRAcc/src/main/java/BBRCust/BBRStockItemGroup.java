package BBRCust;

import javax.persistence.*;

import BBR.BBRDataElement;
import BBRAcc.BBRShop;

@Entity
@Table(name="stockitemgroups")
public class BBRStockItemGroup extends BBRDataElement {
	
	@Id
	@GeneratedValue
	@Column(name="GROUP_ID")
	private Long id;
	
	@Column(name="TITLE")
	private String title;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private BBRShop shop;
	
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

	public BBRShop getShop() {
		return shop;
	}

	public void setShop(BBRShop shop) {
		this.shop = shop;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
