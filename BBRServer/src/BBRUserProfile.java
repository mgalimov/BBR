import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBRAcc.BBRUser;
import BBRAcc.BBRUserManager;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;

@WebServlet("/BBRUserProfile")
public class BBRUserProfile extends BBRBasicServlet<BBRUser, BBRUserManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRUserProfile() throws InstantiationException, IllegalAccessException {
        super(BBRUserManager.class);
    }

    @Override
    protected String getRecordData(String id, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
    	BBRContext context = BBRContext.getContext(request);
    	return context.addJsonField(context.user.toJson(), "userName", context.user.getFirstName() + " " + context.user.getLastName());
    }

    @Override
    protected BBRUser beforeUpdate(BBRUser obj, BBRParams params,
    		HttpServletRequest request, HttpServletResponse response)
    		throws Exception {
    	obj.setLanguage(params.get("language"));
    	BBRContext context = BBRContext.getContext(request);
    	context.setLocale(obj.getLanguage());
    	context.user = obj;
    	return obj;
    }
}
