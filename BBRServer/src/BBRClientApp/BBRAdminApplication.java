package BBRClientApp;

import java.util.Hashtable;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import BBR.BBRErrors;
import BBRAcc.*;

public class BBRAdminApplication {
	public BBRAcc.BBRUser user = null;
	private String lastSignInError = ""; 
	
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

	public String getUsers(int pageNumber, int pageSize, List<Hashtable<String, String>> sortingFields) {
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

	public String getShops(int pageNumber, int pageSize, List<Hashtable<String, String>> sortingFields) {
		BBRShopManager mgr = new BBRShopManager();
		return mgr.list(pageNumber, pageSize, BBRContext.getOrderBy(sortingFields)).toJson();
	}


}
