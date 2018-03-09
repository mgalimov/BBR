package BBRCust;

import java.util.Date;

import javax.persistence.*;

import BBR.BBRDataElement;
import BBRAcc.BBRPoS;

@Entity
@Table(name="stockitemparties")
public class BBRStockItemParty extends BBRDataElement {
	
	@Id
	@GeneratedValue
	@Column(name="PARTY_ID")
	private Long id;

	@Column(name="TITLE")
	private String title;

	@ManyToOne(fetch=FetchType.EAGER)
	private BBRStockItem item;

	@ManyToOne(fetch=FetchType.EAGER)
	private BBRPoS pos;

	@Column(name="DATE")
	private Date date;
	
	@Column(name="PRICE")
	private Float price;

	@Column(name="BEST_BEFORE")
	private Date bestBefore;

	@Column(name="DOC")
	private String doc;

	@Column(name="BALANCE")
	private Float balance;

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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Date getBestBefore() {
		return bestBefore;
	}

	public void setBestBefore(Date bestBefore) {
		this.bestBefore = bestBefore;
	}

	public String getDoc() {
		return doc;
	}

	public void setDoc(String doc) {
		this.doc = doc;
	}

	public Float getBalance() {
		return balance;
	}

	public void setBalance(Float balance) {
		this.balance = balance;
	}

	public BBRStockItem getItem() {
		return item;
	}

	public void setItem(BBRStockItem item) {
		this.item = item;
	}

	public BBRPoS getPos() {
		return pos;
	}

	public void setPos(BBRPoS pos) {
		this.pos = pos;
	}

}
