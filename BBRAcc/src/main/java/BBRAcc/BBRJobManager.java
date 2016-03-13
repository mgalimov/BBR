package BBRAcc;

import org.hibernate.Session;
import BBR.BBRDataManager;
import BBR.BBRUtil;

public class BBRJobManager extends BBRDataManager<BBRJob>{
	
	public BBRJobManager() {
		super();
		sessionIndex = BBRAccReg.sessionIndex;
		classTitle = "Job";	
	}
	
	public void createAndStoreJob(String title) {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);

        BBRJob job = new BBRJob();
        job.setTitle(title);
        session.save(job);

        BBRUtil.commitTran(sessionIndex, tr);
    }    
}
