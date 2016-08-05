package BBR;

import java.lang.reflect.ParameterizedType;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.*;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.cfg.Configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BBRUtil {
    private static SessionFactory sessionFactory = buildSessionFactory();
    public static final GsonBuilder gsonBuilder = createGsonBuilder();
	public static final String recordDivider = "@@";

	public static final String fullDateFormat = "yyyy-MM-dd";
	public static final String fullTimeFormat = "HH:mm";
	public static final String fullDateTimeFormat = "yyyy-MM-dd HH:mm";
	public static final String fullDateTimeFormatWithSecs = "yyyy-MM-dd HH:mm:ss";
	
	public static final String visualTitleDelimiter = " &#8212; ";

    public static SessionFactory buildSessionFactory() {
    	SessionFactory sessionFactory = null;
    	
        for (int i = 0; i <= 5 && sessionFactory == null; i++) {
        	try {
        		Configuration config = new Configuration();
        		config.configure("hibernate.cfg.xml");
        		String host = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
        		if (host != null && !host.equals("")) {
        			String port = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
        			if (port != null && !port.equals(""))
        				port = ":" + port;
        			else
        				port = "";
        			String user = System.getenv("OPENSHIFT_MYSQL_DB_USERNAME");
        			String pass = System.getenv("OPENSHIFT_MYSQL_DB_PASSWORD");
        			config.setProperty("hibernate.connection.url", "jdbc:mysql://" + host + port + "/jb");
        			config.setProperty("hibernate.connection.username", user);
        			config.setProperty("hibernate.connection.password", pass);
        		}
        		
            	StandardServiceRegistryBuilder regBuilder = new StandardServiceRegistryBuilder();
            	ServiceRegistry serviceRegistry = regBuilder.applySettings(config.getProperties()).build();
            	
            	sessionFactory = config.buildSessionFactory(serviceRegistry);
            }
            catch (Throwable ex) {
            	try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					throw new ExceptionInInitializerError(ex);
				}
            }
        }
    	return sessionFactory;

    }

    private static GsonBuilder createGsonBuilder() {
    	GsonBuilder b = new GsonBuilder();
    	b.registerTypeAdapterFactory(BBR.HibernateProxyTypeAdapter.FACTORY);
    	b.registerTypeHierarchyAdapter(BBRDataElement.class, new BBRDataElementSerializer());
    	// b.serializeNulls();
    	b.setDateFormat("yyyy-MM-dd HH:mm");
		return b;
	}

	public static Gson gson() {
		return gsonBuilder.create();
	}

	public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static Session getSession() {
    	return sessionFactory.getCurrentSession();
    }
    
    public static boolean beginTran() {
        Session session = getSession();
        if (!session.getTransaction().isActive()) {
        	session.beginTransaction();
        	return true;
        } else
        	return false;
    }

    public static void commitTran(boolean transactionStarted) {
        Session session = getSession();
        if (transactionStarted)
        	session.getTransaction().commit();
    }

    public static void rollbackTran(boolean transactionStarted) {
        Session session = getSession();
        if (transactionStarted)
        	session.getTransaction().rollback();
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