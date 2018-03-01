package BBRCust;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRErrors;
import BBR.BBRUtil;
import BBRAcc.BBRPoS;

public class BBRStockItemPartyManager extends BBRDataManager<BBRStockItemParty> {

	public BBRStockItemPartyManager() {
		super();
		titleField = "id";
		classTitle = "StockItemParty";
	}

	public BBRStockItemParty create(BBRStockItem item, BBRPoS pos, Date date, Float price, 
			Date bestBefore, String doc, Float balance) throws Exception {
		if (item == null || pos == null)
			throw new Exception(BBRErrors.ERR_ALL_FIELDS_MUST_BE_FILLED);

		boolean tr = BBRUtil.beginTran();
		Session session = BBRUtil.getSession();

		BBRStockItemParty party = new BBRStockItemParty();
		party.setItem(item);
		party.setPos(pos);
		party.setDate(date);
		party.setBestBefore(bestBefore);
		party.setBalance(balance);
		party.setDoc(doc);

		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
		String title = df.format(date) + " / " + String.format("%2.0f", price);
		party.setTitle(title);

		session.save(party);

		BBRUtil.commitTran(tr);
		return party;
	}

	public String whereShop(Long shopId) {
		return "pos.shop.id = " + shopId;
	};

}