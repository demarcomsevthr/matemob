package it.mate.phgcommons.client.plugins;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.phgcommons.client.utils.JSONUtils;
import it.mate.phgcommons.client.utils.OsDetectionUtils;
import it.mate.phgcommons.client.utils.PhgUtils;
import it.mate.phgcommons.client.utils.callbacks.JSOCallback;

import com.google.gwt.core.client.JavaScriptObject;


public class PushPlugin {
  
  /**
   *  DOCUMENTATION
   *  

      > PLUGIN >> https://github.com/phonegap/phonegap-plugin-push
      
        >> IDS PER GCM: si ottengono dalla Google Developer Console
            >> Sender ID = Project ID
            >> Server Key
      
        > WORKFLOW GCM:
          1 > device si registra su GCM inviando Sender ID (project id)
          2 > GCM invia a device Registration ID (onNotification callback)
          3 > device invia al server il RegId, il server lo memorizza nei dati account
          4 > server invia messaggio a GCM con RegId e Server Key
          5 > device riceve notifica in onNotification callback
      
        > ESEMPIO COMPLETO
          > http://www.androidhive.info/2012/10/android-push-notifications-using-google-cloud-messaging-gcm-php-and-mysql/  (OLD)
          > http://www.androidhive.info/2016/02/android-push-notifications-using-gcm-php-mysql-realtime-chat-app-part-2/  (NEW)
            >>> esempio completo in android / php (http)
            >>> come configurare Android Emulator (in fondo)

        > DOCUMENTAZIONE GCM:
          > GCM Overview >> https://developers.google.com/cloud-messaging/gcm
          > GCM Getting Started >> https://developers.google.com/cloud-messaging/android/start
          > GCM Implementing Server >> https://developers.google.com/cloud-messaging/server
          > GCM HTTP Server Implementation (esempio con appengine) >> https://developers.google.com/cloud-messaging/http
          
        > 
          

   *  
   *  
   */
  
  public static native boolean isInstalled () /*-{
    return typeof ($wnd.PushNotification) != 'undefined';
  }-*/;

  public static void register(String senderId, boolean gcmSandbox, final Delegate<PushNotification> delegate) {
    String osType = null;
    if (OsDetectionUtils.isAndroid()) {
      PhgUtils.log("Push Plugin - registering android");
      osType = "android";
    } else if (OsDetectionUtils.isIOs()) {
      boolean gcmUseAPNSDirect = Boolean.parseBoolean(GwtUtils.getJSVar("gcmUseAPNSDirect", "false"));
      if (gcmUseAPNSDirect) {
        PhgUtils.log("Push Plugin - registering ios-apns");
        osType = "ios-apns";
      } else {
        PhgUtils.log("Push Plugin - registering ios");
        osType = "ios";
      }
    } else {
      PhgUtils.log("Push Plugin - os type not allowed!");
      return;
    }
    PhgUtils.log("Push Plugin - registering pluging with senderId " + senderId);
    registerImpl(senderId, osType, gcmSandbox, new JSOCallback() {
      public void handle(JavaScriptObject e) {
        PhgUtils.log("Push Plugin - Registration callback receive: " + JSONUtils.stringify(e));
        delegate.execute(parseNotificationEvent(PushNotification.REGISTRATION_EVENT_NAME, e));
      } 
    }, new JSOCallback() {
      public void handle(JavaScriptObject e) {
        PhgUtils.log("Push Plugin - Notification callback receive: " + JSONUtils.stringify(e));
        delegate.execute(parseNotificationEvent(PushNotification.NOTIFICATION_EVENT_NAME, e));
      }
    }, new JSOCallback() {
      public void handle(JavaScriptObject e) {
        PhgUtils.log("Push Plugin - Error callback receive: " + JSONUtils.stringify(e));
        delegate.execute(parseNotificationEvent(PushNotification.ERROR_EVENT_NAME, e));
      }
    });
  }
  
  private static native void registerImpl(String senderId, String osType, boolean gcmSandbox, JSOCallback registrationCallback, JSOCallback notificationCallback, JSOCallback errorCallback) /*-{
    if (osType == 'android') {
      $wnd._push = $wnd.PushNotification.init({
          android: {
              senderID: senderId
          },
          ios: {
              alert: "true",
              badge: true,
              sound: 'false'
          },
          windows: {}
      });
    } else if (osType == 'ios') {
      $wnd._push = $wnd.PushNotification.init({
          android: {},
          ios: {
              senderID: senderId,
              gcmSandbox: gcmSandbox,
              alert: "true",
              badge: true,
              sound: 'false'
          },
          windows: {}
      });
    } else if (osType == 'ios-apns') {
      $wnd._push = $wnd.PushNotification.init({
          android: {},
          ios: {
              alert: "true",
              badge: true,
              sound: 'false'
          },
          windows: {}
      });
    } else {
      return;
    }
    var jsRegistrationHandler = $entry(function(data) {
      registrationCallback.@it.mate.phgcommons.client.utils.callbacks.JSOSuccess::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(data);
    });
    var jsNotificationHandler = $entry(function(data) {
      notificationCallback.@it.mate.phgcommons.client.utils.callbacks.JSOSuccess::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(data);
    });
    var jsErrorHandler = $entry(function(e) {
      errorCallback.@it.mate.phgcommons.client.utils.callbacks.JSOSuccess::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(e);
    });
    
    $wnd._push.on('error', jsErrorHandler);
    $wnd._push.on('registration', jsRegistrationHandler);
    $wnd._push.on('notification', jsNotificationHandler);
    
  }-*/;
  
  private static PushNotification parseNotificationEvent(String eventName,JavaScriptObject data) {
    PushNotification notification = new PushNotification();
    notification.setEventName(eventName);
    if (notification.isRegistrationEvent()) {
      String regId = GwtUtils.getJsPropertyString(data, "registrationId");
      notification.setRegId(regId);
    } else if (notification.isNotificationEvent()) {
      String message = GwtUtils.getJsPropertyString(data, "message");
      notification.setMessage(message);
    } else if (notification.isErrorEvent()) {
      String message = GwtUtils.getJsPropertyString(data, "message");
      notification.setMessage(message);
    }
    return notification;
  }
  
  private static native void registerIosImpl() /*-{
    
  }-*/;
  
}
