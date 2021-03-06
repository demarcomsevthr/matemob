package it.mate.onscommons.client.utils;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.phgcommons.client.utils.callbacks.VoidCallback;
import it.mate.phgcommons.client.plugins.GlobalizationPlugin;
import it.mate.phgcommons.client.utils.LogPlugin;
import it.mate.phgcommons.client.utils.OsDetectionUtils;
import it.mate.phgcommons.client.utils.PhgUtils;
import it.mate.phgcommons.client.utils.Time;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

public class CdvUtils2 {
  
  private static int tabletWrapperPct = 80;
  
  private static boolean useLogPlugin = false;
  
  private static List<String> trace = null;
  
  private static String defaultDatePattern;
  
  private static String defaultTimePattern;
  
  private static boolean suspendUncaughtExceptionAlerts;
  

  public static void logEnvironment() {
    log("CURRENT LAYOUT = " + getLayoutInfo());
    if (OsDetectionUtils.isIOs()) {
      log("IOs phone layout is " + OsDetectionUtils.IPHONE_WIDTH + " x " + OsDetectionUtils.IPHONE_HEIGHT);
      log("IOs landscape layout is " + OsDetectionUtils.IPAD_LAND_WIDTH + " x " + OsDetectionUtils.IPAD_LAND_HEIGHT);
      log("IOs portrait  layout is " + OsDetectionUtils.IPAD_PORT_WIDTH + " x " + OsDetectionUtils.IPAD_PORT_HEIGHT);
    } else {
      log("Android landscape layout is " + OsDetectionUtils.APAD_LAND_WIDTH + " x " + OsDetectionUtils.APAD_LAND_HEIGHT);
      log("Android portrait  layout is " + OsDetectionUtils.APAD_PORT_WIDTH + " x " + OsDetectionUtils.APAD_PORT_HEIGHT);
    }
    log("(see "+ OsDetectionUtils.class.getName() +" for details)");
    log("NavVer " + Window.Navigator.getAppVersion());
    log("Device.name " + getDeviceName());
    log("Device.phonegap " + getDevicePhonegap());
    log("Device.platform " + getDevicePlatform());
    log("Device.uuid " + getDeviceUuid());
    log("Device.version " + getDeviceVersion());
    log("AppLocalLanguage = " + getAppLocalLanguage());
  }
  
  public static void commonInitializations() {
    commonInitializations(null, null, null);
  }
  
  public static void commonInitializations(String initialLanguage, String initialDatePattern, String initialTimePattern) {
    
    log("Language initialization...");
    if (initialLanguage != null) {
      setAppLocalLanguageImpl(initialLanguage);
    }
    
    log("Date format initialization...");
    Delegate<String> datePatternInitializer = new Delegate<String>() {
      public void execute(String pattern) {
        if (pattern == null) {
          CdvUtils2.log("MAYBE MISSING GLOBALIZATION PLUGIN (cordova plugin add org.apache.cordova.globalization)?");
          pattern = CdvUtils2.getLocalStorageItem("debug-date-pattern");
          if (pattern == null) {
            if ("it".equals(getAppLocalLanguage())) {
              pattern = "dd/MM/yyyy";
            } else {
              pattern = "MM/dd/yyyy";
            }
            CdvUtils2.setLocalStorageItem("debug-date-pattern", pattern);
          }
        }
        defaultDatePattern = pattern;
        CdvUtils2.log("DEFAULT DATE PATTERN = " + defaultDatePattern);
      }
    };
    if (initialDatePattern != null) {
      datePatternInitializer.execute(initialDatePattern);
    } else {
      GlobalizationPlugin.getDatePattern(datePatternInitializer);
    }
    
    log("Time format initialization...");
    Delegate<String> timePatternInitializer = new Delegate<String>() {
      public void execute(String pattern) {
        if (pattern == null) {
          CdvUtils2.log("MAYBE MISSING GLOBALIZATION PLUGIN (cordova plugin add org.apache.cordova.globalization)?");
          pattern = CdvUtils2.getLocalStorageItem("debug-time-pattern");
          if (pattern == null) {
            if ("it".equals(getAppLocalLanguage())) {
              pattern = "HH:mm";
            } else {
              pattern = "h:mm a";
            }
            CdvUtils2.setLocalStorageItem("debug-time-pattern", pattern);
          }
        }
        defaultTimePattern = pattern;
        CdvUtils2.log("DEFAULT TIME PATTERN = " + defaultTimePattern);
        if (pattern.contains("HH")) {
          Time.set24HFormat();
        } else {
          Time.set12HFormat();
        }
        CdvUtils2.log("CURRENT TIME FORMAT IS " + Time.getCurrentFormat().getPattern());
      }
    };
    if (initialTimePattern != null) {
      timePatternInitializer.execute(initialTimePattern);
    } else {
      GlobalizationPlugin.getTimePattern(timePatternInitializer);
    }
    
  }
  
