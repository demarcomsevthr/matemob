package it.mate.commons.server.utils;


public class StringUtils {

  public static String substringSlice (String text, String substring) {
    int index = text.indexOf(substring);
    if (index > -1) {
      index += substring.length();
    } else {
      return null;
    }
    return text.substring(index);
  }
  
  public static String formatNumber(Number value, int digits) {
    String fmt = "%0"+digits+"d";
    return String.format(fmt, value);
  }
  
}
