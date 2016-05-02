package it.mate.phgcommons.client.utils;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

public class Time {
  private int hours;
  
  private int minutes;
  
  private static boolean use12HFormat = false;
  
  private final static DateTimeFormat fmt12 = DateTimeFormat.getFormat("h:mm a");
  
  private final static DateTimeFormat fmt24 = DateTimeFormat.getFormat("HH:mm");
  
  private static DateTimeFormat fmt = null;
  
  public Time() { 
    this(new Date());
  }
  
  public Time(int hours, int minutes) {
    this.hours = hours;
    this.minutes = minutes;
  }
  
  public Time(Date date) { 
    this(date.getHours(), date.getMinutes());
  }
  
  public Time(Time that) {
    this.hours = that.hours;
    this.minutes = that.minutes;
  }
  
  public int getHours() {
    return hours;
  }
  
  public int getHours12() {
    if (hours == 0 || hours == 12) {
      return 12;
    }
    return hours % 12;
  }
  
  public Time setHours(int hours) {
    this.hours = hours;
    return this;
  }
  
  public Time setHours12(int hours) {
    if (hours < 12) {
      if (isAM()) {
        this.hours = hours;
      } else {
        this.hours = hours + 12;
      }
    } else if (hours == 12) {
      if (isAM()) {
        this.hours = 0;
      } else {
        this.hours = 12;
      }
    }
    return this;
  }
  
  public int getMinutes() {
    return minutes;
  }
  
  public Time setMinutes(int minutes) {
    this.minutes = minutes;
    return this;
  }
  
  public Date asDate() {
    return new Date(1971, 1, 1, getHours(), getMinutes());
  }
  
  public String asString() {
    return fmt.format(asDate());
  }
  
  public static String asString(Date date) {
    return fmt.format(date);
  }
  
  @Override
  public String toString() {
    return "Time [" + hours + ":" + minutes + "]";
  }
  
  public static Time fromDate(Date date) {
    return new Time(date);
  }
  
  public static Time fromString(String text) {
    
    Time res = fromStringAllowsException(text, fmt);
    if (res == null) {
      // provo a fare il parsing con il formato inverso
      if (is12HFormat()) {
        res = fromStringAllowsException(text, fmt24);
        if (res == null) {
          res = fromStringAllowsException(text, fmt12);
        }
      } else {
        res = fromStringAllowsException(text, fmt12);
        if (res == null) {
          res = fromStringAllowsException(text, fmt24);
        }
      }
    }
    if (res == null) {
      PhgUtils.log("ALERT: cannot create Time from string " + text);
    }
    return res;
    

    /*
    Date temp = null;
    try {
      PhgUtils.log("Time.fromString.2");
      temp = fmt.parse(text);
    } catch (IllegalArgumentException ex) {
      // provo a fare il parsing con il formato inverso
      if (is12HFormat()) {
        try {
          PhgUtils.log("Time.fromString.3");
          temp = fmt24.parse(text);
        } catch (IllegalArgumentException ex1) {
          try {
            PhgUtils.log("Time.fromString.4");
            temp = fmt12.parse(text);
          } catch (IllegalArgumentException ex2) {
            PhgUtils.log("Time.fromString.5");
            PhgUtils.log("ERROR PARSING TIME FROM STRING " + text);
            return null;
          }
        }
      } else {
        try {
          PhgUtils.log("Time.fromString.4");
          temp = fmt12.parse(text);
        } catch (IllegalArgumentException ex1) {
          try {
            PhgUtils.log("Time.fromString.4");
            temp = fmt24.parse(text);
          } catch (IllegalArgumentException ex2) {
            PhgUtils.log("Time.fromString.5");
            PhgUtils.log("ERROR PARSING TIME FROM STRING " + text);
            return null;
          }
        }
      }
    }
    PhgUtils.log("Time.fromString.6");
    return new Time(temp);
    */
  }
  
  private static Time fromStringAllowsException(String text, DateTimeFormat fmt) {
    Date res = null;
    try {
      res = fmt.parse(text);
    } catch (IllegalArgumentException ex2) {
    }
    if (res != null) {
      return new Time(res);
    }
    return null;
  }
  
  public Date setInDate(Date date) {
    date.setHours(hours);
    date.setMinutes(minutes);
    date.setSeconds(0);
    return date;
  }

  public boolean isValidTime() {
    return (hours >= 0 && hours <= 23 && minutes >= 0 && minutes <= 59);
  }
  
  public Time incHours(int amount) {
    hours = (hours + amount) % 24;
    return this;
  }
  
  public Time incMinutes(int amount) {
    int hoursAmount = (minutes + amount) / 60;
    minutes = (minutes + amount) % 60;
    if (hoursAmount > 0) {
      incHours(hoursAmount);
    }
    return this;
  }
  
  public boolean isAM() {
    return hours < 12;
  }
  
  public boolean isPM() {
    return hours >= 12;
  }
  
  public static boolean is12HFormat() {
    return use12HFormat;
  }
  
  public static void set24HFormat() {
    setUse12HFormat(false);
  }
  
  public static void set12HFormat() {
    setUse12HFormat(true);
  }
  
  private static void setUse12HFormat(boolean use12hFormat) {
    use12HFormat = use12hFormat;
    fmt = use12hFormat ? fmt12 : fmt24;
  }
  
  public static DateTimeFormat getCurrentFormat() {
    return fmt;
  }
  
}
