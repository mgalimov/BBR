import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRErrors;
import BBRClientApp.BBRParams;
import BBRCust.BBRStockItem;
import BBRCust.BBRStockItemGroup;
import BBRCust.BBRStockItemGroupManager;
import BBRCust.BBRStockItemManager;

@WebServlet("/BBRStockItems")
public class BBRStockItems extends BBRBasicServlet<BBRStockItem, BBRStockItemManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRStockItems() throws InstantiationException, IllegalAccessException {
        super(BBRStockItemManager.class);
    }

	@Override
	protected String create(BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String title = params.get("title");
		String description = params.get("description");
		String sGroupId = params.get("group");
		String sState = params.get("state");
		int state;
		BBRStockItemGroup group  = null;
		
		try {
			BBRStockItemGroupManager gmgr = new BBRStockItemGroupManager();
			group = gmgr.findById(Long.parseLong(sGroupId));
		} catch (Exception ex) {
			throw new Exception(BBRErrors.ERR_GROUP_NOT_SPECIFIED);
		}

		if (group == null) {
			throw new Exception(BBRErrors.ERR_GROUP_NOT_SPECIFIED);
		}
		
		try {
			state = Integer.parseInt(sState);
		} catch (Exception ex) {
			throw new Exception(BBRErrors.ERR_WRONG_INPUT_FORMAT);
		}
		BBRStockItem item = manager.create(title, group, description, state);
		return item.getId().toString();
	}

	@Override
	protected BBRStockItem beforeUpdate(BBRStockItem item, BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String title = params.get("title");
		String description = params.get("description");
		String sGroupId = params.get("group");
		String sState = params.get("state");
		int state;
		BBRStockItemGroup group  = null;
		
		try {
			BBRStockItemGroupManager gmgr = new BBRStockItemGroupManager();
			group = gmgr.findById(Long.parseLong(sGroupId));
		} catch (Exception ex) {
			throw new Exception(BBRErrors.ERR_GROUP_NOT_SPECIFIED);
		}

		if (group == null) {
			throw new Exception(BBRErrors.ERR_GROUP_NOT_SPECIFIED);
		}
		
		try {
			state = Integer.parseInt(sState);
		} catch (Exception ex) {
			throw new Exception(BBRErrors.ERR_WRONG_INPUT_FORMAT);
		}

		item.setTitle(title);
		item.setDescription(description);
		item.setGroup(group);
		item.setState(state);
		return item;		
	}
}
