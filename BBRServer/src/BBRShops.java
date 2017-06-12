import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRErrors;
import BBRAcc.BBRShop;
import BBRAcc.BBRShopManager;
import BBRAcc.BBRUser.BBRUserRole;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;

@WebServlet("/BBRShops")
public class BBRShops extends BBRBasicServlet<BBRShop, BBRShopManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRShops() throws InstantiationException, IllegalAccessException {
        super(BBRShopManager.class);
    }

	@Override
	protected String create(BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String title = params.get("title");
		String country = params.get("country");
		String timeZone = params.get("timeZone");
		String status = params.get("status");
		int aStatus;
		
		try {
			aStatus = Integer.parseInt(status);
		} catch (Exception ex) {
			throw new Exception(BBRErrors.ERR_WRONG_INPUT_FORMAT);
		}
		BBRShop shop = manager.create(title, country, timeZone, aStatus);
		return shop.getId().toString();
	}

	@Override
	protected BBRShop beforeUpdate(BBRShop shop, BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String title = params.get("title");
		String country = params.get("country");
		String timeZone = params.get("timeZone");
		String status = params.get("status");
		int aStatus;
		
		try {
			aStatus = Integer.parseInt(status);
		} catch (Exception ex) {
			throw new Exception(BBRErrors.ERR_WRONG_INPUT_FORMAT);
		}

		shop.setTitle(title);
		shop.setCountry(country);
		shop.setTimeZone(timeZone);
		shop.setStatus(aStatus);
		return shop;		
	}

	@Override
	protected String getReferenceData(String query, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		BBRShop shop = null;
		
		if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
			shop = context.user.getShop();
		else
			if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN || context.user.getRole() == BBRUserRole.ROLE_POS_SPECIALIST)
				shop = context.user.getPos().getShop();
		
		return manager.list(context.user, query, "title", shop).toJson();
	}
}
