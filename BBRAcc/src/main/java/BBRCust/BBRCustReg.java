package BBRCust;

import BBRAcc.BBRAccReg;

public class BBRCustReg {
	public final static int sessionIndex = getSessionIndex();
	
	private static int getSessionIndex() {
		/*
			Configuration config = new Configuration();
			config.configure("hibernate2.cfg.xml");
		
			return BBRUtil.buildSessionFactory(config);
		 */

		return BBRAccReg.sessionIndex;
	}
}
