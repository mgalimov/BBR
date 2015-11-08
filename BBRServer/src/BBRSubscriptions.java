import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRErrors;
import BBR.BBRUtil;
import BBRAcc.BBRService;
import BBRAcc.BBRServiceManager;
import BBRAcc.BBRServiceSubscription;
import BBRAcc.BBRServiceSubscriptionManager;
import BBRAcc.BBRShop;
import BBRAcc.BBRShopManager;
import BBRAcc.BBRUser.BBRUserRole;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;

@WebServlet("/BBRSubscriptions")
public class BBRSubscriptions extends BBRBasicServlet<BBRServiceSubscription, BBRServiceSubscriptionManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRSubscriptions() throws InstantiationException, IllegalAccessException {
        super(BBRServiceSubscriptionManager.class);
    }

	@Override
	protected String create(BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
				SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
				startDate = df.parse(sd);
			}
			
			manager.createAndStoreServiceSubscription(service, shop, startDate);
		} catch (Exception ex) {
			throw new Exception(BBRErrors.ERR_WRONG_INPUT_FORMAT);
		}
		return "";
	}

	@Override
	protected BBRServiceSubscription beforeUpdate(BBRServiceSubscription subscr, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		int status = (int)Integer.parseInt(params.get("status"));
		subscr.setStatus(status);
		return subscr;		
	}

	@Override
	protected String getReferenceData(String query, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		BBRShop shop = null;
		String where = "";
		
		if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN || 
			context.user.getRole() == BBRUserRole.ROLE_POS_SPECIALIST ||
			context.user.getRole() == BBRUserRole.ROLE_VISITOR)
			return null;
		else
			if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
				shop = context.user.getShop();
		
		if (shop != null)
			where = " where shop.id = " + shop.getId();
		
		return manager.list(query, manager.getTitleField(), where).toJson();
	}
}
