package it.mate.wolf.server.model;

public class MailRecipient {
  
  private String email;
  
  public MailRecipient(String email) {
    super();
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

}
