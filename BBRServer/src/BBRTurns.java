import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRErrors;
import BBR.BBRUtil;
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
		String startTime = params.get("startTime");
		String endTime = params.get("endTime");

		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat);
		
		BBRSpecialistManager smgr = new BBRSpecialistManager();
		BBRSpecialist spec = smgr.findById(Long.parseLong(specId)); 
		
		Long id = Long.parseLong(manager.createAndStoreTurn(
					spec, 
					df.parse(startTime), 
					df.parse(endTime)));
			
		if (id == null)
			throw new Exception(BBRErrors.ERR_TURN_CROSSES);
		
		return "";
	}

	@Override
	protected BBRTurn beforeUpdate(BBRTurn turn, BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String specId = params.get("specialist");
		String startTime = params.get("startTime");
		String endTime = params.get("endTime");

		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat);
		Date st = df.parse(startTime);
		Date et = df.parse(endTime);
		
		BBRSpecialistManager smgr = new BBRSpecialistManager();
		BBRSpecialist spec = smgr.findById(Long.parseLong(specId)); 
		
		if (manager.checkNoCross(spec, st, et, turn.getId())) {
			turn.setSpecialist(spec);
			turn.setStartTime(st);
			turn.setEndTime(et);
			return turn;
		} else
			return null;
	}
}