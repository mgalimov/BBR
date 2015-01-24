package BBR;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.hibernate.Session;

public class BBRUserManager {

    public BBRUser createAndStoreUser(String email, String firstName, String lastName, String password) throws Exception {
        Session session = BBRUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        if (findUserByEmail(email) != null) {
        	throw new Exception(BBRErrors.ERR_DUPLICATE_EMAIL);
        }
        
        BBRUser user = new BBRUser();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEncodedPassword(encodePassword(password));
        session.save(user);

        session.getTransaction().commit();
        return user;
    }
    
    @SuppressWarnings("unchecked")
	public BBRDataSet<BBRUser> listUsers() {
        Session session = BBRUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<BBRUser> list = session.createQuery("from BBRUser").list();
        session.getTransaction().commit();
        return new BBRDataSet<BBRUser>(list);
    }
    
	public BBRUser findUserByEmail(String email) {
        Session session = BBRUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        BBRUser result = (BBRUser) session.createQuery("from BBRUser as user where user.email = '" + email + "'").uniqueResult();
        session.getTransaction().commit();
        return result;
    }
	
	public static String encodePassword(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			return digest.digest(password.getBytes("UTF-8")).toString();
		}catch (NoSuchAlgorithmException ex) {
	        throw new RuntimeException("No MD5 implementation? Really?");
	    } catch (UnsupportedEncodingException ex) {
	        throw new RuntimeException("No UTF-8 encoding? Really?");
	    }
	}

}
