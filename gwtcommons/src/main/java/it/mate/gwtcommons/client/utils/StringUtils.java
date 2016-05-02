package it.mate.gwtcommons.client.utils;

public class StringUtils {
  
  public static String truncate(String text, int lenght) {
    if (text != null) {
      text = text.substring(0, Math.min(lenght, text.length()));
    }
    return text;
  }
  
  public static boolean isNumber(String text) {
    return text != null && text.trim().length() > 0 && text.matches("[0-9]*");
  }
  
}
