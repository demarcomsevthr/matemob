package it.mate.onscommons.client.event;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;

public class OnsPlaceChangeEvent extends PlaceChangeEvent {

  private boolean isMenuPlace = false;
  
  private Integer insertIndex = null;
  
  private String animation;
  
  public OnsPlaceChangeEvent(Place newPlace) {
    super(newPlace);
  }

  public OnsPlaceChangeEvent(Place newPlace, Integer insertIndex) {
    super(newPlace);
    this.insertIndex = insertIndex;
  }
  
  public OnsPlaceChangeEvent(Place newPlace, boolean isMenuPlace) {
    super(newPlace);
    this.isMenuPlace = isMenuPlace;
  }
  
  public boolean isMenuPlace() {
    return isMenuPlace;
  }
  
  public Integer getInsertIndex() {
    return insertIndex;
  }
  
  public OnsPlaceChangeEvent setAnimation(String animation) {
    this.animation = animation;
    return this;
  }
  
  public String getAnimation() {
    return animation;
  }
  
}