  public static String getDefaultDatePattern() {
    return defaultDatePattern;
  }
  
  public static String getDefaultTimePattern() {
    return defaultTimePattern;
  }
  
  public static String getLayoutInfo() {
    String layoutInfo = "Width " + Window.getClientWidth();
    layoutInfo += " Height " + Window.getClientHeight();
    if (OsDetectionUtils.isTabletLandscape()) {
      layoutInfo += " isTabletLandscape";
    } else if (OsDetectionUtils.isTabletPortrait()) {
      layoutInfo += " isTabletPortrait";
    } else {
      layoutInfo += " isPhone";
    }
    return layoutInfo;
  }
  
  public static void openInAppBrowser(String url) {
    openInAppBrowserImpl(url);
  }
  
  private static native void openInAppBrowserImpl(String url) /*-{
    var inAppBrowser = $wnd.open(url, '_blank', 'location=no');
  }-*/;
  
  public static native Navigator getNavigator() /*-{
    return $wnd.navigator;
  }-*/;
  
  public static native String getLocalStorageItem(String name) /*-{
    return $wnd.localStorage[name];
  }-*/;
  
  public static native void setLocalStorageItem(String name, String value) /*-{
    $wnd.localStorage[name] = value;
  }-*/;

  public static native JsArrayString getLocalStorageKeys() /*-{
    var results = [];
    for (var it = 0; it < localStorage.length; it++){
      results[it] = $wnd.localStorage.key(it);
    }
    return results;
  }-*/;

  public static native void removeLocalStorageItem(String key) /*-{
    $wnd.localStorage.removeItem(key);
  }-*/;

  public static native String getWindowSetting(String name) /*-{
    if ($wnd.Settings) {
      return $wnd.Settings[name];
    }
    return null;
  }-*/;

  public static class Navigator extends JavaScriptObject {
    protected Navigator() {

    }
    public final native String getLanguage() /*-{
      return this.language;
    }-*/;
  }
  
  
  public static void log(String text) {
    String logMsg = GwtUtils.log(text);
    
    if (logMsg != null && !OsDetectionUtils.isDesktop() && "true".equalsIgnoreCase(getWindowSetting("TraceLogToFile"))) {
//    logImpl("Ready to write to trace.log > " + logMsg);
      
      if (trace != null) {
        trace.add(logMsg);
      }
      
    }
    
    if (useLogPlugin && !OsDetectionUtils.isDesktop() && !OsDetectionUtils.isIOs()) {
      LogPlugin.debug(text);
    } else {
      logImpl(text);
    }
  }
  
  public static native void logImpl(String text) /*-{
    $wnd.console.log(text);
  }-*/;

  public static void setSuspendUncaughtExceptionAlerts(boolean flag) {
    suspendUncaughtExceptionAlerts = flag;
  }

  public static boolean isSuspendUncaughtExceptionAlerts() {
    return suspendUncaughtExceptionAlerts;
  }
  
