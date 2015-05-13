import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRDataElement;
import BBR.BBRDataManager;
import BBR.BBRErrors;
import BBRAcc.BBRUser.BBRUserRole;
import BBRClientApp.BBRContext;

@SuppressWarnings("rawtypes")
public abstract class BBRBasicServlet<Cls extends BBRDataElement, Mgr extends BBRDataManager> extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int errorResponseCode = 700;
	protected Mgr manager;
       
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
			} else 
			if (operation.equals("badge")) {
				respText = getBadgeNumber(params, request, response);
			} else 
			if (operation.equals("cancel")) {
				respText = cancel(id, params, request, response);
			} else	{
				if (processOperation(operation, params, request, response) < 0) {
					respText = "Unknown operation";
					response.setStatus(errorResponseCode);
				}
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
			String startItem = params.get("start");
			String pageLength = params.get("length");
			Integer rowsPerPage = Integer.parseInt(pageLength);
			Integer pageNum = Integer.parseInt(startItem) / rowsPerPage;
			Hashtable<Integer, Hashtable<String, String>> sortingFields = params.getArray("order");
			Hashtable<Integer, Hashtable<String, String>> columns = params.getArray("columns");
			
			String drawIndex = params.get("draw");
			respText = getData(pageNum, rowsPerPage, columns, sortingFields, params, request, response);
			respText = "{\"draw\":" + drawIndex + "," + respText.substring(1);
		} catch (Exception ex) {
			respText = ex.getLocalizedMessage();
			response.setStatus(700);
		}
		
		response.setContentType("text/plain");  
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().write(respText); 
	}
	
	// Application methods
	
	protected String getData(int pageNumber, int pageSize, 
								Hashtable<Integer, Hashtable<String, String>> fields,
								Hashtable<Integer, Hashtable<String, String>> sortingFields,
								BBRParams params, HttpServletRequest request, 
								HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		String where = "";
		if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN)
			if (context.user.getPos() != null)
				where = manager.wherePos(context.user.getPos().getId());
		if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
			if (context.user.getShop() != null)
				where = manager.whereShop(context.user.getShop().getId());
		
		return manager.list(pageNumber, pageSize, where, BBRContext.getOrderBy(sortingFields, fields)).toJson();
	}

	protected String getReferenceData(String query, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		String where = "";
		if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN)
			if (context.user.getPos() != null)
				where = manager.wherePos(context.user.getPos().getId());
		if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
			if (context.user.getShop() != null)
				where = manager.whereShop(context.user.getShop().getId());
		
		return manager.list(query, manager.getTitleField(), where).toJson();
	}

	@SuppressWarnings("unchecked")
	protected String getRecordData(long id, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		Cls obj = (Cls)manager.findById(id);
		if (obj != null)
			return obj.toJson();
		else
			return BBRErrors.ERR_RECORD_NOTFOUND + ": " + manager.getClassTitle();
	}


	abstract String create(BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception;

	@SuppressWarnings("unchecked")
	protected String update(Cls obj, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		try {
			obj = beforeUpdate(obj, params, request, response);
			if (obj != null)
				manager.update(obj);
			return "";
		} catch (Exception ex) {
			return ex.getLocalizedMessage();
		}
	}

	protected Cls beforeUpdate(Cls obj, BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	protected String delete(Cls obj, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		try {
			obj = beforeDelete(obj, params, request, response);
			if (obj != null)
				manager.delete(obj);
			return "";
		} catch (Exception ex) {
			return ex.getLocalizedMessage();
		}
	}

	protected Cls beforeDelete(Cls obj, BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return obj;
	};
	
	protected int processOperation(String operation, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		return -1;
	};

	protected String getBadgeNumber(BBRParams params, HttpServletRequest request,
			HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		String where = "";
		if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN)
			if (context.user.getPos() != null)
				where = manager.wherePos(context.user.getPos().getId());
		if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
			if (context.user.getShop() != null)
				where = manager.whereShop(context.user.getShop().getId());
		
		return manager.count(where).toString();
	}

	protected String cancel(String id, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		return "";
	}
}
