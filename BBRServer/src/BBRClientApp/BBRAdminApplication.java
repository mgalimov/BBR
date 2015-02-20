package BBRClientApp;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import BBR.BBRDataSet;
import BBR.BBRErrors;
import BBRAcc.*;

public class BBRAdminApplication {
	public BBRAcc.BBRUser user = null;
	
	public BBRAdminApplication() {
	}
	
	public static BBRAdminApplication getApp(HttpServletRequest request) {
		HttpSession session = request.getSession(true);		
		BBRAdminApplication app = (BBRAdminApplication)session.getAttribute("BBRAdminApplication");
		if (app == null) {
			app = new BBRAdminApplication();
			session.setAttribute("BBRAdminApplication", app);
		}
		return app;
	}

	public String getUserData(Long id) {
		BBRUserManager mgr = new BBRUserManager();
		String json = "";
		
		try {
			BBRUser user = mgr.findById(id);
			if (user != null)
				json = user.toJson();
			else
				json = BBRErrors.ERR_USER_NOTFOUND;
		} catch (Exception ex) {
			json = ex.getLocalizedMessage();
		}
		return json;
	}

	public String deleteUser(Long id) {
		BBRUserManager mgr = new BBRUserManager();
		String respText = "";
		
		try {
			BBRUser user = mgr.findById(id);
			if (user != null)
				mgr.delete(user);
			else
				respText = BBRErrors.ERR_USER_NOTFOUND;
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
		}
		return respText;
	}

	public String updateUser(Long id, String firstName, String lastName, boolean approved, int role) {
		BBRUserManager mgr = new BBRUserManager();
		String respText = "";

		try {
			BBRUser user = mgr.findById(id);
			if (user != null) {
				user.setFirstName(firstName);
				user.setLastName(lastName);
				user.setApproved(approved);
				user.setRole(role);
				mgr.update(user);
			}
			else
				respText = BBRErrors.ERR_USER_NOTFOUND;
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
		}
		
		return respText;
	}

	public String createUser(String email, String firstName, String lastName, String password) {
		BBRUserManager mgr = new BBRUserManager();
		String respText = "";

		try {
			mgr.createAndStoreUser(email, firstName, lastName, password);
		//	BBRUserManager.generatePassword()
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
		}
		
		return respText;
	}

	public String getUsers() {
		BBRUserManager mgr = new BBRUserManager();
		return mgr.list().toJson();
	}

	public String getUsers(int pageNumber, int pageSize, Hashtable<Integer, Hashtable<String, String>> sortingFields) {
		BBRUserManager mgr = new BBRUserManager();

		return mgr.list(pageNumber, pageSize, BBRContext.getOrderBy(sortingFields)).toJson();
	}

	public String getShopData(Long id) {
		BBRShopManager mgr = new BBRShopManager();
		String json = "";
		
		try {
			BBRShop shop = mgr.findById(id);
			if (shop != null)
				json = shop.toJson();
			else
				json = BBRErrors.ERR_USER_NOTFOUND;
		} catch (Exception ex) {
			json = ex.getLocalizedMessage();
		}
		return json;
	}

	public String deleteShop(Long id) {
		BBRShopManager mgr = new BBRShopManager();
		String respText = "";
		
		try {
			BBRShop shop = mgr.findById(id);
			if (shop != null)
				mgr.delete(shop);
			else
				respText = BBRErrors.ERR_USER_NOTFOUND;
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
		}
		return respText;
	}

	public String updateShop(Long id, String title) {
		BBRShopManager mgr = new BBRShopManager();
		String respText = "";

		try {
			BBRShop shop = mgr.findById(id);
			if (shop != null) {
				shop.setTitle(title);
				mgr.update(shop);
			}
			else
				respText = BBRErrors.ERR_USER_NOTFOUND;
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
		}
		
		return respText;
	}

	public String createShop(String title) {
		BBRShopManager mgr = new BBRShopManager();
		String respText = "";

		try {
			mgr.createAndStoreShop(title);
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
		}
		
		return respText;
	}

	public String getShops() {
		BBRShopManager mgr = new BBRShopManager();
		return mgr.list().toJson();
	}

	public String getShops(int pageNumber, int pageSize, Hashtable<Integer, Hashtable<String, String>> sortingFields) {
		BBRShopManager mgr = new BBRShopManager();
		return mgr.list(pageNumber, pageSize, BBRContext.getOrderBy(sortingFields)).toJson();
	}

	public String getShops(BBRUser user, String query) {
		BBRShopManager mgr = new BBRShopManager();
		return mgr.list(user, query, "title").toJson();
	}

	
	public String getPoSData(Long id) {
		BBRPoSManager mgr = new BBRPoSManager();
		String json = "";
		
		try {
			BBRPoS pos = mgr.findById(id);
			if (pos != null)
				json = pos.toJson();
			else
				json = BBRErrors.ERR_USER_NOTFOUND;
		} catch (Exception ex) {
			json = ex.getLocalizedMessage();
		}
		return json;
	}

	public String deletePoS(Long id) {
		BBRPoSManager mgr = new BBRPoSManager();
		String respText = "";
		
		try {
			BBRPoS pos = mgr.findById(id);
			if (pos != null)
				mgr.delete(pos);
			else
				respText = BBRErrors.ERR_USER_NOTFOUND;
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
		}
		return respText;
	}

	public String updatePoS(Long id, Long shopId, String title, String locationDescription) {
		BBRPoSManager mgr = new BBRPoSManager();
		BBRShopManager shopMgr = new BBRShopManager();
		String respText = "";

		try {
			BBRPoS pos = mgr.findById(id);
			if (pos != null) {
				BBRShop shop = shopMgr.findById(shopId);
				if (shop != null) {
					pos.setShop(shop);
					pos.setTitle(title);
					pos.setLocationDescription(locationDescription);
					mgr.update(pos);
				} else
					respText = BBRErrors.ERR_SHOP_NOTFOUND;
			}
			else
				respText = BBRErrors.ERR_POS_NOTFOUND;
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
		}
		
		return respText;
	}

	public String createPoS(Long shopId, String title, String locationDescription) {
		BBRPoSManager mgr = new BBRPoSManager();
		BBRShopManager shopMgr = new BBRShopManager();
		String respText = "";

		try {
			BBRShop shop = shopMgr.findById(shopId);
			if (shop != null)
				mgr.createAndStorePoS(shop, title, locationDescription, null);
			else 
				respText = BBRErrors.ERR_SHOP_NOTFOUND;
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
		}
		
		return respText;
	}

	public String getPoSes(int pageNumber, int pageSize, Hashtable<Integer, Hashtable<String, String>> sortingFields) {
		BBRPoSManager mgr = new BBRPoSManager();
		BBRDataSet<BBRPoS> poses = mgr.list(pageNumber, pageSize, BBRContext.getOrderBy(sortingFields));
		String s = poses.toJson();
		return s;
	}

}