  public static String getNativeProperty(String name) {
    return getNativePropertyImpl(name.substring(0, 1).toUpperCase()+name.substring(1));
  }

  private static native String getNativePropertyImpl(String name) /*-{
    if (!$wnd.jsNativePropertiesWrapper) {
      return null;
    }
    return eval("window.jsNativePropertiesWrapper.get"+name+"()");
  }-*/;

  public static void adaptWrapperPanel(final Panel wrapperPanel, String id, final boolean adaptVerMargin, final int headerPanelHeight, final Delegate<Element> delegate) {
    CdvUtils2.log("adaptWrapperPanel.1");
    wrapperPanel.getElement().setId(id);
    CdvUtils2.log("adaptWrapperPanel.2");
    if (OsDetectionUtils.isTablet()) {
      CdvUtils2.log("adaptWrapperPanel.3");
      GwtUtils.onAvailable(id, new Delegate<Element>() {
        public void execute(final Element wrapperPanelElem) {
          GwtUtils.deferredExecution(new Delegate<Void>() {
            public void execute(Void element) {
              CdvUtils2.log("adaptWrapperPanel.4");
              int height = getTabletWrapperHeight();
              CdvUtils2.log("applying wrapperPanel height = " + height);
              wrapperPanelElem.getStyle().setHeight(height, Unit.PX);
              if (adaptVerMargin) {
                int verMargin = ( Window.getClientHeight() - height ) / 2 - headerPanelHeight;
                wrapperPanelElem.getStyle().setMarginTop(verMargin, Unit.PX);
                wrapperPanelElem.getStyle().setMarginBottom(verMargin, Unit.PX);
              }
              int width = getTabletWrapperWidth();
              wrapperPanelElem.getStyle().setWidth(width, Unit.PX);
              int horMargin = ( Window.getClientWidth() - width ) / 2;
              wrapperPanelElem.getStyle().setMarginLeft(horMargin, Unit.PX);
              wrapperPanelElem.getStyle().setMarginRight(horMargin, Unit.PX);
              if (delegate != null) {
                delegate.execute(wrapperPanelElem);
              }
            }
          });
        }
      });
      
    } else {
      
      //09/10/2013
      // Patch per IPhone per far funzionare le animazioni jquery
      
      int actualHeight = (Window.getClientHeight() - OsDetectionUtils.IOS_HEADER_PANEL_HEIGHT );
      
      PhgUtils.log(">>>>>>>>>>>>>>>>>>>>>>>> CdvUtils2::adaptWrapperPanel");
      
      if (OsDetectionUtils.isIOs() && actualHeight < 380) {
        GwtUtils.deferredExecution(new Delegate<Void>() {
          public void execute(Void element) {
            int actualHeight = (Window.getClientHeight() - OsDetectionUtils.IOS_HEADER_PANEL_HEIGHT );
            CdvUtils2.log("setting wrapperPanel height to " + actualHeight);
            wrapperPanel.setHeight(actualHeight + "px");
            wrapperPanel.setWidth(Window.getClientWidth() + "px");
          }
        });
      } else {
        CdvUtils2.log("setting wrapperPanel height to " + actualHeight);
        wrapperPanel.setHeight(actualHeight + "px");
        wrapperPanel.setWidth(Window.getClientWidth() + "px");
      }
      
    }
  }
  
  private static int getTabletWrapperHeight() {
    int height = Window.getClientHeight() * tabletWrapperPct / 100;
    return height;
  }
  
  private static int getTabletWrapperWidth() {
    int width = Window.getClientWidth() * tabletWrapperPct / 100;
    return width;
  }
  
  public static void setTabletWrapperPct(int tabletWrapperPct) {
    CdvUtils2.tabletWrapperPct = tabletWrapperPct;
  }
  
  public static native String getDeviceName() /*-{
    if (!$wnd.device) {
      return "desktop";
    }
    return $wnd.device.name;
  }-*/;

