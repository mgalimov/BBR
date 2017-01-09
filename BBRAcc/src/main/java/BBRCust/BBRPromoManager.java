package BBRCust;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRDataSet;
import BBR.BBRErrors;
import BBR.BBRUtil;
import BBRAcc.BBRPoS;
import BBRAcc.BBRShop;
import BBRCust.BBRPromo.BBRPromoStatus;
import BBRCust.BBRPromo.BBRPromoType;

public class BBRPromoManager extends BBRDataManager<BBRPromo>{
	
	public BBRPromoManager() {
		super();
		classTitle = "Promo";	
	}
	
	public void checkParams(BBRPromo promo) throws Exception {
		if (promo.getShop() == null)
			throw new Exception(BBRErrors.ERR_SHOP_MUST_BE_SPECIFIED);
		if (promo.getPoses() == null)
			throw new Exception(BBRErrors.ERR_POS_MUST_BE_SPECIFIED);
		if (promo.getPromoType() == BBRPromoType.PROMOTYPE_FREE_VISIT && promo.getVisitsNumber() <= 1)
			throw new Exception(BBRErrors.ERR_PROMO_VISITS_NUMBER_NOT_SPECIFIED);
		if (promo.getPromoType() == BBRPromoType.PROMOTYPE_DISCOUNT && (promo.getProcedures() == null && promo.getSources() == null))
			throw new Exception(BBRErrors.ERR_PROMO_PROCEDURES_OR_SOURCES_NOT_SPECIFIED);
		if (promo.getPromoType() == BBRPromoType.PROMOTYPE_DISCOUNT && promo.getDiscount() <= 0.00001)
			throw new Exception(BBRErrors.ERR_PROMO_DISCOUNT_NOT_SPECIFIED);
	}

	public void createAndStorePromo(BBRPromo promo) throws Exception {
		checkParams(promo);
        boolean tr = BBRUtil.beginTran();
        Session session = BBRUtil.getSession();
        session.save(promo);
        BBRUtil.commitTran(tr);
	}
	
	public void createAndStorePromo(String title, BBRShop shop, Set<BBRPoS> poses, 
									int promoType, Date startDate, Date endDate, 
									Set<BBRProcedure> procedures, Set<Integer> sources, float discount, 
									int visitsNumber, int status) throws Exception {
        BBRPromo promo = new BBRPromo();
        promo.setTitle(title);
        promo.setShop(shop);
        promo.setPoses(poses);
        promo.setPromoType(promoType);
        promo.setStartDate(startDate);
        promo.setEndDate(endDate);
        promo.setDiscount(discount);
        promo.setSources(sources);
        promo.setProcedures(procedures);
        promo.setStatus(status);
        promo.setVisitsNumber(visitsNumber);
        
        createAndStorePromo(promo);
    }

	public BBRDataSet<BBRPromo> list(String query, String titleField, BBRPoS pos, BBRShop shop) {
		String where = "";
		
   		if (pos != null) {
   			if (where.isEmpty())
   				where = " where";
   			else
   				where += " and ";	
   			where += pos.getId() + " member of poses";		
   		}
   		if (shop != null) {
   			if (where.isEmpty())
   				where = " where ";
   			else
   				where += " and";	
   			where += " shop.id = " + shop.getId();		
   		}

		return (BBRDataSet<BBRPromo>)list(query, titleField, where);
	}
	
	@Override
    public String whereShop(Long shopId) {
    	return "shop.id = " + shopId;
    };
    
    public String wherePos(Long posId) {
    	return posId + " member of poses";
    }

	public BBRPromo findFreeVisitPromo(Long posId, Date date) {
		BBRPromo promo = null;
		
		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
		String where = "startDate <= '" + df.format(date) + "'" + 
				  " and coalesce(endDate, '" + df.format(date) + "') >= '" + df.format(date) + "'" + 
				  " and promoType = " + BBRPromoType.PROMOTYPE_FREE_VISIT +
				  " and status = " + BBRPromoStatus.PROMOSTATUS_APPROVED + 
				  " and " + posId + " member of poses";
		BBRDataSet<BBRPromo> l = (BBRDataSet<BBRPromo>)list("", titleField, where);
		
		if (l != null && l.data != null && l.data.size() > 0) {
			for (BBRPromo p : l.data) {
				if (promo == null)
					promo = p;
				else
					if (promo.getVisitsNumber() > p.getVisitsNumber())
						promo = p;
			}
		}
			
		return promo;
	}

	public boolean isPrizeVisit(BBRPromo promo, Long visitsNumber) {
		if (promo != null 
				&& promo.getStatus() == BBRPromoStatus.PROMOSTATUS_APPROVED 
				&& promo.getPromoType() == BBRPromoType.PROMOTYPE_FREE_VISIT) {
			Integer p = promo.getVisitsNumber(); 
			if (p != null && p > 0 && visitsNumber > 0 && visitsNumber % p == 0)
				return true;
		}
		return false;
	}
    
	public BBRPromo findBestApplicablePromo(BBRVisit visit) {
		if (visit == null)
			return null;
		
		Date date = visit.getRealTime();
		if (date == null)
			date = visit.getTimeScheduled();
		
		if (date == null)
			return null;
		
		BBRPromo promo = findFreeVisitPromo(visit.getPos().getId(), date);

		if (promo != null) {
			BBRVisitManager vmgr = new BBRVisitManager();
			Long visitsNumber = vmgr.getVisitsNumber(date, visit.getPos().getId(), null);
			if (isPrizeVisit(promo, visitsNumber))
				return promo;
			promo = null;
		}
		
		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
		String where = "startDate <= '" + df.format(date) + "'" + 
				  " and coalesce(endDate, '" + df.format(date) + "') >= '" + df.format(date) + "'" + 
				  " and status = " + BBRPromoStatus.PROMOSTATUS_APPROVED + 
				  " and promoType = " + BBRPromoType.PROMOTYPE_DISCOUNT +
				  " and " + visit.getPos().getId() + " member of poses";
		BBRDataSet<BBRPromo> l = (BBRDataSet<BBRPromo>)list("", titleField, where);
		
		if (l != null && l.data != null && l.data.size() > 0) {
			for (BBRPromo p : l.data) {
				boolean yes = false;
				
				if (p.getProcedures() != null && !p.getProcedures().isEmpty()) {
					yes = true;
					for (BBRProcedure proc : p.getProcedures()) {
						boolean localyes = false;
						for (BBRProcedure procToCompare : visit.getProcedures()) {
							if (proc.getId() == procToCompare.getId()) {
								localyes = true;
								break;
							}
						}
						if (!localyes && visit.getProcedure() != null && proc.getId() == visit.getProcedure().getId())
							localyes = true;
						
						if (!localyes) {
							yes = false;
							break;
						}
					}
				}
				
				if (yes && p.getSources() != null && !p.getSources().isEmpty()) {
					boolean localyes = false;
					for (int src : p.getSources()) {
						if (src == visit.getSource()) {
							localyes = true;
							break;
						}
					}
					yes = localyes; 
				}
				
				if (yes)
					if (promo == null)
						promo = p;
					else
						if (promo.getDiscount() < p.getDiscount())
							promo = p;
			}
		}
		
		return promo;
	}

}
