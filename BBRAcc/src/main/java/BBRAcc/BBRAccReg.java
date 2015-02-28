package BBRAcc;

import org.hibernate.cfg.Configuration;
import BBR.BBRUtil;

public class BBRAccReg {
	public final static int sessionIndex = getSessionIndex();
	
	private static int getSessionIndex() {
		Configuration config = new Configuration();
		config.configure("hibernate.cfg.xml");
		
		return BBRUtil.buildSessionFactory(config);
	}
}
