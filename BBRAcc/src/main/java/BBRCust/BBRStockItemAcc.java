package BBRCust;

import javax.persistence.*;

import BBR.BBRDataElement;
import BBRAcc.BBRPoS;

@Entity
@Table(name="stockitemacc")
public class BBRStockItemAcc extends BBRDataElement {
	
	@Id
	@GeneratedValue
	@Column(name="TRAN_ID")
	private Long id;
	
	@Column(name="ITEM")
	private BBRStockItem item;
	
	@Column(name="QTY")
	private Float qty;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private BBRPoS pos;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private BBRSpecialist specialist;

	@Column(name="AMOUNT")
	private Float amount;

	
}
