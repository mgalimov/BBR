import java.util.Hashtable;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBRAcc.BBRPoS;
import BBRAcc.BBRShop;
import BBRAcc.BBRUser.BBRUserRole;
import BBRClientApp.BBRContext;
import BBRCust.BBRVisitManager;
import BBRCust.BBRVisit;

@WebServlet("/BBRVisitors")
public class BBRVisitors extends BBRBasicServlet<BBRVisit, BBRVisitManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRVisitors() throws InstantiationException, IllegalAccessException {
        super(BBRVisitManager.class);
    }

	@Override
	protected String getData(int pageNumber, int pageSize, 
								Hashtable<Integer, Hashtable<String, String>> columns, 
								Hashtable<Integer, Hashtable<String, String>> sortingFields, 
								BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		
		BBRPoS pos = null;
		BBRShop shop = null;
		
		if (context.user == null) return "";
		if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN || context.user.getRole() == BBRUserRole.ROLE_POS_SPECIALIST)
			pos = context.user.getPos();
		else
			if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
				shop = context.user.getShop();
		
		
		if (context.user != null)
			return manager.listVisitors(pageNumber, pageSize, BBRContext.getOrderBy(sortingFields, columns), pos, shop).toJson();
		else
			return "";
	}

}
