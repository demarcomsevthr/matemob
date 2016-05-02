package it.mate.phgcommons.client.ui;

import it.mate.gwtcommons.client.ui.SimpleContainer;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBox;
import com.googlecode.mgwt.ui.client.MGWTStyle;

/**
 * 
 * WIDGET UTILIZZATO PER INVESTIGARE SUL ISSUE DEI FIELD TEXT
 * 
 * (OUTLINE E KEYBOARD ISSUE)
 * 
 * SEE http://stackoverflow.com/questions/9423101/disable-android-browsers-input-overlays
 * 
 * @author demarcom 14/01/2014
 *
 */


public class MTextBoxPatched1 extends Composite implements HasValue<String> {
  
  private SimpleContainer wrapper;
  
  private TextBox box;

  public MTextBoxPatched1() {
    MGWTStyle.getTheme().getMGWTClientBundle().getInputCss().ensureInjected();
    initUI();
  }
  
  private void initUI() {
    wrapper = new SimpleContainer();
    wrapper.setStyleName("phg-TextBox");
    box = new TextBox();
    box.addStyleName("phg-InputBox-box");
    box.getElement().setPropertyString("contentEditable", "false");
    wrapper.add(box);
    initWidget(wrapper);
  }

  @Override
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
    return box.addValueChangeHandler(handler);
  }

  @Override
  public String getValue() {
    return box.getValue();
  }

  @Override
  public void setValue(String value) {
    box.setValue(value);
  }

  @Override
  public void setValue(String value, boolean fireEvents) {
    box.setValue(value, fireEvents);
  }
  
  public TextBox getBox() {
    return box;
  }

}
