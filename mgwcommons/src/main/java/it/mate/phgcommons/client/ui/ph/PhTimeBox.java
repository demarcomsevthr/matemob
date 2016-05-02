package it.mate.phgcommons.client.ui.ph;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.phgcommons.client.ui.TimePickerDialog;
import it.mate.phgcommons.client.utils.Time;
import it.mate.phgcommons.client.utils.TouchUtils;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasValue;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.touch.TouchWidget;

public class PhTimeBox extends TouchWidget implements HasValue<Time>, HasChangeHandlers, HasModel {

  private InputElement element;
  
  private Time value;
  
  private List<ValueChangeHandler<Time>> valueChangeHandlers = new ArrayList<ValueChangeHandler<Time>>();
  
  private List<BeforeDialogOpenEvent.Handler> beforeDialogOpenHandlers = new ArrayList<BeforeDialogOpenEvent.Handler>();
  
  private List<AfterDialogCloseEvent.Handler> afterDialogCloseHandlers = new ArrayList<AfterDialogCloseEvent.Handler>();
  
//private static DateTimeFormat fmt = DateTimeFormat.getFormat("HH:mm");
//private static DateTimeFormat fmt = Time.getCurrentFormat();
  
  private Object model;
  
  private Time defaultTime = new Time();
  
  public PhTimeBox() {
    element = DOM.createInputText().cast();
    setElement(element);
    addStyleName("phg-TimeBox");

    element.setReadOnly(true);
    addTapHandler(new TapHandler() {
      public void onTap(TapEvent event) {
        BeforeDialogOpenEvent.fire(PhTimeBox.this, event);
        TouchUtils.applyKeyboardPatch();
        GwtUtils.deferredExecution(500, new Delegate<Void>() {
          public void execute(Void element) {
            TimePickerDialog dialog = new TimePickerDialog(new TimePickerDialog.Options().setOnClose(new Delegate<Time>() {
              public void execute(Time value) {
                setValue(value, true);
              }
            })) {
              protected void onHide() {
                AfterDialogCloseEvent.fire(PhTimeBox.this);
              };
            };
            Time value = PhTimeBox.this.value != null ? PhTimeBox.this.value : defaultTime;
            dialog.setTime(value);
          }
        });
      }
    });
    
  }

