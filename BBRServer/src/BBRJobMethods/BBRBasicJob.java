package BBRJobMethods;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import BBRAcc.BBRJob;
import BBRAcc.BBRJobManager;

public class BBRBasicJob implements Job {
	
    public BBRBasicJob() {
    }

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		String status = "";
		
		try {
			status = run(arg0);
		} catch (Exception ex) {
			status = "FAILED";
		}
		Long id = (Long)arg0.getJobDetail().getJobDataMap().get("id");
		BBRJobManager jm = new BBRJobManager();
		BBRJob j = jm.findById(id);
		j.setLastRun(arg0.getFireTime());
		j.setNextRun(arg0.getNextFireTime());
		j.setLastRunStatus(status);
		try {
			jm.update(j);
		} catch (Exception e) {
			throw new JobExecutionException("Can't execute job");
		}
	}
	
	public String run(JobExecutionContext arg0) {
		return "";
	}
}
