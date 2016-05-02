package it.mate.phgcommons.client.plugins;

import java.util.Date;

public class CalendarEvent {

  private String title;
  private String location = "";
  private String notes = "";
  private Date startDate;
  private Date endDate;
  
  private String notificationId;
  
  private int firstReminderMinutes = 0;
  
  @Override
  public String toString() {
    return "CalendarEvent [title=" + title + ", location=" + location + ", notes=" + notes + ", startDate=" + startDate + ", endDate=" + endDate
        + ", notificationId=" + notificationId + "]";
  }
  
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getLocation() {
    return location;
  }
  public void setLocation(String location) {
    this.location = location;
  }
  public String getNotes() {
    return notes;
  }
  public void setNotes(String notes) {
    this.notes = notes;
  }
  public Date getStartDate() {
    return startDate;
  }
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }
  public Date getEndDate() {
    return endDate;
  }
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }
  public String getNotificationId() {
    return notificationId;
  }
  public void setNotificationId(String notificationId) {
    this.notificationId = notificationId;
  }
  public int getFirstReminderMinutes() {
    return firstReminderMinutes;
  }
  public void setFirstReminderMinutes(int firstReminderMinutes) {
    this.firstReminderMinutes = firstReminderMinutes;
  }
}
