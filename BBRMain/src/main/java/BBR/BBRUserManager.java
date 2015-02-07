package BBR;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

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
        user.setApproved(false);
        session.save(user);

        session.getTransaction().commit();
        return user;
    }
    
    @SuppressWarnings("unchecked")
	public BBRDataSet<BBRUser> listUsers() {
        boolean tr = BBRUtil.beginTran();
        List<BBRUser> list = BBRUtil.getSession().createQuery("from BBRUser").list();
        BBRUtil.commitTran(tr);
        return new BBRDataSet<BBRUser>(list);
    }
    
	public BBRUser findUserByEmail(String email) {
        boolean tr = BBRUtil.beginTran();
        BBRUser result = (BBRUser) BBRUtil.getSession().createQuery("from BBRUser as user where user.email = '" + email + "'").uniqueResult();
        BBRUtil.commitTran(tr);
        return result;
    }

	public BBRUser findUserById(Long id) {
        boolean tr = BBRUtil.beginTran();
        BBRUser result = (BBRUser) BBRUtil.getSession().createQuery("from BBRUser as user where user.id = '" + id.toString() + "'").uniqueResult();
        BBRUtil.commitTran(tr);
        return result;
    }
	
	public void deleteUser(BBRUser user){
        boolean tr = BBRUtil.beginTran();
        BBRUtil.getSession().delete(user);
        BBRUtil.commitTran(tr);
    }

	public void updateUser(BBRUser user) {
        boolean tr = BBRUtil.beginTran();
        BBRUtil.getSession().update(user);
        BBRUtil.commitTran(tr);	
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

	public static String generatePassword() {
		Random rand = new Random();
		int n = rand.nextInt(12) + 1;
		String password = "";
		
		for (int i = 1; i <= n; i++)
			password = password + (char)(rand.nextInt(94)+33); 
		return password;
	}

}
