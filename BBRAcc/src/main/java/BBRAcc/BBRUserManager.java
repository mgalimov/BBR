package BBRAcc;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.hibernate.Session;

import BBR.BBRDataManager;
import BBR.BBRErrors;
import BBR.BBRUtil;

public class BBRUserManager extends BBRDataManager<BBRUser> {

	public BBRUserManager() {
		super();
		sessionIndex = BBRAccReg.sessionIndex;
		titleField = "lastName";
		classTitle = "User";	
	}

	public BBRUser createAndStoreUser(String email, String firstName, String lastName, 
									  String password, int role, BBRShop shop, BBRPoS pos) throws Exception {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        Session session = BBRUtil.getSession(sessionIndex);
        
        if (findUserByEmail(email) != null) {
        	throw new Exception(BBRErrors.ERR_DUPLICATE_EMAIL);
        }
        
        BBRUser user = new BBRUser();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEncodedPassword(encodePassword(password));
        user.setApproved(false);
        user.setRole(role);
        user.setShop(shop);
        user.setPos(pos);
        session.save(user);

        BBRUtil.commitTran(sessionIndex, tr);
        return user;
    }
    

	public BBRUser findUserByEmail(String email) {
        boolean tr = BBRUtil.beginTran(sessionIndex);
        BBRUser result = (BBRUser) BBRUtil.getSession(sessionIndex).createQuery("from BBRUser as user where user.email = '" + email + "'").uniqueResult();
        BBRUtil.commitTran(sessionIndex, tr);
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
	        throw new RuntimeException("No MD5 implementation? Really?");
	    } catch (UnsupportedEncodingException ex) {
	        throw new RuntimeException("No UTF-8 encoding? Really?");
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

}
