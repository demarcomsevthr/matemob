package it.mate.gwtcommons.client.utils;

public class NumberUtils {
  
  public static boolean isInteger(Double value) {
    if (value == null)
      return false;
    if (Math.floor(value) == value)
      return true;
    return false;
  }
  
  public static int doubleAsInt(Double value) {
    if (value == null)
      return 0;
    return (int)Math.floor(value);
  }

  public static Integer doubleAsInteger(Double value) {
    if (value == null)
      return null;
    return (int)Math.floor(value);
  }
  
  public static boolean isNumber(String text) {
    return StringUtils.isNumber(text);
  }
  
}
