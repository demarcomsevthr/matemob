package it.mate.phgcommons.client.plugins;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.phgcommons.client.utils.JSONUtils;
import it.mate.phgcommons.client.utils.PhgUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.i18n.client.DateTimeFormat;


/**
 * 
 * SEE https://github.com/EddyVerbruggen/Calendar-PhoneGap-Plugin
 * 
 *
 */


public class CalendarPlugin {
  
  /*
  public static class Event {
    private String title;
    private String location = "";
    private String notes = "";
    private Date startDate;
    private Date endDate;
    @Override
    public String toString() {
      return "Event [title=" + title + ", location=" + location + ", notes=" + notes + ", startDate=" + startDate + ", endDate=" + endDate + "]";
    }
    public String getTitle() {
      return title;
    }
    public void setTitle(String title) {
      this.title = title;
    }
    public String getLocation() {
      return location;
    }
    public void setLocation(String location) {
      this.location = location;
    }
    public String getNotes() {
      return notes;
    }
    public void setNotes(String notes) {
      this.notes = notes;
    }
    public Date getStartDate() {
      return startDate;
    }
    public void setStartDate(Date startDate) {
      this.startDate = startDate;
    }
    public Date getEndDate() {
      return endDate;
    }
    public void setEndDate(Date endDate) {
      this.endDate = endDate;
    }
  }
  */
  
  private static final DateTimeFormat dtmFMT = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
  
  private static Date jsStringToDate(String sDate) {
    if (sDate != null) {
      return dtmFMT.parse(sDate);
    } else {
      return null;
    }
  }
  
  public static void createEvent(CalendarEvent event) {
    JSONUtils.ensureStringify();
    PhgUtils.log("creating event " + event);
    
    callPluginWithOptionsImpl("createEventWithOptions", event.getTitle(), event.getLocation(), event.getNotes(), event.getStartDate().getTime(), event.getEndDate().getTime(), event.getFirstReminderMinutes(),  new JSOSuccess() {
      public void handleEvent(JavaScriptObject results) {
        PhgUtils.log("Success - " + JSONUtils.stringify(results));
      }
    }, new JSOFailure() {
      public void handleEvent(JavaScriptObject results) {
        PhgUtils.log("Failure - " + JSONUtils.stringify(results));
      }
    });
    
    /*
    if (OsDetectionUtils.isAndroid()) {
      callPluginWithOptionsImpl("createEventWithOptions", event.getTitle(), event.getLocation(), event.getNotes(), event.getStartDate().getTime(), event.getEndDate().getTime(), new JSOSuccess() {
        public void handleEvent(JavaScriptObject results) {
          PhgUtils.log("Success - " + JSONUtils.stringify(results));
        }
      }, new JSOFailure() {
        public void handleEvent(JavaScriptObject results) {
          PhgUtils.log("Failure - " + JSONUtils.stringify(results));
        }
      });
    } else {
      callPluginImpl("createEvent", event.getTitle(), event.getLocation(), event.getNotes(), event.getStartDate().getTime(), event.getEndDate().getTime(), new JSOSuccess() {
        public void handleEvent(JavaScriptObject results) {
          PhgUtils.log("Success - " + JSONUtils.stringify(results));
        }
      }, new JSOFailure() {
        public void handleEvent(JavaScriptObject results) {
          PhgUtils.log("Failure - " + JSONUtils.stringify(results));
        }
      });
    }
    */
    
  }
  
  public static void deleteEvent(CalendarEvent event) {
    JSONUtils.ensureStringify();
    PhgUtils.log("deleting event " + event);
    callPluginImpl("deleteEvent", event.getTitle(), event.getLocation(), event.getNotes(), event.getStartDate().getTime(), event.getEndDate().getTime(), new JSOSuccess() {
      public void handleEvent(JavaScriptObject results) {
        PhgUtils.log("Success - " + JSONUtils.stringify(results));
      }
    }, new JSOFailure() {
      public void handleEvent(JavaScriptObject results) {
        PhgUtils.log("Failure - " + JSONUtils.stringify(results));
      }
    });
  }
  
