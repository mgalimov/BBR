import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import BBR.BBRDataElement;
import BBR.BBRDataManager;
import BBR.BBRErrors;

@SuppressWarnings("rawtypes")
public abstract class BBRBasicServlet<Cls extends BBRDataElement, Mgr extends BBRDataManager> extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int errorResponseCode = 700;
	private Mgr manager;
       
    public BBRBasicServlet() {
        super();
    }
    
    public BBRBasicServlet(Class<Mgr> managerClass) throws InstantiationException, IllegalAccessException {
        super();
    	manager = managerClass.newInstance();
    }
    
    // Performing operations
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String respText = "";
		try {
			BBRParams params = new BBRParams(request.getQueryString());
			String id = params.get("id");
			String operation = params.get("operation");
			
			if (operation.equals("getdata")) {
				respText = getRecordData(Long.parseLong(id), params, request, response);
			} else
			if (operation.equals("delete")) {
				try {
					Cls obj = (Cls)manager.findById(Long.parseLong(id));
					if (obj != null) 
						respText = delete(obj, params, request, response);
					else
						respText = BBRErrors.ERR_RECORD_NOTFOUND + ": " + manager.getClassTitle();
				} catch (Exception ex) {
					respText = ex.getLocalizedMessage();
				}
			} else
			if (operation.equals("update")) {
				try {
					Cls obj = (Cls)manager.findById(Long.parseLong(id));
					if (obj != null) 
						respText = update(obj, params, request, response);
					else
						respText = BBRErrors.ERR_RECORD_NOTFOUND + ": " + manager.getClassTitle();
				} catch (Exception ex) {
					respText = ex.getLocalizedMessage();
				}
			} else
			if (operation.equals("create")) {
				respText = create(params, request, response);
			} else
			if (operation.equals("reference")) {
				String q = params.get("q");
				respText = getReferenceData(q, params, request, response);
			} else {
				respText = "Unknown operation";
				response.setStatus(errorResponseCode);
			}
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
			response.setStatus(errorResponseCode);
		}
		response.setContentType("text/plain");  
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().write(respText); 

	}

	// Getting data
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String respText = "";
		try {
			BBRParams params = new BBRParams(request.getReader());
			String pageNum = params.get("page_num");
			String rowsPerPage = params.get("rows_per_page");
			Hashtable<Integer, Hashtable<String, String>> sortingFields = params.getArray("sorting");
			
			respText = getData(Integer.parseInt(pageNum), Integer.parseInt(rowsPerPage), sortingFields);
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
			response.setStatus(700);
		}
		
		response.setContentType("text/plain");  
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().write(respText); 
	}
	
	// Application methods
	
	protected String getData(int parseInt, int parseInt2,
			Hashtable<Integer, Hashtable<String, String>> sortingFields) {
		return null;
	}

	protected String getReferenceData(String query, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		return manager.list(query, manager.getTitleField()).toJson();
	}

	@SuppressWarnings("unchecked")
	protected String getRecordData(long id, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		Cls obj = (Cls)manager.findById(id);
		if (obj != null)
			return obj.toJson();
		else
			return BBRErrors.ERR_RECORD_NOTFOUND + ": " + manager.getClassTitle();
	}


	abstract String create(BBRParams params, HttpServletRequest request, HttpServletResponse response);

	@SuppressWarnings("unchecked")
	protected String update(Cls obj, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		try {
			beforeUpdate(obj, params, request, response);
			manager.update(obj);
			return "";
		} catch (Exception ex) {
			return ex.getLocalizedMessage();
		}
	}

	protected void beforeUpdate(Cls obj, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		return;
	}
	
	@SuppressWarnings("unchecked")
	protected String delete(Cls obj, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		try {
			beforeDelete(obj, params, request, response);
			manager.delete(obj);
			return "";
		} catch (Exception ex) {
			return ex.getLocalizedMessage();
		}
	}

	protected void beforeDelete(Cls obj, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		return;
	};

}
