import java.util.Calendar;
import java.util.Hashtable;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRErrors;
import BBR.BBRUtil;
import BBRAcc.BBRPoS;
import BBRAcc.BBRShop;
import BBRAcc.BBRUser.BBRUserRole;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;
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
		
		if (context.user != null) {
			if (context.filterEndDate == null) {
				context.filterEndDate = BBRUtil.now(context.getTimeZone());
				Calendar c = Calendar.getInstance();
				c.setTime(context.filterEndDate);
				c.add(Calendar.MONTH, -1);
				context.filterStartDate = c.getTime();
			}
			return manager.listVisitors(pageNumber, pageSize, 
					BBRContext.getOrderBy(sortingFields, columns), pos, shop, 
					context.filterStartDate, context.filterEndDate).toJson();
		}
		else
			return "";
	}

	protected String getRecordData(String id, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		
		String[] userNC = params.get("id").split(BBRUtil.recordDivider);
		String userN = userNC[0];
		String userC = "";
		
		if (userNC.length == 2) {
			userC = userNC[1];
		}
		
		BBRVisitManager.BBRVisitor obj = (BBRVisitManager.BBRVisitor)manager.findVisitor(userN, userC);
		
		if (obj != null)
			return obj.toJson();
		else
			return context.gs(BBRErrors.ERR_RECORD_NOTFOUND, manager.getClassTitle());
	}
	
	@Override
	protected String processOperation(String operation, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		context.filterEndDate = BBRUtil.now(context.getTimeZone());
		Calendar c = Calendar.getInstance();
		c.setTime(context.filterEndDate);
		
		if (operation.equals("toggle30Days"))
			c.add(Calendar.MONTH, -1);
		else
			if (operation.equals("toggle120Days")) 
				c.add(Calendar.MONTH, -4);
			else
				if (operation.equals("toggle360Days"))
					c.add(Calendar.YEAR, -1);
				else
					c.set(1970, 01, 01);
		context.filterStartDate = c.getTime();
		
		return "";
	}
}