  public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Time> handler) {
    this.valueChangeHandlers.add(handler);
    HandlerRegistration registration = new HandlerRegistration() {
      public void removeHandler() {
        valueChangeHandlers.remove(handler);
      }
    };
    return registration;
  }
  
  public HandlerRegistration addBeforeDialogOpenHandler(final BeforeDialogOpenEvent.Handler handler) {
    this.beforeDialogOpenHandlers.add(handler);
    HandlerRegistration registration = new HandlerRegistration() {
      public void removeHandler() {
        beforeDialogOpenHandlers.remove(handler);
      }
    };
    return registration;
  }
  
  public HandlerRegistration addAfterDialogCloseHandler(final AfterDialogCloseEvent.Handler handler) {
    this.afterDialogCloseHandlers.add(handler);
    HandlerRegistration registration = new HandlerRegistration() {
      public void removeHandler() {
        afterDialogCloseHandlers.remove(handler);
      }
    };
    return registration;
  }
  
  public HandlerRegistration addChangeHandler(final ChangeHandler handler) {
    return addDomHandler(handler, ChangeEvent.getType());
  }

  public Time getValue() {
    return value;
  }

  public void setValue(Time value) {
    setValue(value, true);
  }

  public void setValueAsString(String text) {
    setValue(text == null ? null : Time.fromString(text), true);
  }

  public void setValue(Time value, boolean fireEvents) {
    this.value = value;
    if (value != null) {
      String text = timeToString(value);
      element.setValue(text);
      if (fireEvents) {
        TimeChangeEvent.fire(this, value);
      }
    }
  }
  
  private String timeToString(Time time) {
    return Time.getCurrentFormat().format(time.asDate());
  }
  
  @Override
  public void fireEvent(GwtEvent<?> event) {
    if (event instanceof TimeChangeEvent) {
      TimeChangeEvent timeChangeEvent = (TimeChangeEvent)event;
      for (ValueChangeHandler<Time> handler : valueChangeHandlers) {
        handler.onValueChange(timeChangeEvent);
      }
    } else if (event instanceof BeforeDialogOpenEvent) {
      BeforeDialogOpenEvent beforeDialogOpenEvent = (BeforeDialogOpenEvent)event;
      for (BeforeDialogOpenEvent.Handler handler : beforeDialogOpenHandlers) {
        handler.onBeforeDialogOpen(beforeDialogOpenEvent);
      }
    } else if (event instanceof AfterDialogCloseEvent) {
      AfterDialogCloseEvent afterDialogCloseEvent = (AfterDialogCloseEvent)event;
      for (AfterDialogCloseEvent.Handler handler : afterDialogCloseHandlers) {
        handler.onAfterDialogClose(afterDialogCloseEvent);
      }
    } else {
      super.fireEvent(event);
    }
  }
  
  public static class TimeChangeEvent extends ValueChangeEvent<Time> {

    public static <S extends HasValueChangeHandlers<Time> & HasHandlers> void fire(S source, Time value) {
      source.fireEvent(new TimeChangeEvent(value));
    }
    
    public static <S extends HasValueChangeHandlers<Time> & HasHandlers> void fireIfNotEqualDates(S source, Time oldValue, Time newValue) {
      if (ValueChangeEvent.shouldFire(source, oldValue, newValue)) {
        source.fireEvent(new TimeChangeEvent(newValue));
      }
    }

    protected TimeChangeEvent(Time value) {
      // The date must be copied in case one handler causes it to change.
      super(new Time(value.getHours(), value.getMinutes()));
    }

    @Override
    public Time getValue() {
      Time value = super.getValue();
      return new Time(value.getHours(), value.getMinutes());
    }
    
  }
  
  public static class BeforeDialogOpenEvent extends GwtEvent<BeforeDialogOpenEvent.Handler> {

    public interface Handler extends EventHandler {
      public void onBeforeDialogOpen(BeforeDialogOpenEvent event);
    }

    private static GwtEvent.Type<BeforeDialogOpenEvent.Handler> TYPE = new Type<BeforeDialogOpenEvent.Handler>();
    
    private final TapEvent event;

    protected BeforeDialogOpenEvent(TapEvent event) {
      this.event = event;
    }

    public static GwtEvent.Type<BeforeDialogOpenEvent.Handler> getTYPE() {
      return TYPE;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
      return TYPE;
    }

    @Override
    protected void dispatch(Handler handler) {
      handler.onBeforeDialogOpen(this);

    }

    public TapEvent getTapEvent() {
      return event;
    }

    public static void fire(HasHandlers source, TapEvent tapEvent) {
      if (TYPE != null) {
        BeforeDialogOpenEvent event = new BeforeDialogOpenEvent(tapEvent);
        source.fireEvent(event);
      }
    }
    
  }

  public static class AfterDialogCloseEvent extends GwtEvent<AfterDialogCloseEvent.Handler> {

    public interface Handler extends EventHandler {
      public void onAfterDialogClose(AfterDialogCloseEvent event);
    }

    private static GwtEvent.Type<AfterDialogCloseEvent.Handler> TYPE = new Type<AfterDialogCloseEvent.Handler>();
    
    protected AfterDialogCloseEvent() {
    }

    public static GwtEvent.Type<AfterDialogCloseEvent.Handler> getTYPE() {
      return TYPE;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
      return TYPE;
    }

    @Override
    protected void dispatch(Handler handler) {
      handler.onAfterDialogClose(this);

    }

    public static void fire(HasHandlers source) {
      if (TYPE != null) {
        AfterDialogCloseEvent event = new AfterDialogCloseEvent();
        source.fireEvent(event);
      }
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
  
  public void setDefaultTime(Time defaultTime) {
    this.defaultTime = defaultTime;
  }

}
