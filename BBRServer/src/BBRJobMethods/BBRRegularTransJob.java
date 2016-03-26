package BBRJobMethods;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import BBRAcc.BBRJob;
import BBRAcc.BBRJobManager;

public class BBRRegularTransJob extends BBRBasicJob {
	
    public BBRRegularTransJob() {
    }

	@Override
	public String run(JobExecutionContext arg0) {
		return "SUCCESS";
	}
}
