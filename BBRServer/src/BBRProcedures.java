import java.util.Hashtable;

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
import BBRCust.BBRProcedureGroup;
import BBRCust.BBRProcedureGroupManager;
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
		String groupId = params.get("procedureGroup");
		BBRProcedureGroupManager mgr = new BBRProcedureGroupManager();
		BBRProcedureGroup group = mgr.findById(Long.parseLong(groupId));
		
		BBRProcedure proc = null;
		if (group != null && group.getPos() != null) {						
			String length = params.get("length");
			float lengthFloat = (float) 0.5;
			if (!length.isEmpty())
				lengthFloat = Float.parseFloat(length);
			
			String price = params.get("price");
			float priceFloat = 0;
			if (!price.isEmpty())
				priceFloat = Float.parseFloat(price);
			
			String status = params.get("status");
			proc = manager.create(title, lengthFloat, priceFloat, (int) Long.parseLong(status), group);
		}
		return proc.getId().toString();
	}

	@Override
	protected BBRProcedure beforeUpdate(BBRProcedure proc, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		String title = params.get("title");
		String groupId = params.get("procedureGroup");
		BBRProcedureGroupManager mgr = new BBRProcedureGroupManager();
		BBRProcedureGroup group = mgr.findById(Long.parseLong(groupId));
		
		if (group != null && group.getPos() != null) {						
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
	        proc.setLength(lengthFloat);
	        proc.setPrice(priceFloat);
	        proc.setStatus((int) Long.parseLong(status));
	        proc.setProcedureGroup(group);
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
				String where = " procedureGroup.pos.id = " + pos.getId() + " and status = " + BBRProcedureStatus.PROCSTATUS_APPROVED;
				
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
					respText = manager.list(query, "procedureGroup.title, " + manager.getTitleField(), where).toJson();
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
	
	protected String getData(int pageNumber, int pageSize,
			Hashtable<Integer, Hashtable<String, String>> fields,
			Hashtable<Integer, Hashtable<String, String>> sortingFields,
			BBRParams params, HttpServletRequest request,
			HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		String where = "";
		if (context.user != null) {
			if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN
					|| context.user.getRole() == BBRUserRole.ROLE_POS_SPECIALIST)
				if (context.user.getPos() != null)
					where = manager.wherePos(context.user.getPos().getId());
			if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
				if (context.user.getShop() != null)
					where = manager.whereShop(context.user.getShop().getId());
			if (context.user.getRole() == BBRUserRole.ROLE_BBR_OWNER)
				if (context.filterPoS != null)
					where = manager.wherePos(context.filterPoS.getId());
				else if (context.filterShop != null)
					where = manager.whereShop(context.filterShop.getId());
		}

		@SuppressWarnings("unchecked")
		Hashtable<String, String> filter = (Hashtable<String, String>)context.get("filter");
		String groupId = "";
		if (filter != null)
			groupId = filter.get("procedureGroup"); 
		if (groupId != null && !groupId.isEmpty()) {
			where += " and procedureGroup.id = " + groupId; 
		}
		String status = "";
		if (filter != null)
			status = filter.get("status"); 
		if (status != null && !status.isEmpty()) {
			where += " and status = " + status; 
		}
		
		return manager.list(pageNumber, pageSize, where,
				BBRContext.getOrderBy(sortingFields, fields)).toJson();
	}

}
