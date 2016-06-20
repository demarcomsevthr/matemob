package it.mate.testleaflet.client.places;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

@WithTokenizers ({
  AdminPlace.Tokenizer.class
})
public interface AdminPlaceHistoryMapper extends PlaceHistoryMapper {

}
