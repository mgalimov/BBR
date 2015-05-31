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

@WebServlet("/BBRUsers")
public class BBRUsers extends BBRBasicServlet<BBRUser, BBRUserManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRUsers() throws InstantiationException, IllegalAccessException {
        super(BBRUserManager.class);
    }

	@Override
	protected String create(BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String email = params.get("email");
		String firstName = params.get("firstName");
		String lastName = params.get("lastName");
		String password = params.get("password");
		int role = Integer.valueOf(params.get("role"));
		String shopId = params.get("shop");
		String posId = params.get("pos");
		BBRShop shop = null;
		BBRPoS pos = null;
		
		if (role == BBRUserRole.ROLE_SHOP_ADMIN) {
			BBRShopManager mgrShop = new BBRShopManager(); 
			shop = mgrShop.findById(Long.parseLong(shopId));
			if (shop == null) {
				throw new Exception(BBRErrors.ERR_SHOP_MUST_BE_SPECIFIED);
			}
		}

		if (role == BBRUserRole.ROLE_POS_ADMIN) {
			BBRPoSManager mgrPos = new BBRPoSManager(); 
			pos = mgrPos.findById(Long.parseLong(posId));
			if (pos == null) {						
				throw new Exception(BBRErrors.ERR_POS_MUST_BE_SPECIFIED);
			}
		}

		manager.createAndStoreUser(email, firstName, lastName, password, role, shop, pos);
		return "";
	}

	@Override
	protected BBRUser beforeUpdate(BBRUser user, BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String firstName = params.get("firstName");
		String lastName = params.get("lastName");
		String approved = params.get("approved");
		int role = Integer.valueOf(params.get("role"));
		String shopId = params.get("shop");
		String posId = params.get("pos");
		BBRShop shop = null;
		BBRPoS pos = null;
		
		if (role == BBRUserRole.ROLE_SHOP_ADMIN) {
			BBRShopManager mgrShop = new BBRShopManager(); 
			shop = mgrShop.findById(Long.parseLong(shopId));
			if (shop == null) {
				throw new Exception(BBRErrors.ERR_SHOP_MUST_BE_SPECIFIED);
			}
		}

		if (role == BBRUserRole.ROLE_POS_ADMIN) {
			BBRPoSManager mgrPos = new BBRPoSManager(); 
			pos = mgrPos.findById(Long.parseLong(posId));
			if (pos == null) {						
				throw new Exception(BBRErrors.ERR_POS_MUST_BE_SPECIFIED);
			}
		}

		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setApproved(Boolean.parseBoolean(approved));
		user.setRole(role);
		user.setShop(shop);
		user.setPos(pos);
		return user;		
	}
}
