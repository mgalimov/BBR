package BBRCust;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRUtil;
import BBRAcc.BBRPoS;

public class BBRProcedureGroupManager extends BBRDataManager<BBRProcedureGroup>{
	
	public BBRProcedureGroupManager() {
		super();
		classTitle = "ProcedureGroup";	
	}

	public BBRProcedureGroup create(String title, String description, BBRPoS pos) {
        boolean tr = BBRUtil.beginTran();
        Session session = BBRUtil.getSession();

        BBRProcedureGroup group = new BBRProcedureGroup();
        group.setTitle(title);
        group.setDescription(description);
        group.setPos(pos);
        session.save(group);

        BBRUtil.commitTran(tr);
        
        return group;
    }

	@Override
    public String whereShop(Long shopId) {
    	return "pos.shop.id = " + shopId;
    };
}
