package BBRBots;

import java.util.Date;

public class BBRChatDatum {
	 public Long posId;
	 public Long procId;
	 public Long specId;
	 public Date time;
	 
	 public BBRChatDatum(Long posId, Long procId, Long specId, Date time) {
		 this.posId = posId;
		 this.procId = procId;
		 this.specId = specId;
		 this.time = time;
	 }
}