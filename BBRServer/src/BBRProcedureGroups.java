import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBRAcc.BBRPoS;
import BBRAcc.BBRPoSManager;
import BBRAcc.BBRShop;
import BBRAcc.BBRShopManager;
import BBRAcc.BBRUser.BBRUserRole;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;
import BBRCust.BBRProcedureGroup;
import BBRCust.BBRProcedureGroupManager;

@WebServlet("/BBRProcedureGroups")
public class BBRProcedureGroups extends BBRBasicServlet<BBRProcedureGroup, BBRProcedureGroupManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRProcedureGroups() throws InstantiationException, IllegalAccessException {
        super(BBRProcedureGroupManager.class);
    }

	@Override
	protected String create(BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		String title = params.get("title");
		String posId = params.get("pos");
		String desc = params.get("description");
		BBRPoSManager mgr = new BBRPoSManager();
		BBRPoS pos = mgr.findById(Long.parseLong(posId));
		
		BBRProcedureGroup group = null;
		if (pos != null) {						
			group = manager.create(title, desc, pos);
		}
		return group.getId().toString();
	}

	@Override
	protected BBRProcedureGroup beforeUpdate(BBRProcedureGroup group, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		String title = params.get("title");
		String posId = params.get("pos");
		String desc = params.get("description");
		BBRPoSManager mgr = new BBRPoSManager();
		BBRPoS pos = mgr.findById(Long.parseLong(posId));
		
		if (pos != null) {						
			group.setTitle(title);
			group.setDescription(desc);
			group.setPos(pos);
	        return group;
		}
		return null;		
	}
	
	@Override
	protected String getReferenceData(String query, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		String constrains = params.get("constrains");
		
		BBRPoS pos = null;
		BBRShop shop = null;

		if (context.planningVisit != null)
			pos = context.planningVisit.getPos();
		else
			if (!constrains.equals("")) {
				if (constrains.startsWith("s")) {
					try {
						BBRShopManager smgr = new BBRShopManager();
						shop = smgr.findById(Long.parseLong(constrains.substring(1)));
					} catch (Exception ex) {
						shop = null;
					}					
				} else {
					try {
						BBRPoSManager pmgr = new BBRPoSManager();
						pos = pmgr.findById(Long.parseLong(constrains));
					} catch (Exception ex) {
						pos = null;
					}
				}
			}
		if (pos == null)
			if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN || context.user.getRole() == BBRUserRole.ROLE_POS_SPECIALIST )
				pos = context.user.getPos();
			else
				if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
					shop = context.user.getShop();
		
		return manager.list(query, manager.getTitleField(), pos, shop).toJson();
	}

}
