package org.adrianwalker.recipecosting.server.util;

import java.io.IOException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public final class Email {

  private static final String EMAIL_PROPERTIES = "email.properties";
  private Properties properties;

  public Email() throws IOException {
    properties = new Properties();
    properties.load(this.getClass().getResourceAsStream(EMAIL_PROPERTIES));
  }

  public void send(String to, String subject, String text) {

    Session session = Session.getDefaultInstance(properties);

    try {
      MimeMessage message = new MimeMessage(session);
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
      message.setSubject(subject);
      message.setText(text);

      // Send message
      Transport.send(message);
      System.out.println("Sent message successfully....");
    } catch (MessagingException mex) {
      mex.printStackTrace();
    }
  }
}
