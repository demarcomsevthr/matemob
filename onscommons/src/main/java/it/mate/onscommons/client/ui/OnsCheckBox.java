package it.mate.onscommons.client.ui;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.onsen.OnsenUi;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;


public class OnsCheckBox extends OnsTextBox {
  
  private Element inputElement;
  
  public OnsCheckBox() {
    super("checkbox");
    addStyleName("switch__input ons-checkbox");
    
    OnsenUi.onAvailableElement(this, new Delegate<Element>() {
      public void execute(Element inputElement) {
        OnsCheckBox.this.inputElement = inputElement;
        Element parentElement = inputElement.getParentElement();
        inputElement.removeFromParent();
        Element labelElement = DOM.createElement("label");
        labelElement.addClassName("switch");
        labelElement.appendChild(inputElement);
        Element toggleElement = DOM.createElement("div");
        toggleElement.addClassName("switch__toggle");
        labelElement.appendChild(toggleElement);
        parentElement.appendChild(labelElement);
      }
    });
    
  }
  
  public Element getInputElement() {
    return inputElement;
  }

  @Override
  public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<String> handler) {
    return super.addValueChangeHandler(new ValueChangeHandler<String>() {
      public void onValueChange(ValueChangeEvent<String> event) {
        
        if ("true".equalsIgnoreCase(event.getValue()) || "false".equalsIgnoreCase(event.getValue())) {
          handler.onValueChange(event);
        } else {
          /*
          PhgUtils.log("CBX VALUE CHANGED 1 " + event.getValue());
          PhgUtils.log("CBX VALUE CHANGED 2 " + event.getSource().getClass().getName());
          PhgUtils.log("CBX VALUE CHANGED 3 " + event.getSource());
          PhgUtils.log("CBX VALUE CHANGED 4 " + ((OnsCheckBox)event.getSource()).getLabelElement());
          PhgUtils.log("CBX VALUE CHANGED 5 " + GwtUtils.getJsPropertyBool(((OnsCheckBox)event.getSource()).getInputElement(), "checked"));
          */
          if (event.getSource() != null && event.getSource() instanceof OnsCheckBox) {
            OnsCheckBox checkbox = (OnsCheckBox)event.getSource();
            ValueChangeEvent.fire(OnsCheckBox.this, ""+checkbox.getValueAsBool());
          }
        }
        
      }
    });
  }
  
  public boolean getValueAsBool() {
    boolean value = false;
    if (inputElement != null) {
      value = GwtUtils.getJsPropertyBool(inputElement, "checked");
    }
    return value;
  }
  
  public void setValueAsBool(final boolean value) {
    setValueAsBool(value, false);
  }
  
  public void setValueAsBool(final boolean value, final boolean fireValueChangeEvent) {
    OnsenUi.onAvailableElement(this, new Delegate<Element>() {
      public void execute(Element element) {
        if (inputElement != null) {
          GwtUtils.setJsPropertyBool(inputElement, "checked", value);
          if (fireValueChangeEvent) {
            ValueChangeEvent.fire(OnsCheckBox.this, ""+value);
          }
        }
      }
    });
  }

}
