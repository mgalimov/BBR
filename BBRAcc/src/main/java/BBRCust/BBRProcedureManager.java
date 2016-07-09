package BBRCust;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRDataSet;
import BBR.BBRUtil;
import BBRAcc.BBRPoS;
import BBRAcc.BBRShop;

public class BBRProcedureManager extends BBRDataManager<BBRProcedure>{
	
	public BBRProcedureManager() {
		super();
		classTitle = "Procedure";	
	}

	public void createAndStoreProcedure(String title, BBRPoS pos, float length, float price, int status) {
        boolean tr = BBRUtil.beginTran();
        Session session = BBRUtil.getSession();

        BBRProcedure proc = new BBRProcedure();
        proc.setTitle(title);
        proc.setPos(pos);
        proc.setLength(length);
        proc.setPrice(price);
        proc.setStatus(status);
        session.save(proc);

        BBRUtil.commitTran(tr);
    }

	public BBRDataSet<BBRProcedure> list(String query, String titleField, BBRPoS pos, BBRShop shop) {
		String where = "";
		
   		if (pos != null) {
   			if (where.equals(""))
   				where = " where";
   			else
   				where += " and";	
   			where += " pos.id = " + pos.getId();		
   		}

   		if (shop != null) {
   			if (where.equals(""))
   				where = " where";
   			else
   				where += " and";	
   			where += " pos.shop.id = " + shop.getId();		
   		}

		return (BBRDataSet<BBRProcedure>)list(query, titleField, where);
	}
	
	@Override
    public String whereShop(Long shopId) {
    	return "pos.shop.id = " + shopId;
    };
}
