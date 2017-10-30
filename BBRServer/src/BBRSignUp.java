import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRErrors;
import BBR.BBRGPS;
import BBR.BBRMailer;
import BBR.BBRUtil;
import BBRAcc.BBRPoS;
import BBRAcc.BBRPoSManager;
import BBRAcc.BBRShop;
import BBRAcc.BBRShop.BBRShopStatus;
import BBRAcc.BBRPoS.BBRPoSStatus;
import BBRAcc.BBRShopManager;
import BBRAcc.BBRUser;
import BBRAcc.BBRUser.BBRUserRole;
import BBRAcc.BBRUserManager;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;


@WebServlet("/BBRSignUp")
public class BBRSignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public BBRSignUp() {
        super();
    }
 
	protected String checkParams(BBRContext context, String email, String name, String lastName, String password, String passwordRepeat) {
		String result = ""; 
		if (!password.equals(passwordRepeat)) {
			result += "<br/>" + context.gs(BBRErrors.ERR_PASSWORDS_DONT_MATCH); 
		}
		if (email.isEmpty()) 
			result += "<br/>" + context.gs(BBRErrors.ERR_EMAIL_MUST_BE_SPECIFIED);
		else {
			BBRUserManager mgr = new BBRUserManager();
	        if (mgr.findUserByEmail(email) != null) {
	        	result += "<br/>" + context.gs(BBRErrors.ERR_DUPLICATE_EMAIL);
	        }
		}
		if (name.trim().isEmpty() && lastName.trim().isEmpty()) 
			result += "<br/>" + context.gs(BBRErrors.ERR_NAME_MUST_BE_SPECIFIED);
		return result;
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BBRContext context = BBRContext.getContext(request);
		BBRParams params = new BBRParams(request.getQueryString());
		String operation = params.get("operation");
		String result = "";
		
		if (operation.equals("test")) {
			String email = params.get("email");
			String name = params.get("name");
			String lastName = params.get("lastName");
			String password = params.get("password");
			String passwordRepeat = params.get("passwordRepeat");
			result = checkParams(context, email, name, lastName, password, passwordRepeat);
		} else
		if (operation.equals("create")) {
			String email = params.get("email");
			String name = params.get("name");
			String lastName = params.get("lastName");
			String password = params.get("password");
			String passwordRepeat = params.get("passwordRepeat");
			String shop = params.get("shop");
			String country = params.get("country");
			String currency = params.get("currency");
			String urlID = params.get("urlID");
			String pos = params.get("pos");
			String city = params.get("city");
			String lat = params.get("lat");
			String lon = params.get("lon");
			String tz = params.get("tz");
				
			Double flat, flon;
			
			BBRShopManager smgr = new BBRShopManager();
			BBRPoSManager pmgr = new BBRPoSManager();
			BBRUserManager umgr = new BBRUserManager();

			result = checkParams(context, email, name, lastName, password, passwordRepeat);

			if (shop.isEmpty() || 
				pos.isEmpty() || 
				country.isEmpty() || 
				currency.isEmpty() || 
				tz.isEmpty() || 
				city.isEmpty() || 
				urlID.isEmpty()) {
				result += "<br/>" + context.gs(BBRErrors.ERR_ALL_FIELDS_MUST_BE_FILLED);
			} else
				if (umgr.findUserByEmail(email) != null) {
		        	result += "<br/>" + context.gs(BBRErrors.ERR_DUPLICATE_EMAIL);
		        } else
					if (pmgr.findByUrlId(urlID) != null) {
						result += "<br/>" + context.gs(BBRErrors.ERR_URLID_MUST_BE_UNIQUE);
					}
			
			try {
				flat = Double.parseDouble(lat);
				flon = Double.parseDouble(lon);

				BBRShop shopObj = null;
				BBRPoS posObj = null;
				BBRUser userObj = null;
				try {
					shopObj = smgr.create(shop, "RU", tz, BBRShopStatus.SHOPSTATUS_ACTIVE);
					posObj = pmgr.create(shopObj, pos, "", new BBRGPS(flat, flon), null, null, currency, tz, urlID, email, "", city, BBRPoSStatus.POSSTATUS_ACTIVE);
					userObj = umgr.create(email, name, lastName, passwordRepeat, BBRUserRole.ROLE_SHOP_ADMIN, shopObj, posObj);
					String link = "general-approve.jsp?userId=" + userObj.getId() + "&code=" + userObj.getApprovalCode();
					BBRMailer.send(userObj.getEmail(), context.gs("MSG_APPROVE_TITLE"), context.gs("MSG_APPROVE_TEXT", name, lastName, link, 2));
				} catch (Exception ex) {
					if (shopObj != null) smgr.delete(shopObj);
					if (posObj != null) pmgr.delete(posObj);
					if (userObj != null) umgr.delete(userObj);
					result += "<br/>" + context.gs(BBRErrors.ERR_WRONG_INPUT_FORMAT);
				}
				
			} catch (Exception ex) {
				result += "<br/>" + context.gs(BBRErrors.ERR_WRONG_INPUT_FORMAT);
			}
		}		

		if (!result.isEmpty()) {
			if (result.startsWith("<br/>"))
				result = result.substring(5);
			response.setStatus(BBRUtil.errorResponseCode);
		}
		response.setContentType("text/plain");  
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().write(result); 
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
