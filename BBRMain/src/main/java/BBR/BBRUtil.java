package BBR;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static Gson buildGson() {
    	GsonBuilder b = new GsonBuilder();
    	Gson gson = b.registerTypeAdapterFactory(BBR.HibernateProxyTypeAdapter.FACTORY).serializeNulls().setDateFormat("yyyy-MM-dd HH:mm").create();
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
    
    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTime();
    }

    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

 }