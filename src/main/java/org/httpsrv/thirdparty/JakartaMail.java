package org.httpsrv.thirdparty;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import org.httpsrv.conf.Config;

public class JakartaMail {
    private static Session session;

    public static void initSmtpConfig() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", Config.getPropertiesVar().smtpHost);
        props.put("mail.smtp.port", Config.getPropertiesVar().smtpPort);

        session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Config.getPropertiesVar().smtpUsername, Config.getPropertiesVar().smtpPassword);
            }
        });
    }

    public static boolean sendMessage(String toEmail, String subject, String messageBody) {
        if (session == null) {
            return false;
        }

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Config.getPropertiesVar().smtpUsername + "@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(messageBody);

            Transport.send(message);

            return true;
        } catch (MessagingException ignored) {
            return false;
        }
    }
}