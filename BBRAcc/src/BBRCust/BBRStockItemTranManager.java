package BBRCust;

import java.util.Date;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRErrors;
import BBR.BBRUtil;
import BBRAcc.BBRPoS;

public class BBRStockItemTranManager extends BBRDataManager<BBRStockItemTran> {

	public BBRStockItemTranManager() {
		super();
		titleField = "id";
		classTitle = "StockItemTransaction";
	}

	// addition
	public BBRStockItemTran create(BBRStockItem item, BBRPoS pos, BBRSpecialist specialist, Date date,
			Float qty, Float price, Date bestBefore, String doc) throws Exception {
		if (item == null || pos == null)
			throw new Exception(BBRErrors.ERR_ALL_FIELDS_MUST_BE_FILLED);

		char type = 'A';

		BBRStockItemPartyManager mgr = new BBRStockItemPartyManager();
		BBRStockItemParty party = mgr.create(item, pos, date, price, bestBefore, doc, qty);
		
		boolean tr = BBRUtil.beginTran();
		Session session = BBRUtil.getSession();

		BBRStockItemTran tran = new BBRStockItemTran();
		tran.setSpecialist(specialist);
		tran.setItem(item);
		tran.setPos(pos);
		tran.setType(type);
		tran.setDate(date);
		tran.setQty(qty);
		tran.setParty(party);
		session.save(tran);

		BBRUtil.commitTran(tr);
		
		return tran;
	}

	// subtraction
	public BBRStockItemTran create(BBRStockItem item, BBRPoS pos, BBRSpecialist specialist, Date date,
			Float qty, BBRStockItemParty party) throws Exception {
		if (item == null || pos == null || party == null)
			throw new Exception(BBRErrors.ERR_ALL_FIELDS_MUST_BE_FILLED);

		if (party.getBalance() < qty)
			throw new Exception(BBRErrors.ERR_BALANCE_EXCEEDED);
		
		char type = 'S';

		boolean tr = BBRUtil.beginTran();
		Session session = BBRUtil.getSession();

		BBRStockItemTran tran = new BBRStockItemTran();
		tran.setSpecialist(specialist);
		tran.setItem(item);
		tran.setPos(pos);
		tran.setType(type);
		tran.setDate(date);
		tran.setQty(qty);
		tran.setParty(party);
		
		session.save(tran);

		party.setBalance(party.getBalance() - qty);
		session.save(party);
		
		BBRUtil.commitTran(tr);
		
		return tran;
	}

	public boolean isTypeAllowed(char type) {
		if (type == 'A' || type == 'S')
			return true;
		return false;
	}

	public String whereShop(Long shopId) {
		return "pos.shop.id = " + shopId;
	};

}