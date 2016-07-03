package BBRAcc;

import org.hibernate.cfg.Configuration;
import BBR.BBRUtil;

public class BBRAccReg {
	public final static int sessionIndex = getSessionIndex();
	public final static String defaultLanguage = "en_US";
	
	private static int getSessionIndex() {
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
		
		return BBRUtil.buildSessionFactory(config);
	}
}
