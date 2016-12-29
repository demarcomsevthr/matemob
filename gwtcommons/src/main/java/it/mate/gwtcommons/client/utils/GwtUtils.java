package it.mate.gwtcommons.client.utils;

import it.mate.gwtcommons.client.ui.MessageBox;
import it.mate.gwtcommons.client.ui.Spacer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.animation.client.AnimationScheduler.AnimationCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsDate;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.web.bindery.event.shared.Event;

public class GwtUtils {
  
  private static int mockCounter = 0;
  
  private static Map<String, Object> dictionary;

  /*
  private final static DateTimeFormat dt10FMT = DateTimeFormat.getFormat("dd/MM/yyyy");
  private final static DateTimeFormat dt8FMT = DateTimeFormat.getFormat("yyyyMMdd");
  private final static DateTimeFormat dt8aFMT = DateTimeFormat.getFormat("ddMMyyyy");
  private final static DateTimeFormat dFMT = DateTimeFormat.getFormat("dd");
  private final static DateTimeFormat mFMT = DateTimeFormat.getFormat("MM");
  private final static DateTimeFormat m3FMT = DateTimeFormat.getFormat("MMM");
  private final static DateTimeFormat yFMT = DateTimeFormat.getFormat("yyyy");
  private final static DateTimeFormat eFMT = DateTimeFormat.getFormat("EEE");
  private final static DateTimeFormat logFMT = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm:ss,SSS");
  */
  
  private final static String dt10Pattern = "dd/MM/yyyy";
  private final static String dt8Pattern = "yyyyMMdd";
  private final static String dt8aPattern = "ddMMyyyy";
  private final static String dPattern = "dd";
  private final static String mPattern = "MM";
  private final static String m3Pattern = "MMM";
  private final static String yPattern = "yyyy";
  private final static String ePattern = "EEE";
  private final static String logPattern = "dd/MM/yyyy HH:mm:ss,SSS";
  private final static String defaultDTPattern = dt10Pattern;
  
  private static final Map<String, DateTimeFormat> fmtCache = new HashMap<String, DateTimeFormat>();
  
  private static DateTimeFormat defaultDTFormat = DateTimeFormat.getFormat(defaultDTPattern);

  private final static NumberFormat currencyFMT = NumberFormat.getFormat("0.00");
  
  private final static NumberFormat decimalFMT = NumberFormat.getFormat("0.00");
  
  private static final String ATTR_CLIENT_WAIT_PANEL = "common.client.waitPanel";
  
  private static final String ATTR_CLIENT_DEFAULT_WAIT_PANEL = "common.client.defaultWaitPanel";
  
  private static int showWaitPanelRequestCounter = 0;
  
  private static String[] engMM = new String[] {"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"};
  
  private static String[] itaMM = new String[] {"gen", "feb", "mar", "apr", "mag", "giu", "lug", "ago", "set", "ott", "nov", "dic"};
  
  private static String[] itaMMMM = new String[] {"gennaio", "febbraio", "marzo", "aprile", "maggio", "giugno", "luglio", "agosto", "settembre", "ottobre", "novembre", "dicembre"};

  private static boolean devModeActive = Window.Location.getQueryString().contains("gwt.codesvr");
  
  private static boolean mobileOptimizations = false;
  
  private static boolean enableLogInProductionMode = false;
  
  private static boolean useAvailableElementsCache = false;

  protected static int mobileTimerSchedulerImplId = -1;
  
  protected final static long MAX_MOBILE_TIMER_SCHEDULER_AUTO_GARBAGE_TIME = 30000;
  
  protected final static long MAX_WAIT_FOR_AVAILABLE_ELEMENTS = 30000;
  
  private static Map<String, Element> availableElementsCache = new HashMap<String, Element>();
  
  
  
  public static String getPortletContextPath() {
    return GwtUtils.getJSVar("contextPath","");
  }
  
  public static native String getJSVar(String jsVar, String defaultValue) /*-{
		var value = eval('$wnd.' + jsVar);
		if (value) {
			return value;
		}
		return defaultValue;
  }-*/;
  
  public static native String getNavigatorAppName() /*-{
    return $wnd.navigator.appName;
  }-*/;
  
  public static native String getNavigatorUserAgent() /*-{
    return $wnd.navigator.userAgent;
  }-*/;
  
  public static native String getLocationHref() /*-{
    return $wnd.location.href;
  }-*/;
  
  public static boolean isIE () {
    System.out.println("NavigatorUserAgent = " + getNavigatorUserAgent());
    return getNavigatorUserAgent().contains("MSIE");
  }

  public static boolean isChrome () {
    return getNavigatorUserAgent().toLowerCase().contains("chrome");
  }

  public static boolean isGecko () {
    return getNavigatorUserAgent().toLowerCase().contains("gecko");
  }
  
  public static boolean isDevMode() {
    return devModeActive;
  }
  
  public static String getHostPageBaseURL() {
    return GWT.getHostPageBaseURL();
  }

  public static String getModuleBaseURL() {
    return GWT.getModuleBaseURL();
  }

  @SuppressWarnings("deprecation")
  public static Date getDate0000 (Date date) {
    int d = date.getDate();
    int m = date.getMonth();
    int y = date.getYear();
    return new Date(y, m, d);
  }
  
  @SuppressWarnings("deprecation")
  public static Date getDate2359 (Date date) {
    int d = date.getDate();
    int m = date.getMonth();
    int y = date.getYear();
    return new Date(y, m, d, 23, 59, 59);
  }
  
  public static String formatCurrency (Double number) {
    if (number == null)
      return "";
    return currencyFMT.format(number);
  }
  
  public static String replace(String text, String regex, String replacement) {
    return text.replaceAll(regex, replacement);
  }
  
  public static String replaceEx(String text, String what, String replacement) {
    int pos = -1;
    while ((pos = text.indexOf(what)) > -1) {
      String t1 = text.substring(0, pos);
      String t2 = "";
      if (pos + what.length() < text.length()) {
        t2 = text.substring(pos + what.length());
      }
      text = t1 + replacement + t2;
    }
    return text;
  }
  
  public static Double parseDecimal (String text) {
    return decimalFMT.parse(text);
  }
  
