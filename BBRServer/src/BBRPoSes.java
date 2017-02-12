import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRGPS;
import BBR.BBRUtil;
import BBRAcc.BBRPoS;
import BBRAcc.BBRPoSManager;
import BBRAcc.BBRShop;
import BBRAcc.BBRShopManager;
import BBRAcc.BBRUser.BBRUserRole;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;

@WebServlet("/BBRPoSes")
public class BBRPoSes extends BBRBasicServlet<BBRPoS, BBRPoSManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRPoSes() throws InstantiationException, IllegalAccessException {
        super(BBRPoSManager.class);
    }

	@Override
	protected String create(BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		BBRPoS pos = null;
		String shopId = params.get("shop");
		String title = params.get("title");
		String locationDescription = params.get("locationDescription");
		String startWorkHour = params.get("startWorkHour");
		String endWorkHour = params.get("endWorkHour");
		String locationLat = params.get("locationGPS_lat");
		String locationLng = params.get("locationGPS_lng");
		String currency = params.get("currency");
		String timeZone = params.get("timeZone");
		String urlID = params.get("urlID");
		String email = params.get("email");
		String sms = params.get("sms");
		
		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullTimeFormat);
		BBRShopManager shopMgr = new BBRShopManager();
		BBRShop shop = shopMgr.findById(Long.parseLong(shopId));
		if (shop != null) 
			pos = manager.createAndStorePoS(
					shop, 
					title, 
					locationDescription, 
					new BBRGPS(Float.parseFloat(locationLat), Float.parseFloat(locationLng)),
					df.parse(startWorkHour),
					df.parse(endWorkHour),
					currency,
					timeZone,
					urlID,
					email,
					sms
					);
		return pos.getId().toString();
	}

	@Override
	protected BBRPoS beforeUpdate(BBRPoS pos, BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String shopId = params.get("shop");
		String title = params.get("title");
		String locationDescription = params.get("locationDescription");
		String startWorkHour = params.get("startWorkHour");
		String endWorkHour = params.get("endWorkHour");
		String locationLat = params.get("locationGPS_lat");
		String locationLng = params.get("locationGPS_lng");
		String currency = params.get("currency");
		String timeZone = params.get("timeZone");
		String urlID = params.get("urlID");
		String email = params.get("email");
		String sms = params.get("sms");

		if (locationLat.isEmpty())
			locationLat = "0";
		if (locationLng.isEmpty())
			locationLng = "0";
		
		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullTimeFormat);
		BBRShopManager shopMgr = new BBRShopManager();
		BBRShop shop = shopMgr.findById(Long.parseLong(shopId));
		if (shop != null) {
			pos.setShop(shop);
			pos.setTitle(title);
			pos.setLocationDescription(locationDescription);
			pos.setLocationGPS(new BBRGPS(Float.parseFloat(locationLat), Float.parseFloat(locationLng)));
			pos.setStartWorkHour(df.parse(startWorkHour));
			pos.setEndWorkHour(df.parse(endWorkHour));
			pos.setCurrency(currency);
			pos.setTimeZone(timeZone);
			pos.setUrlID(urlID);
			pos.setEmail(email);
			pos.setSms(sms);
			manager.update(pos);
		}
		return null;		
	}
	
	@Override
	protected String processOperation(String operation, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		if (operation.equals("specialList")) {
			BBRContext context = BBRContext.getContext(request);
			BBRShop shop = null;
			BBRPoS pos = null;
			String shopsOnly = params.get("shopsonly");
			
			if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
				shop = context.user.getShop();
			else if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN)
				pos = context.user.getPos();
			
			BBRPoSManager mgr = new BBRPoSManager();
			List<Object[]> list = mgr.listSpecialWithShops(shop, pos);

			Long currentShopId = -1L;
			String res = "[";
			for (Object[] line : list) {
				if (context.user.getRole() >= BBRUserRole.ROLE_SHOP_ADMIN)
					if (currentShopId != line[0]) {
						res += "{";
						res += "\"id\": \"s" + line[0] + "\", ";
						res += "\"title\": \"" + line[1] + "\"";
						res += "}, ";
						currentShopId = (Long)line[0];
					}
				if (!shopsOnly.equalsIgnoreCase("true")) {
					res += "{";
					res += "\"id\": \"" + line[2] + "\", ";
					res += "\"title\": \"" + line[3] + "\"";
					res += "}, ";
				}
			}
			res = res.substring(0, res.length() - 2);
			res += "]";
			
			return res;
		} else
			return "";
	};
}