  public static native String getDevicePhonegap() /*-{
    if (!$wnd.device) {
      return "desktop";
    }
    return $wnd.device.phonegap;
  }-*/;

  public static native String getDevicePlatform() /*-{
    if (!$wnd.device) {
      return "desktop";
    }
    return $wnd.device.platform;
  }-*/;

  public static native String getDeviceUuid() /*-{
    if (!$wnd.device) {
      return "desktop";
    }
    return $wnd.device.uuid;
  }-*/;

  public static native String getDeviceVersion() /*-{
    if (!$wnd.device) {
      return "desktop";
    }
    return $wnd.device.version;
  }-*/;

  public static void setUseLogPlugin(boolean useLogPlugin) {
    CdvUtils2.useLogPlugin = useLogPlugin;
  }
  
  public static String dateToString(Date date) {
    String lang = getAppLocalLanguage();
    if (lang == null) {
      lang = getNavigator().getLanguage();
    }
    if (lang == null) {
      lang = "en";
    }
    if ("it".equals(lang)) {
      return GwtUtils.dateToString(date, "dd/MM/yyyy");
    } else {
      return GwtUtils.dateToString(date, "M/d/yyyy");
    }
  }
  
  public static void startTrace() {
    CdvUtils2.trace = new ArrayList<String>();
  }
  
  public static List<String> getTrace() {
    return trace;
  }
  
  public static void clearTrace() {
    CdvUtils2.trace = null;
  }
  
  public static void callDebugHook(JavaScriptObject jso) {
    if (isGlobalDebugHookUndefined()) {
      createGlobalDebugHookImpl();
    }
    callGlobalDebugHookImpl(jso);
  }
  
  private native static boolean isGlobalDebugHookUndefined() /*-{
    return typeof($wnd.glbDebugHook) == "undefined";
  }-*/;
  
  private native static void createGlobalDebugHookImpl() /*-{
    $wnd.glbDebugHook = function (jso) {
        var _tt = "INSERT BREAK POINT HERE";
      };
  }-*/;
  
  private native static void callGlobalDebugHookImpl(JavaScriptObject jso) /*-{
    $wnd.glbDebugHook(jso);
  }-*/;

  /*
  public static void getLocaleLanguageFromDevice(final Delegate<String> delegate) {
    GlobalizationPlugin.getLocaleName(new StringCallback() {
      @SuppressWarnings("unused")
      public void handle(String language) {

        language = null;
        ////////////////////////////////////////////////////////////////////////////
        
        if (language != null) {
          delegate.execute(getCountryFromLocaleName(language));
        } else {
          delegate.execute(getLocaleLanguageFromLocaleInfo());
        }
      }
    });
  }
  */
  
  public static String getLocaleLanguageFromLocaleInfo() {
    String language = LocaleInfo.getCurrentLocale().getLocaleName();
    if ("default".equals(language)) {
      language = getLocaleLanguageFromNavigator();
    }
    if (language != null) {
      language = getCountryFromLocaleName(language);
    }
    return language;
  }
  
  private static native String getLocaleLanguageFromNavigator() /*-{
    return $wnd.navigator.language;
  }-*/;

  private static String getCountryFromLocaleName(String locale) {
    if (locale != null) {
      if (locale.length() > 2) {
        int len = locale.length();
        locale = locale.substring(len - 2, len);
      }
      locale = locale.toLowerCase();
    }
    return locale;
  }
  
  private static String getLanguageFromLocaleName(String locale) {
    if (locale != null) {
      if (locale.length() > 2) {
        locale = locale.substring(0, 2);
      }
      locale = locale.toLowerCase();
    }
    return locale;
  }
  
  public static void setAppLocalLanguageAndReload(final String language) {
    setAppLocalLanguageImpl(language);
    Cookies.setCookie("mgwtLanguage", language, GwtUtils.getDate(31, 12, 2020));
    reloadApp();
  }
  
