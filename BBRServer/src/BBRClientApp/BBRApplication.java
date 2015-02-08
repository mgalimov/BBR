package BBRClientApp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import BBR.*;

public class BBRApplication {
	public BBRUser user = null;
	
	public BBRApplication() {
	}
	
	public static BBRApplication getApp(HttpServletRequest request) {
		HttpSession session = request.getSession(true);		
		BBRApplication app = (BBRApplication)session.getAttribute("BBRApplication");
		if (app == null) {
			app = new BBRApplication();
			session.setAttribute("BBRApplication", app);
		}
		return app;
	}

	public String SignIn(String email, String password) {
		BBRUserManager mgr = new BBRUserManager();
		String respText = "";
		
		if (email == null || email == "") {
			respText = BBRErrors.ERR_EMPTY_EMAIL;
		} else {
			BBRUser candidate = mgr.findUserByEmail(email);
			if (candidate == null) {
				respText = BBRErrors.ERR_USER_NOTFOUND;
			} else {
				if (candidate.comparePasswordTo(BBRUserManager.encodePassword(password))) {
					user = candidate;
					respText = "";
				} else {
					respText = BBRErrors.ERR_INCORRECT_PASSWORD;
				}
			}
		}
		
		return respText;
	}
	
	public String SignOut() {
		user = null;
		return BBRErrors.MSG_USER_SIGNED_OUT;
	}
	
	public String SignUp(String email, String firstName, String lastName, String password) {
		BBRUserManager mgr = new BBRUserManager();
		String respText = "";
		
		try {
			BBRUser candidate = mgr.createAndStoreUser(email, firstName, lastName, password);
			user = candidate;
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
		}
		return respText;
	}
	
	public String getUserData(Long id) {
		BBRUserManager mgr = new BBRUserManager();
		String json = "";
		
		try {
			BBRUser user = mgr.findUserById(id);
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
			BBRUser user = mgr.findUserById(id);
			if (user != null)
				mgr.deleteUser(user);
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
			BBRUser user = mgr.findUserById(id);
			if (user != null) {
				user.setFirstName(firstName);
				user.setLastName(lastName);
				user.setApproved(approved);
				user.setRole(role);
				mgr.updateUser(user);
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
			//BBRUserManager.generatePassword()
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
		}
		
		return respText;
	}

	public String getUsers() {
		BBRUserManager mgr = new BBRUserManager();
		return mgr.listUsers().toJson();
	}

	public String getShopData(Long id) {
		BBRShopManager mgr = new BBRShopManager();
		String json = "";
		
		try {
			BBRShop shop = mgr.findShopById(id);
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
			BBRShop shop = mgr.findShopById(id);
			if (shop != null)
				mgr.deleteShop(shop);
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
			BBRShop shop = mgr.findShopById(id);
			if (shop != null) {
				shop.setTitle(title);
				mgr.updateShop(shop);
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
		return mgr.listShops().toJson();
	}

	public String getWelcomePage() {
		if (user == null)
			return "general-signin.jsp";

		if (user.getApproved())
			return "user-profile.jsp";
					
		if (user.getRole() == BBRUser.BBRUserRole.ROLE_BBR_OWNER)
			return "admin-index.jsp";
		
		return "general-signin.jsp";
	}
	
	public boolean isPageAvailable(String page) {
		if (user == null)
			if (page.equals("general_signin.jsp")) return true;
		
		if (user.getRole() == BBRUser.BBRUserRole.ROLE_BBR_OWNER)
			if (page.contains("admin-")) return true;

		return false;
	}
}
