import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRDataSet;
import BBR.BBRErrors;
import BBR.BBRUtil;
import BBRAcc.BBRPoS;
import BBRAcc.BBRPoSManager;
import BBRAcc.BBRUser.BBRUserRole;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;
import BBRCust.BBRSpecialist;
import BBRCust.BBRSpecialistManager;
import BBRCust.BBRTurn;
import BBRCust.BBRTurnManager;

@WebServlet("/BBRTurns")
public class BBRTurns extends BBRBasicServlet<BBRTurn, BBRTurnManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRTurns() throws InstantiationException, IllegalAccessException {
        super(BBRTurnManager.class);
    }

	@Override
	protected String create(BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String specId = params.get("specialist");
		String date = params.get("date");
		String startTime = params.get("startTime");
		String endTime = params.get("endTime");
		
		Date sTime, eTime;
		
		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
		SimpleDateFormat tf = new SimpleDateFormat(BBRUtil.fullTimeFormat);
		
		BBRSpecialistManager smgr = new BBRSpecialistManager();
		BBRSpecialist spec = smgr.findById(Long.parseLong(specId));
		
		if (startTime == null || startTime.trim().isEmpty())
			sTime = spec.getStartWorkHour();
		else
			sTime = tf.parse(startTime);

		if (endTime == null || endTime.trim().isEmpty())
			eTime = spec.getEndWorkHour();
		else
			eTime = tf.parse(endTime);

		
		Long id = Long.parseLong(manager.createAndStoreTurn(
					spec, 
					df.parse(date),
					sTime, 
					eTime));
			
		if (id == null)
			throw new Exception(BBRErrors.ERR_TURN_CROSSES);
		
		return "";
	}

	@Override
	protected BBRTurn beforeUpdate(BBRTurn turn, BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String specId = params.get("specialist");
		String date = params.get("date");
		String startTime = params.get("startTime");
		String endTime = params.get("endTime");

		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
		SimpleDateFormat tf = new SimpleDateFormat(BBRUtil.fullTimeFormat);

		Date dt = df.parse(date);
		Date st = tf.parse(startTime);
		Date et = tf.parse(endTime);
		
		BBRSpecialistManager smgr = new BBRSpecialistManager();
		BBRSpecialist spec = smgr.findById(Long.parseLong(specId)); 
		
		return manager.setTurnFields(turn, spec, dt, st, et);
	}

	@Override
	protected String processOperation(String operation, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);

		if (operation.equals("guessturn")) {
			String specId = params.get("specialist");
			if (specId == null || specId.isEmpty())
				return "";

			BBRSpecialistManager smgr = new BBRSpecialistManager();
			BBRSpecialist specialist = smgr.findById(Long.parseLong(specId));
			
			if (specialist != null) {
				BBRTurnManager tmgr = new BBRTurnManager();
				BBRTurn turn = tmgr.guessNextTurn(specialist);
				if (turn != null) {
					SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
					SimpleDateFormat tf = new SimpleDateFormat(BBRUtil.fullTimeFormat);
					String[] s = new String[3];
					s[0] = df.format(turn.getDate());
					s[1] = tf.format(turn.getStartTime());
					s[2] = tf.format(turn.getEndTime());
					return BBRUtil.gson().toJson(s);
				}
			}
		} else
		if (operation.equals("toggleAllTurns")) {
			context.set("turnsList", "all");
		} else
		if (operation.equals("toggleFutureTurns")) {
			context.set("turnsList", null);
		} else
		if (operation.equals("getTurns")) {
			String posId = params.get("posId");
			BBRPoS pos = null;
			BBRPoSManager pmgr = new BBRPoSManager();
			if (posId != null && posId != "")
				pos = pmgr.findById(Long.parseLong(posId));
			if (pos == null)
				return "";
			if (context.user.getRole() == BBRUserRole.ROLE_BBR_OWNER ||
				(context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN && context.user.getPos().getId() == pos.getId()) ||
				(context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN && context.user.getShop().getId() == pos.getShop().getId()))
			{
				String startDate = params.get("startDate");
				String endDate = params.get("endDate");
				try {
					BBRDataSet<BBRTurn> turns = manager.list("", "", 
								"specialist.pos.id = " + pos.getId() + " and date >= '"+startDate+"' and date <= '"+endDate+"'");
					return turns.toJson();
				} catch (Exception ex) {
					return "";
				}
			} else
				return "";
		} 
		
		return "";
	}

	@Override
	protected String getData(int pageNumber, int pageSize, 
			Hashtable<Integer, Hashtable<String, String>> fields,
			Hashtable<Integer, Hashtable<String, String>> sortingFields,
			BBRParams params, HttpServletRequest request, 
			HttpServletResponse response) {

		BBRContext context = BBRContext.getContext(request);
		String where = "";
		String tz = "";
		if (context.user != null) {
			if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN || context.user.getRole() == BBRUserRole.ROLE_POS_SPECIALIST)
				if (context.user.getPos() != null) {
					where = manager.wherePos(context.user.getPos().getId());
					tz = context.user.getPos().getTimeZone();
				}
			if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
				if (context.user.getShop() != null) {
					where = manager.whereShop(context.user.getShop().getId());
					tz = context.user.getShop().getTimeZone();
				}
		}
		
		String specId = (String)context.get("spec");
		if (specId != null && !specId.isEmpty()) {
			if (!where.isEmpty())
				where += " and ";
			where += "specialist.id = " + specId;
		}
			
		if (context.get("turnsList") == null) {
			SimpleDateFormat sdf = new SimpleDateFormat(BBRUtil.fullDateFormat);
			if (!where.isEmpty())
				where += " and ";
			where += "date >= '" +  sdf.format(BBRUtil.now(tz)) + "'";
		}
		
		return manager.list(pageNumber, pageSize, where, BBRContext.getOrderBy(sortingFields, fields)).toJson();
	}
}