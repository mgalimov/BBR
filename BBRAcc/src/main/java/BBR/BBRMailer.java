package BBR;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class BBRMailer {
    public static void send(String address, String subject, String text) {
        final String username = "agent@barbiny.ru";
        final String password = "barb2807GM";

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.yandex.ru");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.port", "465");

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

            Thread thread = new Thread() {;
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
}
