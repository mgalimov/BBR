package BBRClientApp;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import BBR.*;

public class BBRApplication {
	public BBRUser user = null;
	private String lastSignInError = ""; 
	
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
		BBRUserManager mgr = new BBRUserManager(BBRUser.class);
		
		if (email == null || email == "") {
			lastSignInError = BBRErrors.ERR_EMPTY_EMAIL;
		} else {
			BBRUser candidate = mgr.findUserByEmail(email);
			if (candidate == null) {
				lastSignInError = BBRErrors.ERR_USER_NOTFOUND;
			} else {
				if (candidate.comparePasswordTo(BBRUserManager.encodePassword(password))) {
					user = candidate;
					lastSignInError = "";
				} else {
					lastSignInError = BBRErrors.ERR_INCORRECT_PASSWORD;
				}
			}
		}
		
		return lastSignInError;
	}
	
	public String getLastSignInError() {
		return lastSignInError;
	}

	public String SignInByCookie(HttpServletRequest request) {
		BBRUserManager mgr = new BBRUserManager(BBRUser.class);

		String email = "";
		String pwdhash = "";
		Cookie[] cookies = request.getCookies();
		for (int i = 0; i < cookies.length; i++) {
			Cookie c = cookies[i];
			if (c.getName().equals("email"))
				email = c.getValue();
			if (c.getName().equals("pwdhash"))
				pwdhash = c.getValue();
		}
		
		if (email != "" && pwdhash != "") {
			BBRUser candidate = mgr.findUserByEmail(email);
			if (candidate == null) {
				lastSignInError = BBRErrors.ERR_USER_NOTFOUND;
			} else {
				if (candidate.comparePasswordTo(pwdhash)) {
					user = candidate;
					lastSignInError = "";
				} else {
					lastSignInError = BBRErrors.ERR_INCORRECT_PASSWORD;
				}
			}
			
		}
				
		return lastSignInError;
	}

	public String SignOut(HttpServletResponse response) {
		response.addCookie(new Cookie("email", ""));
		response.addCookie(new Cookie("pwdhash", ""));
		user = null;
		return BBRErrors.MSG_USER_SIGNED_OUT;
	}
	
	public String SignUp(String email, String firstName, String lastName, String password, String passwordCopy) {
		BBRUserManager mgr = new BBRUserManager(BBRUser.class);
		
		if (!password.equals(passwordCopy)) {
			lastSignInError = BBRErrors.ERR_INCORRECT_PASSWORD;
			return lastSignInError;
		}
			
		try {
			BBRUser candidate = mgr.createAndStoreUser(email, firstName, lastName, password);
			user = candidate;
			lastSignInError = "";
		} catch (Exception ex) {
			lastSignInError = ex.getLocalizedMessage();
		}
		return lastSignInError;
	}


	public String getUserData(Long id) {
		BBRUserManager mgr = new BBRUserManager(BBRUser.class);
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
		BBRUserManager mgr = new BBRUserManager(BBRUser.class);
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
		BBRUserManager mgr = new BBRUserManager(BBRUser.class);
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
		BBRUserManager mgr = new BBRUserManager(BBRUser.class);
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
		BBRUserManager mgr = new BBRUserManager(BBRUser.class);
		return mgr.list().toJson();
	}

	public String getUsers(int pageNumber, int pageSize) {
		BBRUserManager mgr = new BBRUserManager(BBRUser.class);
		return mgr.list(pageNumber, pageSize).toJson();
	}

	public String getShopData(Long id) {
		BBRShopManager mgr = new BBRShopManager(BBRShop.class);
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
		BBRShopManager mgr = new BBRShopManager(BBRShop.class);
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
		BBRShopManager mgr = new BBRShopManager(BBRShop.class);
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
		BBRShopManager mgr = new BBRShopManager(BBRShop.class);
		String respText = "";

		try {
			mgr.createAndStoreShop(title);
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
		}
		
		return respText;
	}

	public String getShops() {
		BBRShopManager mgr = new BBRShopManager(BBRShop.class);
		return mgr.list().toJson();
	}

	public String getShops(int pageNumber, int pageSize) {
		BBRShopManager mgr = new BBRShopManager(BBRShop.class);
		return mgr.list(pageNumber, pageSize).toJson();
	}

	public String getWelcomePage() {
		if (user == null)
			return "general-signin.jsp";

		if (user.getRole() == BBRUser.BBRUserRole.ROLE_BBR_OWNER)
			return "admin-index.jsp";
		
		if (user.getApproved())
			return "user-profile.jsp";
		else
			return "user-profile.jsp";
		
	}
	
	public boolean isPageAvailable(String page) {
		if (user == null)
			if (page.equals("general_signin.jsp")) return true;
		
		if (user.getRole() == BBRUser.BBRUserRole.ROLE_BBR_OWNER)
			if (page.contains("admin-")) return true;

		return false;
	}
	
}
