package BBRCust;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRDataSet;
import BBR.BBRUtil;
import BBRAcc.BBRPoS;
import BBRAcc.BBRUser;
import BBRCust.BBRCustReg;

public class BBRSpecialistManager extends BBRDataManager<BBRSpecialist>{
	
	public BBRSpecialistManager() {
		super();
		sessionIndex = BBRCustReg.sessionIndex;
		titleField = "name";
		classTitle = "Specialist";	
	}

	public void createAndStoreSpecialist(String name, String position, BBRUser user, BBRPoS pos) {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);

        BBRSpecialist spec = new BBRSpecialist();
        spec.setName(name);
        spec.setPosition(position);
        spec.setUser(user);
        spec.setPos(pos);
        session.save(spec);

        BBRUtil.commitTran(sessionIndex, tr);
    }
	
    @SuppressWarnings({ "unchecked", "unused" })
	public BBRDataSet<BBRSpecialist> list(String queryTerm, String sortBy, BBRPoS pos) {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        
        Session session = BBRUtil.getSession(sessionIndex);
   		String orderBy = " order by " + sortBy;
   		String where = "";
   		
   		if (queryTerm != null && !queryTerm.equals("")) {
   			queryTerm.replaceAll("\\s", "%");
   			where = " where spec.name like '%" + queryTerm + "%'";
   		}
   		
   		if (pos != null) {
   			if (where.equals(""))
   				where = " where";
   			else
   				where += " and";	
   			where += " spec.pos.id = " + pos.getId();		
   		}
   			
        Long count = (Long)session.createQuery("Select count(*) from " + typeName + " spec " + where).uniqueResult();
        Query query = session.createQuery("from " + typeName + " spec " + where + orderBy);
        
        if (maxRowsToReturn > 0)
        	query.setMaxResults(maxRowsToReturn);
        
        List<BBRSpecialist> list = query.list();
        BBRUtil.commitTran(sessionIndex, tr);

        return new BBRDataSet<BBRSpecialist>(list, count);
    }
    
    @SuppressWarnings({ "unchecked", "unused" })
	public BBRDataSet<BBRSpecialist> list(String queryTerm, String sortBy) {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        
        Session session = BBRUtil.getSession(sessionIndex);
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
        BBRUtil.commitTran(sessionIndex, tr);

        return new BBRDataSet<BBRSpecialist>(list, count);
    }

}
