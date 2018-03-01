import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRErrors;
import BBR.BBRUtil;
import BBRAcc.BBRPoS;
import BBRAcc.BBRPoSManager;
import BBRAcc.BBRUser.BBRUserRole;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;
import BBRCust.BBRSpecialist;
import BBRCust.BBRSpecialistManager;
import BBRCust.BBRStockItem;
import BBRCust.BBRStockItemManager;
import BBRCust.BBRStockItemParty;
import BBRCust.BBRStockItemPartyManager;
import BBRCust.BBRStockItemTran;
import BBRCust.BBRStockItemTranManager;

@WebServlet("/BBRStockItemTrans")
public class BBRStockItemTrans extends
		BBRBasicServlet<BBRStockItemTran, BBRStockItemTranManager> {
	private static final long serialVersionUID = 1L;

	public BBRStockItemTrans() throws InstantiationException,
			IllegalAccessException {
		super(BBRStockItemTranManager.class);
	}

	protected BBRStockItemTran parseParams(BBRStockItemTran tran, BBRParams params) throws Exception {
		String itemId = params.get("item");
		String posId = params.get("pos");
		String specId = params.get("specialist");
		String sDate = params.get("date");
		String sType = params.get("type");
		String sQty = params.get("qty");
		String partyId = params.get("partyId");
		
		BBRStockItem item = null;
		try {
			BBRStockItemManager mgr = new BBRStockItemManager();
			item = mgr.findById(Long.parseLong(itemId));
		} catch (Exception ex) {
			throw new Exception(BBRErrors.ERR_ITEM_NOT_SPECIFIED);
		}

		if (item == null) {
			throw new Exception(BBRErrors.ERR_ITEM_NOT_SPECIFIED);
		}

		BBRPoS pos = null;
		try {
			BBRPoSManager mgr = new BBRPoSManager();
			pos = mgr.findById(Long.parseLong(posId));
		} catch (Exception ex) {
			throw new Exception(BBRErrors.ERR_POS_MUST_BE_SPECIFIED);
		}

		if (pos == null) {
			throw new Exception(BBRErrors.ERR_POS_MUST_BE_SPECIFIED);
		}

		BBRSpecialist spec = null;
		if (specId != null && !specId.isEmpty())
			try {
				BBRSpecialistManager mgr = new BBRSpecialistManager();
				spec = mgr.findById(Long.parseLong(specId));
			} catch (Exception ex) {
				throw new Exception(BBRErrors.ERR_SPEC_MUST_BE_SPECIFIED);
			}
		
		char type;
		if (sType != null && !sType.isEmpty())
			type = sType.charAt(0);
		else
			throw new Exception(BBRErrors.ERR_WRONG_INPUT_FORMAT);

		Date date = null;
		if (sDate != null && !sDate.isEmpty()) {
			try {
				SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat);
				date = df.parse(sDate);
			} catch (Exception ex) {
				throw new Exception(BBRErrors.ERR_DATE_INCORRECT);
			}
		}	

		Float qty = null;
		if (sQty != null && !sQty.isEmpty()) {
			try {
				qty = Float.parseFloat(sQty);
			} catch (Exception ex) {
				throw new Exception(BBRErrors.ERR_WRONG_INPUT_FORMAT);
			}
		}	
		
		BBRStockItemParty party = null;
		if (partyId != null && !partyId.isEmpty())
			try {
				BBRStockItemPartyManager mgr = new BBRStockItemPartyManager();
				party = mgr.findById(Long.parseLong(partyId));
			} catch (Exception ex) {
				throw new Exception(BBRErrors.ERR_BALANCE_EXCEEDED);
			}

		tran.setDate(date);
		tran.setItem(item);
		tran.setPos(pos);
		tran.setQty(qty);
		tran.setSpecialist(spec);
		tran.setType(type);
		tran.setParty(party);
		
		return tran;
	}
	
	@Override
	protected String create(BBRParams params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		BBRStockItemTran tran = new BBRStockItemTran();
		tran = parseParams(tran, params);

		if (tran.getType() == 'A') {
			String sPrice = params.get("price");
			String sBestBefore = params.get("bestBefore");
			String sDoc = params.get("doc");

			Float price = null;
			if (sPrice != null && !sPrice.isEmpty()) {
				try {
					price = Float.parseFloat(sPrice);
				} catch (Exception ex) {
					throw new Exception(BBRErrors.ERR_WRONG_INPUT_FORMAT);
				}
			}
			
			Date bestBefore = null;
			if (sBestBefore != null && !sBestBefore.isEmpty()) {
				try {
					SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat);
					bestBefore = df.parse(sBestBefore);
				} catch (Exception ex) {
					throw new Exception(BBRErrors.ERR_DATE_INCORRECT);
				}
			}	
			tran = manager.create(tran.getItem(), tran.getPos(), tran.getSpecialist(), tran.getDate(), tran.getQty(), price, bestBefore, sDoc);
		} else if (tran.getType() == 'S') {
			tran = manager.create(tran.getItem(), tran.getPos(), tran.getSpecialist(), tran.getDate(), tran.getQty(), tran.getParty());
		}

		if (tran != null)
			return tran.getId().toString();
		else
			return null;
	}

	@Override
	protected BBRStockItemTran beforeUpdate(BBRStockItemTran tran, BBRParams params,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return parseParams(tran, params);
	}

	protected String getData(int pageNumber, int pageSize,
			Hashtable<Integer, Hashtable<String, String>> fields,
			Hashtable<Integer, Hashtable<String, String>> sortingFields,
			BBRParams params, HttpServletRequest request,
			HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		String where = "";
		if (context.user != null) {
			if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN
					|| context.user.getRole() == BBRUserRole.ROLE_POS_SPECIALIST)
				if (context.user.getPos() != null)
					where = manager.wherePos(context.user.getPos().getId());
			if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
				if (context.user.getShop() != null)
					where = manager.whereShop(context.user.getShop().getId());
			if (context.user.getRole() == BBRUserRole.ROLE_BBR_OWNER)
				if (context.filterPoS != null)
					where = manager.wherePos(context.filterPoS.getId());
				else if (context.filterShop != null)
					where = manager.whereShop(context.filterShop.getId());
		}

//		@SuppressWarnings("unchecked")
//		Hashtable<String, String> filter = (Hashtable<String, String>)context.get("filter");
//		String groupId = "";
//		if (filter != null)
//			groupId = filter.get("group"); 
//		if (groupId != null && !groupId.isEmpty()) {
//			where += " and group.id = " + groupId; 
//		}
//		
		return manager.list(pageNumber, pageSize, where,
				BBRContext.getOrderBy(sortingFields, fields)).toJson();
	}
}
