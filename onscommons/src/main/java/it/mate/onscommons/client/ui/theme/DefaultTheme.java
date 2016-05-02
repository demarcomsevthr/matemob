package it.mate.onscommons.client.ui.theme;

import it.mate.phgcommons.client.utils.OsDetectionUtils;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.user.client.Window;

public class DefaultTheme {

  public static class Impl {
    private static ThemeBundle bundle = null;
    public static ThemeBundle get() {
      if (bundle == null) {
        if (OsDetectionUtils.isAndroid()) {
          bundle = GWT.create(ThemeBundleAndroid.class);
        } else {
          bundle = GWT.create(ThemeBundleIos.class);
        }
      }
      return bundle;
    }
  }
  
  public static String getWaitingDivWidth() {
    String res = "40px";
    if (Window.getClientWidth() < 768) {
      res = "40px"; 
    } else if (Window.getClientWidth() < 1024) {
      res = "60px";
    } else {
      res = "80px";
    }
    return res;
  }
  
  public static String getWindowHeight() {
    return Window.getClientHeight() + "px";
  }
  
  public static String getWindowWidth() {
    return Window.getClientWidth() + "px";
  }
  
  public static boolean isHeightLessThanOrEqualTo(int maxHeight) {
    return (Window.getClientHeight() <= maxHeight);
  }
  
  public static boolean isWidthLessThanOrEqualTo(int maxWidth) {
    return (Window.getClientWidth() <= maxWidth);
  }
  
  public interface ThemeBundle extends ClientBundle {
    @Source({"css/ons.css"})
    public CssResource css();
    @Source("resources/calendar.png")
    DataResource calendarImage();
    @Source("resources/clock.png")
    DataResource clockImage();
  }
  
  public interface ThemeBundleAndroid extends ThemeBundle {
    @Source({"css/ons.css"})
    public CssResource css();
  }
  
  public interface ThemeBundleIos extends ThemeBundle {
    @Source({"css/ons.css"})
    public CssResource css();
  }
  
}
