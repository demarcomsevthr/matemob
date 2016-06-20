package it.mate.testleaflet.client.logic;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class AppEvent extends GwtEvent<AppEvent.Handler> {

  public interface Handler extends EventHandler {
    void onAppStateChange(AppEvent event);
  }
  
  public final static int DB_READY = 1;
  
  public final static int UPDATED_ORDERS_AVAILABLE = 2;
  
  private int state;
  
  private Object model;
  
  public static final Type<Handler> TYPE = new Type<Handler>();
  
  public AppEvent(int state) {
    this(state, null);
  }

  public AppEvent(int state, Object model) {
    this.state = state;
    this.model = model;
  }

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }
  
  public int getState() {
    return state;
  }
  
  public Object getModel() {
    return model;
  }
  
  @Override
  protected void dispatch(Handler handler) {
    handler.onAppStateChange(this);
  }

  @Override
  public String toString() {
    return "AppStateChangeEvent [state=" + state + "]";
  }
  
}
