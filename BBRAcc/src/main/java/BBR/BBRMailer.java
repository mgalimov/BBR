package BBR;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.MessageClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.OptionalParameters;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.TimeFormatter;

public class BBRMailer {
	public static String mailServerUserName = "agent@barbiny.ru";
	public static String mailServerPassword = "barb2807GM";
	public static String mailServerURL = "smtp.yandex.ru";
	public static String mailServerPort = "465";
	
	public static String smsServerUserName = "barbiny";
	public static String smsServerPassword = "Gal4502";
	public static String smsServerURL = "smpp.smsc.ru";
	public static int smsServerPort = 3700;

	public static void send(String address, String subject, String text) {
		if (address == null || address.trim().equals(""))
			return;
		if (subject == null || subject.trim().equals(""))
			return;
		if (text == null || text.trim().equals(""))
			return;

        final String username = mailServerUserName;
        final String password = mailServerPassword;

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", mailServerURL);
        props.put("mail.smtp.socketFactory.port", mailServerPort);
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.port", mailServerPort);

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
          });

        try {
            final MimeMessage message = new MimeMessage(session);
            message.setContent(text, "text/plain; charset=UTF-8");
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(address));
            message.setSubject(subject, "UTF-8");
            message.setText(text, "UTF-8");
        	BBRUtil.log.info(text);

            Thread thread = new Thread() {
            	public void run() {
            		try {
						Transport.send(message);
					} catch (MessagingException e) {
						BBRUtil.log.warn(e.getMessage());
						throw new RuntimeException(e);
					}
            	}
            };
            thread.start();
        } catch (Exception e) {
        }
    }
    
    public static void sendSMS(String phones, String text) {
        Thread thread = new Thread() {
	    	public void run() {
		       	 TimeFormatter timeFormatter = new AbsoluteTimeFormatter();
		    	 SMPPSession session = new SMPPSession();
		    	 
		    	 try {
		             session.connectAndBind(
		             	smsServerURL,//"31.186.99.90",
		             	smsServerPort,//2775,
		             	new BindParameter(
		             		BindType.BIND_TRX,
		             		smsServerUserName, 	
		             		smsServerPassword,	
		             		"",
		             		TypeOfNumber.ALPHANUMERIC,
		             		NumberingPlanIndicator.ISDN,
		             		null));
		         } catch (IOException e) {
		             BBRUtil.log.error("SMPP: Failed connect and bind to host");
		         }
		    	 
		    	 Random random = new Random();
		    	 OptionalParameter sarMsgRefNum = OptionalParameters.newSarMsgRefNum((short)random.nextInt());
		         OptionalParameter sarTotalSegments = OptionalParameters.newSarTotalSegments(1);
		         OptionalParameter sarSegmentSeqnum = OptionalParameters.newSarSegmentSeqnum(1);
		    	 
		    	 String messageId = null;
		    	 GeneralDataCoding coding = new GeneralDataCoding(Alphabet.ALPHA_UCS2, MessageClass.CLASS1, false);
		    	 

		         if (phones != null && !phones.trim().isEmpty())
		        	 for (String phone : phones.split(",")) {
				         try {
				             BBRUtil.log.info("SMPP: message sending " + text.replace("\n", "\\n"));
				             messageId = session.submitShortMessage(
				            		 	"CMT", 
				            		 	TypeOfNumber.ALPHANUMERIC, 
				            		 	NumberingPlanIndicator.ISDN, 
				            		 	"Barbiny", 
				            		 	TypeOfNumber.INTERNATIONAL, 
				            		 	NumberingPlanIndicator.ISDN, 
				            		 	phone, 
				            		 	new ESMClass(), 
				            		 	(byte)0, 
				            		 	(byte)1,  
				            		 	timeFormatter.format(new Date()), 
				            		 	null, 
				            		 	new RegisteredDelivery(SMSCDeliveryReceipt.DEFAULT), 
				            		 	(byte)0, 
				            		 	coding,//DataCodings.ZERO, 
				            		 	(byte)0, 
				            		 	text.getBytes(Charset.forName("UTF-16")), 
				            		 	sarMsgRefNum, 
				            		 	sarSegmentSeqnum, 
				            		 	sarTotalSegments);
				         } catch (PDUException e) {
				        	 BBRUtil.log.error("SMPP: Invalid PDU parameter");
				         } catch (ResponseTimeoutException e) {
				        	 BBRUtil.log.error("SMPP: Response timeout");
				         } catch (InvalidResponseException e) {
				        	 BBRUtil.log.error("SMPP: Receive invalid respose");
				         } catch (NegativeResponseException e) {
				        	 BBRUtil.log.error("SMPP: Receive negative response");
				         } catch (IOException e) {
				        	 BBRUtil.log.error("SMPP: IO error occur");
				         }
				         
				         BBRUtil.log.info("SMPP: message sent " + (messageId == null ? "null" : messageId));
		        	}
	    	 }
        };
        thread.start();
    }
}
