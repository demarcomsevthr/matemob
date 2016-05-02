package it.mate.phgcommons.client.plugins;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.phgcommons.client.utils.callbacks.JSOCallback;
import it.mate.phgcommons.client.utils.callbacks.JSOSuccess;
import it.mate.phgcommons.client.utils.callbacks.VoidCallback;

import com.google.gwt.core.client.JavaScriptObject;

/*
 * SEE https://github.com/chariotsolutions/phonegap-nfc
 */

public class NfcPlugin {

  private static JavaScriptObject registeredListenerCallback = null;
  
  public static native boolean isInstalled () /*-{
    return typeof ($wnd.nfc) != 'undefined';
  }-*/;

  public static boolean isNdefListenerRegistered() {
    return registeredListenerCallback != null;
  }
  
  public static void addNdefListener(final Delegate<String> delegate) {
    addNdefListenerImpl(new JSOSuccess() {
      public void handle(JavaScriptObject jso) {
        delegate.execute("received tag");
      }
    }, new VoidCallback() {
      public void handle() {
        
      }
    }, new JSOCallback() {
      public void handle(JavaScriptObject jso) {
        
      }
    });
  }
  
  private static native void addNdefListenerImpl (JSOSuccess callback, VoidCallback success, JSOCallback failure) /*-{
    var jsCallback = $entry(function(nfcEvent) {
      callback.@it.mate.phgcommons.client.utils.callbacks.JSOSuccess::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(nfcEvent);
    });
    var jsSuccess = $entry(function() {
      @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)("nfc listener added");
      success.@it.mate.phgcommons.client.utils.callbacks.VoidCallback::handle()();
    });
    var jsFailure = $entry(function(error) {
      var ers = @it.mate.phgcommons.client.utils.JSONUtils::stringify(Lcom/google/gwt/core/client/JavaScriptObject;)(error);
      @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)("nfc listener error - " + ers);
      failure.@it.mate.phgcommons.client.utils.callbacks.JSOCallback::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(error);
    });
    $wnd.nfc.addNdefListener (jsCallback, jsSuccess, jsFailure);
    @it.mate.phgcommons.client.plugins.NfcPlugin::registeredListenerCallback = jsCallback;
  }-*/;

  public static void removeNdefListener(final Delegate<Void> delegate) {
    removeNdefListenerImpl(registeredListenerCallback);
    GwtUtils.deferredExecution(500, new Delegate<Void>() {
      public void execute(Void element) {
        registeredListenerCallback = null;
        delegate.execute(null);
      }
    });
  }
  
  private static native void removeNdefListenerImpl (JavaScriptObject jsCallback) /*-{
    $wnd.nfc.removeNdefListener (jsCallback);
  }-*/;

}
