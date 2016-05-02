package it.mate.gwtcommons.client.places;

import com.google.gwt.place.shared.Place;

public interface HistoryPlace {
  
  String getHistoryName();
  
  Place setHistoryName(String name);
  
  boolean isHistoryAppend();
  
  Place setHistoryAppend();
  
}
