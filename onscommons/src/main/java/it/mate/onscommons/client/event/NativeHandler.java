package it.mate.onscommons.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface NativeHandler extends EventHandler {

  public void handle(NativeEvent event);
  
}
