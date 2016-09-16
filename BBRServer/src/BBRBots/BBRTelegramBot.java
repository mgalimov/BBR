package BBRBots;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import BBR.BBRDataSet;
import BBRAcc.BBRPoS;
import BBRAcc.BBRPoSManager;
import BBRBots.BBRChatStatuses.BBRChatStatus;
import BBRCust.BBRProcedure;
import BBRCust.BBRProcedureManager;

public class BBRTelegramBot extends TelegramLongPollingBot {
	protected String COMMAND_START = "/start";
	protected String COMMAND_HELP = "/help";
	protected String COMMAND_SALON = "/salon";
	
	public class BBRTelegramBotConfig {
	    public static final String BOT_USERNAME = "barbinybot";
	    public static final String BOT_TOKEN = "239482583:AAGDJWReWhtePhvAvGd_SZkUnI2vgTXjKoQ";
	}
	
	@Override
	public String getBotUsername() {
		return BBRTelegramBotConfig.BOT_USERNAME;
	}

	@Override
	public void onUpdateReceived(Update update) {
		if(update.hasMessage()){
	        Message message = update.getMessage();

	        if(message.hasText()){
	        	String msg = message.getText();
	        	String resp = "";
	            SendMessage send = new SendMessage();
	            String chatId = message.getChatId().toString();
	            int status = BBRChatStatuses.getStatus(chatId);
	            
	            send.setChatId(chatId);
	            
	            if (msg.startsWith(COMMAND_START)) {
	            	BBRChatStatuses.setStatus(chatId, BBRChatStatus.CHAT_STEP_INIT);
	            	send = chooseSalon(msg, send);
	            	send.setText("Добро пожаловать! Я помогаю записаться в салон красоты. " + send.getText());
	            } else
	            if (msg.startsWith(COMMAND_HELP)) {
	            	resp = "Я поддерживаю команды:\n" +
	            		   "/start - начало работы\n" +
	            		   "/help - справка\n" +
	            		   "/salon ID - выбор салона";
	            	send.setText(resp);
	            } else
	            if (msg.startsWith(COMMAND_SALON)) {
	            	send = chooseSalon(msg, send);
	            } else
	            if (status == BBRChatStatus.CHAT_STEP_POS_BEFORE) {
	            	BBRPoSManager pmgr = new BBRPoSManager();
	            	BBRPoS p;
	            	p = pmgr.findByUrlId(msg.split(":")[0]);
	            	resp = posSelected(send, p);
	            	send.setText(resp);
	            	send = chooseProcedure(msg, send);
	            } else
	            if (status == BBRChatStatus.CHAT_STEP_POS_SELECTED) {
	            	send = chooseProcedure(msg, send);
	            }
	            if (status == BBRChatStatus.CHAT_STEP_PROC_BEFORE) {
	            	BBRProcedureManager pmgr = new BBRProcedureManager();
	            	BBRProcedure proc;
	            	try {
	            		proc = pmgr.findById(Long.parseLong(msg.split(":")[0]));
	            		resp = procedureSelected(send, proc);
	            	} catch (Exception ex) {
	            		resp = "Не нашли мы такую услугу :(";
	            	}
	            	send.setText(resp);
	            }

	            try {
	                sendMessage(send);
	            } catch (TelegramApiException e) {
	            }
	        }
	        
	    }
	}

	@Override
	public String getBotToken() {
		return BBRTelegramBotConfig.BOT_TOKEN;
	}

