package BBRClientApp;

import java.util.Date;
import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import BBR.BBRErrors;
import BBRAcc.BBRPoS;
import BBRCust.BBRSpecialist;
import BBRCust.BBRSpecialistManager;
import BBRCust.BBRVisit;
import BBRCust.BBRVisitManager;
import BBRCust.BBRVisit.BBRVisitStatus;

public class BBRManagementApplication {
	public BBRAcc.BBRShop shop = null;
	private String lastVisitScheduled = null;
	
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

	public String getSpecs(int pageNumber, int pageSize, Hashtable<Integer, Hashtable<String, String>> sortingFields) {
		BBRSpecialistManager mgr = new BBRSpecialistManager();
		return mgr.list(pageNumber, pageSize, BBRContext.getOrderBy(sortingFields)).toJson();
	}

	public String getSpecs(String query) {
		BBRSpecialistManager mgr = new BBRSpecialistManager();
		return mgr.list(query, "name").toJson();
	}

	public String getVisitData(Long id) {
		BBRVisitManager mgr = new BBRVisitManager();
		String json = "";
		
		try {
			BBRVisit visit = mgr.findById(id);
			if (visit != null)
				json = visit.toJson();
			else
				json = BBRErrors.ERR_VISIT_NOTFOUND;
		} catch (Exception ex) {
			json = ex.getLocalizedMessage();
		}
		return json;
	}

	public String cancelVisit(Long id) {
		BBRVisitManager mgr = new BBRVisitManager();
		String respText = "";
		
		try {
			BBRVisit visit = mgr.findById(id);
			if (visit != null) {
				visit.setStatus(BBRVisitStatus.VISSTATUS_CANCELLED);
				mgr.update(visit);
			}
			else
				respText = BBRErrors.ERR_VISIT_NOTFOUND;
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
		}
		return respText;
	}

	public String approveVisit(Long id) {
		BBRVisitManager mgr = new BBRVisitManager();
		String respText = "";
		
		try {
			BBRVisit visit = mgr.findById(id);
			if (visit != null) {
				visit.setStatus(BBRVisitStatus.VISSTATUS_APPROVED);
				mgr.update(visit);
			}
			else
				respText = BBRErrors.ERR_VISIT_NOTFOUND;
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
		}
		return respText;
	}

	public String createVisit(BBRPoS pos, Date timeScheduled, String procedure, String userName, String userContacts, Long userId) {
		BBRVisitManager mgr = new BBRVisitManager();
		String respText = "";

		try {
			lastVisitScheduled = mgr.createAndStoreVisit(pos.getId(), pos.getTitle(), timeScheduled, procedure, userName, userContacts, userId);
		} catch (Exception ex) {
			lastVisitScheduled = null;
			respText = ex.getLocalizedMessage();
		}
		
		return respText;
	}

	public String getVisits(Long userId, int pageNumber, int pageSize, Hashtable<Integer, Hashtable<String, String>> sortingFields) {
		BBRVisitManager mgr = new BBRVisitManager();
		return mgr.list(userId, pageNumber, pageSize, BBRContext.getOrderBy(sortingFields)).toJson();
	}

	public String getLastVisitScheduled() {
		String lastVisit = lastVisitScheduled;
		lastVisitScheduled = null;
		return lastVisit;
	}
}
