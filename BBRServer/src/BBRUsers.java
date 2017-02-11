import java.util.Hashtable;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRErrors;
import BBRAcc.BBRPoS;
import BBRAcc.BBRPoSManager;
import BBRAcc.BBRShop;
import BBRAcc.BBRShopManager;
import BBRAcc.BBRUser;
import BBRAcc.BBRUserManager;
import BBRAcc.BBRUser.BBRUserRole;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;

@WebServlet("/BBRUsers")
public class BBRUsers extends BBRBasicServlet<BBRUser, BBRUserManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRUsers() throws InstantiationException, IllegalAccessException {
        super(BBRUserManager.class);
    }

    protected BBRUser check(BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		BBRContext context = BBRContext.getContext(request);
		BBRUser user = new BBRUser();

		String email = params.get("email");
		String firstName = params.get("firstName");
		String lastName = params.get("lastName");
		int role = Integer.valueOf(params.get("role"));
		String shopId = params.get("shop");
		String posId = params.get("pos");
		String approved = params.get("approved");
		
		BBRShop shop = null;
		BBRPoS pos = null;
		
		if (context.user.getRole() != BBRUserRole.ROLE_BBR_OWNER && 
			context.user.getRole() != BBRUserRole.ROLE_SHOP_ADMIN)
			throw new Exception(BBRErrors.ERR_INSUFFICIENT_RIGHTS);
    	
		if (role > context.user.getRole()) {
			throw new Exception(BBRErrors.ERR_INSUFFICIENT_RIGHTS);
		}
		
		if (role == BBRUserRole.ROLE_SHOP_ADMIN) {
			if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
				shop = context.user.getShop();
			else {
				BBRShopManager mgrShop = new BBRShopManager(); 
				shop = mgrShop.findById(Long.parseLong(shopId));
			}
			if (shop == null) {
				throw new Exception(BBRErrors.ERR_SHOP_MUST_BE_SPECIFIED);
			}
		}

		if (role == BBRUserRole.ROLE_POS_ADMIN || role == BBRUserRole.ROLE_POS_SPECIALIST) {
			BBRPoSManager mgrPos = new BBRPoSManager(); 
			pos = mgrPos.findById(Long.parseLong(posId));
			if (pos == null) {						
				throw new Exception(BBRErrors.ERR_POS_MUST_BE_SPECIFIED);
			}
			if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN && 
				pos.getShop().getId() != context.user.getShop().getId())
				throw new Exception(BBRErrors.ERR_INSUFFICIENT_RIGHTS);
		}

		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setApproved(Boolean.parseBoolean(approved));
		user.setRole(role);
		user.setShop(shop);
		user.setPos(pos);
		return user;		
    }
    
	@Override
	protected String create(BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		BBRUser user = check(params, request, response);
		String password = params.get("password");
		manager.createAndStoreUser(user.getEmail(), user.getFirstName(), user.getLastName(), 
				password, user.getRole(), user.getShop(), user.getPos());
		return "";
	}

	@Override
	protected BBRUser beforeUpdate(BBRUser user, BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return check(params, request, response);
	}

	@Override
	protected String getData(int pageNumber, int pageSize,
			Hashtable<Integer, Hashtable<String, String>> fields,
			Hashtable<Integer, Hashtable<String, String>> sortingFields,
			BBRParams params, HttpServletRequest request,
			HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		String where = "";
		if (context.user != null) {
			if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN
					|| context.user.getRole() == BBRUserRole.ROLE_POS_SPECIALIST)
				if (context.user.getPos() != null)
					where = manager.wherePos(context.user.getPos().getId());
			if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
				if (context.user.getShop() != null)
					where = manager.whereShop(context.user.getShop().getId());
			if (context.user.getRole() == BBRUserRole.ROLE_BBR_OWNER)
				if (context.filterPoS != null)
					where = manager.wherePos(context.filterPoS.getId());
				else if (context.filterShop != null)
					where = manager.whereShop(context.filterShop.getId());
		}

		return manager.list(pageNumber, pageSize, where,
				BBRContext.getOrderBy(sortingFields, fields)).toJson();
	}

}
