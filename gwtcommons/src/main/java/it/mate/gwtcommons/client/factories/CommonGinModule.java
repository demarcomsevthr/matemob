package it.mate.gwtcommons.client.factories;


import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
//import com.google.gwt.event.shared.EventBus;

public class CommonGinModule extends AbstractGinModule {

  protected void configure() {
    
    // 23/12/2014
//  bind(EventBus.class).to(NotAllowedEventBus.class).in(Singleton.class);
//  bind(EventBus.class).to(LoggedSimpleEventBus.class).in(Singleton.class);

    bind(PlaceController.class).to(InjectedPlaceController.class).in(Singleton.class);
    
    bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
    
  }

  
}