  public static void reloadApp() {
    // se lanciato da una combo da una IllegalStateException su una onDetach
    // workaround: disabilito l'handler delle eccezioni
    GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
      public void onUncaughtException(Throwable e) {
        // do nothing
      }
    });
    Window.Location.reload();
  }
  
  private static native void setAppLocalLanguageImpl(String language) /*-{
    if ($wnd.setAppLocalLanguage === undefined) {
      $wnd.localStorage.setItem("app-local-language", language);
    } else {
      $wnd.setAppLocalLanguage(language);
    }
  }-*/;
  
  public static boolean isAppLocalLanguageIT() {
    return "it".equals(getAppLocalLanguage());
  }

  public static String getAppLocalLanguage() {
    String lang = getAppLocalLanguageImpl();
    if (lang == null || lang.trim().length() == 0) {
      lang = getLocaleLanguageFromLocaleInfo();
    }
    return lang;
  }
  
  public static native String getAppLocalLanguageImpl() /*-{
    if (typeof($wnd.getAppLocalLanguage) == 'undefined') {
      return $wnd.localStorage.getItem("app-local-language");
    }
    return $wnd.getAppLocalLanguage();
  }-*/;
  
  public static void addOrientationChangeHandler(final Delegate<Void> delegate) {
//  JQuery.select("body").bind("orientationchange", delegate);
    addOrientationChangeHandler(new VoidCallback() {
      public void handle() {
        delegate.execute(null);
      }
    });
  }
  
  public static void addOrientationChangeHandler(VoidCallback callback) {
    addWindowHandlerImpl("orientationchange", callback);
  }
  
  public static void addResizeHandler(VoidCallback callback) {
    addWindowHandlerImpl("resize", callback);
  }
  
  private static native void addWindowHandlerImpl(String eventName, VoidCallback callback) /*-{
    if (callback != null) {
      var jsCallback = null;
      jsCallback = $entry(function() {
        callback.@it.mate.phgcommons.client.utils.callbacks.VoidCallback::handle()();
      });
      $wnd.addEventListener(eventName, jsCallback, false);
    }
  }-*/;
  
  public static void setDesktopDebugBorder(int width, int height) {
    if (OsDetectionUtils.isDesktop()) {
      CdvUtils2.log("SETTING DESKTOP DEBUG BORDER AT " + width + " x " + height);
      RootPanel.getBodyElement().getStyle().setWidth(width, Unit.PX);
      RootPanel.getBodyElement().getStyle().setHeight(height, Unit.PX);
      RootPanel.getBodyElement().getStyle().setBorderWidth(1, Unit.PX);
      RootPanel.getBodyElement().getStyle().setBorderStyle(BorderStyle.SOLID);
      RootPanel.getBodyElement().getStyle().setBorderColor("red");
      RootPanel.getBodyElement().getStyle().setMarginLeft(0.5, Unit.PCT);
    }
  }
  
  public static void setDefaultExceptionHandler(final Logger log) {
    GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
      public void onUncaughtException(Throwable ex) {
        if (log != null) {
          log.log(Level.SEVERE, "uncaught exception", ex);
        }
        ex.printStackTrace();
        if (!CdvUtils2.isSuspendUncaughtExceptionAlerts()) {
//        Window.alert("uncaught: " + ex.getClass().getName() + " - " + ex.getMessage());
        }
        StackTraceElement stea[] = ex.getStackTrace();
        if (stea != null) {
          for (StackTraceElement ste : stea) {
            System.out.println("Stack trace: " + ste.getClassName()+" "+ste.getMethodName());;
          }
        }
      }
    });
  }
  
  public static String elementToString(Element element) {
    return GwtUtils.getJsPropertyString(element, "outerHTML");
  }
  
  public static native boolean isReallyAttached(String elemId) /*-{
    return $doc.getElementById(elemId) != null;
  }-*/;
  
  public static void ensureId(Element element) {
    if (element.getId() == null || "".equals(element.getId())) {
      element.setId(DOM.createUniqueId());
    }
  }
  
  
}
