package BBRCust;

import java.util.Date;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRDataSet;
import BBR.BBRUtil;
import BBRAcc.BBRPoS;
import BBRAcc.BBRServiceSubscription;
import BBRAcc.BBRServiceSubscriptionManager;
import BBRAcc.BBRUser;
import BBRCust.BBRCustReg;
import BBRCust.BBRTask.BBRTaskState;

public class BBRTaskManager extends BBRDataManager<BBRTask>{
	
	public BBRTaskManager() {
		super();
		sessionIndex = BBRCustReg.sessionIndex;
		titleField = "title";
		classTitle = "Task";	
	}

	public void createAndStoreTask(String title, BBRUser performer, BBRPoS pos, Date createdAt, Date deadline,
								   String text, String objectType, Long objectId) {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);

        BBRTask task = new BBRTask();
    	task.setTitle(title);
    	task.setPerformer(performer);
    	if (performer != null)
    		if (performer.getPos() != null)
    			pos = performer.getPos();
    	task.setPos(pos);
    	task.setDeadline(deadline);
    	task.setCreatedAt(createdAt);
    	task.setText(text);
    	task.setObjectType(objectType);
    	task.setObjectId(objectId);
    	task.setState(BBRTaskState.TASKSTATE_INITIALIZED);

    	session.save(task);

        BBRUtil.commitTran(sessionIndex, tr);
    }
	
	public BBRDataSet<BBRTask> list(String queryTerm, String sortBy, BBRUser performer) {
    	String where = "";
    	
   		if (performer != null) {
   			where = " performer.id = " + performer.getId();		
   		}
        return list(queryTerm, sortBy, where);
    }
    
	public BBRDataSet<BBRTask> list(Long performerId, int pageNumber, int pageSize, String orderBy) {
   		if (performerId == null)
   			return null;
   			
        String where = " where performer.id=" + performerId.toString();
        return list(pageNumber, pageSize, where, orderBy);
	}
	
	@Override
    public String whereShop(Long shopId) {
    	return "pos.shop.id = " + shopId;
    };
    
    public BBRVisit getVisit(BBRTask task) {
    	if (task == null)
    		return null;
    	
    	if (task.getObjectType().equals("BBRCust.BBRVisit")) {
    		BBRVisitManager mgr = new BBRVisitManager();
    		return mgr.findById(task.getObjectId());
    	} else
    		return null;
    }
    
    public BBRServiceSubscription getSubscription(BBRTask task) {
    	if (task == null)
    		return null;
    	
    	if (task.getObjectType().equals("BBRAcc.BBRServiceSubscription")) {
    		BBRServiceSubscriptionManager mgr = new BBRServiceSubscriptionManager();
    		return mgr.findById(task.getObjectId());
    	} else
    		return null;
    }
}
