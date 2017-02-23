import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRErrors;
import BBRAcc.BBRShop;
import BBRAcc.BBRShopManager;
import BBRClientApp.BBRParams;
import BBRCust.BBRStockItemGroup;
import BBRCust.BBRStockItemGroupManager;

@WebServlet("/BBRStockItemGroups")
public class BBRStockItemGroups extends BBRBasicServlet<BBRStockItemGroup, BBRStockItemGroupManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRStockItemGroups() throws InstantiationException, IllegalAccessException {
        super(BBRStockItemGroupManager.class);
    }

	@Override
	protected String create(BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String title = params.get("title");
		String description = params.get("description");
		String sShop = params.get("shop");
		BBRShop shop = null;
		
		try {
			BBRShopManager smgr = new BBRShopManager();
			shop = smgr.findById(Long.parseLong(sShop));
		} catch (Exception ex) {
			throw new Exception(BBRErrors.ERR_SHOP_MUST_BE_SPECIFIED);
		}

		if (shop == null) {
			throw new Exception(BBRErrors.ERR_SHOP_MUST_BE_SPECIFIED);
		}
		
		BBRStockItemGroup group = manager.create(title, shop, description);
		return group.getId().toString();
	}

	@Override
	protected BBRStockItemGroup beforeUpdate(BBRStockItemGroup group, BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String title = params.get("title");
		String description = params.get("description");
		String sShop = params.get("shop");
		BBRShop shop = null;
		
		try {
			BBRShopManager smgr = new BBRShopManager();
			shop = smgr.findById(Long.parseLong(sShop));
		} catch (Exception ex) {
			throw new Exception(BBRErrors.ERR_SHOP_MUST_BE_SPECIFIED);
		}

		if (shop == null) {
			throw new Exception(BBRErrors.ERR_SHOP_MUST_BE_SPECIFIED);
		}
		
		group.setTitle(title);
		group.setDescription(description);
		group.setShop(shop);
		return group;		
	}
}
