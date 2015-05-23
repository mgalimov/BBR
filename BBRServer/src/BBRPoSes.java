import java.text.SimpleDateFormat;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRGPS;
import BBRAcc.BBRPoS;
import BBRAcc.BBRPoSManager;
import BBRAcc.BBRShop;
import BBRAcc.BBRShopManager;

@WebServlet("/BBRPoSes")
public class BBRPoSes extends BBRBasicServlet<BBRPoS, BBRPoSManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRPoSes() throws InstantiationException, IllegalAccessException {
        super(BBRPoSManager.class);
    }

	@Override
	String create(BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String shopId = params.get("shop");
		String title = params.get("title");
		String locationDescription = params.get("locationDescription");
		String startWorkHour = params.get("startWorkHour");
		String endWorkHour = params.get("endWorkHour");
		String locationLat = params.get("locationGPS_lat");
		String locationLng = params.get("locationGPS_lng");
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		BBRShopManager shopMgr = new BBRShopManager();
		BBRShop shop = shopMgr.findById(Long.parseLong(shopId));
		if (shop != null) 
			manager.createAndStorePoS(
					shop, 
					title, 
					locationDescription, 
					new BBRGPS(Float.parseFloat(locationLat), Float.parseFloat(locationLng)),
					df.parse(startWorkHour),
					df.parse(endWorkHour)
					);
		return "";
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
		if (locationLat.isEmpty())
			locationLat = "0";
		if (locationLng.isEmpty())
			locationLng = "0";
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		BBRShopManager shopMgr = new BBRShopManager();
		BBRShop shop = shopMgr.findById(Long.parseLong(shopId));
		if (shop != null) {
			pos.setShop(shop);
			pos.setTitle(title);
			pos.setLocationDescription(locationDescription);
			pos.setLocationGPS(new BBRGPS(Float.parseFloat(locationLat), Float.parseFloat(locationLng)));
			pos.setStartWorkHour(df.parse(startWorkHour));
			pos.setEndWorkHour(df.parse(endWorkHour));
			manager.update(pos);
		}
		return null;		
	}

}
