package BBRAcc;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRErrors;
import BBR.BBRUtil;
import BBRAcc.BBRShop.BBRShopStatus;

public class BBRUserManager extends BBRDataManager<BBRUser> {

	public BBRUserManager() {
		super();
		titleField = "lastName";
		classTitle = "User";	
	}

	public BBRUser create(String email, String firstName, String lastName, 
									  String password, int role, BBRShop shop, BBRPoS pos) throws Exception {
        boolean tr = BBRUtil.beginTran();
        Session session = BBRUtil.getSession();
        
        if (findUserByEmail(email) != null) {
        	throw new Exception(BBRErrors.ERR_DUPLICATE_EMAIL);
        }
        
        BBRUser user = new BBRUser();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEncodedPassword(encodePassword(password));
        user.setApproved(true);
        user.setRole(role);
        user.setShop(shop);
        user.setPos(pos);
        user.setLanguage(BBRAccReg.defaultLanguage);
        user.setApprovalDate(new Date());
        
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest((firstName+lastName+user.getApprovalDate().toString()+Math.random()).getBytes());
        user.setApprovalCode(digest.toString());
        
        session.save(user);

        BBRUtil.commitTran(tr);
        return user;
    }
    
	public BBRUser findUserByEmail(String email) {
        boolean tr = BBRUtil.beginTran();
        BBRUser result = (BBRUser) BBRUtil.getSession()
        			.createQuery("from BBRUser as user where user.email = '" + email + "'")
        			.uniqueResult();
        BBRUtil.commitTran(tr);
        return result;
    }

	public static String encodePassword(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(password.getBytes("UTF-8"));
			byte msg[] = digest.digest();
			
			StringBuffer hexString = new StringBuffer();
	        for (int i=0; i < msg.length; i++) {
	            hexString.append(Integer.toHexString(0xFF & msg[i]));
	        }
	        
	        return hexString.toString();
		} catch (NoSuchAlgorithmException ex) {
	        throw new RuntimeException(BBRErrors.ERR_NOT_IMPLEMENTED);
	    } catch (UnsupportedEncodingException ex) {
	        throw new RuntimeException(BBRErrors.ERR_NOT_IMPLEMENTED);
	    }
	}

	public static String generatePassword() {
		Random rand = new Random();
		int n = rand.nextInt(6) + 8;
		String password = "";
		
		for (int i = 1; i <= n; i++)
			password = password + (char)(rand.nextInt(94)+33); 
		return password;
	}

    public String wherePos(Long posId) {
    	return "pos.id = " + posId;
    };
    
    public String whereShop(Long shopId) {
    	return "(shop.id = " + shopId + " or pos.id = " + shopId + ")";
    };
    
    public void disapprove(BBRUser user) {
        boolean tr = BBRUtil.beginTran();

        try {
	    	BBRShop shop = user.getShop();
	    	if (shop != null) {
	    		shop.setStatus(BBRShopStatus.SHOPSTATUS_INACTIVE);
	    		BBRShopManager smgr = new BBRShopManager();
	    		smgr.delete(shop);
	    	}
	    	delete(user);
        } catch (Exception ex) {
        }

        BBRUtil.commitTran(tr);
    }

    public BBRUser approve(BBRUser user) {
        boolean tr = BBRUtil.beginTran();
        Session session = BBRUtil.getSession();

    	user.setApprovalDate(null);
        session.save(user);

        BBRUtil.commitTran(tr);
    	return user;
    }

    public BBRUser checkUserForApproval(BBRUser user, String code) {
    	if (code.equals(user.getApprovalCode()))
    		return approve(user);
    	else 
    		disapprove(user);
    	return null;
    }
}
