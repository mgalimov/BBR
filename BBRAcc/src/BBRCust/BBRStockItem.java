package BBRCust;

import javax.persistence.*;

import BBR.BBRDataElement;

@Entity
@Table(name="stockitems")
public class BBRStockItem extends BBRDataElement {
	
	@Id
	@GeneratedValue
	@Column(name="ITEM_ID")
	private Long id;
	
	@Column(name="TITLE")
	private String title;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private BBRStockItemGroup group;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="STATE")
	private int state;
	BBRStockItem() {}

	public class BBRStockItemState {
		public static final int ITEMSTATE_ACTIVE = 1;
		public static final int ITEMSTATE_INACTIVE = 2;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public BBRStockItemGroup getGroup() {
		return group;
	}

	public void setGroup(BBRStockItemGroup group) {
		this.group = group;
	}

}
