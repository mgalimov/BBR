package BBRClientApp;

import java.util.Collections;
import java.util.Formatter;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import BBR.BBRErrors;
import BBR.BBRGPS;
import BBRAcc.*;
import BBRAcc.BBRUser.BBRUserRole;
import BBRCust.BBRVisit;

public class BBRContext {
	public BBRAcc.BBRUser user = null;
	private String lastSignInError = "";
	private BBRGPS location = null;
	private int lastVisitStep = 1;
	public BBRVisit planningVisit = null;
	public boolean viewAllTasks = false;
	private Locale locale = null;
	private ResourceBundle resourceBundle;
	
	public BBRContext() {
	}
	
	public static BBRContext getContext(HttpServletRequest request) {
		HttpSession session = request.getSession(true);		
		BBRContext app = (BBRContext)session.getAttribute("BBRContext");
		if (app == null) {
			app = new BBRContext();
			app.setLocale("ru", "RU");
			session.setAttribute("BBRContext", app);
		}
		return app;
	}

	public String SignIn(String email, String password) {
		BBRUserManager mgr = new BBRUserManager();
		
		if (email == null || email == "") {
			lastSignInError = gs(BBRErrors.ERR_EMPTY_EMAIL);
		} else {
			BBRUser candidate = mgr.findUserByEmail(email);
			if (candidate == null) {
				lastSignInError = gs(BBRErrors.ERR_USER_NOTFOUND);
			} else {
				if (candidate.comparePasswordTo(BBRUserManager.encodePassword(password))) {
					user = candidate;
					lastSignInError = "";
				} else {
					lastSignInError = gs(BBRErrors.ERR_INCORRECT_PASSWORD);
				}
			}
		}
		
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
				lastSignInError = gs(BBRErrors.ERR_USER_NOTFOUND);
			} else {
				if (candidate.comparePasswordTo(pwdhash)) {
					user = candidate;
					lastSignInError = "";
				} else {
					lastSignInError = gs(BBRErrors.ERR_INCORRECT_PASSWORD);
				}
			}
			
		}
				
		return lastSignInError;
	}

	public String SignOut(HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		response.addCookie(new Cookie("email", ""));
		response.addCookie(new Cookie("pwdhash", ""));
		user = null;
		return context.gs(BBRErrors.MSG_USER_SIGNED_OUT);
	}
	
	public String SignUp(String email, String firstName, String lastName, String password, String passwordCopy) {
		BBRUserManager mgr = new BBRUserManager();
			
		if (!password.equals(passwordCopy)) {
			lastSignInError = gs(BBRErrors.ERR_INCORRECT_PASSWORD);
			return lastSignInError;
		}
			
		try {
			BBRUser candidate = mgr.createAndStoreUser(email, firstName, lastName, password, BBRUserRole.ROLE_VISITOR, null, null);
			user = candidate;
			lastSignInError = "";
		} catch (Exception ex) {
			lastSignInError = ex.getLocalizedMessage();
		}
		return lastSignInError;
	}

	public String getLastSignInError() {
		return lastSignInError;
	}

	public String getWelcomePage() {
		if (user == null)
			return getSignInPage();

		if (user.getRole() == BBRUser.BBRUserRole.ROLE_BBR_OWNER)
			return "admin-index.jsp";
		
		if (user.getApproved())
			return "general-user-profile.jsp";
		else
			return "general-user-profile.jsp";
		
	}

	public String getSignInPage() {
		return "general-signin.jsp";
	}
	
	public boolean isPageAllowed(String page) {
		if (page.startsWith("/"))
			page.substring(1, page.length());
		
		if (user == null)
			if (page.startsWith("general-")) return true;
			else return false;
		
		if (user.getRole() == BBRUser.BBRUserRole.ROLE_BBR_OWNER)
			if (page.startsWith("admin")) return true;

		if (user.getRole() >= BBRUser.BBRUserRole.ROLE_POS_ADMIN) {
			if (page.startsWith("manager")) return true;
		}

		if (user.getRole() >= BBRUser.BBRUserRole.ROLE_VISITOR)
			if (page.startsWith("general")) return true;

		return false;
	}
	
    public static String getOrderBy(Hashtable<Integer, Hashtable<String, String>> sortingFields, 
    								Hashtable<Integer, Hashtable<String, String>> columns) {
    	String orderBy = "";
    	Hashtable<String, String> element;
    	
    	List<Integer> keys = Collections.list(sortingFields.keys());
    	Collections.sort(keys);
    	
    	for (Integer key : keys) {
    		element = sortingFields.get(key);
    		Integer orderByColumnIndex = Integer.parseInt(element.get("column"));
    		String orderBylocal = columns.get(orderByColumnIndex).get("data");
    		String direction = element.get("dir");
    		if (direction != null)
    			orderBylocal += " " + direction;
    		orderBy += orderBylocal + ", ";
    	}
    	if (orderBy.length() > 2)
    		orderBy = orderBy.substring(0, orderBy.length() - 2);
		return orderBy;
    }

	public void setLocation(BBRGPS gps) {
		location = gps;
	}

	public BBRGPS getLocation() {
		return location;
	}

	public int getLastVisitStep() {
		return lastVisitStep;
	}

	public void setLastVisitStep(int lastVisitStep) {
		this.lastVisitStep = lastVisitStep;
	}

	public static String getSelectFields(Hashtable<Integer, Hashtable<String, String>> fields) {
	       String selectFields = "";
	       List<Integer> keys = Collections.list(fields.keys());
	   	   Collections.sort(keys);
	   	   Hashtable<String, String> element;
	     
	   	   for (Integer key: keys) {
	    	   element = fields.get(key);
	    	   selectFields += element.get("data") + ", ";
	       }
	       if (selectFields.length() > 0)
	    	   selectFields = selectFields.substring(0, selectFields.length() - 2);
	       
	       return selectFields;
	}
	
	public void setLocale(String language, String country) {
		locale = new Locale(language, country);
		resourceBundle = ResourceBundle.getBundle("bbr", locale, new BBR.UTF8Control());
	}

	public Locale getLocale() {
		return locale;
	}
	
	public String gs(String msg, Object... args) {
		String res = resourceBundle.getString(msg);
		
		@SuppressWarnings("resource")
		Formatter fmt = new Formatter(locale);
		return fmt.format(res, args).out().toString();
	}
}
