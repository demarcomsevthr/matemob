package it.mate.phgcommons.client.plugins;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.phgcommons.client.utils.callbacks.StringCallback;

import com.google.gwt.core.client.JavaScriptObject;

public class GlobalizationPlugin {

  public static void getDatePattern(final Delegate<String> resultDelegate) {
    JavaScriptObject options = JavaScriptObject.createObject();
    GwtUtils.setJsPropertyString(options, "formatLength", "short");
    GwtUtils.setJsPropertyString(options, "selector", "date");
    /*
    GwtUtils.setJSOProperty(options, "formatLength", "short");
    GwtUtils.setJSOProperty(options, "selector", "date");
    */
    getDatePatternImpl(options, new StringCallback() {
      public void handle(String result) {
        resultDelegate.execute(result);
      }
    });
  }
  public static void getTimePattern(final Delegate<String> resultDelegate) {
    JavaScriptObject options = JavaScriptObject.createObject();
    GwtUtils.setJsPropertyString(options, "formatLength", "short");
    GwtUtils.setJsPropertyString(options, "selector", "time");
    /*
    GwtUtils.setJSOProperty(options, "formatLength", "short");
    GwtUtils.setJSOProperty(options, "selector", "time");
    */
    getDatePatternImpl(options, new StringCallback() {
      public void handle(String result) {
        resultDelegate.execute(result);
      }
    });
  }
  
  private static native void getDatePatternImpl(JavaScriptObject options, StringCallback callback) /*-{
    if (typeof($wnd.cordova) != 'undefined' && typeof($wnd.cordova.exec) != 'undefined') {
      @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)("calling globalization plugin");
      var jsSuccessCallback = $entry(function(date) {
        callback.@it.mate.phgcommons.client.utils.callbacks.StringCallback::handle(Ljava/lang/String;)(date.pattern);
      });
      var jsErrorCallback = $entry(function() {
        @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)("js error during call globalization plugin");
        callback.@it.mate.phgcommons.client.utils.callbacks.StringCallback::handle(Ljava/lang/String;)(null);
      });
      $wnd.cordova.exec(jsSuccessCallback, jsErrorCallback, "Globalization", "getDatePattern", [{"options": options}]);
    } else {
      @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)("cordova is undefined");
      callback.@it.mate.phgcommons.client.utils.callbacks.StringCallback::handle(Ljava/lang/String;)(null);
    }
  }-*/;

  public static native void getLocaleName(StringCallback callback) /*-{
    if (typeof($wnd.navigator.globalization) == 'undefined') {
      callback.@it.mate.phgcommons.client.utils.callbacks.StringCallback::handle(Ljava/lang/String;)(null);
    } else {
      var jsSuccessCallback = $entry(function(locale) {
        callback.@it.mate.phgcommons.client.utils.callbacks.StringCallback::handle(Ljava/lang/String;)(locale.value);
      });
      var jsErrorCallback = $entry(function(locale) {
        callback.@it.mate.phgcommons.client.utils.callbacks.StringCallback::handle(Ljava/lang/String;)(null);
      });
      $wnd.navigator.globalization.getLocaleName(jsSuccessCallback,jsErrorCallback);    
    }
  }-*/;
  
}
