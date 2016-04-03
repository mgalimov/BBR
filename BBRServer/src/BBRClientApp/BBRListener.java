package BBRClientApp;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.TimeZone;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import BBR.BBRDataSet;
import BBRAcc.BBRJob;
import BBRAcc.BBRJobManager;

public class BBRListener implements ServletContextListener {
	private static Scheduler sched;
	
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
    	try {
			sched.shutdown();
		} catch (SchedulerException e) {
		}
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
    	SchedulerFactory sf;
    	try {
    		//TimeZone.setDefault(TimeZone.getTimeZone("GMT+4"));
			sf = new StdSchedulerFactory();
			sched = sf.getScheduler();
	    	reschedule();
    	} catch (Exception ex) {
    	}
    }
    
    public static void reschedule() {
    	Thread thread = new Thread(){
    		public void run() {
    	    	try {
    				sched.clear();

    				BBRJobManager jm = new BBRJobManager();
    				BBRDataSet<BBRJob> jl = jm.list("", "runConditions", "substring(runConditions,1,1)<>'-' and  runConditions<>''");
    				for (BBRJob j : jl.data) {
    					try {
    						@SuppressWarnings("unchecked")
    						Class<? extends Job> c = (Class<? extends Job>) Class.forName(j.getRunMethod());
    				    	JobDetail job = newJob(c)   
    				    		    .withIdentity(j.getTitle(), "main")
    				    		    .usingJobData("id", j.getId())
    				    		    .build();	
    						CronTrigger trigger = newTrigger()
    								.withIdentity(j.getTitle(), "main")
    								.withSchedule(cronSchedule(j.getRunConditions()))
    								.startAt(j.getNextRun())
    								.build();
    					
    						sched.scheduleJob(job, trigger);
    					} catch (Exception e) {
    						
    					}
    				}
    				sched.start();
    			} catch (SchedulerException e) {

    			}
    		}
    	};
    	thread.start();
   }
}
