import javax.servlet.annotation.MultipartConfig;
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
@MultipartConfig
public class BBRUsers extends BBRBasicServlet<BBRUser, BBRUserManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRUsers() throws InstantiationException, IllegalAccessException {
        super(BBRUserManager.class);
    }

    protected BBRUser check(BBRUser user, BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		BBRContext context = BBRContext.getContext(request);
		if (user == null)
			user = new BBRUser();

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
		BBRUser user = check(null, params, request, response);
		String password = params.get("password");
		user = manager.create(user.getEmail(), user.getFirstName(), user.getLastName(), 
				password, user.getRole(), user.getShop(), user.getPos());
		return user.getId().toString();
	}

	@Override
	protected BBRUser beforeUpdate(BBRUser user, BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return check(user, params, request, response);
	}
	
	@Override
	protected boolean checkRightsForPic(String id, BBRParams params,
			HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		
		if (context.user.getRole() == BBRUserRole.ROLE_BBR_OWNER)
			return true;
		
		if ((context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN || 
			 context.user.getRole() == BBRUserRole.ROLE_POS_SPECIALIST || 
			 context.user.getRole() == BBRUserRole.ROLE_VISITOR) && 
			 context.user.getId() == Long.parseLong(id))
			return true;
		
		if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN) {
			BBRUser user = new BBRUserManager().findById(Long.parseLong(id));
			if (user != null && user.getShop() != null && user.getShop().getId() == context.user.getShop().getId())
				return true;
			if (user != null && user.getPos() != null && user.getPos().getShop().getId() == context.user.getShop().getId())
				return true;
		}
		
		return false;
	}
}
