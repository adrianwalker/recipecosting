package org.adrianwalker.recipecosting.server.controller;

import ch.qos.logback.core.net.LoginAuthenticator;
import java.io.IOException;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public final class EmailController {

  private static final String EMAIL_PROPERTIES = "/email.properties";

  public EmailController() {
  }

  public void send(final String to, final String subject,
    final String text) throws IOException, MessagingException {

    Properties properties = new Properties();
    properties.load(EmailController.class.getResourceAsStream(EMAIL_PROPERTIES));

    final String username = properties.getProperty("mail.smtp.user");
    final String password = properties.getProperty("mail.smtp.pass");

    Session session = Session.getDefaultInstance(properties, new Authenticator() {

      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    });

    MimeMessage message = new MimeMessage(session);
    message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
    message.setSubject(subject);
    message.setText(text);

    // Send message
    Transport.send(message);
  }
}
