package BBRAcc;

import org.hibernate.Session;
import BBR.BBRDataManager;
import BBR.BBRUtil;
import java.util.Date;

public class BBRJobManager extends BBRDataManager<BBRJob>{
	
	public BBRJobManager() {
		super();
		sessionIndex = BBRAccReg.sessionIndex;
		classTitle = "Job";	
	}
	
	public void createAndStoreJob(String title, Date nextRun, String runConditions, String runMethod) {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);

        BBRJob job = new BBRJob();
        job.setTitle(title);
        job.setNextRun(nextRun);
        job.setRunConditions(runConditions);
        job.setRunMethod(runMethod);
        job.setLastRunStatus("NEVER STARTED");
        session.save(job);

        BBRUtil.commitTran(sessionIndex, tr);
    }    
}
