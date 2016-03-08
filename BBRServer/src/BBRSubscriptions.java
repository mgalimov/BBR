import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRErrors;
import BBR.BBRUtil;
import BBRAcc.BBRService;
import BBRAcc.BBRServiceManager;
import BBRAcc.BBRServiceSubscription;
import BBRAcc.BBRServiceSubscription.BBRServiceSubscriptionStatuses;
import BBRAcc.BBRServiceSubscriptionManager;
import BBRAcc.BBRShop;
import BBRAcc.BBRShopManager;
import BBRAcc.BBRUser.BBRUserRole;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;

@WebServlet("/BBRSubscriptions")
public class BBRSubscriptions extends
		BBRBasicServlet<BBRServiceSubscription, BBRServiceSubscriptionManager> {
	private static final long serialVersionUID = 1L;

	public BBRSubscriptions() throws InstantiationException,
			IllegalAccessException {
		super(BBRServiceSubscriptionManager.class);
	}

	@Override
	protected String create(BBRParams params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			Long serviceId = Long.parseLong(params.get("service"));
			BBRServiceManager smgr = new BBRServiceManager();
			BBRService service = smgr.findById(serviceId);

			Long shopId = Long.parseLong(params.get("shop"));
			BBRShopManager shmgr = new BBRShopManager();
			BBRShop shop = shmgr.findById(shopId);

			String sd = params.get("startDate");
			Date startDate = new Date();
			if (!sd.isEmpty()) {
				SimpleDateFormat df = new SimpleDateFormat(
						BBRUtil.fullDateFormat);
				startDate = df.parse(sd);
			}

			BBRServiceSubscription ss = manager
					.createAndStoreServiceSubscription(service, shop, startDate);
			if (ss == null)
				throw new Exception();
		} catch (Exception ex) {
			throw new Exception(BBRErrors.ERR_WRONG_INPUT_FORMAT);
		}
		return "";
	}

	@Override
	protected BBRServiceSubscription beforeUpdate(
			BBRServiceSubscription subscr, BBRParams params,
			HttpServletRequest request, HttpServletResponse response) {
		int status = (int) Integer.parseInt(params.get("status"));
		subscr.setStatus(status);
		return subscr;
	}

	@Override
	protected String getReferenceData(String query, BBRParams params,
			HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		BBRShop shop = null;
		String where = "";

		if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN
				|| context.user.getRole() == BBRUserRole.ROLE_POS_SPECIALIST
				|| context.user.getRole() == BBRUserRole.ROLE_VISITOR)
			return null;
		else if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
			shop = context.user.getShop();

		if (shop != null)
			where = " where shop.id = " + shop.getId();

		return manager.list(query, manager.getTitleField(), where).toJson();
	}

	@Override
	protected String processOperation(String operation, BBRParams params,
			HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		if (operation.equals("cancelSubscriptionByShopAdmin")) {
			if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN) {
				Long subscrId = Long.parseLong(params.get("subscrId"));
				BBRServiceSubscriptionManager smgr = new BBRServiceSubscriptionManager();
				BBRServiceSubscription subscr = smgr.findById(subscrId);
				if (subscr != null)
					smgr.cancelSubscriptionByShopAdmin(context.user, subscr);
			}
		} else if (operation.equals("toggleallsubscriptions")) {
			context.set("viewsubscriptions", "all");
		} else if (operation.equals("toggleactivesubscriptions")) {
			context.set("viewsubscriptions", null);
		}
		return "";
	};

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
		
		if (context.get("viewsubscriptions") == null) {
			if (!where.equals(""))
				where += " and ";
			where += "(status = " + BBRServiceSubscriptionStatuses.SUBSCRIPTION_ACTIVE + ")";
		}
		
		return manager.list(pageNumber, pageSize, where,
				BBRContext.getOrderBy(sortingFields, fields)).toJson();
	}

}
