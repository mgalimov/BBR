package BBRClientApp;

import java.util.Date;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import BBR.BBRErrors;
import BBRAcc.BBRPoS;
import BBRAcc.BBRUser;
import BBRCust.BBRProcedure;
import BBRCust.BBRProcedureManager;
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

	public String updateSpec(Long id, String name, String position, BBRUser user, BBRPoS pos) {
		BBRSpecialistManager mgr = new BBRSpecialistManager();
		String respText = "";

		try {
			BBRSpecialist spec = mgr.findById(id);
			if (spec != null) {
				spec.setName(name);
				spec.setPosition(position);
				spec.setPos(pos);
				spec.setUser(user);
				mgr.update(spec);
			}
			else
				respText = BBRErrors.ERR_USER_NOTFOUND;
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
		}
		
		return respText;
	}

	public String createSpec(String name, String position, BBRUser user, BBRPoS pos) {
		BBRSpecialistManager mgr = new BBRSpecialistManager();
		String respText = "";

		try {
			mgr.createAndStoreSpecialist(name, position, user, pos);
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

	public String createVisit(BBRPoS pos, BBRUser user, Date timeScheduled, BBRProcedure procedure, String userName, String userContacts) {
		BBRVisitManager mgr = new BBRVisitManager();
		String respText = "";

		try {
			lastVisitScheduled = mgr.createAndStoreVisit(pos, user, timeScheduled, procedure, userName, userContacts);
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
	
	public String getProcedureData(Long id) {
		BBRProcedureManager mgr = new BBRProcedureManager();
		String json = "";
		
		try {
			BBRProcedure proc = mgr.findById(id);
			if (proc != null)
				json = proc.toJson();
			else
				json = BBRErrors.ERR_PROC_NOTFOUND;
		} catch (Exception ex) {
			json = ex.getLocalizedMessage();
		}
		return json;
	}

	public String deleteProcedure(Long id) {
		BBRProcedureManager mgr = new BBRProcedureManager();
		String respText = "";
		
		try {
			BBRProcedure proc = mgr.findById(id);
			if (proc != null)
				mgr.delete(proc);
			else
				respText = BBRErrors.ERR_PROC_NOTFOUND;
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
		}
		return respText;
	}

	public String updateProcedure(Long id, String title, BBRPoS pos, float length, float price, String currency, int status) {
		BBRProcedureManager mgr = new BBRProcedureManager();
		String respText = "";

		try {
			BBRProcedure proc = mgr.findById(id);
			if (proc != null) {
		        proc.setTitle(title);
		        proc.setPos(pos);
		        proc.setLength(length);
		        proc.setPrice(price);
		        proc.setCurrency(currency);
		        proc.setStatus(status);
				mgr.update(proc);
			}
			else
				respText = BBRErrors.ERR_USER_NOTFOUND;
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
		}
		
		return respText;
	}

	public String createProcedure(String title, BBRPoS pos, float length, float price, String currency, int status) {
		BBRProcedureManager mgr = new BBRProcedureManager();
		String respText = "";

		try {
			mgr.createAndStoreProcedure(title, pos, length, price, currency, status);
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
		}
		
		return respText;
	}

	public String getProcedures(int pageNumber, int pageSize, Hashtable<Integer, Hashtable<String, String>> sortingFields) {
		BBRProcedureManager mgr = new BBRProcedureManager();
		return mgr.list(pageNumber, pageSize, BBRContext.getOrderBy(sortingFields)).toJson();
	}

	public String getProcedures(String query) {
		BBRProcedureManager mgr = new BBRProcedureManager();
		return mgr.list(query, "title").toJson();
	}
}
