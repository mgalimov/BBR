import java.text.SimpleDateFormat;
import java.util.Hashtable;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRUtil;
import BBRAcc.BBRServiceSubscription;
import BBRAcc.BBRTransaction;
import BBRAcc.BBRTransactionManager;
import BBRAcc.BBRUser.BBRUserRole;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;

@WebServlet("/BBRTransactions")
public class BBRTransactions extends BBRBasicServlet<BBRTransaction, BBRTransactionManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRTransactions() throws InstantiationException, IllegalAccessException {
        super(BBRTransactionManager.class);
    }

	@Override
	protected String getData(int pageNumber, int pageSize, 
			Hashtable<Integer, Hashtable<String, String>> fields,
			Hashtable<Integer, Hashtable<String, String>> sortingFields,
			BBRParams params, HttpServletRequest request, 
			HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		String where = "1=1";
		
		if (context.user == null || 
				!(context.user.getRole() == BBRUserRole.ROLE_BBR_OWNER || 
				  context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN))
			return null;

		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
		
		BBRServiceSubscription subscription = (BBRServiceSubscription)context.get("subscriptions");
		if (subscription != null)
			where += " and subscription.id = " + subscription.getId(); 
		if (context.filterShop != null)
			where += " and subscription.shop.id = " + context.filterShop.getId();
		if (context.filterPoS != null)
			where += " and subscription.shop.id = " + context.filterPoS.getShop().getId();
		if (context.filterStartDate != null)
			where += " and date >= '" + df.format(context.filterStartDate) + "'";
		if (context.filterEndDate != null)
			where += " and date <= '" + df.format(context.filterEndDate) + "'";
		
		return manager.list(pageNumber, pageSize, where, BBRContext.getOrderBy(sortingFields, fields)).toJson();
	}	
}
