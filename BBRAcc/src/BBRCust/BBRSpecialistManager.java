package BBRCust;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRDataSet;
import BBR.BBRUtil;
import BBRAcc.BBRPoS;
import BBRAcc.BBRShop;
import BBRAcc.BBRUser;
import BBRCust.BBRSpecialist.BBRSpecialistState;

public class BBRSpecialistManager extends BBRDataManager<BBRSpecialist>{
	
	public BBRSpecialistManager() {
		super();
		titleField = "name";
		classTitle = "Specialist";	
	}

	public BBRSpecialist create(String name, String position, Float dailyAmount, Float procedurePercent, 
										 BBRUser user, BBRPoS pos, int status,
										 Date startWorkHour, Date endWorkHour, Set<BBRProcedure> procedures,
										 Set<BBRProcedureGroup> procedureGroups) {
        boolean tr = BBRUtil.beginTran();
        Session session = BBRUtil.getSession();

        BBRSpecialist spec = new BBRSpecialist();
        spec.setName(name);
        spec.setPosition(position);
        spec.setDailyAmount(dailyAmount);
        spec.setProcedurePercent(procedurePercent);
        spec.setUser(user);
        spec.setPos(pos);
        spec.setStatus(status);
        spec.setStartWorkHour(startWorkHour);
        spec.setEndWorkHour(endWorkHour);
        spec.setProcedures(procedures);
        spec.setProcedureGroups(procedureGroups);
        session.save(spec);

        BBRUtil.commitTran(tr);
        
        return spec;
    }
	
    @SuppressWarnings({ "unchecked", "unused" })
	public BBRDataSet<BBRSpecialist> list(String queryTerm, String sortBy, BBRPoS pos, BBRShop shop) {
        return list(queryTerm, sortBy, pos, shop, false);
    }

    @SuppressWarnings({ "unchecked", "unused" })
	public BBRDataSet<BBRSpecialist> list(String queryTerm, String sortBy, BBRPoS pos, BBRShop shop, boolean onlyActive) {
        boolean tr = BBRUtil.beginTran();
        
        Session session = BBRUtil.getSession();
   		String orderBy = " order by " + sortBy;
   		String where = "";
   		
   		if (queryTerm != null && !queryTerm.equals("")) {
   			queryTerm.replaceAll("\\s", "%");
   			where = " where spec.name like '%" + queryTerm + "%'";
   		}
   		
   		if (onlyActive) {
   			if (where.equals(""))
   				where = " where";
   			else
   				where += " and";	
   			where += " spec.status = " + BBRSpecialistState.SPECSTATE_ACTIVE;
   		}

   		if (pos != null) {
   			if (where.equals(""))
   				where = " where";
   			else
   				where += " and";	
   			where += " spec.pos.id = " + pos.getId();		
   		}
   			
   		if (shop != null) {
   			if (where.equals(""))
   				where = " where";
   			else
   				where += " and";	
   			where += " spec.pos.shop.id = " + shop.getId();		
   		}
   		
        Long count = (Long)session.createQuery("Select count(*) from " + typeName + " spec " + where).uniqueResult();
        Query query = session.createQuery("from " + typeName + " spec " + where + orderBy);
        
        if (maxRowsToReturn > 0)
        	query.setMaxResults(maxRowsToReturn);
        
        List<BBRSpecialist> list = query.list();
        BBRUtil.commitTran(tr);

        return new BBRDataSet<BBRSpecialist>(list, count);
    }

    
    @SuppressWarnings({ "unchecked", "unused" })
	public BBRDataSet<BBRSpecialist> list(String queryTerm, String sortBy) {
        boolean tr = BBRUtil.beginTran();
        
        Session session = BBRUtil.getSession();
   		String orderBy = " order by " + sortBy;
   		String where = "";
   		
   		if (queryTerm != null && !queryTerm.equals("")) {
   			queryTerm.replaceAll("\\s", "%");
   			where = " where spec.name like '%" + queryTerm + "%'";
   		}
   		
        Long count = (Long)session.createQuery("Select count(*) from " + typeName + " spec " + where).uniqueResult();
        Query query = session.createQuery("from " + typeName + " spec " + where + orderBy);
        
        if (maxRowsToReturn > 0)
        	query.setMaxResults(maxRowsToReturn);
        
        List<BBRSpecialist> list = query.list();
        BBRUtil.commitTran(tr);

        return new BBRDataSet<BBRSpecialist>(list, count);
    }

	@Override
    public String whereShop(Long shopId) {
    	return "pos.shop.id = " + shopId;
    };
    
    public BBRDataSet<BBRSpecialist> listAvailableSpecialists(BBRPoS pos) {
    	if (pos == null) return null;
    	String where = "pos.id = " + pos.getId();
    	where += " and status = " + BBRSpecialistState.SPECSTATE_ACTIVE;
    	
    	return list(-1, 0, where, "name ASC");
    }
}
