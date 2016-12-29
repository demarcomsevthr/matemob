package it.mate.onscommons.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface NativeGestureHandler extends EventHandler {

  public void on(NativeGestureEvent event);
  
}
