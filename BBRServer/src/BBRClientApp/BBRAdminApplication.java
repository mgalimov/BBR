package BBRClientApp;

import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import BBR.BBRErrors;
import BBRAcc.*;
import BBRCust.BBRSpecialist;
import BBRCust.BBRSpecialistManager;

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

	public String SignIn(String email, String password) {
		BBRUserManager mgr = new BBRUserManager();
		
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
		BBRUserManager mgr = new BBRUserManager();
		
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
		BBRUserManager mgr = new BBRUserManager();
			
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

		return mgr.list(pageNumber, pageSize, getOrderBy(sortingFields)).toJson();
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
		return mgr.list(pageNumber, pageSize, getOrderBy(sortingFields)).toJson();
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
	
    public static String getOrderBy(List<Hashtable<String, String>> sortingFields) {
    	String orderBy = "";
    	for (Hashtable<String, String> element : sortingFields) {
    		orderBy += element.get("field");
    		if (element.get("order").equals("ascending"))
    			orderBy += " asc, ";
    		else if (element.get("order").equals("descending"))
    			orderBy += " desc, ";
    	}
		return orderBy.substring(0, orderBy.length() - 2);
    }
    
	public String getSpecData(Long id) {
		BBRSpecialistManager mgr = new BBRSpecialistManager();
		String json = "";
		
		try {
			BBRSpecialist spec = mgr.findById(id);
			if (spec != null)
				json = spec.toJson();
			else
				json = BBRErrors.ERR_USER_NOTFOUND;
		} catch (Exception ex) {
			json = ex.getLocalizedMessage();
		}
		return json;
	}

	public String deleteSpec(Long id) {
		BBRSpecialistManager mgr = new BBRSpecialistManager();
		String respText = "";
		
		try {
			BBRSpecialist spec = mgr.findById(id);
			if (spec != null)
				mgr.delete(spec);
			else
				respText = BBRErrors.ERR_USER_NOTFOUND;
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
		}
		return respText;
	}

	public String updateSpec(Long id, String name, String position) {
		BBRSpecialistManager mgr = new BBRSpecialistManager();
		String respText = "";

		try {
			BBRSpecialist spec = mgr.findById(id);
			if (spec != null) {
				spec.setName(name);
				spec.setPosition(position);
				mgr.update(spec);
			}
			else
				respText = BBRErrors.ERR_USER_NOTFOUND;
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
		}
		
		return respText;
	}

	public String createSpec(String name, String position) {
		BBRSpecialistManager mgr = new BBRSpecialistManager();
		String respText = "";

		try {
			mgr.createAndStoreSpecialist(name, position);
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
		}
		
		return respText;
	}

	public String getSpecs(int pageNumber, int pageSize, List<Hashtable<String, String>> sortingFields) {
		BBRSpecialistManager mgr = new BBRSpecialistManager();
		return mgr.list(pageNumber, pageSize, getOrderBy(sortingFields)).toJson();
	}

}
