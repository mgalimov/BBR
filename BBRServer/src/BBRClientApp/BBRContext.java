package BBRClientApp;

import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import BBR.BBRErrors;
import BBRAcc.*;

public class BBRContext {
	public BBRAcc.BBRUser user = null;
	public BBRAcc.BBRShop shop = null;
	private String lastSignInError = ""; 
	
	public BBRContext() {
	}
	
	public static BBRContext getContext(HttpServletRequest request) {
		HttpSession session = request.getSession(true);		
		BBRContext app = (BBRContext)session.getAttribute("BBRContext");
		if (app == null) {
			app = new BBRContext();
			session.setAttribute("BBRContext", app);
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
	
	public boolean isPageAllowed(String page) {
		if (user == null)
			if (page.startsWith("general_signin.jsp")) return true;
		
		if (user.getRole() == BBRUser.BBRUserRole.ROLE_BBR_OWNER)
			if (page.startsWith("admin-")) return true;

		if (user.getRole() >= BBRUser.BBRUserRole.ROLE_SHOP_ADMIN)
			if (page.startsWith("management-")) return true;

		if (user.getRole() >= BBRUser.BBRUserRole.ROLE_VISITOR)
			if (page.startsWith("general-")) return true;

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

}
