package BBRCust;

import BBR.BBRUtil;
import BBRAcc.BBRAccReg;

public class BBRCustReg {
	public final static int sessionIndex = getSessionIndex();
	
	private static int getSessionIndex() {
		/*
			Configuration config = new Configuration();
			config.configure("hibernate2.cfg.xml");
		
			return BBRUtil.buildSessionFactory(config);
		 */

		BBRUtil.gsonBuilder.registerTypeAdapter(BBRVisit.class, new BBRVisitSerializer());

		return BBRAccReg.sessionIndex;
	}
}
