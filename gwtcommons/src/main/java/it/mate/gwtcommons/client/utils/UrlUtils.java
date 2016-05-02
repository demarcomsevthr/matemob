package it.mate.gwtcommons.client.utils;

import com.google.gwt.core.client.GWT;

public class UrlUtils {
  
  public static String getDefaultThemeId() {
    return "standardTheme";
  }
  
  public static String getThemeResourceUrl(String path) {
    return getThemeResourceUrl(getDefaultThemeId(), path);
  }
  
  public static String getThemeResourceUrl(String themeId, String path) {
    return GWT.getHostPageBaseURL()+".resource?op=THI&id="+themeId+"&path="+path;
  }
  
}
