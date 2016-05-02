package it.mate.gwtcommons.client.factories;

//import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;

public interface CommonGinjector extends Ginjector {

//@Deprecated
//public com.google.gwt.event.shared.EventBus getEventBus();
  
  public PlaceController getPlaceController();
  
  public com.google.web.bindery.event.shared.EventBus getBinderyEventBus();
  
}
