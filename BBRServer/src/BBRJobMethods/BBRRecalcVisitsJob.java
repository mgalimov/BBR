package BBRJobMethods;

import org.hibernate.Session;
import org.quartz.JobExecutionContext;

import BBR.BBRDataSet;
import BBR.BBRUtil;
import BBRCust.BBRVisit;
import BBRCust.BBRVisitManager;

public class BBRRecalcVisitsJob extends BBRBasicJob {
	
    public BBRRecalcVisitsJob() {
    }

	@Override
	public String run(JobExecutionContext arg0) {
		BBRVisitManager mgr = new BBRVisitManager();
		BBRDataSet<BBRVisit> d = mgr.list();
		for (BBRVisit v : d.data) {
			if (v.getAmountToSpecialist() <= 0.0001F || v.getAmountToSpecialist() > v.getPricePaid()) {
				boolean tr = BBRUtil.beginTran();
				Session session = BBRUtil.getSession();
				try {
					if (mgr.checkBeforeUpdate(v)) {
						session.update(v);
						BBRUtil.commitTran(tr);
					} else {
						BBRUtil.rollbackTran(tr);
					}
				} catch (Exception e) {
					BBRUtil.rollbackTran(tr);
				}
			}
		}
		return "SUCCESS";
	}
}
