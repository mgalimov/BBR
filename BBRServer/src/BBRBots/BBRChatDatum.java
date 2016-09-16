package BBRBots;

import java.util.Date;

public class BBRChatDatum {
	 public Long posId;
	 public Long procId;
	 public Long specId;
	 public Date time;
	 public String phone;
	 public String name;
	 
	 public BBRChatDatum(Long posId, Long procId, Long specId, Date time, String phone, String name) {
		 this.posId = posId;
		 this.procId = procId;
		 this.specId = specId;
		 this.time = time;
		 this.phone = phone;
		 this.name = name;
	 }
}