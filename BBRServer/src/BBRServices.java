import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRErrors;
import BBRAcc.BBRService;
import BBRAcc.BBRServiceManager;
import BBRAcc.BBRService.BBRServiceStatus;
import BBRAcc.BBRUser.BBRUserRole;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;

@WebServlet("/BBRServices")
public class BBRServices extends BBRBasicServlet<BBRService, BBRServiceManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRServices() throws InstantiationException, IllegalAccessException {
        super(BBRServiceManager.class);
    }

	@Override
	protected String create(BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String title = params.get("title");
			int status = (int)Integer.parseInt(params.get("status"));
			Boolean demo = Boolean.parseBoolean(params.get("demo"));
			Boolean basic = Boolean.parseBoolean(params.get("basic"));
			manager.createAndStoreService(title, status, demo, basic);
		} catch (Exception ex) {
			throw new Exception(BBRErrors.ERR_WRONG_INPUT_FORMAT);
		}
		return "";
	}

	@Override
	protected BBRService beforeUpdate(BBRService service, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		String title = params.get("title");
		int status = (int)Integer.parseInt(params.get("status"));
		Boolean demo = Boolean.parseBoolean(params.get("demo"));
		Boolean basic = Boolean.parseBoolean(params.get("basic"));
		
		service.setTitle(title);
		service.setStatus(status);
		service.setDemo(demo);
		service.setBasic(basic);
		return service;		
	}

	@Override
	protected String getReferenceData(String query, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		
		if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN || 
			context.user.getRole() == BBRUserRole.ROLE_BBR_OWNER)
			return manager.list(query, manager.getTitleField(), "and status=" + BBRServiceStatus.SERVICE_ACTIVE).toJson();
		else
			return null;
	}
}
