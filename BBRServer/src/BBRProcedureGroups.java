import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBRAcc.BBRPoS;
import BBRAcc.BBRPoSManager;
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
}
