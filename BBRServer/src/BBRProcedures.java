import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBRAcc.BBRPoS;
import BBRAcc.BBRPoSManager;
import BBRClientApp.BBRContext;
import BBRCust.BBRProcedure;
import BBRCust.BBRProcedureManager;

@WebServlet("/BBRProcedures")
public class BBRProcedures extends BBRBasicServlet<BBRProcedure, BBRProcedureManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRProcedures() throws InstantiationException, IllegalAccessException {
        super(BBRProcedureManager.class);
    }

	@Override
	String create(BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		String title = params.get("title");
		String posId = params.get("pos");
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
			
			String currency = params.get("currency");
			String status = params.get("status");
			manager.createAndStoreProcedure(title, pos, lengthFloat, priceFloat, currency, (int) Long.parseLong(status));
		}
		return "";
	}

	@Override
	protected BBRProcedure beforeUpdate(BBRProcedure proc, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		String title = params.get("title");
		String posId = params.get("pos");
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
			String currency = params.get("currency");
			String status = params.get("status");
			
			proc.setTitle(title);
	        proc.setPos(pos);
	        proc.setLength(lengthFloat);
	        proc.setPrice(priceFloat);
	        proc.setCurrency(currency);
	        proc.setStatus((int) Long.parseLong(status));
	        return proc;
		}
		return null;		
	}

	@Override
	protected String getReferenceData(String query, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		BBRContext context = BBRContext.getContext(request);
		return manager.list(query, manager.getTitleField(), context.planningVisit).toJson();
	}


}
