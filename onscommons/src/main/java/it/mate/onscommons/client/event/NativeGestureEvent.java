package it.mate.onscommons.client.event;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.GwtEvent;

public class NativeGestureEvent extends GwtEvent<NativeGestureHandler> {

  private static final Type<NativeGestureHandler> TYPE = new Type<NativeGestureHandler>();
  
  private NativeGesture gesture;
  
  public NativeGestureEvent(NativeGesture gesture) {
    this.gesture = gesture;
  }
  
  @Override
  public com.google.gwt.event.shared.GwtEvent.Type<NativeGestureHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(NativeGestureHandler handler) {
    handler.on(this);
  }
  
  public static Type<NativeGestureHandler> getType() {
    return TYPE;
  }

  public NativeGesture getGesture() {
    return gesture;
  }
  
  public Element getTarget() {
    return gesture.getTarget();
  }

}
