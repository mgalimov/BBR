package BBRJobMethods;

import org.quartz.JobExecutionContext;

public class BBRRegularTransJob extends BBRBasicJob {
	
    public BBRRegularTransJob() {
    }

	@Override
	public String run(JobExecutionContext arg0) {
		return "SUCCESS";
	}
}
