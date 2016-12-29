package it.mate.onscommons.client.ui;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.gwtcommons.client.utils.JQuery;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.utils.OnsDatePicker;
import it.mate.phgcommons.client.utils.OsDetectionUtils;
import it.mate.phgcommons.client.utils.PhgUtils;

import java.util.Date;

import com.google.gwt.dom.client.Element;


public class OnsDateBox extends OnsTextBox {
  

  // 02/11/2015
  private static boolean USE_ONS_DATEPICKER = OsDetectionUtils.isAndroid() /* || OsDetectionUtils.isDesktop() */; 
  private static boolean USE_JS_DATEPICKER = false; 
  /////////////
  
      
  /*
  // 28/10/2015 - SEMBRA FUNZIONARE CON LA PATCH IN pickadate.js/compressed/themes/default.css
  private static boolean USE_JS_DATEPICKER = OsDetectionUtils.isAndroid() && 
      !PhgUtils.getDeviceVersion().startsWith("4.0") && 
      !PhgUtils.getDeviceVersion().startsWith("4.1") && 
      !PhgUtils.getDeviceVersion().startsWith("4.2"); 
      */
  
  
  private static String JS_DATEPICKER_FORMAT = "dd/MM/yyyy";
  
  private String actualFormat = "dd/MM/yyyy";
  
  private static String actualInputType = (USE_ONS_DATEPICKER ? "text" : "date");
  
  private String language;
  
  public OnsDateBox() {
    super( actualInputType );
    language = PhgUtils.getAppLocalLanguage();
    
    if (actualInputType.equals("date")) {
      actualFormat = "yyyy-MM-dd";
    } else {
      if ("it".equalsIgnoreCase(language)) {
        actualFormat = "dd/MM/yyyy";
      } else {
        actualFormat = "yyyy/MM/dd";
      }
    }
    
    addStyleName("text-input");
    
    PhgUtils.log("OnsDateBox: USE_JS_DATEPICKER=" + USE_JS_DATEPICKER + " USE_ONS_DATEPICKER="+USE_ONS_DATEPICKER + " language="+language + " actualFormat="+actualFormat + " actualInputType=" + actualInputType);
    
    if (USE_JS_DATEPICKER) {
      initializeDatepicker();
    } else if (USE_ONS_DATEPICKER) {
      OnsenUi.onAvailableElement(this, new Delegate<Element>() {
        public void execute(Element element) {
          new OnsDatePicker(OnsDateBox.this, language);
        }
      });
    }
    
  }

  public static String getActualInputType() {
    return actualInputType;
  }
  
  public Date getValueAsDate() {
    String text = getText();
    if (text == null || text.trim().length() == 0) {
      return null;
    }
    Date result = null;
    if (USE_JS_DATEPICKER) {
      result = GwtUtils.stringToDate(text, JS_DATEPICKER_FORMAT);
    } else {
      String tryFormat = "yyyy-MM-dd";
      result = tryParseDate(tryFormat);
      actualFormat = tryFormat;
      if (result == null) {
        tryFormat = "dd/MM/yyyy";
        result = tryParseDate(tryFormat);
        actualFormat = tryFormat;
      }
    }
    return result;
  }
  
  public void setValueAsDate(Date date) {
    setValue(GwtUtils.dateToString(date, actualFormat));
  }
  
  public String getActualFormat() {
    return actualFormat;
  }
  
  private Date tryParseDate(String pattern) {
    try {
      return GwtUtils.stringToDate(getText(), pattern);
    } catch (IllegalArgumentException e1) {
      return null;
    }
  }
  
  @Override
  public void setValue(String value, boolean fireEvents) {
    super.setValue(value, fireEvents);
    if (USE_JS_DATEPICKER) {
      setDataValue(value);
    }
    final String fValue = value;
    OnsenUi.onAvailableElement(this, new Delegate<Element>() {
      public void execute(Element element) {
        element.setInnerText(fValue);
      }
    });
  }

  public void setDataValue(final String dataValue) {
    OnsenUi.onAvailableElement(this, new Delegate<Element>() {
      public void execute(Element element) {
        element.setAttribute("data-value", dataValue);
        initializeDatepicker();
      }
    });
  }
  
  private void initializeDatepicker() {
    GwtUtils.deferredExecution(400, new Delegate<Void>() {
      public void execute(Void element) {
        OnsenUi.onAvailableElement(OnsDateBox.this, new Delegate<Element>() {
          public void execute(Element element) {
            String containerId = null;
            JQuery.withElement(element).pickdate(JS_DATEPICKER_FORMAT.toLowerCase(), containerId, true, true);
          }
        });
      }
    });
  }
  
  
}
