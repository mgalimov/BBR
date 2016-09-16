package BBRBots;

import java.util.Date;
import java.util.Hashtable;

public class BBRChatStatuses {
	private static Hashtable<String, Integer> chatStatus = new Hashtable<String, Integer>();  
	private static Hashtable<String, BBRChatDatum> chatData = new Hashtable<String, BBRChatDatum>();
	
	public class BBRChatStatus {
		 public static final int CHAT_STEP_INIT 			= 0; 
		 public static final int CHAT_STEP_POS_BEFORE	 	= 5;
		 public static final int CHAT_STEP_POS_SELECTED 	= 10;
		 public static final int CHAT_STEP_PROC_BEFORE 		= 15;
		 public static final int CHAT_STEP_PROC_SELECTED 	= 20;
		 public static final int CHAT_STEP_SPEC_BEFORE 		= 25;
		 public static final int CHAT_STEP_SPEC_SELECTED 	= 30;
		 public static final int CHAT_STEP_TIME_BEFORE 		= 45;
		 public static final int CHAT_STEP_TIME_SELECTED 	= 50;
		 public static final int CHAT_STEP_INFO_BEFORE 		= 55;
		 public static final int CHAT_STEP_PHONE_SELECTED 	= 60;
		 public static final int CHAT_STEP_NAME_SELECTED 	= 65;
	}
	
	public static void setStatus(String chatId, int status) {
		chatStatus.put(chatId, status);
	}

	public static int getStatus(String chatId) {
		if (!chatStatus.containsKey(chatId))
			chatStatus.put(chatId, BBRChatStatus.CHAT_STEP_INIT);
		return chatStatus.get(chatId);
	}

	public static void setData(String chatId, Long posId, Long procId, Long specId, Date time, String phone, String name) {
		BBRChatDatum d = new BBRChatDatum(posId, procId, specId, time, phone, name);
		chatData.put(chatId, d);
	}
	
	public static BBRChatDatum getData(String chatId) {
		if (!chatData.containsKey(chatId))
			chatData.put(chatId, null);
		return chatData.get(chatId);
	}

}
