package it.mate.gwtcommons.shared.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertiesHolder {

  private static PropertiesHolder INSTANCE = null;
  
  private Map<? extends Object, ? extends Object> properties;
  
  public static void setProperties(Map<? extends Object, ? extends Object> properties) {
    setProperties(properties, false);
  }
  
  public static void setProperties(Map<? extends Object, ? extends Object> properties, boolean cleanInstance) {
    if (INSTANCE == null || cleanInstance)
      INSTANCE = new PropertiesHolder();
    if (INSTANCE.properties == null)
      INSTANCE.properties = new HashMap<Object, Object>();
    Map<Object, Object> instanceProperties = (Map<Object, Object>)INSTANCE.properties;
    for (Object key : properties.keySet()) {
      Object value = properties.get(key);
      instanceProperties.put(key, value);
    }
  }
  
  public static Map<? extends Object, ? extends Object> getProperties() {
    return INSTANCE.properties;
  }

  public static Object get(String name) {
    if (INSTANCE == null || INSTANCE.properties == null)
      return null;
    return INSTANCE.properties.get(name);
  }
  
  public static Object get(String name, Object defaultValue) {
    if (INSTANCE == null || INSTANCE.properties == null)
      return null;
    Object value = INSTANCE.properties.get(name);
    if (value == null) {
      return defaultValue;
    } else {
      return value;
    }
  }
  
  public static List<String> getStringList(String name, String separator) {
    String value = getString(name, "");
    String[] values = value.split(separator);
    return Arrays.asList(values);
  }
  
  public static String getString(String name) {
    return getString(name, null);
  }
  
  public static String getString(String name, String defaultValue) {
    return (String)get(name, defaultValue);
  }
  
  public static Boolean getBooleanAllowNull(String name) {
    return getBoolean(name, null);
  }
  
  public static Boolean getBoolean(String name) {
    return getBoolean(name, false);
  }
  
  public static Boolean getBoolean(String name, Boolean defaultValue) {
    String value = getString(name, (defaultValue != null ? Boolean.toString(defaultValue) : null));
    if (value == null) {
      return defaultValue;
    } else {
      return Boolean.valueOf(value);
    }
  }
  
  public static int getInt(String name, int defaultValue) {
    String value = getString(name, Integer.toString(defaultValue));
    if (value == null) {
      return defaultValue;
    } else {
      return Integer.parseInt(value);
    }
  }
  
}
