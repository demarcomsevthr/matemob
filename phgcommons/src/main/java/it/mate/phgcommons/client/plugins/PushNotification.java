package it.mate.phgcommons.client.plugins;

public class PushNotification {
  public static final String REGISTRATION_EVENT_NAME = "registration";
  public static final String NOTIFICATION_EVENT_NAME = "notification";
  public static final String ERROR_EVENT_NAME = "error";
  private String eventName;
  private String regId;
  private String message;
  public boolean isRegistrationEvent() {
    return REGISTRATION_EVENT_NAME.equalsIgnoreCase(eventName);
  }
  public boolean isNotificationEvent() {
    return NOTIFICATION_EVENT_NAME.equalsIgnoreCase(eventName);
  }
  public boolean isErrorEvent() {
    return ERROR_EVENT_NAME.equalsIgnoreCase(eventName);
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
