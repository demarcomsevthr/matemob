package it.mate.phgcommons.client.utils;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;


public class DatePickerPluginUtil {
  
  private final static String ACTION_SHOW_DATE_DIALOG = "showDateDialog";

  private final static String ACTION_SHOW_TIME_DIALOG = "showTimeDialog";

  private final static String ACTION_SHOW_CALENDAR_VIEW = "showCalendarView";

  public static void showDateDialog(Date value, final Delegate<Date> delegate) {
    execImpl(ACTION_SHOW_DATE_DIALOG, JsDateTimeResult.fromDate(value), new JsCallback() {
      public void execute(JavaScriptObject jso) {
        JsDateTimeResult res = JsDateTimeResult.from(jso);
        PhgUtils.log("received " + res.asString());
        delegate.execute(res.asDate());
      }
    }, new JsCallback() {
      public void execute(JavaScriptObject jso) {
        PhgUtils.log("error executing plugin");
      }
    });
  }
  
  public static void showCalendarView(Date value, final Delegate<Date> delegate) {
    execImpl(ACTION_SHOW_CALENDAR_VIEW, JsDateTimeResult.fromDate(value), new JsCallback() {
      public void execute(JavaScriptObject jso) {
        JsDateTimeResult res = JsDateTimeResult.from(jso);
        PhgUtils.log("received " + res.asString());
        delegate.execute(res.asDate());
      }
    }, new JsCallback() {
      public void execute(JavaScriptObject jso) {
        PhgUtils.log("error executing plugin");
      }
    });
  }
  
  public static void showTimeDialog(final Delegate<Time> delegate) {
    execImpl(ACTION_SHOW_TIME_DIALOG, null, new JsCallback() {
      public void execute(JavaScriptObject jso) {
        JsDateTimeResult res = JsDateTimeResult.from(jso);
        PhgUtils.log("received " + res.asString());
        delegate.execute(res.asTime());
      }
    }, new JsCallback() {
      public void execute(JavaScriptObject jso) {
        PhgUtils.log("error executing plugin");
      }
    });
  }
  
  protected static class JsDateTimeResult extends JavaScriptObject {
    protected JsDateTimeResult() { }
    public static JsDateTimeResult from(JavaScriptObject jso) {
      return jso.<JsDateTimeResult>cast();
    }
    public static JsDateTimeResult fromDate(Date date) {
      if (date == null)
        return null;
      JsDateTimeResult inst = Document.createObject().cast();
      inst.setYear(date.getYear() + 1900);
      inst.setMonth(date.getMonth());
      inst.setDay(date.getDate());
      return inst;
    }
    private native void setPropertyImpl (String name, Integer value) /*-{
      this[name] = value;
    }-*/;  
    public final int getYear() {
      return GwtUtils.getJsPropertyInt(this, "year");
    }
    public final void setYear (int value) {
      setPropertyImpl("year", value);
    }
    public final int getMonth() {
      return GwtUtils.getJsPropertyInt(this, "month");
    }
    public final void setMonth (int value) {
      setPropertyImpl("month", value);
    }
    public final int getDay() {
      return GwtUtils.getJsPropertyInt(this, "day");
    }
    public final void setDay (int value) {
      setPropertyImpl("day", value);
    }
    public final int getHour() {
      return GwtUtils.getJsPropertyInt(this, "hour");
    }
    public final int getMinute() {
      return GwtUtils.getJsPropertyInt(this, "minute");
    }
    @SuppressWarnings("deprecation")
    public final Date asDate() {
      return new Date(getYear() - 1900, getMonth(), getDay());
    }
    public final Time asTime() {
      return new Time(getHour(), getMinute());
    }
    public final String asString() {
      return "JsDateTimeResult [year=" + getYear() + ", month=" + getMonth() + ", day=" + getDay() + ", hour=" + getHour() + ", minute=" + getMinute() + "]";
    }
  }
  
  protected static interface JsCallback {
    public void execute(JavaScriptObject jso);
  }
  
  private static native void execImpl (String action, JsDateTimeResult value, JsCallback success, JsCallback failure) /*-{
    var jsSuccess = $entry(function(results) {
      success.@it.mate.phgcommons.client.utils.DatePickerPluginUtil.JsCallback::execute(Lcom/google/gwt/core/client/JavaScriptObject;)(results);
    });
    var jsFailure = $entry(function(err) {
      failure.@it.mate.phgcommons.client.utils.DatePickerPluginUtil.JsCallback::execute(Lcom/google/gwt/core/client/JavaScriptObject;)(err);
    });
    $wnd.cordova.exec(jsSuccess, jsFailure, "DatePickerPlugin", action, [value]);
  }-*/;
  
  private static native void execSimulImpl (String action, JsCallback success, JsCallback failure) /*-{
    var jsSuccess = $entry(function(results) {
      success.@it.mate.phgcommons.client.utils.DatePickerPluginUtil.JsCallback::execute(Lcom/google/gwt/core/client/JavaScriptObject;)(results);
    });
    var result = {"year": 2013, "month":11, "day":14};
    jsSuccess(result);
  }-*/;

}
