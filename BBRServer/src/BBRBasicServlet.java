import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.hibernate.JDBCException;

import BBR.BBRDataElement;
import BBR.BBRDataManager;
import BBR.BBRDataSet;
import BBR.BBRErrors;
import BBR.BBRUtil;
import BBRAcc.BBRUser.BBRUserRole;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;

@SuppressWarnings("rawtypes")
@MultipartConfig   
public abstract class BBRBasicServlet<Cls extends BBRDataElement, Mgr extends BBRDataManager> extends HttpServlet {
	private static final long serialVersionUID = 1L;
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
		BBRContext context = BBRContext.getContext(request); 

		String respText = "";
		try {
			BBRParams params = new BBRParams(request.getQueryString());
			String id = params.get("id");
			String operation = params.get("operation");
			if (operation == null)
				operation = "";
			
			if (operation.equals("")) {
				doPost(request, response);
			} else
			if (operation.equals("getdata")) {
				respText = getRecordData(id, params, request, response);
			} else
			if (operation.equals("delete")) {
				Cls obj = (Cls)manager.findById(Long.parseLong(id));
				if (obj != null) 
					respText = delete(obj, params, request, response);
				else
					respText = context.gs(BBRErrors.ERR_RECORD_NOTFOUND, manager.getClassTitle());
			} else
			if (operation.equals("update")) {
				Cls obj = (Cls)manager.findById(Long.parseLong(id));
				if (obj != null) 
					respText = update(obj, params, request, response);
				else
					respText = context.gs(BBRErrors.ERR_RECORD_NOTFOUND, manager.getClassTitle());
			} else
			if (operation.equals("create")) {
				respText = create(params, request, response);
			} else
			if (operation.equals("reference")) {
				String q = params.get("q");
				respText = getReferenceData(q, params, request, response);
				if (respText.isEmpty()) {
					BBRDataSet ds = new BBRDataSet<BBRDataElement>(null);
					respText = ds.toJson();
				}
			} else 
			if (operation.equals("badge")) {
				respText = getBadgeNumber(params, request, response);
			} else 
			if (operation.equals("cancel")) {
				respText = cancel(id, params, request, response);
			} else
			if (operation.equals("pic")) {
				respText = getPicture(id, params, request, response);
				return;
			} else	{
				respText = processOperation(operation, params, request, response);
			}
		} catch (JDBCException ex) {
			BBRUtil.log.error(ex.getMessage());
			BBRUtil.log.error(ex.getSQL());
			BBRUtil.log.error(ex.getStackTrace());
			respText = ex.getMessage();
			if (context != null)
				respText = context.gs(respText);
			response.setStatus(BBRUtil.errorResponseCode);
		} catch (Exception ex) {
			BBRUtil.log.error(ex.getMessage());
			BBRUtil.log.error(ex.getStackTrace());
			respText = ex.getMessage();
			if (context != null)
				respText = context.gs(respText);
			response.setStatus(BBRUtil.errorResponseCode);
		}
		response.setContentType("text/plain");  
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().write(respText); 
	}

    private String getPartAttrName(Part part, String attr) {
        String contentDisp = part.getHeader("content-disposition");
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith(attr)) {
                return token.substring(token.indexOf("=") + 2, token.length()-1);
            }
        }
        return "";
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getContentType() != null && 
			request.getContentType().toLowerCase().indexOf("multipart/form-data") > -1 ) {
			
			InputStream in = null;
			FileOutputStream out = null;
			String name = "";
			String id = "";

			for (Part filePart : request.getParts()) {
				try {
					if (filePart != null) {
						String fname = getPartAttrName(filePart, "name");
						String ext = "";
						id = fname.split("#")[0];
						name = fname.split("#")[1];
						name = name.substring(0, name.length() - 5);
	
						fname = getPartAttrName(filePart, "filename");
						if (!fname.isEmpty()) {
							int i = fname.lastIndexOf('.');
							ext = fname.substring(i); 
						
							fname =  manager.getClassTitle() + "_" + id + ext;
						    in = filePart.getInputStream();
						} else {
							in = null;
						}
						
						Long oId = Long.parseLong(id);
						manager.setBlobFieldValue(oId, name, in);
						manager.setStringFieldValue(oId, name + "Ext", ext);
							
						//BBRUtil.log.info("Successfully saved image: " + fname);
					}
				} catch (Exception ex) {
					BBRUtil.log.error("Cannot read / write image: " + manager.getClassTitle() + ", " + id + ", " + name);
				} finally {
						if (out != null) {
				            out.close();
				        }
				        if (in != null) {
				            in.close();
				        }
				}
			}			
		} else {
			BBRParams params = new BBRParams(request.getReader());
			String operation = params.get("operation");
			String respText = "";
			BBRContext context = BBRContext.getContext(request);
	
			if (operation == null)
				operation = "";
			if (operation.equals("getGrid") || operation.isEmpty()) {
				try {
					String startItem = params.get("start");
					String pageLength = params.get("length");
					Integer rowsPerPage = Integer.parseInt(pageLength);
					Integer pageNum = Integer.parseInt(startItem) / rowsPerPage;
					Hashtable<Integer, Hashtable<String, String>> sortingFields = params.getArray("order");
					Hashtable<Integer, Hashtable<String, String>> columns = params.getArray("columns");
					
					String drawIndex = params.get("draw");
					respText = getData(pageNum, rowsPerPage, columns, sortingFields, params, request, response);
					if (respText == null || respText.isEmpty()) {
						BBRDataSet ds = new BBRDataSet<BBRDataElement>(null);
						respText = ds.toJson();
					}
					respText = "{\"draw\":" + drawIndex + "," + respText.substring(1);
				} catch (JDBCException ex) {
					BBRUtil.log.error(ex.getMessage());
					BBRUtil.log.error(ex.getSQL());
					BBRUtil.log.error(ex.getStackTrace());
					respText = context.gs(ex.getMessage());
					response.setStatus(700);
				} catch (Exception ex) {
					BBRUtil.log.error(ex.getMessage());
					BBRUtil.log.error(ex.getStackTrace());
					respText = context.gs(ex.getMessage());
					response.setStatus(700);
				}
				
				response.setContentType("text/plain");  
				response.setCharacterEncoding("UTF-8"); 
				response.getWriter().write(respText);
			}
		}
	}
	
	// Application methods
	
	protected String getData(int pageNumber, int pageSize, 
								Hashtable<Integer, Hashtable<String, String>> fields,
								Hashtable<Integer, Hashtable<String, String>> sortingFields,
								BBRParams params, HttpServletRequest request, 
								HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		String where = "";
		if (context.user != null) {
			if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN || 
			    context.user.getRole() == BBRUserRole.ROLE_POS_SPECIALIST)
				if (context.user.getPos() != null)
					where = manager.wherePos(context.user.getPos().getId());
			if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
				if (context.user.getShop() != null)
					if (context.filterPoS != null)
						where = manager.wherePos(context.filterPoS.getId());
					else
						where = manager.whereShop(context.user.getShop().getId());
			if (context.user.getRole() == BBRUserRole.ROLE_BBR_OWNER)
				if (context.filterPoS != null)
					where = manager.wherePos(context.filterPoS.getId());
				else
					if (context.filterShop != null)
						where = manager.whereShop(context.filterShop.getId());
		}
		
		return manager.list(pageNumber, pageSize, where, BBRContext.getOrderBy(sortingFields, fields)).toJson();
	}

	protected String getReferenceData(String query, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		String where = "";
		if (context.user != null) {
			if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN)
				if (context.user.getPos() != null)
					where = manager.wherePos(context.user.getPos().getId());
			if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
				if (context.user.getShop() != null)
					where = manager.whereShop(context.user.getShop().getId());
		}
		return manager.list(query, manager.getTitleField(), where).toJson();
	}

	@SuppressWarnings("unchecked")
	protected String getRecordData(String id, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		Cls obj = (Cls)manager.findById(Long.parseLong(id));
		if (obj != null)
			return obj.toJson();
		else
			return context.gs(BBRErrors.ERR_RECORD_NOTFOUND, manager.getClassTitle());
	}


	protected String create(BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return null;
	}

	@SuppressWarnings("unchecked")
	protected String update(Cls obj, BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		obj = beforeUpdate(obj, params, request, response);
		if (obj != null)
			manager.update(obj);
		return "";
	}

	protected Cls beforeUpdate(Cls obj, BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	protected String delete(Cls obj, BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			obj = beforeDelete(obj, params, request, response);
			if (obj != null)
				manager.delete(obj);
		} catch (Exception ex) {
			throw new Exception(BBRErrors.ERR_CANT_DELETE_RECORD);
		}
		return "";
	}

	protected Cls beforeDelete(Cls obj, BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return obj;
	};
	
	protected String processOperation(String operation, BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return "";
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

	protected String getPicture(String id, BBRParams params, HttpServletRequest request,
			HttpServletResponse response) {
		ServletOutputStream out = null;
		
		try {
			if (!checkRightsForPic(id, params, request, response))
				throw new Exception(BBRErrors.ERR_INSUFFICIENT_RIGHTS);
			String fieldName = params.get("fld");
			Long oId = Long.parseLong(id);
			out = response.getOutputStream();
			manager.getBlobFieldValue(oId, fieldName, out);
			String ext = manager.getStringFieldValue(oId, fieldName + "Ext");
			if (ext == null)
				response.setContentType("image");
			else
				if (ext.startsWith(".jp"))
					response.setContentType("image/jpeg");
				else
					response.setContentType("image/" + ext.substring(1));
		} catch (Exception ex) {
			BBRUtil.log.error(ex.getMessage());
		} finally {
			try {
			    if (out != null)
			    	out.close();
			} catch (Exception ex1) {
				
			}
		}
		    
		return "";
	}

	protected boolean checkRightsForPic(String id, BBRParams params,
			HttpServletRequest request, HttpServletResponse response) {
		return true;
	}
}
