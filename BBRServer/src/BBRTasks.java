import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRUtil;
import BBRAcc.BBRServiceSubscription;
import BBRAcc.BBRServiceSubscriptionManager;
import BBRAcc.BBRUser;
import BBRAcc.BBRUser.BBRUserRole;
import BBRAcc.BBRUserManager;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;
import BBRCust.BBRTask;
import BBRCust.BBRTask.BBRTaskState;
import BBRCust.BBRTaskManager;
import BBRCust.BBRVisit;
import BBRCust.BBRVisitManager;

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
			SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat);
			Date createdAt = BBRUtil.now(BBRContext.getContext(request).timeZone);
			Date deadline;
			try {
				deadline = df.parse(params.get("deadline"));
			} catch (Exception ex) {
				deadline = createdAt;
			}
			String text = params.get("text");
			BBRTask task = manager.createAndStoreTask(title, performer, null, createdAt, deadline, text, null, null);
			return task.getId().toString();
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
			SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat);
			Date createdAt = BBRUtil.now(BBRContext.getContext(request).timeZone);
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
	    	if (task.getCreatedAt() == null)
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
				where = " pos.id = " + context.user.getPos().getId() + " or performer.id = " + context.user.getId();
		if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
			if (context.user.getShop() != null)
				where = " pos.shop.id = " + context.user.getShop().getId() + " or performer.id = " + context.user.getId();
		if (context.user.getRole() == BBRUserRole.ROLE_BBR_OWNER)
				where = " pos.id is null and performer.id is null";
		
		if ((String)context.get("viewtasks") == null)
			where = "(" + where + ") and (state < " + BBRTaskState.TASKSTATE_COMPLETED + ")";
				
		return manager.list(pageNumber, pageSize, where, BBRContext.getOrderBy(sortingFields, fields)).toJson();
	}

	@Override
	protected String processOperation(String operation, BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String res = "";
		BBRContext context = BBRContext.getContext(request);
		
		if (operation.equals("approve")) {
			Long taskId = Long.parseLong(params.get("taskId"));
			BBRTask task = manager.findById(taskId);
			if (task != null) {
				task.setState(BBRTaskState.TASKSTATE_COMPLETED);
				BBRVisit visit = manager.getVisit(task);
				if (visit != null) {
					BBRVisitManager mgr = new BBRVisitManager();
					mgr.approve(visit);
				}
				BBRServiceSubscription subscr = manager.getSubscription(task);
				if (subscr != null) {
					BBRServiceSubscriptionManager mgr = new BBRServiceSubscriptionManager();
					mgr.approve(subscr);
				}
				manager.update(task);
			}
			res = "";
		} else
		if (operation.equals("disapprove")) {
			Long taskId = Long.parseLong(params.get("taskId"));
			BBRTask task = manager.findById(taskId);
			if (task != null) {
				task.setState(BBRTaskState.TASKSTATE_COMPLETED);
//				BBRVisit visit = manager.getVisit(task);
//				if (visit != null) {
//					BBRVisitManager mgr = new BBRVisitManager();
//					mgr.cancel(visit);
//				}
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
				BBRVisit visit = manager.getVisit(task);
				if (visit != null)
					res = visit.getId().toString();
			}
		} else
		if (operation.equals("getsubscription")) {
			Long taskId = Long.parseLong(params.get("taskId"));
			BBRTask task = manager.findById(taskId);
			if (task != null) {
				BBRServiceSubscription subscr = manager.getSubscription(task);
				if (subscr != null)
					res = subscr.getId().toString();
			}
		} else
		if (operation.equals("togglealltasks")) {
			context.set("viewtasks", "all");
		} else 
		if (operation.equals("toggleincompletetasks")) {
			context.set("viewtasks", null);
		}
		
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

		if (context.user.getRole() == BBRUserRole.ROLE_BBR_OWNER)
				where = "pos.id is null and performer.id is null";

		if (!where.equals("")) 
			where = "(" + where +") and";

		where += "(state <> " + BBRTaskState.TASKSTATE_COMPLETED + ")";
		return manager.count(where).toString();
	}
}
