package com.revspeed.utility;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
public class GEmailSender {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String USERNAME = "revspeed.org@gmail.com";
    private static final String PASSWORD = "cvdhdfpadzulgwzf";

    public boolean sendRegistrationEmail(String to) {
        String subject = "Welcome to RevSpeed";
        String text = "Congratulations on registering with RevSpeed. Welcome to RevSpeed!";

        return sendEmail(to, subject, text);
    }

    public boolean sendPurchaseConfirmationEmail(String to, String serviceName, double billAmount) {
        String subject = "Purchase Confirmation from RevSpeed";
        String text = "Thank you for purchasing the service '" + serviceName + "'. Your bill ammount is: \u20B9 " + billAmount;

        return sendEmail(to, subject, text);
    }

    public boolean sendSubscriptionReminderEmail(String to, String serviceName, int daysRemaining) {
        String subject = "Subscription Reminder from RevSpeed";
        String text = "Your subscription for the service '" + serviceName +
                "' will end in " + daysRemaining + " days. Renew now to continue enjoying our services.";

        return sendEmail(to, subject, text);
    }

    private boolean sendEmail(String to, String subject, String text) {
        boolean flag = false;

        // Configure the SMTP properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.host", SMTP_HOST);

        // Create a session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            // Create a message
            Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setFrom(new InternetAddress(USERNAME));
            message.setSubject(subject);
            message.setText(text);

            // Send the message
            Transport.send(message);
            flag = true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return flag;
    }
}

