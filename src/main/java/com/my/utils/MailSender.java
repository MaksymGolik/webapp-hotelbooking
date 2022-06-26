package com.my.utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class MailSender {
    private static final Logger log = Logger.getLogger(MailSender.class);


    private static MailSender instance;

    private MailSender(){
        ResourceBundle mailSenderConfig = ResourceBundle.getBundle("mailSender");
        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", mailSenderConfig.getString("host"));
        properties.put("mail.smtp.port", mailSenderConfig.getString("port"));
        properties.put("mail.smtp.ssl.enable", mailSenderConfig.getString("sslEnable"));
        properties.put("mail.smtp.auth", mailSenderConfig.getString("auth"));
        properties.put("mail.mime.charset","utf-8");

    }

    public static synchronized MailSender getInstance(){
        if(instance==null){
            instance = new MailSender();
        }
        return instance;
    }


    private void sendMessage(String emailTo, String subject, String content){
        Properties properties = System.getProperties();
        ResourceBundle mailSenderConfig = ResourceBundle.getBundle("mailSender");
        Session session = Session.getInstance(properties, new jakarta.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(mailSenderConfig.getString("from"), mailSenderConfig.getString("password"));

            }

        });

        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(mailSenderConfig.getString("from")));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));

            message.setSubject(subject);

            message.setText(content.toString());

            // Send message
            Transport.send(message);
        } catch (MessagingException  e) {
            log.error(e);
        }
    }

    public void sendMessageAfterRegistration(String emailTo, String filePath, String nameNewUser) throws FileNotFoundException {

        File file = new File(filePath);

        Scanner scanner = new Scanner(file);

        String subject = "";

        if(scanner.hasNextLine()) subject = scanner.nextLine();

        StringBuilder content = new StringBuilder();

        while (scanner.hasNextLine())
            content.append(scanner.nextLine()).append('\n');
        content.replace(content.indexOf("*"),content.indexOf("*")+1,nameNewUser);
        sendMessage(emailTo,subject,content.toString());
    }

    public void sendMessageAfterStatusChanging(String emailTo, String filePath, String userName, long bookingId) throws FileNotFoundException {
        File file = new File(filePath);

        Scanner scanner = new Scanner(file);

        String subject = "";

        if(scanner.hasNextLine()) subject = scanner.nextLine();

        StringBuilder content = new StringBuilder();

        while (scanner.hasNextLine())
            content.append(scanner.nextLine()).append('\n');
        content.replace(content.indexOf("*"),content.indexOf("*")+1,userName);
        content.replace(content.indexOf("%"),content.indexOf("%")+1, String.valueOf(bookingId));
        sendMessage(emailTo,subject,content.toString());
    }

}
