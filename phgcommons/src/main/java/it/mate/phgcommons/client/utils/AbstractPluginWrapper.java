package it.mate.phgcommons.client.utils;

import it.mate.gwtcommons.client.utils.Delegate;

import com.google.gwt.core.client.JavaScriptObject;


public abstract class AbstractPluginWrapper {

  protected static void exec(String pluginName, String action, JavaScriptObject value, final Delegate<JavaScriptObject> delegate) {
    execImpl(pluginName, action, value, new JsCallback() {
      public void execute(JavaScriptObject jso) {
        delegate.execute(jso);
      }
    }, new JsCallback() {
      public void execute(JavaScriptObject jso) {
        PhgUtils.log("error executing plugin");
      }
    });
  }
  
  protected static void execString(String pluginName, String action, String value, final Delegate<String> delegate) {
    execStringImpl(pluginName, action, value, new JsStringCallback() {
      public void execute(String result) {
        delegate.execute(result);
      }
    }, new JsStringCallback() {
      public void execute(String jso) {
        PhgUtils.log("error executing plugin");
      }
    });
  }
  
  protected static void execVoid(String pluginName, String action, String value, final Delegate<Void> delegate) {
    execVoidImpl(pluginName, action, value, new JsVoidCallback() {
      public void execute() {
        if (delegate != null)
          delegate.execute(null);
      }
    }, new JsStringCallback() {
      public void execute(String jso) {
        PhgUtils.log("error executing plugin");
      }
    });
  }
  
  protected static interface JsCallback {
    public void execute(JavaScriptObject jso);
  }
  
  protected static interface JsStringCallback {
    public void execute(String value);
  }
  
  protected static interface JsVoidCallback {
    public void execute();
  }
  
  private static native void execImpl (String pluginName, String action, JavaScriptObject value, JsCallback success, JsCallback failure) /*-{
    var jsSuccess = $entry(function(results) {
      success.@it.mate.phgcommons.client.utils.AbstractPluginWrapper.JsCallback::execute(Lcom/google/gwt/core/client/JavaScriptObject;)(results);
    });
    var jsFailure = $entry(function(err) {
      failure.@it.mate.phgcommons.client.utils.AbstractPluginWrapper.JsCallback::execute(Lcom/google/gwt/core/client/JavaScriptObject;)(err);
    });
    $wnd.cordova.exec(jsSuccess, jsFailure, pluginName, action, [value]);
  }-*/;
  
  private static native void execStringImpl (String pluginName, String action, String value, JsStringCallback success, JsStringCallback failure) /*-{
    var jsSuccess = $entry(function(results) {
      success.@it.mate.phgcommons.client.utils.AbstractPluginWrapper.JsStringCallback::execute(Ljava/lang/String;)(results);
    });
    var jsFailure = $entry(function(err) {
      failure.@it.mate.phgcommons.client.utils.AbstractPluginWrapper.JsStringCallback::execute(Ljava/lang/String;)(err);
    });
    $wnd.cordova.exec(jsSuccess, jsFailure, pluginName, action, [value]);
  }-*/;

  private static native void execVoidImpl (String pluginName, String action, String value, JsVoidCallback success, JsStringCallback failure) /*-{
    var jsSuccess = $entry(function() {
      success.@it.mate.phgcommons.client.utils.AbstractPluginWrapper.JsVoidCallback::execute()();
    });
    var jsFailure = $entry(function(err) {
      failure.@it.mate.phgcommons.client.utils.AbstractPluginWrapper.JsStringCallback::execute(Ljava/lang/String;)(err);
    });
    $wnd.cordova.exec(jsSuccess, jsFailure, pluginName, action, [value]);
  }-*/;

  private static native void execSimulImpl (String action, JavaScriptObject result, JsCallback success, JsCallback failure) /*-{
    var jsSuccess = $entry(function(results) {
      success.@it.mate.phgcommons.client.utils.AbstractPluginWrapper.JsCallback::execute(Lcom/google/gwt/core/client/JavaScriptObject;)(results);
    });
    jsSuccess(result);
  }-*/;

}
