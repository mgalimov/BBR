package BBRJobMethods;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import BBRAcc.BBRJob;
import BBRAcc.BBRJobManager;

public class BBRRegularTransJob implements Job {
	
    public BBRRegularTransJob() {
    }

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Long id = (Long)arg0.getJobDetail().getJobDataMap().get("id");
		BBRJobManager jm = new BBRJobManager();
		BBRJob j = jm.findById(id);
		j.setLastRun(arg0.getFireTime());
		j.setNextRun(arg0.getNextFireTime());
		jm.update(j);
	}
}
