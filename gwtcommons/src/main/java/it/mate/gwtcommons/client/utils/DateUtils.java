package it.mate.gwtcommons.client.utils;

import java.util.Date;

import com.google.gwt.user.datepicker.client.CalendarUtil;

public class DateUtils {
  
  public static Date addDaysToDate(Date date, int days) {
    Date copy = CalendarUtil.copyDate(date);
    CalendarUtil.addDaysToDate(copy, days);
    return copy;
  }

}
