import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import BBRAcc.BBRShop;
import BBRAcc.BBRShopManager;
import BBRClientApp.BBRContext;

@WebServlet("/BBRShops")
public class BBRShops extends BBRBasicServlet<BBRShop, BBRShopManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRShops() throws InstantiationException, IllegalAccessException {
        super(BBRShopManager.class);
    }

	@Override
	String create(BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String title = params.get("title");
		manager.createAndStoreShop(title);
		return "";
	}

	@Override
	protected BBRShop beforeUpdate(BBRShop shop, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		String title = params.get("title");
		shop.setTitle(title);
		return shop;		
	}

	@Override
	protected String getReferenceData(String query, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		return manager.list(context.user, query, "title").toJson();
	}
}
