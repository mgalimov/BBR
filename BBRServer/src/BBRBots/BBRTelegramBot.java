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

import BBRAcc.BBRPoS;
import BBRAcc.BBRPoSManager;

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
	            
	            send.setChatId(message.getChatId().toString());
	            
	            if (msg.startsWith(COMMAND_START)) {
	            	send = chooseSalon(msg, send);
	            	send.setText("Добро пожаловать! Я помогаю записаться в салон красоты. " + send.getText());
	            }

	            if (msg.startsWith(COMMAND_HELP)) {
	            	resp = "Я поддерживаю команды:\n" +
	            		   "/start - начало работы\n" +
	            		   "/help - справка\n" +
	            		   "/salon ID - выбор салона";
	            	send.setText(resp);
	            }
	            
	            if (msg.startsWith(COMMAND_SALON)) {
	            	send = chooseSalon(msg, send);
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
        				        	krow.add(pos.getTitle() + ", " + pos.getLocationDescription());
        				        	keyboard.add(krow);
        				        }
        				        
        				        replyKeyboardMarkup.setKeyboard(keyboard);
        						send.setReplyMarkup(replyKeyboardMarkup);
        						resp = "Выберите подходящий салон";
        					}
        				} else
        					p = null;
        			} catch (Exception ex) {
        				
        			}	 
        		}
    		}
    		
    		if (p != null)
    			resp = "Ваш салон красоты " + p.getTitle() + ", " + p.getLocationDescription();
    	}

    	send.setText(resp);
    	return send;
	}
}
