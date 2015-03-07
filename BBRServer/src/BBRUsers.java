import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import BBRAcc.BBRUser;
import BBRAcc.BBRUserManager;


@WebServlet("/BBRUsers")
public class BBRUsers extends BBRBasicServlet<BBRUser, BBRUserManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRUsers() throws InstantiationException, IllegalAccessException {
        super(BBRUserManager.class);
    }

	@Override
	String create(BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String email = params.get("email");
		String firstName = params.get("firstName");
		String lastName = params.get("lastName");
		String password = params.get("password");
		String role = params.get("role");
		manager.createAndStoreUser(email, firstName, lastName, password, Integer.valueOf(role));
		return "";
	}

	@Override
	protected BBRUser beforeUpdate(BBRUser user, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		String firstName = params.get("firstName");
		String lastName = params.get("lastName");
		String approved = params.get("approved");
		String role = params.get("role");
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setApproved(Boolean.parseBoolean(approved));
		user.setRole(Integer.valueOf(role));
		return user;		
	}
}
