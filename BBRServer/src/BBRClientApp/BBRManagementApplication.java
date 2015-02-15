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

public class BBRManagementApplication {
	public BBRAcc.BBRShop shop = null;
	
	public BBRManagementApplication() {
	}
	
	public static BBRManagementApplication getApp(HttpServletRequest request) {
		HttpSession session = request.getSession(true);		
		BBRManagementApplication app = (BBRManagementApplication)session.getAttribute("BBRManagementApplication");
		if (app == null) {
			app = new BBRManagementApplication();
			session.setAttribute("BBRManagementApplication", app);
		}
		return app;
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
		return mgr.list(pageNumber, pageSize, BBRAdminApplication.getOrderBy(sortingFields)).toJson();
	}

}
