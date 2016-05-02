package it.mate.commons.server.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {
  
  public static String dateToString(Date date) {
    return dateToString(date, "dd/MM/yyyy");
  }

  public static String dateToString(Date date, String pattern) {
    SimpleDateFormat fmt = new SimpleDateFormat(pattern);
    return fmt.format(date);
  }

  public static Date stringToDate(String text, String pattern) {
    SimpleDateFormat fmt = new SimpleDateFormat(pattern);
    try {
      return fmt.parse(text);
    } catch (ParseException e) {
      return null;
    }
  }
  
  public static Date addDaysToDate(Date date, int amount) {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(date);
    cal.add(Calendar.DAY_OF_YEAR, amount);
    return cal.getTime();
  }

}
