package BBR;

import java.util.List;

import org.hibernate.Session;

public class BBRUserManager {

    public void createAndStoreUser(String email, String firstName, String lastName, BBRShop shop) {
        Session session = BBRUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        if (findUserByEmail(email) != null) {
        	throw new Error(BBRErrors.ERR_DUPLICATE_EMAIL);
        }
        
        BBRUser user = new BBRUser();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setShop(shop);
        session.save(user);

        session.getTransaction().commit();
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

}
