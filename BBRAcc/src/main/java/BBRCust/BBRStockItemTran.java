package BBRCust;

import java.util.Date;

import javax.persistence.*;

import BBR.BBRDataElement;
import BBRAcc.BBRPoS;

@Entity
@Table(name="stockitemacc")
public class BBRStockItemTran extends BBRDataElement {
	
	@Id
	@GeneratedValue
	@Column(name="TRAN_ID")
	private Long id;
	
	@Column(name="ITEM")
	private BBRStockItem item;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private BBRPoS pos;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private BBRSpecialist specialist;

	@Column(name="DATE")
	private Date date;

	@Column(name="TYPE")
	private char type;
	
	@Column(name="QTY")
	private Float qty;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public BBRSpecialist getSpecialist() {
		return specialist;
	}

	public void setSpecialist(BBRSpecialist specialist) {
		this.specialist = specialist;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public Float getQty() {
		return qty;
	}

	public void setQty(Float qty) {
		this.qty = qty;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