	protected SendMessage chooseSalon(String msg, SendMessage send) {
		String resp = "";
    	String[] s = msg.split(" ");
    	String salon = "";
    	if (s.length > 1)
    		salon = s[1];
    	if (salon.isEmpty()) {
    		resp = "Укажите название, код или ИД салона, например /salon strizhi"; 
    	} else
    	{
    		BBRPoSManager mgr = new BBRPoSManager();
    		BBRPoS p = mgr.findByUrlId(salon);
    		if (p == null) {
    			try {
    				p = mgr.findById(Long.parseLong(salon));
    			} catch (Exception ex) {
    				
    			}
        		if (p == null) {
        			try {
        				List<BBRPoS> list = mgr.findByTitleLike(salon);
        				if (list != null && list.size() > 0) {
        					if (list.size() == 1)
        						p = list.get(0);
        					else {
        						send.enableMarkdown(true);
        						ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        						replyKeyboardMarkup.setSelective(true);
        				        replyKeyboardMarkup.setResizeKeyboard(true);
        				        replyKeyboardMarkup.setOneTimeKeyboad(true);
        				        
        				        List<KeyboardRow> keyboard = new ArrayList<>();
        				        
        				        for (BBRPoS pos : list) {
            				        KeyboardRow krow = new KeyboardRow();
        				        	krow.add(pos.getUrlID() + ": " + pos.getTitle() + ", " + pos.getLocationDescription());
        				        	keyboard.add(krow);
        				        }
        				        
        				        replyKeyboardMarkup.setKeyboard(keyboard);
        						send.setReplyMarkup(replyKeyboardMarkup);
        						resp = "Выберите подходящий салон";
        						BBRChatStatuses.setStatus(send.getChatId(), BBRChatStatus.CHAT_STEP_POS_BEFORE);
        					}
        				} else
        					p = null;
        			} catch (Exception ex) {
        				
        			}	 
        		}
    		}
    		
    		if (p != null) {
    			resp = posSelected(send, p);
    			send.setText(resp);
    			send = chooseProcedure(msg, send);
    		}
    	}

    	send.setText(resp);
    	return send;
	}
	
	protected String posSelected(SendMessage send, BBRPoS pos) {
		BBRChatStatuses.setStatus(send.getChatId(), BBRChatStatus.CHAT_STEP_POS_SELECTED);
		BBRChatStatuses.setData(send.getChatId(), pos.getId(), null, null, null);
		return "Ваш салон красоты " + pos.getTitle() + ", " + pos.getLocationDescription();
	}
	
	
	protected SendMessage chooseProcedure(String msg, SendMessage send) {
		String resp = "";

		BBRPoSManager pmgr = new BBRPoSManager();
		BBRPoS pos = new BBRPoS();
		
		try {
			pos = pmgr.findById(BBRChatStatuses.getData(send.getChatId()).posId);
		} catch (Exception ex) {
			resp = "Вы пока не выбрали салон красоты!";
	    	send.setText(resp);
	    	return send;
		}
		
   		BBRProcedureManager mgr = new BBRProcedureManager();
   		BBRDataSet<BBRProcedure> plist = mgr.list("", "title", pos, null);
   		BBRProcedure proc = null;
   		
		if (plist != null && plist.data.size() > 0) {
			if (plist.data.size() == 1)
				proc = plist.data.get(0);
			else {
				send.enableMarkdown(true);
				ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
				replyKeyboardMarkup.setSelective(true);
		        replyKeyboardMarkup.setResizeKeyboard(true);
		        replyKeyboardMarkup.setOneTimeKeyboad(true);
		        
		        List<KeyboardRow> keyboard = new ArrayList<>();
		        
		        for (BBRProcedure p : plist.data) {
			        KeyboardRow krow = new KeyboardRow();
		        	krow.add(p.getId() + ": " + p.getTitle() + " " + p.getLength() + " " + p.getPrice() + " " + pos.getCurrency());
		        	keyboard.add(krow);
		        }
		        
		        replyKeyboardMarkup.setKeyboard(keyboard);
				send.setReplyMarkup(replyKeyboardMarkup);
				resp = "Выберите услугу";
				BBRChatStatuses.setStatus(send.getChatId(), BBRChatStatus.CHAT_STEP_PROC_BEFORE);
			}
		} else
			proc = null;
    		
   		if (proc != null) {
   			resp = procedureSelected(send, proc);
   		}

    	send.setText(send.getText() + "\n" + resp);
    	return send;
	}
	
	protected String procedureSelected(SendMessage send, BBRProcedure proc) {
			BBRChatStatuses.setStatus(send.getChatId(), BBRChatStatus.CHAT_STEP_PROC_SELECTED);
			BBRChatStatuses.setData(send.getChatId(), proc.getPos().getId(), proc.getId(), null, null);
			return "Ваша услуга " + proc.getTitle() + " " + proc.getLength() + " " + proc.getPrice() + " " + proc.getPos().getCurrency();
	}

}
