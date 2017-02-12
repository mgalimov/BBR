import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRDataSet;
import BBRAcc.BBRPoS;
import BBRAcc.BBRPoSManager;
import BBRAcc.BBRShop;
import BBRAcc.BBRUser.BBRUserRole;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;
import BBRCust.BBRProcedure;
import BBRCust.BBRProcedure.BBRProcedureStatus;
import BBRCust.BBRProcedureManager;
import BBRCust.BBRSpecialist;
import BBRCust.BBRSpecialistManager;

@WebServlet("/BBRProcedures")
public class BBRProcedures extends BBRBasicServlet<BBRProcedure, BBRProcedureManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRProcedures() throws InstantiationException, IllegalAccessException {
        super(BBRProcedureManager.class);
    }

	@Override
	protected String create(BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		String title = params.get("title");
		String posId = params.get("pos");
		String group = params.get("procGroup");
		BBRPoSManager mgr = new BBRPoSManager();
		BBRPoS pos = mgr.findById(Long.parseLong(posId));
		BBRProcedure proc = null;
		if (pos != null) {						
			String length = params.get("length");
			float lengthFloat = (float) 0.5;
			if (!length.isEmpty())
				lengthFloat = Float.parseFloat(length);
			
			String price = params.get("price");
			float priceFloat = 0;
			if (!price.isEmpty())
				priceFloat = Float.parseFloat(price);
			
			String status = params.get("status");
			proc = manager.createAndStoreProcedure(title, pos, lengthFloat, priceFloat, (int) Long.parseLong(status), group);
		}
		return proc.getId().toString();
	}

	@Override
	protected BBRProcedure beforeUpdate(BBRProcedure proc, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		String title = params.get("title");
		String posId = params.get("pos");
		String group = params.get("procGroup");
		BBRPoSManager mgr = new BBRPoSManager();
		BBRPoS pos = mgr.findById(Long.parseLong(posId));
		if (pos != null) {						
			String length = params.get("length");
			float lengthFloat = (float) 0.5;
			if (!length.isEmpty())
				lengthFloat = Float.parseFloat(length);
			
			String price = params.get("price");
			float priceFloat = 0;
			if (!price.isEmpty())
				priceFloat = Float.parseFloat(price);
			String status = params.get("status");
			
			proc.setTitle(title);
	        proc.setPos(pos);
	        proc.setLength(lengthFloat);
	        proc.setPrice(priceFloat);
	        proc.setStatus((int) Long.parseLong(status));
	        proc.setProcGroup(group);
	        return proc;
		}
		return null;		
	}

	@Override
	protected String getReferenceData(String query, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		String constrains = params.get("constrains");
		
		BBRPoS pos = null;
		BBRShop shop = null;

		if (context.planningVisit != null)
			pos = context.planningVisit.getPos();
		else
			if (!constrains.equals("")) {
				BBRPoSManager pmgr = new BBRPoSManager();
				pos = pmgr.findById(Long.parseLong(constrains));
			}
		if (pos == null)
			if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN || context.user.getRole() == BBRUserRole.ROLE_POS_SPECIALIST )
				pos = context.user.getPos();
			else
				if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
					shop = context.user.getShop();
		
		return manager.list(query, manager.getTitleField(), pos, shop).toJson();
	}
	
	@Override
	protected String processOperation(String operation, BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (operation.equals("limitedreference")) {
			String constrains = params.get("constrains");
			String specId = params.get("specId");
			String query = params.get("q");
			String respText = "";
			
			BBRPoS pos = null;
			if (!constrains.equals("")) {
				BBRPoSManager pmgr = new BBRPoSManager();
				pos = pmgr.findById(Long.parseLong(constrains));
			}

			if (pos != null) {
				String where = " pos.id = " + pos.getId() + " and status = " + BBRProcedureStatus.PROCSTATUS_APPROVED;
				
				if (specId != null && !specId.isEmpty()) {
					BBRSpecialistManager smgr = new BBRSpecialistManager();
					BBRSpecialist s = smgr.findById(Long.parseLong(specId));
					String procIds = "";
					for (BBRProcedure pr : s.getProcedures())
						procIds += "," + pr.getId();
					procIds = procIds.substring(1);
					where += " and id in (" + procIds + ")";
				}
				
				try {
					respText = manager.list(query, "procGroup, " + manager.getTitleField(), where).toJson();
				} catch (Exception ex) {
					respText = "";
				}
			}
			
			if (respText.isEmpty()) {
				BBRDataSet<BBRProcedure> ds = new BBRDataSet<BBRProcedure>(null);
				respText = ds.toJson();
			}
			
			return respText;
		}
		return "";
	};
}
