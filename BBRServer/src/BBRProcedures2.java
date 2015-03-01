import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBRCust.BBRProcedure;
import BBRCust.BBRProcedureManager;

@WebServlet("/BBRProcedures")
public class BBRProcedures2 extends BBRBasicServlet<BBRProcedure, BBRProcedureManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRProcedures2() throws InstantiationException, IllegalAccessException {
        super(BBRProcedureManager.class);
    }

	@Override
	String create(BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	@Override
	protected void beforeUpdate(BBRProcedure proc, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		return;		
	}
	
	@Override
	protected void beforeDelete(BBRProcedure proc, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		return;		
	}
}