  public static void findEvent(CalendarEvent event, final Delegate<List<CalendarEvent>> delegate) {
    JSONUtils.ensureStringify();
    if (event.getTitle() == null)
      event.setTitle("");
    if (event.getLocation() == null)
      event.setLocation("");
    if (event.getNotes() == null)
      event.setNotes("");
    PhgUtils.log("finding event " + event);
    callPluginImpl("findEvent", event.getTitle(), event.getLocation(), event.getNotes(), event.getStartDate().getTime(), event.getEndDate().getTime(), new JSOSuccess() {
      public void handleEvent(JavaScriptObject results) {
        PhgUtils.log("Success - " + JSONUtils.stringify(results));
        JsArray<JavaScriptObject> jsEvents = results.cast();
        List<CalendarEvent> events = new ArrayList<CalendarEvent>();
        for (int it = 0; it < jsEvents.length(); it++) {
          JavaScriptObject jsEvent = jsEvents.get(it);
          CalendarEvent event = new CalendarEvent(); 
          event.setTitle(GwtUtils.getJsPropertyString(jsEvent, "title"));
          event.setStartDate(jsStringToDate(GwtUtils.getJsPropertyString(jsEvent, "startDate")));
          event.setEndDate(jsStringToDate(GwtUtils.getJsPropertyString(jsEvent, "endDate")));
          event.setNotes(GwtUtils.getJsPropertyString(jsEvent, "message"));
          event.setLocation(GwtUtils.getJsPropertyString(jsEvent, "location"));
          events.add(event);
        }
        delegate.execute(events);
      }
    }, new JSOFailure() {
      public void handleEvent(JavaScriptObject results) {
        PhgUtils.log("Failure - " + JSONUtils.stringify(results));
      }
    });
  }

  private static native void callPluginImpl (String methodName, String title, String location, String notes, double startTime, double endTime, JSOSuccess success, JSOFailure failure) /*-{
    var jsSuccess = $entry(function(message) {
      success.@it.mate.phgcommons.client.plugins.CalendarPlugin.JSOSuccess::handleEvent(Lcom/google/gwt/core/client/JavaScriptObject;)(message);
    });
    var jsFailure = $entry(function(message) {
      failure.@it.mate.phgcommons.client.plugins.CalendarPlugin.JSOFailure::handleEvent(Lcom/google/gwt/core/client/JavaScriptObject;)(message);
    });
    $wnd.cordova.exec(jsSuccess, jsFailure, "Calendar", methodName, [{
      "title": title,
      "location": location,
      "notes": notes,
      "startTime": startTime,
      "endTime": endTime
    }]);
  }-*/;

  private static native void callPluginWithOptionsImpl (String methodName, String title, String location, String notes, double startTime, double endTime, int firstReminderMinutes, JSOSuccess success, JSOFailure failure) /*-{
    var jsSuccess = $entry(function(message) {
      success.@it.mate.phgcommons.client.plugins.CalendarPlugin.JSOSuccess::handleEvent(Lcom/google/gwt/core/client/JavaScriptObject;)(message);
    });
    var jsFailure = $entry(function(message) {
      failure.@it.mate.phgcommons.client.plugins.CalendarPlugin.JSOFailure::handleEvent(Lcom/google/gwt/core/client/JavaScriptObject;)(message);
    });
    $wnd.cordova.exec(jsSuccess, jsFailure, "Calendar", methodName, [
      {
        "title": title,
        "location": location,
        "notes": notes,
        "startTime": startTime,
        "endTime": endTime,
        "options": {
          "calendarName": null,
          "firstReminderMinutes": firstReminderMinutes,
          "secondReminderMinutes": 0,
          "recurrence": null,
          "recurrenceEndTime": null
        }
      }
    ]);
  }-*/;

  protected static interface JSOCallback {
    public void handleEvent(JavaScriptObject jso);
  }

  protected static interface JSOSuccess extends JSOCallback { }

  protected static interface JSOFailure extends JSOCallback { }

}
