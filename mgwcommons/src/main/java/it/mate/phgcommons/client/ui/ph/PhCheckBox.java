package it.mate.phgcommons.client.ui.ph;

import it.mate.phgcommons.client.ui.HasTag;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasValue;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndHandler;
import com.googlecode.mgwt.ui.client.widget.touch.TouchWidget;

public class PhCheckBox extends TouchWidget implements HasTag, HasValue<Boolean>, HasChangeHandlers, HasModel {

  private String tag;
  
  private Element elem;
  
  private boolean value;
  
  private boolean circle;
  
  private List<ValueChangeHandler<Boolean>> valueChangeHandlers = new ArrayList<ValueChangeHandler<Boolean>>();
  
  private Object model;
  
  public PhCheckBox() {
    elem = DOM.createDiv();
    setElement(elem);
    elem.addClassName("phg-CheckBox");
    
    addTouchEndHandler(new TouchEndHandler() {
      public void onTouchEnd(TouchEndEvent event) {
        setValue(!value);
      }
    });
    
  }
  
  public void setCircle(boolean circle) {
    this.circle = circle;
    if (circle) {
      elem.addClassName("phg-CheckBox-Circle");
    }
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }
  
  public boolean isSelected() {
    return value;
  }

  @Override
  public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Boolean> handler) {
    this.valueChangeHandlers.add(handler);
    HandlerRegistration registration = new HandlerRegistration() {
      public void removeHandler() {
        valueChangeHandlers.remove(handler);
      }
    };
    return registration;
  }

  @Override
  public HandlerRegistration addChangeHandler(ChangeHandler handler) {
    return addDomHandler(handler, ChangeEvent.getType());
  }

  @Override
  public Boolean getValue() {
    return value;
  }

  @Override
  public void setValue(Boolean value) {
    setValue(value, true);
  }

  @Override
  public void setValue(Boolean value, boolean fireEvents) {
    this.value = value != null ? value : false;
    if (this.value) {
      elem.addClassName(circle ? "phg-CheckBox-Circle-selected" : "phg-CheckBox-selected");
    } else {
      elem.removeClassName(circle ? "phg-CheckBox-Circle-selected" : "phg-CheckBox-selected");
    }
    if (fireEvents) {
      SelectedChangeEvent.fire(this, value);
    }
  }
  
  @Override
  public void fireEvent(GwtEvent<?> event) {
    if (event instanceof SelectedChangeEvent) {
      SelectedChangeEvent valueChangeEvent = (SelectedChangeEvent)event;
      for (ValueChangeHandler<Boolean> handler : valueChangeHandlers) {
        handler.onValueChange(valueChangeEvent);
      }
    } else {
      super.fireEvent(event);
    }
  }
  
  public static class SelectedChangeEvent extends ValueChangeEvent<Boolean> {

    public static <S extends HasValueChangeHandlers<Boolean> & HasHandlers> void fire(S source, Boolean value) {
      source.fireEvent(new SelectedChangeEvent(value));
    }
    
    public static <S extends HasValueChangeHandlers<Boolean> & HasHandlers> void fireIfNotEqualDates(S source, Boolean oldValue, Boolean newValue) {
      if (ValueChangeEvent.shouldFire(source, oldValue, newValue)) {
        source.fireEvent(new SelectedChangeEvent(newValue));
      }
    }

    protected SelectedChangeEvent(Boolean value) {
      super(value);
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

}