  public static String formatDecimal (Double number, int decimals) {
    return createDecimalFormat(decimals).format(number);
  }
  
  public static Double composeDouble (int intPart, int decimalPart, int decimals) {
    return createDecimalFormat(decimals).parse(intPart+"."+decimalPart);
  }
  
  private static NumberFormat createDecimalFormat(int decimals) {
    StringBuffer pattern = new StringBuffer("0");
    for (int ip = 0; ip < decimals; ip++) {
      if (ip == 0)
        pattern.append(".");
      pattern.append("0");
    }
    NumberFormat decimalFMT = NumberFormat.getFormat(pattern.toString());
    return decimalFMT;
  }
  
  public static int getDecimals(double value, int decimals) {
    String [] parts = createDecimalFormat(decimals).format(value).split("\\.");
    if (parts.length == 2) {
      return Integer.parseInt(parts[1]);
    } else {
      return 0;
    }
  }
  
  public static boolean isNumber(String text) {
    return StringUtils.isNumber(text);
  }
  
  public static NumberFormat getDefaultCurrencyFmt() {
    return currencyFMT;
  }
  
  public static Date getDate (int day, int month, int year) {
    return formatFromCache(dt10Pattern).parse(day+"/"+month+"/"+year);
  }
  
  public static int getDay (Date date) {
    return Integer.valueOf(formatFromCache(dPattern).format(date));
  }
  
  public static int getMonth (Date date) {
    return Integer.valueOf(formatFromCache(mPattern).format(date));
  }
  
  public static String getShortMonthName (Date date) {
    String mm = formatFromCache(m3Pattern).format(date);
    for (int it = 0; it < engMM.length; it++) {
      if (engMM[it].equalsIgnoreCase(mm)) {
        mm = itaMM[it];
      }
    }
    return mm.substring(0, 1).toUpperCase() + mm.substring(1);
  }
  
  public static String getMonthName (Date date) {
    return getMonthName(date, itaMMMM);
  }
  
  public static String getMonthName (Date date, String[] monthNames) {
    String mm = formatFromCache(m3Pattern).format(date);
    for (int it = 0; it < engMM.length; it++) {
      if (engMM[it].equalsIgnoreCase(mm)) {
        mm = monthNames[it];
      }
    }
    return mm.substring(0, 1).toUpperCase() + mm.substring(1);
  }
  
  public static int getYear (Date date) {
    return Integer.valueOf(formatFromCache(yPattern).format(date));
  }

  public static String getWeekDay (Date date) {
    return formatFromCache(ePattern).format(date);
  }
  
  public static void setDefaultDTFormat(DateTimeFormat defaultDTFormat) {
    GwtUtils.defaultDTFormat = defaultDTFormat;
  }

  public static String dateToString (Date date) {
    if (date == null)
      return null;
    return dateToString(date, defaultDTFormat);
  }
  
  public static String dateToString (Date date, int len) {
    if (len == 8)
      return dateToString(date, formatFromCache(dt8Pattern));
    if (len == 10)
      return dateToString(date, formatFromCache(dt10Pattern));
    return dateToString(date, defaultDTFormat);
  }
  
  public static String dateToString (Date date, DateTimeFormat fmt) {
    return fmt != null ? fmt.format(date) : defaultDTFormat.format(date);
  }
  
  public static String dateToString (Date date, String fmt) {
    return DateTimeFormat.getFormat(fmt).format(date);
  }
  
  public static boolean dateEquals(Date d1, Date d2, int precision) {
    DateTimeFormat dtf = DateTimeFormat.getFormat("yyyyMMddHHmmssSSSS".substring(0, precision));
    return dtf.format(d1).equals(dtf.format(d2));
  }

  private static String[] engDW = new String[] {"mon", "tue", "wed", "thu", "fri", "sat", "sun"};
  private static String[] itaDW = new String[] {"lun", "mar", "mer", "gio", "ven", "sab", "dom"};
  public static String getDayOfWeek(Date date) {
    String eee = GwtUtils.dateToString(date, formatFromCache(ePattern));
    for (int it = 0; it < engDW.length; it++) {
      if (engDW[it].equals(eee.toLowerCase())) {
        eee = itaDW[it];
      }
    }
    return eee.substring(0, 1).toUpperCase() + eee.substring(1);
  }
  
  public static int getDayNumberOfWeek(Date date) {
    String eee = GwtUtils.dateToString(date, formatFromCache(ePattern));
    for (int it = 0; it < engDW.length; it++) {
      if (engDW[it].equals(eee.toLowerCase())) {
        return it + 1;
      }
    }
    for (int it = 0; it < itaDW.length; it++) {
      if (itaDW[it].equals(eee.toLowerCase())) {
        return it + 1;
      }
    }
    throw new RuntimeException("Impossibile stabilire il giorno della settimana per la data " + dateToString(date));
  }
  
  public static Date stringToDate (String text) {
    return stringToDate(text, dt8Pattern);
  }
  
  public static Date stringToDate (String text, String pattern) {
    return formatFromCache(pattern).parse(text);
  }
  
  public static Element getElement (String id) {
    return DOM.getElementById(id);
  }
  
  public static void setStyleAttribute(com.google.gwt.dom.client.Element element, String attr, String value) {
    DOM.setStyleAttribute((com.google.gwt.user.client.Element)element, attr, value);
  }
  
  public static void setStyleAttribute(String id, String attr, String value) {
    DOM.setStyleAttribute(DOM.getElementById(id), attr, value);
  }
  
  public static <U extends UIObject> U setStyleAttribute(U widget, String attr, String value) {
    DOM.setStyleAttribute(widget.getElement(), attr, value);
    return widget;
  }
  
  public static <U extends UIObject> U setBorderRadius(U widget, String value) {
    setStyleAttribute(widget, "WebkitBorderRadius", value);
    setStyleAttribute(widget, "MozBorderRadius", value);
    setStyleAttribute(widget, "borderRadius", value);
    return widget;
  }
  
  public static <U extends UIObject> U setOpacity(U widget, int value) {
    if (GwtUtils.isIE()) {
      return GwtUtils.setStyleAttribute(widget, "filter", "alpha(opacity = "+value+")");
    } else {
      return GwtUtils.setStyleAttribute(widget, "opacity", "" + value);
    }
  }
  
