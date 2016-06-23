package it.mate.onscommons.client.event;

import com.google.gwt.event.shared.HandlerRegistration;


public interface HasTapHandler {

  public HandlerRegistration addTapHandler(TapHandler handler);
  
}
