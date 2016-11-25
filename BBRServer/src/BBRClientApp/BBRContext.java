package BBRClientApp;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Formatter;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import BBR.*;
import BBRAcc.*;
import BBRAcc.BBRUser.BBRUserRole;
import BBRCust.BBRVisit;

public class BBRContext {
	public BBRAcc.BBRUser user = null;
	public BBRVisit planningVisit = null;
	public Date filterStartDate = null;
	public Date filterEndDate = null;
	public BBRShop filterShop = null;
	public BBRPoS filterPoS = null;
	public String timeZone = "";
	
	private String lastSignInError = "";
	private BBRGPS location = null;
	private int lastVisitStep = 1;
	private Locale locale = null;
	private ResourceBundle resourceBundle;
	private Hashtable<String, Object> data = new Hashtable<String, Object>();
	
	
	public BBRContext() {
		setLocale("ru", "RU");
		timeZone = "UTC+3";
	}
	
	public static BBRContext getContext(HttpServletRequest request) {
		HttpSession session = request.getSession(true);		
		BBRContext app = (BBRContext)session.getAttribute("context");
		if (app == null) {
			app = new BBRContext();
			//app.setLocale("en", "US");
			session.setAttribute("context", app);
		}
		return app;
	}

	public String SignIn(String email, String password, String timeZone) {
		BBRUserManager mgr = new BBRUserManager();
		
		if (email == null || email.equals("")) {
			lastSignInError = gs(BBRErrors.ERR_EMPTY_EMAIL);
		} else {
			BBRUser candidate = mgr.findUserByEmail(email);
			if (candidate == null) {
				lastSignInError = gs(BBRErrors.ERR_USER_NOTFOUND);
			} else {
				if (candidate.comparePasswordTo(BBRUserManager.encodePassword(password))) {
					user = candidate;
					setLocale(user.getLanguage());
					this.timeZone = timeZone;
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
		String timeZone = "";
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
			for (int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];
				if (c.getName().equals("email"))
					email = c.getValue();
				if (c.getName().equals("pwdhash"))
					pwdhash = c.getValue();
				if (c.getName().equals("timezone"))
					timeZone = c.getValue();
			}
		
		if (email != "" && !pwdhash.equals("")) {
			BBRUser candidate = mgr.findUserByEmail(email);
			if (candidate == null) {
				lastSignInError = gs(BBRErrors.ERR_USER_NOTFOUND);
			} else {
				if (candidate.comparePasswordTo(pwdhash)) {
					user = candidate;
					setLocale(user.getLanguage());
					this.timeZone = timeZone;
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
			setLocale(user.getLanguage());
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
			return "system-dashboard.jsp";

		if (user.getRole() == BBRUser.BBRUserRole.ROLE_SHOP_ADMIN)
			return "admin-dashboard.jsp";

		if (user.getRole() == BBRUser.BBRUserRole.ROLE_POS_ADMIN)
			return "manager-dashboard.jsp";

		if (user.getApproved())
			return getUserProfilePage();
		else
			return getUserProfilePage();
		
	}

	public String getSignInPage() {
		return "general-signin.jsp";
	}
	
	public String getUserProfilePage() {
		return "general-user-profile.jsp";
	}
	
	public boolean isPageAllowed(String page) {
		if (page.startsWith("/"))
			page.substring(1, page.length());
		
		if (user == null)
			if (page.startsWith("general-")) return true;
			else return false;
		
		if (user.getRole() == BBRUser.BBRUserRole.ROLE_BBR_OWNER)
			if (page.startsWith("system")) return true;
		
		if (user.getRole() >= BBRUser.BBRUserRole.ROLE_SHOP_ADMIN)
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
		Locale alocale = new Locale(language, country);
		ResourceBundle aresourceBundle = ResourceBundle.getBundle("bbr", alocale, new UTF8Control());
		ResourceBundle.clearCache();
		locale = alocale;
		resourceBundle = aresourceBundle;
	}

	public void setLocale(String lang_country) {
		if (lang_country == null)
			return;
		String lang = lang_country.split("_")[0];
		String country = lang_country.split("_")[1];
		if (lang != null && country != null)
			setLocale(lang, country);
	}

	public Locale getLocale() {
		return locale;
	}

	public String getLocaleString() {
		return locale.getLanguage()+"_"+locale.getCountry();
	}

	public String gs(String msg, Object... args) {
		String res = gs(msg);
		
		@SuppressWarnings("resource")
		Formatter fmt = new Formatter(locale);
		return fmt.format(res, args).out().toString();
	}
	
	public String gs(String msg) {
		if (msg == null) return "";
		if (msg.equals("")) return "";
		
		String res;
		try {
			res = resourceBundle.getString(msg);
		} catch (Exception ex) {
			res = "[*" + msg + "*]";
		}
		
		return res;
	}
	
	public String addJsonField(String json, String field, String value) {
		return json.substring(0, json.length() - 1) + ",\"" + field + "\":\"" + value + "\"}";
	}

	public Object set(String param, Object value) {
		if (value == null) {
			data.remove(param);
			return null;
		}
		return data.put(param, value);
	}
	
	public Object get(String param) {
		return data.get(param);
	}
	
	public Object clear(String param) {
		return data.remove(param);
	}
	
	public String getStringDate(Date date) {
		return getStringDate(date, BBRUtil.fullDateFormat);
	}
	
	public String getStringDate(Date date, String format) {
		if (date == null) date = new Date();
		
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	public String getFilterStartDate() {
		return getStringDate(filterStartDate);
	}
	
	public String getFilterEndDate() {
		return getStringDate(filterEndDate);
	}
	
	public String getFilterShopPosId() {
		if (filterShop != null)
			return "s"+filterShop.getId().toString();
		else
			if (filterPoS != null)
				return filterPoS.getId().toString();
			else 
				return "";
	}
	
	public String getDurationString (float duration) {
		long hour = Math.round(Math.floor(duration));
		long minutes = Math.round((duration - hour) * 60);
		
		String hrs;
		String mins = "";
		
		if (minutes == 1)
			mins = minutes + " " + gs("SYS_MINUTE");
		else
			if (minutes > 0)
				mins = minutes + " " + gs("SYS_MINUTES");
			else
				mins = "";
		
		if (hour == 1)
			hrs = hour + " " + gs("SYS_HOUR");
		else
			if (hour > 0)
				hrs = hour + " " + gs("SYS_HOURS");
			else
				hrs = "";
				
		if (hrs.isEmpty() && mins.isEmpty())
			return "--";
		else
			if (hrs.isEmpty())
				return mins;
			else
				if (mins.isEmpty())
					return hrs;
				else
					return hrs + " " + mins;
	}

	public static String getDurationStringL (float duration) {
		long hour = Math.round(Math.floor(duration));
		long minutes = Math.round((duration - hour) * 60);
		
		String hrs;
		String mins = "";
		
		if (minutes == 1)
			mins = minutes + " минута";
		else
			if (minutes > 0)
				mins = minutes + " минут";
			else
				mins = "";
		
		if (hour == 1)
			hrs = hour + " час";
		else
			if (hour >= 2 && hour <= 4)
				hrs = hour + " часа";
			else
				if (hour >= 5)
					hrs = hour + " часов";
				else 
					hrs = "";
				
		if (hrs.isEmpty() && mins.isEmpty())
			return "--";
		else
			if (hrs.isEmpty())
				return mins;
			else
				if (mins.isEmpty())
					return hrs;
				else
					return hrs + " " + mins;
	}

	public String getNowString() {
		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat);
		return df.format(BBRUtil.now(timeZone));
	}

	public String getTimeZone() {
		return timeZone;
	}
}
