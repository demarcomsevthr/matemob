package it.mate.phgcommons.client.plugins;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.phgcommons.client.utils.JSONUtils;
import it.mate.phgcommons.client.utils.PhgUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.i18n.client.DateTimeFormat;


/**
 * 
 * SEE https://github.com/katzer/cordova-plugin-local-notifications
 * 
 *
 */


public class LocalNotificationsPlugin {
  
  private static final DateTimeFormat dtmFMT = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
  
  private static Date jsStringToDate(String sDate) {
    if (sDate != null) {
      return dtmFMT.parse(sDate);
    } else {
      return null;
    }
  }
  
  public static native boolean isInstalled () /*-{
    return typeof ($wnd.plugin) != 'undefined' && 
           typeof ($wnd.plugin.notification) != 'undefined' && 
           typeof ($wnd.plugin.notification.local) != 'undefined';
  }-*/;
  
  public static void createEvent(CalendarEvent event) {
    JSONUtils.ensureStringify();
    PhgUtils.log("creating event " + event);
    if (!isInstalled()) {
      return;
    }
    createEventImpl(event.getNotificationId(), event.getStartDate().getTime(), event.getNotes(), event.getTitle(), new JSOCallback() {
      public void handleEvent(JavaScriptObject jso) {
        PhgUtils.log("LocalNotificationsPlugin - Success");
      }
    });
  }
  
  private static native void createEventImpl (String id, double date, String message, String title, JSOCallback success) /*-{
    var jsDate = new Date(date);
    var jsSuccess = $entry(function(message) {
      success.@it.mate.phgcommons.client.plugins.LocalNotificationsPlugin.JSOCallback::handleEvent(Lcom/google/gwt/core/client/JavaScriptObject;)();
    });
    $wnd.plugin.notification.local.add({
        id:         id,      // A unique id of the notifiction
        date:       jsDate,  // This expects a date object
        message:    message, // The message that is displayed
        title:      title,   // The title of the message
        // repeat:     String,  // Either 'secondly', 'minutely', 'hourly', 'daily', 'weekly', 'monthly' or 'yearly'
        // badge:      Number,  // Displays number badge to notification
        // sound:      String,  // A sound to be played
        // json:       String,  // Data to be passed through the notification
        // autoCancel: true,    // Setting this flag and the notification is automatically canceled when the user clicks it
        // ongoing:    Boolean, // Prevent clearing of notification (Android only)
    }, jsSuccess);
  }-*/;
  
  public static void deleteEvent(CalendarEvent event) {
    JSONUtils.ensureStringify();
    PhgUtils.log("deleting event " + event);
    if (!isInstalled()) {
      return;
    }
    deleteEventImpl(event.getNotificationId(), new JSOCallback() {
      public void handleEvent(JavaScriptObject jso) {
        PhgUtils.log("LocalNotificationsPlugin - Success");
      }
    });
  }
  
  private static native void deleteEventImpl (String id, JSOCallback success) /*-{
    var jsSuccess = $entry(function(message) {
      success.@it.mate.phgcommons.client.plugins.LocalNotificationsPlugin.JSOCallback::handleEvent(Lcom/google/gwt/core/client/JavaScriptObject;)();
    });
    $wnd.plugin.notification.local.cancel(id, jsSuccess);
  }-*/;

  public static void getScheduledIds(final Delegate<List<String>> delegate) {
    JSONUtils.ensureStringify();
    PhgUtils.log("getting scheduled ids");
    if (!isInstalled()) {
      return;
    }
    getScheduledIdsImpl(new JSOCallback() {
      public void handleEvent(JavaScriptObject ids) {
        PhgUtils.log("LocalNotificationsPlugin - Scheduled Ids = " + JSONUtils.stringify(ids));
        if (ids != null) {
          JsArrayString jsa = ids.cast();
          List<String> results = new ArrayList<String>();
          for (int it = 0; it < jsa.length(); it++) {
            results.add(jsa.get(it));
          }
          delegate.execute(results);
        }
      }
    });
  }
  
  private static native void getScheduledIdsImpl (JSOCallback success) /*-{
    var jsSuccess = $entry(function(ids) {
      success.@it.mate.phgcommons.client.plugins.LocalNotificationsPlugin.JSOCallback::handleEvent(Lcom/google/gwt/core/client/JavaScriptObject;)(ids);
    });
    $wnd.plugin.notification.local.getScheduledIds(jsSuccess);
  }-*/;

  protected static interface JSOCallback {
    public void handleEvent(JavaScriptObject jso);
  }
  
  public static interface JSOEventCallback {
    public void handleEvent(String id, String state, String json);
  }
  
  public static void setOnCancel (JSOEventCallback callback) {
    setOnCancelImpl(callback);
  }
  
  private static native void setOnCancelImpl (JSOEventCallback callback) /*-{
    var jsCallback = $entry(function(id, state, json) {
      callback.@it.mate.phgcommons.client.plugins.LocalNotificationsPlugin.JSOEventCallback::handleEvent(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(id, state, json);
    });
    $wnd.plugin.notification.local.oncancel = jsCallback;
  }-*/;

}
