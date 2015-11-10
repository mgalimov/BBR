import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BBR.BBRErrors;
import BBR.BBRUtil;
import BBRAcc.BBRService;
import BBRAcc.BBRServiceManager;
import BBRAcc.BBRServicePrice;
import BBRAcc.BBRServicePriceManager;
import BBRClientApp.BBRContext;
import BBRClientApp.BBRParams;

@WebServlet("/BBRServicePrices")
public class BBRServicePrices extends BBRBasicServlet<BBRServicePrice, BBRServicePriceManager> {
	private static final long serialVersionUID = 1L;
       
    public BBRServicePrices() throws InstantiationException, IllegalAccessException {
        super(BBRServicePriceManager.class);
    }

	@Override
	protected String create(BBRParams params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			Long serviceId = Long.parseLong(params.get("service"));
			BBRServiceManager smgr = new BBRServiceManager();
			BBRService service = smgr.findById(serviceId);

			String sd = params.get("startDate");
			Date startDate = null;
			if (!sd.isEmpty()) {
				SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
				startDate = df.parse(sd);
			}

			String ed = params.get("endDate");
			Date endDate = null;
			if (!ed.isEmpty()) {
				SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
				endDate = df.parse(ed);
			}

			String country = params.get("country");
			String currency = params.get("country");
			Float price = Float.parseFloat(params.get("price"));
			Float creditLimit = Float.parseFloat(params.get("creditLimit"));
			
			manager.createAndStoreServicePrice(service, country, startDate, endDate, price, currency, creditLimit);
		} catch (Exception ex) {
			throw new Exception(BBRErrors.ERR_WRONG_INPUT_FORMAT);
		}
		return "";
	}

	@Override
	protected BBRServicePrice beforeUpdate(BBRServicePrice price, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		try {
			String ed = params.get("endDate");
			Date endDate = new Date();
			if (!ed.isEmpty()) {
				SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
				endDate = df.parse(ed);
			}
			price.setEndDate(endDate);
			return price;
		} catch (Exception ex) {
			return null; 
		}		
	}

	@Override
	protected String getReferenceData(String query, BBRParams params, HttpServletRequest request, HttpServletResponse response) {
		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
		String d = df.format(new Date());
		String where = "(startDate <= '" + d + "' and (endDate is null or endDate >= '" + d + "'))";

		BBRContext context = BBRContext.getContext(request);
		BBRService service = (BBRService)context.get("service");
		if (service != null)
			where += " and service.id = " + service.getId(); 
		
		return manager.list(query, manager.getTitleField(), where).toJson();
	}
}
