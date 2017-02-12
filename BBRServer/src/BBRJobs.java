import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRUtil;
import BBRAcc.BBRJob;
import BBRAcc.BBRJobManager;
import BBRAcc.BBRUser.BBRUserRole;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRListener;
import BBRClientApp.BBRParams;

@WebServlet("/BBRJobs")
public class BBRJobs extends BBRBasicServlet<BBRJob, BBRJobManager> {
	private static final long serialVersionUID = 1L;

	public BBRJobs() throws InstantiationException, IllegalAccessException {
		super(BBRJobManager.class);
	}

	@Override
	protected String create(BBRParams params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String title = params.get("title");
		String nextRunString = params.get("nextRun");
		String runConditions = params.get("runConditions");
		String runMethod = params.get("runMethod");
		
		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
		Date nextRun;
		try {
			nextRun = df.parse(nextRunString);
		} catch (Exception ex) {
			nextRun = null;
		}
		
		BBRJob job = manager.createAndStoreJob(title, nextRun, runConditions, runMethod);
		BBRListener.reschedule();
		return job.getId().toString();
	}

	@Override
	protected BBRJob beforeUpdate(BBRJob job, BBRParams params,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String title = params.get("title");
		String nextRunString = params.get("nextRun");
		String runConditions = params.get("runConditions");
		String runMethod = params.get("runMethod");
		
		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat);
		Date nextRun;
		try {
			nextRun = df.parse(nextRunString);
		} catch (Exception ex) {
			nextRun = null;
		}
		
		job.setTitle(title);
		job.setNextRun(nextRun);
		job.setRunConditions(runConditions);
		job.setRunMethod(runMethod);
		manager.update(job);
		BBRListener.reschedule();

		return null;
	}

	@Override
	protected String getData(int pageNumber, int pageSize,
			Hashtable<Integer, Hashtable<String, String>> fields,
			Hashtable<Integer, Hashtable<String, String>> sortingFields,
			BBRParams params, HttpServletRequest request,
			HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		String where = "";
		if (context.user != null && context.user.getRole() == BBRUserRole.ROLE_BBR_OWNER)
			return manager.list(pageNumber, pageSize, where,
					BBRContext.getOrderBy(sortingFields, fields)).toJson();
		else
			return "";
	}

}
