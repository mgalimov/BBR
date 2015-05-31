import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBRAcc.BBRUser;
import BBRAcc.BBRUser.BBRUserRole;
import BBRAcc.BBRUserManager;
import BBRClientApp.BBRContext;
import BBRCust.BBRTask;
import BBRCust.BBRTask.BBRTaskState;
import BBRCust.BBRTaskManager;

@WebServlet("/BBRTasks")
public class BBRTasks extends BBRBasicServlet<BBRTask, BBRTaskManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRTasks() throws InstantiationException, IllegalAccessException {
        super(BBRTaskManager.class);
    }

	@Override
	protected String create(BBRParams params, HttpServletRequest request, HttpServletResponse response) {
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
			manager.createAndStoreTask(title, performer, null, createdAt, deadline, text, null, null);
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
	
	@Override
	protected String getData(int pageNumber, int pageSize, 
			Hashtable<Integer, Hashtable<String, String>> fields,
			Hashtable<Integer, Hashtable<String, String>> sortingFields,
			BBRParams params, HttpServletRequest request, 
			HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request); 
		if (context.user == null)
			return null;
		if (context.user.getRole() < BBRUserRole.ROLE_POS_ADMIN)
			return null;
		
		String where = "";
		if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN)
			if (context.user.getPos() != null)
				where = " pos.id = " + context.user.getPos().getId() + " or ";
		if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
			if (context.user.getShop() != null)
				where = " pos.shop.id = " + context.user.getShop().getId() + " or ";
		where += " performer.id = " + context.user.getId();
		
		if (!context.viewAllTasks)
			where = "(" + where + ") and (state < " + BBRTaskState.TASKSTATE_COMPLETED + ")";
				
		return manager.list(pageNumber, pageSize, where, BBRContext.getOrderBy(sortingFields, fields)).toJson();
	}

	@Override
	protected String processOperation(String operation, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		String res = "";
		
		if (operation.equals("approve")) {
			Long taskId = Long.parseLong(params.get("taskId"));
			BBRTask task = manager.findById(taskId);
			if (task != null) {
				task.setState(BBRTaskState.TASKSTATE_COMPLETED);
				manager.update(task);
			}
			res = "";
		} else
		if (operation.equals("markread")) {
			Long taskId = Long.parseLong(params.get("taskId"));
			BBRTask task = manager.findById(taskId);
			if (task != null) {
				if (task.getState() < BBRTaskState.TASKSTATE_READ) {
					task.setState(BBRTaskState.TASKSTATE_READ);
					manager.update(task);
				}
			}
			res = "";
		} else
		if (operation.equals("getvisit")) {
			Long taskId = Long.parseLong(params.get("taskId"));
			BBRTask task = manager.findById(taskId);
			if (task != null) {
				if (task.getObjectType().equals("BBRCust.BBRVisit"))
					res = task.getObjectId().toString();
			}
		} else
		if (operation.equals("togglealltasks")) {
			BBRContext context = BBRContext.getContext(request);
			context.viewAllTasks = !context.viewAllTasks;
		};	
		
		return res;
	}
	
	@Override
	protected String getBadgeNumber(BBRParams params, HttpServletRequest request,
			HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		String where = "";
		if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN)
			if (context.user.getPos() != null)
				where = manager.wherePos(context.user.getPos().getId());
		if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
			if (context.user.getShop() != null)
				where = manager.whereShop(context.user.getShop().getId());
		if (!where.equals("")) 
			where = "(" + where +") and";
		where += "(state <> 2)";
		return manager.count(where).toString();
	}
}
