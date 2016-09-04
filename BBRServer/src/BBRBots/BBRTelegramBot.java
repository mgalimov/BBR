package BBRBots;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class BBRTelegramBot extends TelegramLongPollingBot {

	public class BBRTelegramBotConfig {
	    public static final String BOT_USERNAME = "barbinybot";
	    public static final String BOT_TOKEN = "239482583:AAGDJWReWhtePhvAvGd_SZkUnI2vgTXjKoQ";
	}
	
	@Override
	public String getBotUsername() {
		return null;
	}

	@Override
	public void onUpdateReceived(Update update) {
		if(update.hasMessage()){
	        Message message = update.getMessage();

	        if(message.hasText()){
	            SendMessage sendMessageRequest = new SendMessage();
	            sendMessageRequest.setChatId(message.getChatId().toString());
	            sendMessageRequest.setText("you said: " + message.getText());
	            try {
	                sendMessage(sendMessageRequest);
	            } catch (TelegramApiException e) {
	            
	            }
	        }
	    }
	}

	@Override
	public String getBotToken() {
		return BBRTelegramBotConfig.BOT_TOKEN;
	}

}
