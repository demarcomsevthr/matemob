package it.mate.gwtcommons.client.history;

import it.mate.gwtcommons.client.factories.BaseClientFactory;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

@SuppressWarnings("unchecked")
public abstract class BaseActivityMapper implements ActivityMapper {

  protected BaseClientFactory clientFactory;
  
  public BaseActivityMapper(BaseClientFactory clientFactory) {
    this.clientFactory = clientFactory;
  }

  protected void beforePlaceChange(Place place) {
//  clientFactory.setCurrentPlace(PlaceUtils.beforePlaceChange(place, clientFactory.getCurrentPlace()));
  }
  
  public abstract Place getDefaultPlace();
  
  public abstract String getHistoryName();
  
  private Activity activityAlreadyInstantiated;

  public Activity popActivityAlreadyInstantiated() {
    Activity result = activityAlreadyInstantiated;
    activityAlreadyInstantiated = null;
    return result;
  }

  public void setActivityAlreadyInstantiated(Activity activityAlreadyInstantiated) {
    this.activityAlreadyInstantiated = activityAlreadyInstantiated;
  }

  

}
