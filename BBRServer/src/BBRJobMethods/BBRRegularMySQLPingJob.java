package BBRJobMethods;

import org.quartz.JobExecutionContext;

import BBRCust.BBRVisit;
import BBRCust.BBRVisitManager;

public class BBRRegularMySQLPingJob extends BBRBasicJob {
	
    public BBRRegularMySQLPingJob() {
    }

	@Override
	public String run(JobExecutionContext arg0) {
		// touching MySQL by reading visits to avoid losing connection to MySQL in OpenShift
		BBRVisitManager mgr = new BBRVisitManager();
		@SuppressWarnings("unused")
		BBRVisit v = mgr.findById(1L);
		return "SUCCESS";
	}
}
