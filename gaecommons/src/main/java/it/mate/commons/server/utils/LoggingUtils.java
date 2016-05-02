package it.mate.commons.server.utils;

public class LoggingUtils {

  public static java.util.logging.Logger getJavaLogging(Class<?> clazz) {
    return java.util.logging.Logger.getLogger(clazz.getName());
  }
  
  public static void debug(Class<?> clazz, String message) {
    org.apache.log4j.Logger.getLogger(clazz).debug(message);
    java.util.logging.Logger.getLogger(clazz.getName()).fine(message);
  }
  
  public static void info(Class<?> clazz, String message) {
    org.apache.log4j.Logger.getLogger(clazz).info(message);
    java.util.logging.Logger.getLogger(clazz.getName()).info(message);
  }
  
  public static void error(Class<?> clazz, String message) {
    org.apache.log4j.Logger.getLogger(clazz).error(message);
    java.util.logging.Logger.getLogger(clazz.getName()).severe(message);
  }
  
  public static void error(Class<?> clazz, String message, Throwable th) {
    org.apache.log4j.Logger.getLogger(clazz).error(message, th);
    java.util.logging.Logger.getLogger(clazz.getName()).severe(message + " - " + th.getClass() + " - " + th.getMessage());
  }
  
}
