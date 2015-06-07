package BBRAcc;

import org.hibernate.cfg.Configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import BBR.BBRUtil;

public class BBRAccReg {
	public final static int sessionIndex = getSessionIndex();
	public final static String defaultLanguage = "en_US";
	
	private static int getSessionIndex() {
		Configuration config = new Configuration();
		config.configure("hibernate.cfg.xml");
		
		return BBRUtil.buildSessionFactory(config);
	}
	
    public static final Gson gson = buildGson();

    private static Gson buildGson() {
    	GsonBuilder b = new GsonBuilder();
    	b.registerTypeAdapterFactory(BBR.HibernateProxyTypeAdapter.FACTORY);
    	b.serializeNulls().setDateFormat("yyyy-MM-dd HH:mm");
    	b.registerTypeAdapter(BBRPoS.class, new BBRPoSAdapter().nullSafe());
    	Gson gson = b.create();
		return gson;
	}

}
