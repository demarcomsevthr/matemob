package it.mate.gwtcommons.client.mvp;

import com.google.gwt.place.shared.Place;

public interface ReversiblePlace {
  
  public Place getPreviousPlace();
  
  public void setPreviousPlace(Place previousPlace);

}
