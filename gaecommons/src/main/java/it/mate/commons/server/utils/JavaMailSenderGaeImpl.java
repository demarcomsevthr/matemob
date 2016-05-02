package it.mate.commons.server.utils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class JavaMailSenderGaeImpl extends JavaMailSenderImpl {
  
  private boolean debug = false;
  
  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  @Override
  public synchronized Session getSession() {
    Session session = super.getSession();
    if (debug) {
      session.setDebug(true);
      session.setDebugOut(System.out);
    }
    return session;
  }
  
  @Override
  public MimeMessage createMimeMessage() {
    return new MimeMessage(getSession());
  }
  
  @Override
  protected void doSend(MimeMessage[] mimeMessages, Object[] originalMessages) throws MailException {
    try {
      for (int it = 0; it < mimeMessages.length; it++) {
        Message msg = mimeMessages[it];
        Transport.send(msg);
      }
    } catch (AddressException e) {
      throw new MailSendException("AddressException", e);
    } catch (MessagingException e) {
      throw new MailSendException("MessagingException", e);
    }
  }

}
