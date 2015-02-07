package BBRClientApp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import BBR.*;

public class BBRApplication {
	public BBRUser user = null;
	
	public BBRApplication() {
	}
	
	public static BBRApplication GetApp(HttpServletRequest request) {
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
					respText = BBRErrors.MSG_USER_SIGNED_IN;
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

	public String updateUser(Long id, String firstName, String lastName, boolean approved) {
		BBRUserManager mgr = new BBRUserManager();
		String respText = "";

		try {
			BBRUser user = mgr.findUserById(id);
			if (user != null) {
				user.setFirstName(firstName);
				user.setLastName(lastName);
				user.setApproved(approved);
				mgr.updateUser(user);
			}
			else
				respText = BBRErrors.ERR_USER_NOTFOUND;
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
		}
		
		return respText;
	}

	public String createUser(String email, String firstName, String lastName) {
		BBRUserManager mgr = new BBRUserManager();
		String respText = "";

		try {
			mgr.createAndStoreUser(email, firstName, lastName, BBRUserManager.generatePassword());
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
		}
		
		return respText;
	}

}
