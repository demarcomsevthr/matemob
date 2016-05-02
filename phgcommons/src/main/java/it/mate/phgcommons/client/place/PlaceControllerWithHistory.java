package it.mate.phgcommons.client.place;

import it.mate.gwtcommons.client.places.HasToken;
import it.mate.phgcommons.client.utils.PhgUtils;

import java.util.LinkedList;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;

public class PlaceControllerWithHistory extends PlaceController {
  
  private EventBus eventBus;
  
  LinkedList<Place> history = new LinkedList<Place>();
  
  public PlaceControllerWithHistory(EventBus eventBus, Delegate delegate) {
    super(eventBus, delegate);
    this.eventBus = eventBus;
  }
  
  @Override
  public void goTo(Place newPlace) {
    super.goTo(newPlace);
    insertPlaceInHistory(newPlace);
    logHistory();
  }
  
  public void goToWithEvent(PlaceChangeEvent event) {
    eventBus.fireEvent(event);
    insertPlaceInHistory(event.getNewPlace());
    logHistory();
  }
  
  private void logHistory() {
    for (int it = 0; it < history.size(); it++) {
      Place place = history.get(it);
      PhgUtils.log("PlaceControllerWithHistory.history["+it+"] = " + place);
    }
  }
  
  private void insertPlaceInHistory(Place newPlace) {
    if (!(newPlace instanceof HasToken)) {
      PhgUtils.log("ERROR: " + getClass() + " can be used only with place implementing HasToken interface!");
    }
    if (history.size() > 0) {
      for (int it = 0; it < history.size(); it++) {
        HasToken placeInHistory = (HasToken)history.get(it);
        HasToken newPlaceWithToken = (HasToken)newPlace;
        if (placeInHistory.getToken().equals(newPlaceWithToken.getToken())) {
          history = truncate(it);
        }
      }
      
    }
    history.add(newPlace);
    String logMsg = " history = {";
    for (int it = 0; it < history.size(); it++) {
      Place placeInHistory = history.get(it);
      if (it > 0)
        logMsg+= ", ";
      logMsg += placeInHistory;
    }
    logMsg += "}";
    PhgUtils.log(logMsg);
  }
  
  private LinkedList<Place> truncate(int index) {
    LinkedList<Place> newHistory = new LinkedList<Place>();
    for (int it = 0; it < index; it++) {
      newHistory.add(history.get(it));
    }
    return newHistory;
  }
  
  public void goBack(Place defaultPlace) {
    Place previousPlace = getPreviousPlace();
    if (previousPlace != null) {
      goTo(previousPlace);
    } else {
      goTo(defaultPlace);
    }
  }
  
  public Place getPreviousPlace() {
    if (history.size() > 1) {
      return history.get(history.size() - 2);
    } else {
      return null;
    }
  }

}
