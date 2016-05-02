package it.mate.phgcommons.client.plugins;

public class PushNotificationOld {
  protected static final String REGISTERED_EVENT_NAME = "registered";
  protected static final String MESSAGE_EVENT_NAME = "message";
  private String eventName;
  private String regId;
  private String message;
  public boolean isRegistrationEvent() {
    return REGISTERED_EVENT_NAME.equalsIgnoreCase(eventName);
  }
  public boolean isMessageEvent() {
    return MESSAGE_EVENT_NAME.equalsIgnoreCase(eventName);
  }
  protected void setEventName(String event) {
    this.eventName = event;
  }
  public String getRegId() {
    return regId;
  }
  protected void setRegId(String regId) {
    this.regId = regId;
  }
  public String getMessage() {
    return message;
  }
  protected void setMessage(String message) {
    this.message = message;
  }
}
