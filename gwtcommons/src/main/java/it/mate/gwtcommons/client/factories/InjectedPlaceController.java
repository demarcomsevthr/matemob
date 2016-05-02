package it.mate.gwtcommons.client.factories;

//import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class InjectedPlaceController extends PlaceController {

  @Inject
  public InjectedPlaceController(EventBus eventBus) {
    super(eventBus);
  }
  
}
