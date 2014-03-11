package org.adrianwalker.recipecosting.server.controller;

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
  private Properties properties;

  public EmailController() {
    properties = new Properties();
    try {
      properties.load(EmailController.class.getResourceAsStream(EMAIL_PROPERTIES));
    } catch (final IOException ioe) {
      throw new IllegalStateException(ioe);
    }
  }

  public void send(final String to, final String subject,
          final String text) throws IOException, MessagingException {

    Session session = Session.getDefaultInstance(properties, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(
                properties.getProperty("mail.smtp.user"),
                properties.getProperty("mail.smtp.password"));
      }
    });

    MimeMessage message = new MimeMessage(session);
    message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
    message.setSubject(subject);
    message.setText(text);

    Transport.send(message);
  }
}