  public static void onAvailable (Element element, final Delegate<Element> delegate) {
    ensureId(element);
    onAvailable(element.getId(), delegate);
  }
  
  public static void onAvailable (final String id, final Delegate<Element> delegate) {
    onAvailable(id, MAX_WAIT_FOR_AVAILABLE_ELEMENTS, delegate);
  }
  
  public static void onAvailable (final String id, final long maxWait, final Delegate<Element> delegate) {
    
    if (useAvailableElementsCache) {
      if (availableElementsCache.containsKey(id)) {
        GwtUtils.log("-- available element cache: reusing cached element id = " + id);
        delegate.execute(availableElementsCache.get(id));
      }
    }
    
    final long t0 = System.currentTimeMillis();
    new Timer() {
      public void run() {
        long t1 = System.currentTimeMillis();
        Element element = getElementById(id);
        if (element != null) {
          this.cancel();
          
          if (useAvailableElementsCache) {
            availableElementsCache.put(id, element);
          }
          
          delegate.execute(element);
        } else if (t1 - t0 > maxWait) {
          this.cancel();
        }
      }
    }.scheduleRepeating(100);
  }
  
  public static native Element getElementById(String elementId) /*-{
    return $doc.getElementById(elementId);
  }-*/;
  
  public static native NodeList<Element> getElementsByTagName(String tagName) /*-{
    return $doc.getElementsByTagName(tagName);
  }-*/;
  
  public static Element getFirstElementByTagName(String tagName) {
    NodeList<Element> nodes = getElementsByTagName(tagName);
    if (nodes != null && nodes.getLength() > 0) {
      return nodes.getItem(0);
    }
    return null;
  }
  
  public static Element getElement(Widget widget) {
    String id = widget.getElement().getId();
    if (id != null && !"".equals(id.trim())) {
      return getElementById(id);
    }
    return null;
  }
  
  
  protected abstract static class MobileTimerAnimationSchedulerImpl extends Timer {
    
    boolean cancelRequested = false;
    
    long lastShotTime;
    
    AnimationCallback animationCallback;
    
    boolean repeating = false;
    
    public MobileTimerAnimationSchedulerImpl() {
//    GwtUtils.log("creating new mobile timer");
    }

    @Override
    public void scheduleRepeating(int periodMillis) {
      repeating = true;
      startLoop(periodMillis);
    }
    
    @Override
    public void schedule(int delayMillis) {
      repeating = false;
      startLoop(delayMillis);
    }
    
    private void startLoop(final int periodMillis) {
      lastShotTime = System.currentTimeMillis();
      animationCallback = new AnimationCallback() {
        public void execute(double shotTime) {
//        GwtUtils.log("shot");
          if (cancelRequested)
            return;
          if (shotTime > lastShotTime + periodMillis) {
            lastShotTime = (long)shotTime;
            run();
            if (!repeating)
              cancelRequested = true;
          }
          AnimationScheduler.get().requestAnimationFrame(this);
        }
      };
      animationCallback.execute(lastShotTime);
    }

    @Override
    public void cancel() {
//    GwtUtils.log("cancel mobile timer");
      this.cancelRequested = true;
    }
    
  }
  
