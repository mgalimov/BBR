import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRErrors;
import BBR.BBRUtil;
import BBRAcc.BBRPoS;
import BBRAcc.BBRPoSManager;
import BBRAcc.BBRShop;
import BBRAcc.BBRShopManager;
import BBRAcc.BBRUser.BBRUserRole;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;
import BBRCust.BBRProcedure;
import BBRCust.BBRProcedureManager;
import BBRCust.BBRPromo;
import BBRCust.BBRPromo.BBRPromoStatus;
import BBRCust.BBRPromoManager;

@WebServlet("/BBRPromos")
public class BBRPromos extends BBRBasicServlet<BBRPromo, BBRPromoManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRPromos() throws InstantiationException, IllegalAccessException {
        super(BBRPromoManager.class);
    }

    protected BBRPromo analyzeParams(BBRPromo promo, BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String title = params.get("title");
		String shopId = params.get("shop");
		String posesIds = params.get("poses");
		String spromoType = params.get("promoType");
		String startDate = params.get("startDate");
		String endDate = params.get("endDate");
		String proceduresIds = params.get("procedures");
		String sources = params.get("sources");
		String discount = params.get("discount");
		String visitsNumber = params.get("visitsNumber");
		String status = params.get("status");
		
		BBRShop shop = null;
		BBRShopManager smgr = new BBRShopManager();
		try {
			shop = smgr.findById(Long.parseLong(shopId));
			if (shop == null)
				throw new Exception(BBRErrors.ERR_SHOP_NOTFOUND);
		} catch (Exception ex) {
			throw new Exception(BBRErrors.ERR_SHOP_NOTFOUND);
		}
		
		BBRPoSManager mgr = new BBRPoSManager();
		String[] aPosesIds = posesIds.split(",");
		Set<BBRPoS> poses = new HashSet<BBRPoS>();
		for (String pId : aPosesIds) {
			try {
				BBRPoS pos = mgr.findById(Long.parseLong(pId));
				if (pos == null)
					throw new Exception(BBRErrors.ERR_POS_NOTFOUND);
				poses.add(pos);
			} catch (Exception ex) {
				throw new Exception(BBRErrors.ERR_POS_NOTFOUND);
			}
		}

		Set<BBRProcedure> procedures = null;
		BBRProcedureManager prmgr = new BBRProcedureManager();
		if (!proceduresIds.isEmpty()) {
			String[] aProcIds = proceduresIds.split(",");
			procedures = new HashSet<BBRProcedure>();
			for (String prId : aProcIds) {
				try {
					BBRProcedure proc = prmgr.findById(Long.parseLong(prId));
					if (proc == null)
						throw new Exception(BBRErrors.ERR_PROC_NOTFOUND);
					procedures.add(proc);
				} catch (Exception ex) {
					throw new Exception(BBRErrors.ERR_PROC_NOTFOUND);
				}
			}
		}
		
		Integer promoType;
		try {
			promoType = Integer.parseInt(spromoType);
		} catch (Exception ex) {
			throw new Exception(BBRErrors.ERR_WRONG_INPUT_FORMAT);
		}
		
		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
		Date aStartDate = null;
		Date aEndDate = null;
		try {
			aStartDate = df.parse(startDate);
			if (!endDate.isEmpty())
				aEndDate = df.parse(endDate);
		} catch (Exception ex) {
			throw new Exception(BBRErrors.ERR_WRONG_INPUT_FORMAT);
		}
		
		Set<Integer> sSources = null;
		if (!sources.isEmpty()) {
			String[] aSources = sources.split(",");
			sSources = new HashSet<Integer>();
			for (String src : aSources) {
				try {
					Integer isrc = Integer.parseInt(src);
					sSources.add(isrc);
				} catch (Exception ex) {
					throw new Exception(BBRErrors.ERR_WRONG_INPUT_FORMAT);
				}
			}
		}
		
		Integer aVisitsNumber;
		if (visitsNumber.isEmpty())
			aVisitsNumber = 0;
		else
			try {
				aVisitsNumber = Integer.parseInt(visitsNumber);
			} catch (Exception ex) {
				throw new Exception(BBRErrors.ERR_WRONG_INPUT_FORMAT);
			}
		
		Float aDiscount;
		if (discount.isEmpty())
			aDiscount = 0F;
		else
			try {
				aDiscount = Float.parseFloat(discount);
			} catch (Exception ex) {
				throw new Exception(BBRErrors.ERR_WRONG_INPUT_FORMAT);
			}

		Integer aStatus;
		try {
			aStatus = Integer.parseInt(status);
		} catch (Exception ex) {
			throw new Exception(BBRErrors.ERR_WRONG_INPUT_FORMAT);
		}
		
		if (promo == null)
			promo = new BBRPromo();
        promo.setTitle(title);
        promo.setShop(shop);
        promo.setPoses(poses);
        promo.setPromoType(promoType);
        promo.setStartDate(aStartDate);
        promo.setEndDate(aEndDate);
        promo.setDiscount(aDiscount);
        promo.setSources(sSources);
        promo.setProcedures(procedures);
        promo.setStatus(aStatus);
        promo.setVisitsNumber(aVisitsNumber);
        
        return promo;
    }
    
	@Override
	protected String create(BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		BBRPromo promo = analyzeParams(null, params, request, response);
		promo = manager.create(promo);
		return promo.getId().toString();
	}

	@Override
	protected BBRPromo beforeUpdate(BBRPromo promo, BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		promo = analyzeParams(promo, params, request, response);
		return promo;		
	}

	@Override
	protected String getReferenceData(String query, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		String constrains = params.get("constrains");
		
		BBRPoS pos = null;
		BBRShop shop = null;

		if (!constrains.isEmpty()) {
			BBRPoSManager pmgr = new BBRPoSManager();
			pos = pmgr.findById(Long.parseLong(constrains));
		}
		if (pos == null)
			if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN || context.user.getRole() == BBRUserRole.ROLE_POS_SPECIALIST )
				pos = context.user.getPos();
			else
				if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
					shop = context.user.getShop();
		
		return manager.list(query, manager.getTitleField(), pos, shop).toJson();
	}

	@Override
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
		}
		
		if (context.filterPoS != null)
			where = manager.wherePos(context.filterPoS.getId());
		else if (context.filterShop != null)
			where = manager.whereShop(context.filterShop.getId());

		if (context.get("promos") == null || !context.get("promos").equals("all")) {
			if (!where.equals(""))
				where += " and ";
			
			SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
			String now = df.format(BBRUtil.now(context.getTimeZone()));
			where += " (status = " + BBRPromoStatus.PROMOSTATUS_APPROVED + ") and (startDate <= '" + now + "' and coalesce(endDate, '" + now + "') >= '" + now + "')";
		}
		
		return manager.list(pageNumber, pageSize, where,
				BBRContext.getOrderBy(sortingFields, fields)).toJson();
	}

	@Override
	protected String processOperation(String operation, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);

		if (operation.equals("toggleAllPromos")) {
			context.set("promos", "all");
		} else
		if (operation.equals("toggleActivePromos")) {
			context.set("promos", null);
		} 
		
		return "";
	}
	
}
