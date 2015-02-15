package BBRCust;

import org.hibernate.cfg.Configuration;

import BBR.BBRUtil;

public class BBRCustReg {
	public final static int sessionIndex = getSessionIndex();
	
	private static int getSessionIndex() {
		Configuration config = new Configuration();
		config.configure("hibernate2.cfg.xml");
		
		return BBRUtil.buildSessionFactory(config);
	}
}