  protected abstract static class MobileTimerSchedulerImpl extends Timer {
    boolean cancelRequested = false;
    private int id;
    private long startTime;
    public MobileTimerSchedulerImpl() { 
      mobileTimerSchedulerImplId ++;
      this.id = mobileTimerSchedulerImplId;
      this.startTime = System.currentTimeMillis();
      //GwtUtils.log("-- MobileTimerSchedulerImpl - creating new mobile timer -- id " + this.id + " startTime = " + startTime);
    }
    @Override
    public void scheduleRepeating(int periodMillis) {
      Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
        public boolean execute() {
          //GwtUtils.log("-- MobileTimerSchedulerImpl - execute repeating -- id " + id);
          
          // 02/04/2015
          if (System.currentTimeMillis() > startTime + MAX_MOBILE_TIMER_SCHEDULER_AUTO_GARBAGE_TIME) {
            GwtUtils.log("-- MobileTimerSchedulerImpl - execute repeating -- AUTO GARBAGE TIME -- id " + id);
            return false;
          }
          
          if (!cancelRequested) {
            MobileTimerSchedulerImpl.this.run();
          }
          return !cancelRequested;
        }
      }, periodMillis);
    }
    @Override
    public void schedule(int periodMillis) {
      Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
        public boolean execute() {
          //GwtUtils.log("-- MobileTimerSchedulerImpl - execute -- id " + id);
          
          // 02/04/2015
          if (System.currentTimeMillis() > startTime + MAX_MOBILE_TIMER_SCHEDULER_AUTO_GARBAGE_TIME) {
            GwtUtils.log("-- MobileTimerSchedulerImpl - execute -- AUTO GARBAGE TIME -- id " + id);
            return false;
          }
          
          MobileTimerSchedulerImpl.this.run();
          return true;
        }
      }, periodMillis);
    }
    @Override
    public void cancel() {
      //GwtUtils.log("-- MobileTimerSchedulerImpl - cancel requested -- id " + id);
      this.cancelRequested = true;
    }
  }
  
  private static Timer createTimerImpl (int periodMillis, boolean runAndWait, final Delegate<Void> delegate) {
    Timer timer = null;
    if (mobileOptimizations) {
      //GwtUtils.log("-- create timer impl - periodMillis = " + periodMillis + " runAndWait = " + runAndWait);
      timer = new MobileTimerSchedulerImpl() {
        public void run() {
          //GwtUtils.log("-- run timer MobileTimerSchedulerImpl");
          delegate.execute(null);
        }
      };
    } else {
      timer = new Timer() {
        public void run() {
          //GwtUtils.log("-- run timer createTimerImpl");
          delegate.execute(null);
        }
      };
    }
    if (runAndWait) {
      delegate.execute(null);
    }
    return timer;
  }
  
  public static Timer createTimer (int periodMillis, final Delegate<Void> delegate) {
    return createTimer(periodMillis, false, delegate);
  }
  
  public static Timer createTimer (int periodMillis, boolean runAndWait, final Delegate<Void> delegate) {
    //GwtUtils.log("-- create timer ");
    Timer timer = createTimerImpl(periodMillis, runAndWait, delegate);
    timer.scheduleRepeating(periodMillis);
    return timer;
  }
  
  protected static class TimerWrapper {
    Timer timer = new Timer() {
      public void run() {
        //GwtUtils.log("-- run timer TimerWrapper fakeTimer");
      }
    };
    Delegate<Void> voidDelegate = null;
    protected TimerWrapper(int periodMillis, boolean runAndWait, final Delegate<Timer> timerDelegate) {
      voidDelegate = new Delegate<Void>() {
        public void execute(Void element) {
          timerDelegate.execute(timer);
        }
      };
      //GwtUtils.log("-- create timer wrapper ");
      timer = createTimerImpl(periodMillis, runAndWait, voidDelegate);
      timer.scheduleRepeating(periodMillis);
    }
  }
  
  public static void createTimerDelegate (int periodMillis, boolean runAndWait, final Delegate<Timer> delegate) {
    new TimerWrapper(periodMillis, runAndWait, delegate);
  }
  
  public static void deferredExecution (Delegate<Void> callback) {
    deferredExecution(0, callback);
  }
  
  public static void deferredExecution (final int delayMillis, final Delegate<Void> delegate) {
    if (mobileOptimizations) {
      if (delayMillis <= 0) {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
          public void execute() {
            //GwtUtils.log("-- run scheduler deferredExecution");
            delegate.execute(null);
          }
        });
      } else {
        Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
          public boolean execute() {
            //GwtUtils.log("-- run scheduler deferredExecution");
            delegate.execute(null);
            return false;
          }
        }, delayMillis);
      }
    } else {
      if (delayMillis > 0) {
        new Timer() {
          public void run() {
            //GwtUtils.log("-- run timer deferredExecution delayMillis = " + delayMillis);
            deferredExecution(0, delegate);
          }
        }.schedule(delayMillis);
        return;
      }
      createMockElement(new Delegate<Element>() {
        public void execute(Element element) {
          delegate.execute(null);
        }
      });
    }
  }
  
  private static void createMockElement (final Delegate<Element> callback) {
    mockCounter++;
    final SimplePanel div = new SimplePanel();
    String id = "ksdurcgnys"+mockCounter;
    div.getElement().setId(id);
    RootPanel.get().add(div);
    onAvailable(id, new Delegate<Element>() {
      public void execute(Element element) {
        callback.execute(element);
        RootPanel.get().remove(div);
      }
    });
  }
  
  public static void setFlexCellColSpan (FlexTable table, int row, int col, int value) {
    table.getFlexCellFormatter().setColSpan(row, col, value);
  }
  
  public static void setFlexCellRowSpan (FlexTable table, int row, int col, int value) {
    table.getFlexCellFormatter().setRowSpan(row, col, value);
  }
  
  public static void setFlexCellAlignment (FlexTable table, int row, int col, HorizontalAlignmentConstant hor, VerticalAlignmentConstant ver) {
    table.getFlexCellFormatter().setAlignment(row, col, hor, ver);
  }
  
  public static void setFlexCellHeight (FlexTable table, int row, int col, String value) {
    table.getFlexCellFormatter().setHeight(row, col, value);
  }
  
  public static void setFlexCellWidth (FlexTable table, int row, int col, String value) {
    table.getFlexCellFormatter().setWidth(row, col, value);
  }
  
  public static Element getFlexCellElement (FlexTable table, int row, int col) {
    return table.getFlexCellFormatter().getElement(row, col);
  }
  
  public static Element getFlexRowElement (FlexTable table, int row) {
    return table.getRowFormatter().getElement(row);
  }
  
  public static void setFlexCellBckColor (FlexTable table, int row, int col, String value) {
    DOM.setStyleAttribute((com.google.gwt.user.client.Element)GwtUtils.getFlexCellElement(table, row, col), "backgroundColor", value) ;
  }
  
  public static void setFlexCellStyleAttribute (FlexTable table, int row, int col, String attr, String value) {
    DOM.setStyleAttribute((com.google.gwt.user.client.Element)GwtUtils.getFlexCellElement(table, row, col), attr, value) ;
  }
  
  public static void setFlexCellClassName (FlexTable table, int row, int col, String className) {
    GwtUtils.getFlexCellElement(table, row, col).setClassName(className) ;
  }
  
  public static void setFlexRowClassName (FlexTable table, int row, String className) {
//  GwtUtils.getFlexRowElement(table, row).setClassName(className) ;
    for (int col = 0; col < table.getCellCount(row); col++) {
      GwtUtils.setFlexCellClassName(table, row, col, className);
    }
  }
  
  public static native String getContextURL(String ctx) /*-{
    var h = $doc.location.host;
    var p = $doc.location.protocol;
    if (ctx != '') {
      return p + "//" + h + "/" + ctx + "/";
    } else {
      return p + "//" + h + "/" ;
    }
  }-*/;
  
  public static List<Element> getChildsByTagName(Element element, String name) {
    List<Element> childs = new ArrayList<Element>();
    for (int it = 0; it < element.getChildCount(); it++) {
      Element child = (Element)element.getChild(it);
      if (name.equalsIgnoreCase(child.getNodeName())) {
        childs.add(child);
        childs.addAll(getChildsByTagName(child, name));
      }
    }
    return childs;
  }
  
  public static Element getChildByTagName(Element element, String name) {
    for (int it = 0; it < element.getChildCount(); it++) {
      Element child = (Element)element.getChild(it);
      if (name.equalsIgnoreCase(child.getNodeName())) {
        return child;
      }
    }
    return null;
  }
  
  public static Element findParentByTypeAndClass (Element element, String type, String className) {
    Element parent = element.getParentElement();
    if (parent == null)
      return null;
    if (type.equalsIgnoreCase(parent.getNodeName())) {
      String parClassName = parent.getClassName();
      if (parClassName != null && className.equalsIgnoreCase(parClassName)) {
        return parent;
      }
    }
    return findParentByTypeAndClass(parent, type, className);
  }
  
  public static Element getChildByType(Element element, String type) {
    for (int it = 0; it < element.getChildCount(); it++) {
      Element child = (Element)element.getChild(it);
      if (type.equalsIgnoreCase(child.getNodeName())) {
        return child;
      }
    }
    return null;
  }
  
  public static Label createHorizontalSpacer (int width) {
    return createSpacer(width, 1);
  }
  
  public static Label createVerticalSpacer (int height) {
    return createSpacer(1, height);
  }
  
  public static Label createSpacer (int width, int height) {
    Label label = new Label(" ");
    label.setPixelSize(width, height);
    return label;
  }
  
  public static void addEnterKeyPressHandler (FocusWidget widget, final Delegate<KeyPressEvent> delegate) {
    widget.addKeyPressHandler(new KeyPressHandler() {
      public void onKeyPress(KeyPressEvent event) {
        if (event.getNativeEvent().getKeyCode() == 13) {
          delegate.execute(event);
        }
      }
    });
  }
  
  private static Map<String, Object> getDictionary() {
    if (dictionary == null)
      dictionary = new HashMap<String, Object>();
    return dictionary;
  }
  
  public static void setDictionaryAttribute(String name, Object value) {
    getDictionary().put(name, value);
  }
  
  public static Object getDictionaryAttribute(String name) {
    return getDictionary().get(name);
  }
  
  @Deprecated
  public static void setClientAttribute(Object value) {
    setClientAttribute(value.getClass().getName(), value);
  }
  
  public static void setClientAttribute(String name, Object value) {
    getDictionary().put(name, value);
  }
  
  public static void removeClientAttribute(Class<?> type) {
    removeClientAttribute(type.getName());
  }
  
  public static void removeClientAttribute(String name) {
    getDictionary().remove(name);
  }
  
  @Deprecated
  @SuppressWarnings("unchecked")
  public static <T> T getClientAttribute(Class<T> type) {
    return (T)getClientAttribute(type.getName());
  }
  
  public static Object getClientAttribute(String name) {
    return getDictionary().get(name);
  }
  
  public static boolean existsClientAttribute(String name) {
    return getDictionary().containsKey(name);
  }
  
  public static void setParentWidth (Widget widget, String width) {
    DOM.setStyleAttribute((com.google.gwt.user.client.Element)widget.getElement().getParentElement(), "width", width); 
  }
  
  public static void setParentHeight (Widget widget, String height) {
    DOM.setStyleAttribute((com.google.gwt.user.client.Element)widget.getElement().getParentElement(), "height", height); 
  }
  
  public static void setFocus(final Focusable widget) {
    GwtUtils.deferredExecution(new Delegate<Void>() {
      public void execute(Void element) {
        widget.setFocus(true);
      }
    });
  }
  
  public static void setDateBoxHandler (final DateBox dateBox) {
    dateBox.getTextBox().addKeyPressHandler(new KeyPressHandler() {
      public void onKeyPress(KeyPressEvent event) {
        GwtUtils.deferredExecution(new Delegate<Void>() {
          public void execute(Void element) {
            TextBox textBox = dateBox.getTextBox();
            String text = textBox.getValue();
            if (text.length() == 8 && isDigit(text)) {
              try {
                Date date = formatFromCache(dt8Pattern).parse(text);
                textBox.setValue(formatFromCache(dt10Pattern).format(date));
                dateBox.hideDatePicker();
                dateBox.showDatePicker();
              } catch (Exception ex) {
              }
            } else {
              char[] chs = text.toCharArray();
              boolean tobechanged = false;
              for (int it = 0; it < chs.length; it++) {
                if (chs[it] >= '0' && chs[it] <= '9') {
                } else if (chs[it] == '/'){
                } else {
                  chs[it] = '/';
                  tobechanged = true;
                }
              }
              if (tobechanged) {
                text = new String(chs);
                textBox.setValue(text);
                dateBox.hideDatePicker();
                dateBox.showDatePicker();
              }
            }
          }
        });
      }
    });
  }
  
  private static boolean isDigit(String text) {
    for (int it = 0; it < text.length(); it++) {
      char ch = text.charAt(it);
      if (ch < '0' || ch > '9') {
        return false;
      }
    }
    return true;
  }
  
  public static boolean isEmpty(Object value) {
    if (value == null)
      return true;
    if (value instanceof String) {
      String text = (String)value;
      return text.trim().isEmpty();
    }
    if (value instanceof Collection) {
      Collection collection = (Collection)value;
      return collection.isEmpty();
    }
    if (value instanceof Object[]) {
      Object[] array = (Object[])value;
      return array.length == 0;
    }
    return false;
  }
  
  public static String purgeSpaces(String text) {
    return text.replace(" ", "");
  }

  public static Element getParentByTagName(Element elem, String name) {
    Element parent = elem.getParentElement();
    while (true) {
      if (parent == null)
        break;
      if (name.equalsIgnoreCase(parent.getNodeName())) {
        return parent;
      }
      parent = parent.getParentElement();
    }
    return null;
  }
  
  public static Element getHead() {
    NodeList<Element> elements = Document.get().getDocumentElement().getElementsByTagName("head");
    if (elements != null && elements.getLength() > 0) {
      Element head = elements.getItem(0);
      return head;
    }
    return null;
  }
  
  public static Panel createPopupPanelItem(String labelText, Widget box, String spacerHeight, String labelWidth) {
    HorizontalPanel panel = new HorizontalPanel();
    panel.add(new Spacer("1px", spacerHeight));
    Label label = new Label(labelText);
    label.setWidth(labelWidth);
    panel.add(label);
    panel.add(box);
    return panel;
  }
  
  public static MessageBox messageBox(String bodyText) {
    return messageBox(bodyText, -1, -1, null, true, null);
  }
  
  public static MessageBox messageBoxHtml(String bodyText) {
    return messageBox(bodyText, -1, -1, null, true, null);
  }
  
  public static MessageBox messageBox(String bodyText, int buttonType) {
    return messageBox(bodyText, buttonType, -1, null, false, null);
  }
  
  public static MessageBox messageBox(String bodyText, int buttonType, int iconType) {
    return messageBox(bodyText, buttonType, iconType, null, false, null);
  }
  
  public static MessageBox messageBox(String bodyText, int buttonType, int iconType, MessageBox.Callbacks callbacks) {
    return messageBox(bodyText, buttonType, iconType, null, false, callbacks);
  }
  
  public static MessageBox messageBox(String bodyText, int buttonType, int iconType, String captionText, boolean isHtml, MessageBox.Callbacks callbacks) {
    MessageBox.Configuration config = MessageBox.configuration();
    if (bodyText != null) {
      if (isHtml) {
        config.setHtmlText(bodyText);
      } else {
        config.setBodyText(bodyText);
      }
    }
    if (buttonType != -1) {
      config.setButtonType(buttonType);
    }
    if (iconType != -1) {
      config.setIconType(iconType);
    } else {
      switch (buttonType) {
      case MessageBox.BUTTONS_OK:
      case MessageBox.BUTTONS_OKCANCEL:
        config.setIconType(MessageBox.ICON_ALERT);
        break;
      case MessageBox.BUTTONS_YESNO:
      case MessageBox.BUTTONS_YESNOCANCEL:
        config.setIconType(MessageBox.ICON_QUESTION);
        break;
      }
    }
    if (captionText != null) {
      config.setCaptionText(captionText);
    }
    if (callbacks != null) {
      config.setCallbacks(callbacks);
    }
    return MessageBox.create(config);
  }
  
  public static void logEnvironment(Class<?> klass, String methodName) {
    log(klass, methodName, "app.codeName = " + Window.Navigator.getAppCodeName());
    log(klass, methodName, "app.name = " + Window.Navigator.getAppName());
    log(klass, methodName, "app.version = " + Window.Navigator.getAppVersion());
    log(klass, methodName, "platform = " + Window.Navigator.getPlatform());
    log(klass, methodName, "user.agent = " + Window.Navigator.getUserAgent());
    log(klass, methodName, "current locale = " + LocaleInfo.getCurrentLocale().getLocaleName());
    String startupModule = getJSVar("startupModule", null);
    if (startupModule != null) {
      log(klass, methodName, "startup module = " + startupModule);
    }
    String startupActivity = getJSVar("startupActivity", null);
    if (startupActivity != null) {
      log(klass, methodName, "startup activity = " + startupActivity);
    }
    String clientUserId = getJSVar("clientUserId", null);
    if (clientUserId != null) {
      log(klass, methodName, "client user id = " + clientUserId);
    }
    String contextPath = getJSVar("contextPath", null);
    if (contextPath != null) {
      log(klass, methodName, "context path = " + contextPath);
    }
  }

  public static String log (String message) {
    return log(null, -1, null, message, null);
  }
  
  public static String log (Class<?> type, String methodName, String message) {
    return log(type, -1, methodName, message, null);
  }
  
  public static String log (Class<?> type, int hashcode, String methodName, String message) {
    return log(type, hashcode, methodName, message, null);
  }
  
  public static String log (Class<?> type, String methodName, String message, Exception ex) {
    return log(type, -1, methodName, message, ex);
  }
  
  public static String log (Class<?> callingClass, int hashcode, String methodName, String message, Exception ex) {
    if (!isDevMode() && !enableLogInProductionMode) {
      return null;
    }
    String dts = GwtUtils.dateToString(new Date(), formatFromCache(logPattern)); 
    if (ex != null) {
      message = message + " - " + ex.getClass().getName() + " - " + ex.getMessage();
    }
    String cls = callingClass != null ? (callingClass.getName()+".") : "";
    methodName = getCallingMethodName("log");
    String logMsg = dts + " DEBUG " + "["+cls+methodName+(hashcode > -1 ? ("@"+ Integer.toHexString(hashcode)) : "") + "] " + message;
    if (isDevMode()) {
      System.out.println(logMsg);
    }
    consoleLogImpl(logMsg);
    return logMsg;
  }
  
  public static void setEnableLogInProductionMode(boolean enableLogInProductionMode) {
    GwtUtils.enableLogInProductionMode = enableLogInProductionMode;
  }
  
  private static String getCallingMethodName(String excludeMethodName) {
    String result = null;
    result = getCallingMethodName(excludeMethodName, true);
    if (result == null || !result.startsWith("it.")) {
      result = getCallingMethodName(excludeMethodName, false);
    }
    return result;
  }
  
  private static String getCallingMethodName(String excludeMethodName, boolean excludeInnerClasses) {
    try {
      throw new RuntimeException();
    } catch (Exception ex) {
      StackTraceElement[] stacktrace = ex.getStackTrace();
      for (int it = 0; it < stacktrace.length; it++) {
        StackTraceElement st = stacktrace[it];
        if (excludeInnerClasses && st.getClassName().contains("$")) {
          continue;
        }
        if (!st.getMethodName().contains(excludeMethodName) && !st.getMethodName().equals("getCallingMethodName")) {
          return st.getClassName()+"."+st.getMethodName()+"["+st.getLineNumber()+"]";
        }
      }
    }
    return "";
  }
  
  public static void setDefaultWaitPanel(PopupPanel popup) {
    setClientAttribute(ATTR_CLIENT_DEFAULT_WAIT_PANEL, popup);
  }
  
  public static void showWait() {
    showWait(null);
  }
  
  public static void showWait(PopupPanel defaultWaitPanel) {
    DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "wait");
    showWaitPanel(defaultWaitPanel);
  }
  
  public static void showWaitPanel() {
    showWaitPanel(null);
  }
  
  public static void showWaitPanel(boolean glassEnabled) {
    showWaitPanel(null, glassEnabled);
  }
  
  public static void showWaitPanel(PopupPanel defaultWaitPanel) {
    showWaitPanel(defaultWaitPanel, false);
  }
  
  public static void showWaitPanel(PopupPanel defaultWaitPanel, boolean glassEnabled) {
    showWaitPanelRequestCounter++;
    GwtUtils.log("showWaitPanelRequestCounter = " + showWaitPanelRequestCounter + " callingMethod = " + getCallingMethodName("showWait"));
    PopupPanel waitPanel = (PopupPanel)getClientAttribute(ATTR_CLIENT_WAIT_PANEL);
    if (waitPanel == null) {
      if (existsClientAttribute(ATTR_CLIENT_DEFAULT_WAIT_PANEL)) {
        defaultWaitPanel = (PopupPanel)getClientAttribute(ATTR_CLIENT_DEFAULT_WAIT_PANEL);
      }
      if (defaultWaitPanel != null) {
        waitPanel = defaultWaitPanel;
      } else {
        
        /* 11/01/2013
        waitPanel = new PopupPanel();
        waitPanel.setGlassEnabled(false);
        waitPanel.setAnimationEnabled(true);
        Label lb = new Label("Attendere prego...");
        GwtUtils.setStyleAttribute(lb, "font", "bold 16px Verdana");
        GwtUtils.setStyleAttribute(lb, "padding", "30px");
        GwtUtils.setStyleAttribute(lb, "color", "red");
        waitPanel.setWidget(lb);
        */
        
        waitPanel = new PopupPanel();
        GwtUtils.setStyleAttribute(waitPanel, "border", "none");
        GwtUtils.setStyleAttribute(waitPanel, "background", "transparent");
        waitPanel.setGlassEnabled(false);
        waitPanel.setAnimationEnabled(true);
        Image waitingImg = new Image(UriUtils.fromTrustedString("/images/commons/transp-loading.gif"));
        waitPanel.setWidget(waitingImg);
        
      }
      waitPanel.setGlassEnabled(glassEnabled);
      waitPanel.center();
      setClientAttribute(ATTR_CLIENT_WAIT_PANEL, waitPanel);
    }
  }
  
  public static void hideWait() {
    DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
    hideWaitPanel();
  }
  
  public static void hideWaitPanel() {
    hideWaitPanel(false);
  }
  
  public static void hideWaitPanel(boolean forceCounterReset) {
    if (forceCounterReset)
      showWaitPanelRequestCounter = 1;
    showWaitPanelRequestCounter--;
    GwtUtils.log("showWaitPanelRequestCounter = " + showWaitPanelRequestCounter + " callingMethod = " + getCallingMethodName("hideWait"));
    if (showWaitPanelRequestCounter <= 0) {
      showWaitPanelRequestCounter = 0;
      PopupPanel waitPanel = (PopupPanel)getClientAttribute(ATTR_CLIENT_WAIT_PANEL);
      if (waitPanel != null) {
        waitPanel.hide();
        removeClientAttribute(ATTR_CLIENT_WAIT_PANEL);
      }
    }
  }
  
  public static void setLocationHash(String newHash) {
    if (newHash == null || "null".equals(newHash))
      return;
    boolean hashchanged = false;
    String href = GwtUtils.getLocationHref();
    if (href.contains("#")) {
      if (href.contains("#"+newHash)) {
        hashchanged = false;
      } else {
        int pound = href.indexOf('#');
        href = href.substring(0, pound) + "#" + newHash;
        hashchanged = true;
      }
    } else {
      href = href + "#" + newHash;
      hashchanged = true;
    }
    if (hashchanged) {
//    Window.Location.replace(href);
      Window.Location.assign(href);
    }
  }
  
  public static String getSelectedValue(ListBox listBox) {
    if (listBox.getSelectedIndex() >= 0) {
      return listBox.getValue(listBox.getSelectedIndex());
    } else {
      return null;
    }
  }
  
  public static void printWithUrl (final String url) {
    String reportElementId = "gwtUtilsReportFrame";
    Element reportElement = DOM.getElementById(reportElementId);
    if (reportElement == null) {
      HTML reportHtml = new HTML(SafeHtmlUtils.fromTrustedString("<iframe id=\""+reportElementId+"\" style=\"visibility: hidden;\"></iframe>"));
      RootPanel.get().add(reportHtml);
    }
    GwtUtils.onAvailable(reportElementId, new Delegate<Element>() {
      public void execute(Element reportElement) {
        reportElement.setAttribute("src", url);
      }
    });
  }
  
  public static void addElementEventListener(com.google.gwt.dom.client.Element element, int eventId, EventListener listener) {
    DOM.sinkEvents((com.google.gwt.user.client.Element)element, eventId);
    DOM.setEventListener((com.google.gwt.user.client.Element)element, listener);
  }
  
  private static DateTimeFormat formatFromCache(String pattern) {
    if (!fmtCache.containsKey(pattern)) {
      fmtCache.put(pattern, DateTimeFormat.getFormat(pattern));
    }
    return fmtCache.get(pattern);
  }
  
  public static void setDateBoxFormat(DateBox dateBox, String pattern) {
    dateBox.setFormat(new DateBox.DefaultFormat(formatFromCache(pattern)));
  }
  
  public static void setWidgetModel(Widget widget, Object model) {
    widget.getElement().setPropertyObject("widgetModel", model);
  }
  
  public static Object getWidgetModel (Widget widget) {
    return widget.getElement().getPropertyObject("widgetModel");
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static <M> M getEventSourceWidgetModel (Event event, Class<M> modelClass) {
    Widget widget = getEventSourceWidget(event);
    if (widget != null) {
      return (M)getWidgetModel(widget);
    }
    return null;
  }
  
  @SuppressWarnings("rawtypes")
  public static Widget getEventSourceWidget (Event event) {
    Object source = event.getSource();
    if (source instanceof Widget) {
      return (Widget)source;
    }
    return null;
  }
  
  public static Widget getParentWidgetByClassAndStyle(Widget widget, String classname, String stylename) {
    Widget parent = widget.getParent();
    if (parent == null)
      return null;
    boolean found = true;
    if (classname != null) {
      found = found && (classname.equals(parent.getClass().getName()));
    }
    if (stylename != null) {
      String parentStylenames = parent.getElement().getClassName();
      found = found && (parentStylenames != null && parentStylenames.contains(stylename));
    }
    if (found) {
      return parent;
    }
    return getParentWidgetByClassAndStyle(parent, classname, stylename);
  }

  
  /**
   * 
   * get/set JS properties
   * 
   */
  
  public native static void setJsPropertyString(JavaScriptObject obj, String name, String value) /*-{
    obj[name] = value;
  }-*/;
  public native static void setJsPropertyInteger(JavaScriptObject obj, String name, Integer value) /*-{
    obj[name] = value;
  }-*/;
  public native static void setJsPropertyBool(JavaScriptObject obj, String name, boolean value) /*-{
    obj[name] = value;
  }-*/;
  public native static void setJsPropertyDouble(JavaScriptObject obj, String name, double value) /*-{
    obj[name] = value;
  }-*/;
  public native static void setJsPropertyJso(JavaScriptObject obj, String name, JavaScriptObject value) /*-{
    obj[name] = value;
  }-*/;
  
  public native static JavaScriptObject getJsPropertyJso(JavaScriptObject obj, String name) /*-{
    if (obj[name]===undefined) {
      return null;
    }
    return obj[name];
  }-*/;
  
  public native static Object getJsPropertyObject(JavaScriptObject obj, String name) /*-{
    if (obj[name]===undefined) {
      return null;
    }
    return obj[name];
  }-*/;

  
  // TODO
  public native static String getJsPropertyString(JavaScriptObject obj, String name) /*-{
  
    if (obj == null) {
      @it.mate.gwtcommons.client.utils.GwtUtils::logWithStackTrace(Ljava/lang/String;)('obj is null!');
    }
  
    if (obj[name]===undefined) {
      return null;
    }
    return obj[name];
  }-*/;

  public native static JsDate getJsPropertyDate(JavaScriptObject obj, String name) /*-{
    if (obj[name]===undefined) {
      return null;
    }
    return obj[name];
  }-*/;

  public native static JsArray<JavaScriptObject> getJsPropertyArray(JavaScriptObject obj, String name) /*-{
    if (obj[name]===undefined) {
      return null;
    }
    return obj[name];
  }-*/;

  public native static boolean getJsPropertyBool(JavaScriptObject obj, String name) /*-{
    if (obj[name]===undefined) {
      return false;
    }
    return obj[name];
  }-*/;
  
  public native static int getJsPropertyInt(JavaScriptObject obj, String name) /*-{
    if (obj[name]===undefined) {
      return 0;
    }
    return obj[name];
  }-*/;

  public native static double getJsPropertyDouble(JavaScriptObject obj, String name) /*-{
    if (obj[name]===undefined) {
      return 0;
    }
    return obj[name];
  }-*/;

  public static String unescapeHtml(String text) {
    HTML html = new HTML(text);
    return html.getText();
  }

  public static void setMobileOptimizations(boolean mobileOptimizations) {
    GwtUtils.mobileOptimizations = mobileOptimizations;
  }
  
  public static void setDebugExceptionHandler() {
    GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
      public void onUncaughtException(Throwable th) {
        GwtUtils.log("Uncaught exception: " + th.getClass().getName());
        StackTraceElement[] st = th.getStackTrace();
        if (st != null) {
          for (StackTraceElement ste : st) {
            GwtUtils.log("Stack trace: " + ste.getClassName()+" "+ste.getMethodName()+" "+ste.getLineNumber());
          }
        }
      }
    });
  }
  
  public static native int getElementOffsetLeft(Element elem) /*-{
    var offsetLeft = 0;
    do {
      if (!isNaN(elem.offsetLeft)) {
          offsetLeft += elem.offsetLeft;
      }
    } while(elem = elem.offsetParent );
    return offsetLeft;
  }-*/;
  
  public static native int getElementOffsetTop(Element elem) /*-{
    var offsetTop = 0;
    do {
      if (!isNaN(elem.offsetTop)) {
          offsetTop += elem.offsetTop;
      }   
    } while(elem = elem.offsetParent );
    return offsetTop;
  }-*/;

  public static native int getElementOffsetWidth(Element elem) /*-{
    return elem.offsetWidth;
  }-*/;

  public static native int getElementOffsetHeight(Element elem) /*-{
    return elem.offsetHeight;
  }-*/;
  
  public static native JavaScriptObject getElementOffset(Element elem) /*-{
    var left = @it.mate.gwtcommons.client.utils.GwtUtils::getElementOffsetLeft(Lcom/google/gwt/dom/client/Element;)(elem);
    var top = @it.mate.gwtcommons.client.utils.GwtUtils::getElementOffsetTop(Lcom/google/gwt/dom/client/Element;)(elem);
    var width = @it.mate.gwtcommons.client.utils.GwtUtils::getElementOffsetWidth(Lcom/google/gwt/dom/client/Element;)(elem);
    var height = @it.mate.gwtcommons.client.utils.GwtUtils::getElementOffsetHeight(Lcom/google/gwt/dom/client/Element;)(elem);
    return {left: left, top: top, width: width, height: height};
  }-*/;

  public static void addMoveHandler(final Element element, final MoveHandler handler) {
    element.setPropertyObject("_moveHelper", new MoveHelper());
    GwtUtils.createTimerDelegate(200, false, new Delegate<Timer>() {
      public void execute(Timer timer) {
        if (element.getOwnerDocument() == null) {
          timer.cancel();
          return;
        }
        MoveHelper helper = (MoveHelper)element.getPropertyObject("_moveHelper");
        Integer currentTop = element.getAbsoluteTop();
        Integer currentLeft = element.getAbsoluteLeft();
        if (helper.getTop() != null && helper.getLeft() != null) {
          if (!helper.getTop().equals(currentTop) || !helper.getLeft().equals(currentLeft)) {
            handler.onMove(element, currentTop, currentLeft);
          }
        }
        helper.setTop(currentTop);
        helper.setLeft(currentLeft);
      }
    });
  }
  
  public static void ensureId(Element element) {
    if (element.getId() == null || "".equals(element.getId())) {
      element.setId(DOM.createUniqueId());
    }
  }
  
  private static native void consoleLogImpl(String text) /*-{
    $wnd.console.log(text);
  }-*/;
  
  public static void setUseAvailableElementsCache(boolean useAvailableElementsCache) {
    GwtUtils.useAvailableElementsCache = useAvailableElementsCache;
  }
  
  public static void logWithStackTrace(String msg) {
    GwtUtils.log(msg);
    try {
      throw new RuntimeException("Printing stack: " + msg);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  public static native String getOuterHtml(Element element) /*-{
    return element.outerHTML;
  }-*/;
  
}
