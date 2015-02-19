package BBR;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.*;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.cfg.Configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BBRUtil {
    private static final List<SessionFactory> sessionFactory = new ArrayList<SessionFactory>();
    private static int lastIndex = 0;
    public static final Gson gson = buildGson();

    public static int buildSessionFactory(Configuration configuration) {
        try {
        	StandardServiceRegistryBuilder regBuilder = new StandardServiceRegistryBuilder();
        	ServiceRegistry serviceRegistry = regBuilder.applySettings(configuration.getProperties()).build();
        	
        	sessionFactory.add(lastIndex, configuration.buildSessionFactory(serviceRegistry));
    		return lastIndex++;
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static Gson buildGson() {
    	GsonBuilder b = new GsonBuilder();
    	b.registerTypeAdapterFactory(BBR.HibernateProxyTypeAdapter.FACTORY);
    	Gson gson = b.create();
		return gson;
	}

	public static SessionFactory getSessionFactory(int index) {
        return sessionFactory.get(index);
    }
    
    public static Session getSession (int index) {
    	return sessionFactory.get(index).getCurrentSession();
    }
    
    public static boolean beginTran(int index) {
        Session session = getSession(index);
        if (!session.getTransaction().isActive()) {
        	session.beginTransaction();
        	return true;
        } else
        	return false;
    }

    public static void commitTran(int index, boolean transactionStarted) {
        Session session = getSession(index);
        if (transactionStarted)
        	session.getTransaction().commit();
    }

    @SuppressWarnings("rawtypes")
	public static Class getGenericParameterClass(Class actualClass, int parameterIndex) {
    //	http://habrahabr.ru/post/66593/
        return (Class) ((ParameterizedType) actualClass.getGenericSuperclass()).getActualTypeArguments()[parameterIndex];
    }
}