package BBR;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.*;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.cfg.Configuration;

public class BBRUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
        	Configuration configuration = new Configuration();
        	configuration.configure("hibernate.cfg.xml");
        	StandardServiceRegistryBuilder regBuilder = new StandardServiceRegistryBuilder();
        	ServiceRegistry serviceRegistry = regBuilder.applySettings(configuration.getProperties()).build();
        	
        	return configuration.buildSessionFactory(serviceRegistry);
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}