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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EmailController {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmailController.class);
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

    LOGGER.info("to = " + to);
    
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

  public String getProperty(final String key) {
    return properties.getProperty(key);
  }
}
