import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBRAcc.BBRUser;
import BBRAcc.BBRUserManager;
import BBRCust.BBRTask;
import BBRCust.BBRTaskManager;

@WebServlet("/BBRTasks")
public class BBRTasks extends BBRBasicServlet<BBRTask, BBRTaskManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRTasks() throws InstantiationException, IllegalAccessException {
        super(BBRTaskManager.class);
    }

	@Override
	String create(BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		String title = params.get("title");
		String performerId = params.get("performer");
		BBRUserManager mgr = new BBRUserManager();
		BBRUser performer = mgr.findById(Long.parseLong(performerId));
		if (performer != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date createdAt = new Date();
			Date deadline;
			try {
				deadline = df.parse(params.get("deadline"));
			} catch (Exception ex) {
				deadline = createdAt;
			}
			String text = params.get("text");
			manager.createAndStoreTask(title, performer, createdAt, deadline, text, null, null);
		}
		return "";
	}

	@Override
	protected BBRTask beforeUpdate(BBRTask task, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		String title = params.get("title");
		String performerId = params.get("performer");
		BBRUserManager mgr = new BBRUserManager();
		BBRUser performer = mgr.findById(Long.parseLong(performerId));
		if (performer != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date createdAt = new Date();
			Date deadline;
			try {
				deadline = df.parse(params.get("deadline"));
			} catch (Exception ex) {
				deadline = createdAt;
			}
			String text = params.get("text");
			String state = params.get("state");
	    	task.setTitle(title);
	    	task.setPerformer(performer);
	    	task.setDeadline(deadline);
	    	task.setCreatedAt(createdAt);
	    	task.setText(text);
	        task.setState((int) Long.parseLong(state));
	        return task;
		}
		return null;		
	}
	
}
