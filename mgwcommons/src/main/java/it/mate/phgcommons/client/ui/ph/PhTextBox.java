package it.mate.phgcommons.client.ui.ph;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.phgcommons.client.utils.OsDetectionUtils;
import it.mate.phgcommons.client.utils.PhgUtils;

import java.util.Date;

import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.TextBox;
import com.googlecode.mgwt.ui.client.theme.base.InputCss;
import com.googlecode.mgwt.ui.client.widget.MTextBox;

public class PhTextBox extends MTextBox implements HasModel {

  private Object model;
  
  public PhTextBox() {
    super();
    applyPatch();
  }

  public PhTextBox(InputCss css, TextBox textBox) {
    super(css, textBox);
    applyPatch();
  }

  public PhTextBox(InputCss css) {
    super(css);
    applyPatch();
  }

  private void applyPatch() {
    if (OsDetectionUtils.isAndroid()) {
      String version = PhgUtils.getDeviceVersion().trim();
      if (version.startsWith("2") || version.startsWith("3") || version.startsWith("4.0")) {
        box.getElement().getStyle().setProperty("webkitUserModify", "initial");
      }
    }
  }

  /**
   * Available types: https://developer.apple.com/library/safari/documentation/AppleApplications/Reference/SafariHTMLRef/Articles/InputTypes.html
   */
  public void setType(String type) {
    if ("password".equalsIgnoreCase(type)) {
    } else if ("date".equalsIgnoreCase(type)) {
    } else if ("datetime".equalsIgnoreCase(type)) {
    } else if ("email".equalsIgnoreCase(type)) {
    } else if ("month".equalsIgnoreCase(type)) {
    } else if ("number".equalsIgnoreCase(type)) {
    } else if ("password".equalsIgnoreCase(type)) {
    } else if ("range".equalsIgnoreCase(type)) {
    } else if ("search".equalsIgnoreCase(type)) {
    } else if ("tel".equalsIgnoreCase(type)) {
    } else if ("time".equalsIgnoreCase(type)) {
    } else if ("url".equalsIgnoreCase(type)) {
    } else if ("week".equalsIgnoreCase(type)) {
    } else {
      PhgUtils.log("cannot set input type " + type);
      return;
    }
    box.getElement().setAttribute("type", type.toLowerCase());
  }
  
  public Double getValueAsDouble() {
    String textValue = getText();
    textValue = GwtUtils.replaceEx(textValue, ",", ".");
    try {
      return NumberFormat.getDecimalFormat().parse(textValue);
    } catch (NumberFormatException ex) {
      return null;
    }
  }
  
  public int getValueAsInt() {
    String textValue = getText();
    try {
      return Integer.parseInt(textValue);
    } catch (NumberFormatException ex) {
      return 0;
    }
  }
  
  public Integer getValueAsInteger() {
    String textValue = getText();
    try {
      return Integer.parseInt(textValue);
    } catch (NumberFormatException ex) {
      return null;
    }
  }
  
  public Long getValueAsLong() {
    String textValue = getText();
    try {
      return Long.parseLong(textValue);
    } catch (NumberFormatException ex) {
      return null;
    }
  }
  
  public Date getValueAsDate() {
    Date result = null;
    result = getValueAsDate(PhgUtils.getDefaultDatePattern());
    if (result == null) {
      result = getValueAsDate("dd/MM/yyyy");
    }
    if (result == null) {
      result = getValueAsDate("MM/dd/yyyy");
    }
    if (result == null) {
      result = getValueAsDate("yyyy-MM-dd");
    }
    return result;
  }
  
  private Date getValueAsDate(String pattern) {
    if (pattern == null)
      return null;
    try {
      return GwtUtils.stringToDate(getText(), pattern);
    } catch (Exception ex) {
      return null;
    }
  }
  
  public void setValue(Number value) {
    if (value != null) {
      setValue(NumberFormat.getDecimalFormat().format(value), true);
    }
  }

  @Override
  public HasModel setModel(Object model) {
    this.model = model;
    return this;
  }

  @Override
  public Object getModel() {
    return model;
  }
  
  private Timer uppercaseFixTimer = null;

  /**
   * 03/07/2014
   * 
   * Provato a patchare il bug dell'uppercase ma non si riesce: il primo carattere viene perso, e se provo a fare la trasformazione 
   * al secondo carattere, si perde il terzo!!!!!
   * 
   *  UN VERO DRAMMA!
   *  
   *  CI RINUNCIO!
   * 
   */
  public void setUppercase(boolean uppercase) {
    PhgUtils.log("UPPERCASE FIX 1");
    if (uppercase) {
      PhgUtils.log("UPPERCASE FIX 2");
      if (OsDetectionUtils.isAndroid()) {
        PhgUtils.log("UPPERCASE FIX 3");
        String version = PhgUtils.getDeviceVersion().trim();
        if (version.startsWith("2") || version.startsWith("3") || version.startsWith("4.0")) {
          PhgUtils.log("UPPERCASE FIX 4");

          uppercaseFixTimer = GwtUtils.createTimer(500, new Delegate<Void>() {
            public void execute(Void element) {
              String text = box.getValue().toUpperCase();
              if (text.length() > 1 ) {
                box.setValue(text);
              }
              /*
              if (text.length() == 1 ) {
                PhgUtils.log("firstchar fix");
                //firstcharFix = true;
                box.setValue(text+" ");
                /*
                GwtUtils.deferredExecution(new Delegate<Void>() {
                  public void execute(Void element) {
                    box.setValue(text);
                  }
                });
              }
                */
            }
          });
          
          /*
          Element elem = box.getElement();
          applyUppercaseFix(elem);
           */
          
        }
      }
    }
  }
  
  @Override
  protected void onUnload() {
    super.onUnload();
    if (uppercaseFixTimer != null) {
      uppercaseFixTimer.cancel();
    }
  }
  
  private static native void setCaretPosition(Element elem, int pos) /*-{
    elem.setSelectionRange(pos, pos);
  }-*/;

}
