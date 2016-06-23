package it.mate.testons.server.services;

import it.mate.testons.server.model.MailRecipient;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class MailAdapterImpl implements MailAdapter {

  private static final Logger logger = Logger.getLogger(MailAdapterImpl.class);

  private JavaMailSender mailSender;

  private SimpleMailMessage mailTemplate;

  public void setMailSender(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void setMailTemplate(SimpleMailMessage mailTemplate) {
    this.mailTemplate = mailTemplate;
  }

  public void sendMailNotification(String messageBody, String emailAddr) throws MessagingException {
    StringBuffer text = new StringBuffer(messageBody);
    addFixedMailFooter(text);
    doSendMail(new MailRecipient(emailAddr), "Therapy Reminder", text, null, null);
  }

  protected void doSendMail (MailRecipient recipient, String subject, StringBuffer text, String attachName, byte[] attachBuffer) throws MessagingException {

    JavaMailSender javaMailSender = (JavaMailSender) mailSender;
    MimeMessage msg = javaMailSender.createMimeMessage();
    Multipart mp = new MimeMultipart(); 
    
    String escapedContent = escapeMailText(text.toString());
    
    MimeBodyPart htmlPart = new MimeBodyPart(); 
    htmlPart.setContent(escapedContent, "text/html"); 
    
    msg.setFrom(new InternetAddress(mailTemplate.getFrom())); 
    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient.getEmail())); 
    msg.setSubject(subject); 
    
    logger.debug("@@@@@@@@ sending mail to " + recipient.getEmail());
    logger.debug("@@@@@@@@ subject = " + subject);
    logger.debug("@@@@@@@@ content = \n\n" + escapedContent + "\n");
    
    MimeBodyPart attachment = null; 
    if (attachName != null && attachBuffer != null) {
      attachment = new MimeBodyPart(); 
      DataSource src = new ByteArrayDataSource(attachBuffer, "application/pdf"); 
      attachment.setFileName(attachName); 
      attachment.setDataHandler(new DataHandler(src)); 
    }

    mp.addBodyPart(htmlPart); 
    
    if (attachment != null) {
      mp.addBodyPart(attachment); 
    }

    msg.setContent(mp); 
    msg.saveChanges();//I think this is necessary, 

    javaMailSender.send(msg);
    
  }

  private void addFixedMailFooter(StringBuffer text) {
    text.append("\n\n\n\n");
    text.append("<hr/>");
    text.append("<span style='font-size:10px;font-style:italic;'>This email was sent to you by the Therapy Reminder Service. This is an automatically generated message: please do not reply. Thanks.</span>");
  }

  private String escapeMailText(String text) {
    text = text.replace("\n", "<br>");
    return text;
  }

}
