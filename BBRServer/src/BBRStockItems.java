import java.util.Hashtable;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRErrors;
import BBRAcc.BBRUser.BBRUserRole;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;
import BBRCust.BBRStockItem;
import BBRCust.BBRStockItemGroup;
import BBRCust.BBRStockItemGroupManager;
import BBRCust.BBRStockItemManager;

@WebServlet("/BBRStockItems")
public class BBRStockItems extends
		BBRBasicServlet<BBRStockItem, BBRStockItemManager> {
	private static final long serialVersionUID = 1L;

	public BBRStockItems() throws InstantiationException,
			IllegalAccessException {
		super(BBRStockItemManager.class);
	}

	@Override
	protected String create(BBRParams params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String title = params.get("title");
		String description = params.get("description");
		String sGroupId = params.get("group");
		String sState = params.get("state");
		int state;
		BBRStockItemGroup group = null;

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
	protected BBRStockItem beforeUpdate(BBRStockItem item, BBRParams params,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String title = params.get("title");
		String description = params.get("description");
		String sGroupId = params.get("group");
		String sState = params.get("state");
		int state;
		BBRStockItemGroup group = null;

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

		@SuppressWarnings("unchecked")
		Hashtable<String, String> filter = (Hashtable<String, String>)context.get("filter");
		String groupId = "";
		if (filter != null)
			groupId = filter.get("group"); 
		if (groupId != null && !groupId.isEmpty()) {
			where += " and group.id = " + groupId; 
		}
		
		return manager.list(pageNumber, pageSize, where,
				BBRContext.getOrderBy(sortingFields, fields)).toJson();
	}
}
