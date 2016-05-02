package it.mate.phgcommons.client.ui.theme;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.DataResource;
import com.googlecode.mgwt.ui.client.MGWT;

public class DefaultTheme {

  public static class Impl {
    private static ThemeBundle bundle = null;
    public static ThemeBundle get() {
      if (bundle == null) {
        if (MGWT.getOsDetection().isAndroid()) {
          bundle = GWT.create(ThemeBundleAndroid.class);
        } else if (MGWT.getOsDetection().isIOs()) {
          bundle = GWT.create(ThemeBundleAndroid.class);
        }
      }
      return bundle;
    }
  }
  
  public interface ThemeBundle extends ClientBundle {
    @Source({"css/phg.css"})
    public CssResource css();
    @Source("resources/calendar.png")
    DataResource calendarImage();
    @Source("resources/clock.png")
    DataResource clockImage();
  }
  
  public interface ThemeBundleAndroid extends ThemeBundle {
    @Source({"css/phg.css"})
    public CssResource css();
  }
  
  public interface ThemeBundleIos extends ThemeBundle {
    @Source({"css/phg.css"})
    public CssResource css();
  }
  
}
