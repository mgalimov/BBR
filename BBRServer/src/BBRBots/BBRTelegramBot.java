package BBRBots;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import BBR.BBRDataSet;
import BBR.BBRUtil;
import BBRAcc.BBRPoS;
import BBRAcc.BBRPoSManager;
import BBRBots.BBRChatStatuses.BBRChatStatus;
import BBRClientApp.BBRContext;
import BBRCust.BBRProcedure;
import BBRCust.BBRProcedure.BBRProcedureStatus;
import BBRCust.BBRProcedureManager;
import BBRCust.BBRSpecialist;
import BBRCust.BBRVisit;
import BBRCust.BBRVisit.BBRVisitSource;
import BBRCust.BBRVisitManager;

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
	            
	            if (msg.startsWith(COMMAND_START) || msg.toLowerCase().startsWith("отмен")) {
	            	BBRChatStatuses.setStatus(chatId, BBRChatStatus.CHAT_STEP_INIT);
	            	send = chooseSalon(msg, send);
	            	send.setText("Добро пожаловать! Я помогаю записаться в салон красоты.\n\n" + send.getText());
	            } else
	            if (msg.startsWith(COMMAND_HELP)) {
	            	resp = "Я поддерживаю команды:\n" +
	            		   "/start [ID] - начало работы [и выбор салона]\n" +
	            		   "/help - справка\n" +
	            		   "/salon ID - выбор салона";
	            	send.setText(resp);
	            } else
	            if (msg.startsWith(COMMAND_SALON) || msg.toLowerCase().startsWith("help") || msg.toLowerCase().startsWith("помо")) {
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
	            } else
	            if (status == BBRChatStatus.CHAT_STEP_PROC_BEFORE) {
	            	BBRProcedureManager pmgr = new BBRProcedureManager();
	            	BBRProcedure proc;
	            	try {
	            		proc = pmgr.findById(Long.parseLong(msg.split(":")[0]));
	            		resp = procedureSelected(send, proc);
	            		send.setText(resp);
	            		send = chooseTime(msg, send);
	            	} catch (Exception ex) {
	            		resp = "Не нашли мы такую услугу :(";
	            		send.setText(resp);
	            	}
	            } else
	            if (status == BBRChatStatus.CHAT_STEP_TIME_BEFORE) {
	            	resp = parseTime(send, msg);
	            	if (resp != null)
	            		send.setText(resp);
	            	else {
	            		send.setText("В это время нет свободных мастеров, очень жаль.");
	            		send = chooseTime(msg, send);
	            	}
	            }  else
	            if (status == BBRChatStatus.CHAT_STEP_TIME_SELECTED) {
	            	resp = parsePhone(send, msg);
	            	if (resp != null)
	            		send.setText("Как к вам обращаться?");
	            	else
	            		send.setText("Неправильный какой-то телефон. Введите снова");
	            } else
	            if (status == BBRChatStatus.CHAT_STEP_PHONE_SELECTED) {
	            	resp = msg;
	            	if (resp != null && !resp.isEmpty()) {
	            		send.setText(nameSelected(send, resp));
	            	}
	            	else
	            		send.setText("А давайте снова?");
	            }


	            try {
	                sendMessage(send);
	            } catch (TelegramApiException e) {
	    			BBRUtil.log.error(e.getMessage());
	    			BBRUtil.log.error(e.getStackTrace());
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
    		send.setText(resp);
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
        						BBRChatStatuses.setStatus(send.getChatId(), BBRChatStatus.CHAT_STEP_POS_BEFORE);
        						resp = "Выберите подходящий салон";
        				    	send.setText(resp);
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

    	return send;
	}
	
	protected String posSelected(SendMessage send, BBRPoS pos) {
		BBRChatStatuses.setStatus(send.getChatId(), BBRChatStatus.CHAT_STEP_POS_SELECTED);
		BBRChatStatuses.setData(send.getChatId(), pos.getId(), null, null, null, null, null);
		send.enableMarkdown(false);
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
   		BBRDataSet<BBRProcedure> plist = mgr.list("", "id", "pos.id = " + pos.getId() + " and status = " + BBRProcedureStatus.PROCSTATUS_APPROVED);
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
		        	krow.add(p.getId() + ": " + p.getTitle() + " " + BBRContext.getDurationStringL(p.getLength()) + ", " + p.getPrice() + " " + p.getPos().getCurrency());
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
   			chooseTime(msg, send);
   		}

    	send.setText(send.getText() + "\n\n" + resp);
    	return send;
	}
	
	protected String procedureSelected(SendMessage send, BBRProcedure proc) {

		BBRChatStatuses.setStatus(send.getChatId(), BBRChatStatus.CHAT_STEP_PROC_SELECTED);
		BBRChatStatuses.setData(send.getChatId(), proc.getPos().getId(), proc.getId(), null, null, null, null);
		send.enableMarkdown(false);
		return "Ваша услуга " + proc.getTitle() + " " + BBRContext.getDurationStringL(proc.getLength()) + ", " + proc.getPrice() + " " + proc.getPos().getCurrency();
	}

	protected SendMessage chooseTime(String msg, SendMessage send) {
		String resp = "";
		Date date;
		
		try {
			BBRPoSManager mgr = new BBRPoSManager();
			BBRPoS pos = mgr.findById(BBRChatStatuses.getData(send.getChatId()).posId);
			date = BBRUtil.now(pos.getTimeZone());
		} catch (Exception ex) {
			date = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.roll(Calendar.DAY_OF_YEAR, true);
        SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
        
		resp = "Укажите дату и время. Например, 'завтра в 9:00' или '" + df.format(c.getTime()) + " в 6 вечера'";
		BBRChatStatuses.setStatus(send.getChatId(), BBRChatStatus.CHAT_STEP_TIME_BEFORE);
		
    	send.setText(send.getText() + "\n\n" + resp);
    	return send;
	
	}
	
	protected SendMessage chooseTime2(String msg, SendMessage send) {
		String resp = "";

		BBRPoSManager pmgr = new BBRPoSManager();
		BBRPoS pos = new BBRPoS();
		BBRProcedureManager prmgr = new BBRProcedureManager();
		BBRProcedure proc = null;
		
		try {
			pos = pmgr.findById(BBRChatStatuses.getData(send.getChatId()).posId);
			proc = prmgr.findById(BBRChatStatuses.getData(send.getChatId()).procId);
		} catch (Exception ex) {
			resp = "Не указан салон и/или процедура";
	    	send.setText(resp);
	    	return send;
		}
		
   		BBRVisitManager mgr = new BBRVisitManager();

		send.enableMarkdown(false);

		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow krow = new KeyboardRow();
        
		Date date = BBRUtil.now(pos.getTimeZone());
		
        SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
        Calendar c = Calendar.getInstance();
        int n = 0;
                
        for (int i = 0; i <= 2; i++) {
        	List<String> times = null;
        	try {
        		times = mgr.getFreeTimesByProc(date, pos.getId().toString(), proc.getId().toString());
        	} catch (Exception ex) {
        		
        	}
	   		
			if (times != null && times.size() > 0) {
		        for (String time : times) {
	        		krow.add(df.format(date) + " " + time);
	        		n++;
			        if (n == 3) {
			        	n = 0;
			        	keyboard.add(krow);
			        	krow = new KeyboardRow();
			        }
	        	}
			}
			
			c.setTime(date);
			c.roll(Calendar.DAY_OF_MONTH, true);
        }
		        
        replyKeyboardMarkup.setKeyboard(keyboard);
		send.setReplyMarkup(replyKeyboardMarkup);
		resp = "Выберите дату и время";
		BBRChatStatuses.setStatus(send.getChatId(), BBRChatStatus.CHAT_STEP_TIME_BEFORE);


    	send.setText(send.getText() + "\n\n" + resp);
    	return send;
	}
	
	protected String timeSelected(SendMessage send, Date time) {
		String chatId = send.getChatId();
		Long posId = BBRChatStatuses.getData(chatId).posId;
		Long procId = BBRChatStatuses.getData(chatId).procId;
		BBRChatStatuses.setStatus(chatId, BBRChatStatus.CHAT_STEP_TIME_SELECTED);
		BBRChatStatuses.setData(chatId, posId, procId, null, time, null, null);
		send.enableMarkdown(false);
		
		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat);
		return "Ваше время " + df.format(time) + "\n\n Укажите ваш телефон";
	}

	protected String parseTime(SendMessage send, String msg) {
		Date time = null;
		String chatId = send.getChatId();
		Long posId = BBRChatStatuses.getData(chatId).posId;
		Long procId = BBRChatStatuses.getData(chatId).procId;
		
		SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
		SimpleDateFormat df1 = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat tf = new SimpleDateFormat(BBRUtil.fullTimeFormat);
		BBRVisitManager mgr = new BBRVisitManager();
		Calendar c = Calendar.getInstance();
		
		Date today;
		try {
			BBRPoSManager pmgr = new BBRPoSManager();
			BBRPoS pos = pmgr.findById(BBRChatStatuses.getData(send.getChatId()).posId);
			today = BBRUtil.now(pos.getTimeZone());
		} catch (Exception ex1) {
			today = new Date();
		}
		
		try {
			time = df.parse(msg);
		} catch (Exception ex) {
			String[] timeParts = msg.split("\\s+");
			String datePart = timeParts[0].trim();
			if (datePart.equalsIgnoreCase("сегодня")) {
				time = today;
			} else
				if (datePart.equalsIgnoreCase("завтра")) {
					c.setTime(today);
					c.roll(Calendar.DAY_OF_YEAR, true);	
				} else
					if (datePart.trim().equalsIgnoreCase("послезавтра")) {
						c.setTime(today);
						c.add(Calendar.DAY_OF_YEAR, 2);
					} else
					{
						try {
							c.setTime(df.parse(datePart));
						} catch (Exception ex0)	{
							try {
								c.setTime(df1.parse(datePart));
							} catch (Exception ex1)	{
								try {
									c.setTime(df2.parse(datePart));
								} catch (Exception ex2)	{
									c.setTime(today);
								}
							}
						}
					}
			
			for (int i = 1; i < timeParts.length; i++) {
				String tm = timeParts[i].trim().toLowerCase();
				if (tm.equals("в")) {}
				if (tm.startsWith("вечер")) {
					int h = c.get(Calendar.HOUR_OF_DAY);
					if (h <= 11 && h >= 4)
						c.add(Calendar.HOUR_OF_DAY, 12);
				} else
				if (tm.startsWith("утр")) {
				} else
				if (tm.startsWith("ноч")) {
				} else
				if (tm.startsWith("дня")) {
					int h = c.get(Calendar.HOUR_OF_DAY);
					if (h <= 4 && h >= 1)
						c.add(Calendar.HOUR_OF_DAY, 12);
				} else
				{
					try {
						if (!tm.contains(":"))
							tm = tm + ":00";
						Date t = tf.parse(tm);
						Calendar c1 = Calendar.getInstance();
						c1.setTime(t);
						c.set(Calendar.HOUR_OF_DAY, c1.get(Calendar.HOUR_OF_DAY));
						c.set(Calendar.MINUTE, c1.get(Calendar.MINUTE));
					} catch (Exception ex3) {
						c.set(Calendar.HOUR_OF_DAY, 12);
						c.set(Calendar.MINUTE, 00);
					}
					
				}
			}
		}

		time = c.getTime();
		
		if (time != null && !mgr.isTimeAvailable(time, posId, procId)) {
			return null;
		}
		
		if (time == null) {
			return null;
		}
		
		return timeSelected(send, time);
	}

	protected String phoneSelected(SendMessage send, String phone) {
		String chatId = send.getChatId();
		Long posId = BBRChatStatuses.getData(chatId).posId;
		Long procId = BBRChatStatuses.getData(chatId).procId;
		Date time = BBRChatStatuses.getData(chatId).time;
		BBRChatStatuses.setStatus(chatId, BBRChatStatus.CHAT_STEP_PHONE_SELECTED);
		BBRChatStatuses.setData(chatId, posId, procId, null, time, phone, null);
		send.enableMarkdown(false);
		
		return "";
	}

	protected String parsePhone(SendMessage send, String msg) {
		String phone = null;
		
		try {
			Pattern p = Pattern.compile("^([+]?[0-9\\s-\\(\\)]{3,25})*$");  
	        Matcher m = p.matcher(msg);
	        if (m.matches())
	        	phone = msg;
		} catch (Exception ex) {
			phone = null;
		}
		
		if (phone == null) {
			return null;
		}
		
		return phoneSelected(send, phone);
	}

	protected String nameSelected(SendMessage send, String name) {
		String chatId = send.getChatId();
		Long posId = BBRChatStatuses.getData(chatId).posId;
		Long procId = BBRChatStatuses.getData(chatId).procId;
		Date time = BBRChatStatuses.getData(chatId).time;
		String phone = BBRChatStatuses.getData(chatId).phone;
		BBRChatStatuses.setStatus(chatId, BBRChatStatus.CHAT_STEP_INIT);
		BBRChatStatuses.setData(chatId, posId, procId, null, time, phone, name);

		BBRVisitManager mgr = new BBRVisitManager();
		BBRPoSManager pmgr = new BBRPoSManager();
		BBRPoS pos = pmgr.findById(posId);
		BBRProcedureManager prmgr = new BBRProcedureManager();
		BBRProcedure proc = prmgr.findById(procId);
		BBRSpecialist spec = null;
		String resp = "";
		
		if (proc != null)
			spec = mgr.findSpecByTimeAndProc(time, proc, pos);

			
		if (spec == null) {
			resp = "Не удалось записать вас к мастеру. Попробуйте другое время.";
			BBRChatStatuses.setStatus(chatId, BBRChatStatus.CHAT_STEP_TIME_BEFORE);
		} else {
			BBRVisit visit = mgr.scheduleVisit(true, pos, null, time, proc, spec, name, phone, "", BBRVisitSource.BOT_TELEGRAM);
			
			SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat);
			resp = "Спасибо, " + name + "!\n\n" + 
			 	  "Вы успешно записались в " + visit.getPos().getTitle() + "\n" +
				  "на услугу " + visit.getProcedure().getTitle() + "\n" +
				  "к специалисту " + visit.getSpec().getName() + ".\n\n" +
				  "Ваше время " + df.format(visit.getTimeScheduled()) + ".\n\n" +
				  "Код бронирования " + visit.getBookingCode() + ".\n\n" +
				  "Нажмите /start для того, чтобы начать заново, нажмите /help для справки.";
		}
		return resp;
	}

}
