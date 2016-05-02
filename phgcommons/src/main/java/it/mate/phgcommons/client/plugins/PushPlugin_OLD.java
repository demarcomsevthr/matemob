package it.mate.phgcommons.client.plugins;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.phgcommons.client.utils.JSONUtils;
import it.mate.phgcommons.client.utils.OsDetectionUtils;
import it.mate.phgcommons.client.utils.PhgUtils;
import it.mate.phgcommons.client.utils.callbacks.JSOCallback;

import com.google.gwt.core.client.JavaScriptObject;


public class PushPlugin_OLD {
  
  /**
   *  DOCUMENTATION
   *  

      > PLUGIN >> https://github.com/phonegap-build/PushPlugin
      
        >> IDS PER GCM: si ottengono dalla Google Developer Console
            >> Sender ID = Project ID
            >> Server Key
      
        > WORKFLOW GCM:
          1 > device si registra su GCM inviando Sender ID (project id)
          2 > GCM invia a device Registration ID (onNotification callback)
          3 > device invia al server il RegId, il server lo memorizza nei dati account
          4 > server invia messaggio a GCM con RegId e Server Key
          5 > device riceve notifica in onNotification callback
      
        > http://www.androidhive.info/2012/10/android-push-notifications-using-google-cloud-messaging-gcm-php-and-mysql/
            >>> esempio completo in android / php (http)
            >>> come configurare Android Emulator (in fondo)

        > DOCUMENTAZIONE GCM:
          > GCM Overview >> http://developer.android.com/google/gcm/gcm.html
          > GCM Getting Started >> http://developer.android.com/google/gcm/gs.html
          > GCM Implementing Server >> http://developer.android.com/google/gcm/server.html
          > GCM HTTP Server Implementation (esempio con appengine) >> http://developer.android.com/google/gcm/http.html
          

   *  
   *  
   */
  
  public static native boolean isInstalled () /*-{
    return typeof ($wnd.plugins) != 'undefined' && typeof ($wnd.plugins.pushNotification) != 'undefined';
  }-*/;

  public static void register(String senderId, final Delegate<PushNotificationOld> delegate) {
    if (OsDetectionUtils.isAndroid()) {
      PhgUtils.log("Push Plugin - registering android with " + senderId);
      registerAndroidImpl(senderId, new JSOCallback() {
        public void handle(JavaScriptObject e) {
          PhgUtils.log("Push Plugin - Notification callback receive: " + JSONUtils.stringify(e));
          delegate.execute(parseNotificationEvent(e));
        } 
      }, new JSOCallback() {
        public void handle(JavaScriptObject result) {
          PhgUtils.log("Push Plugin - Success callback receive: " + JSONUtils.stringify(result));
        }
      }, new JSOCallback() {
        public void handle(JavaScriptObject error) {
          PhgUtils.log("Push Plugin - Failure callback receive: " + JSONUtils.stringify(error));
        }
      });
    } else {
      PhgUtils.log("Push Plugin - registering ios");
      registerIosImpl();
    }
  }
  
  private static PushNotificationOld parseNotificationEvent(JavaScriptObject e) {
    PushNotificationOld notification = new PushNotificationOld();
    String event = GwtUtils.getJsPropertyString(e, "event");
    notification.setEventName(event);
    if (notification.isRegistrationEvent()) {
      String regId = GwtUtils.getJsPropertyString(e, "regid");
      notification.setRegId(regId);
    } else if (notification.isMessageEvent()) {
      String message = GwtUtils.getJsPropertyString(e, "message");
      notification.setMessage(message);
    }
    return notification;
  }
  
  private static native void registerAndroidImpl(String senderId, JSOCallback notificationCallback, JSOCallback success, JSOCallback failure) /*-{
    $wnd.globalPushNotificationCallback = $entry(function(e) {
      notificationCallback.@it.mate.phgcommons.client.utils.callbacks.JSOSuccess::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(e);
    });
    var jsSuccessHandler = $entry(function(result) {
      success.@it.mate.phgcommons.client.utils.callbacks.JSOSuccess::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(result);
    });
    var jsErrorHandler = $entry(function(error) {
      failure.@it.mate.phgcommons.client.utils.callbacks.JSOSuccess::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(error);
    });
    $wnd.plugins.pushNotification.register(jsSuccessHandler, jsErrorHandler, {
       "senderID" : senderId,
       "ecb" : "globalPushNotificationCallback"
      }); 
  }-*/;
  
  private static native void registerIosImpl() /*-{
    
  }-*/;
  
}
