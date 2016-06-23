package it.mate.testons.client.activities;

import it.mate.testons.client.factories.AdminClientFactory;
import it.mate.testons.client.places.AdminPlace;
import it.mate.gwtcommons.client.history.BaseActivityMapper;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;

public class AdminActivityMapper extends BaseActivityMapper {
  
  public AdminActivityMapper(AdminClientFactory clientFactory) {
    super(clientFactory);
  }

  @Override
  public Activity getActivity(Place place) {
//  clientFactory.setCurrentPlace(PlaceUtils.beforePlaceChange(place, clientFactory.getCurrentPlace()));
    AbstractActivity currentActivity = null;
    if (place instanceof AdminPlace) {
      AdminActivity mainActivity = ((AdminClientFactory)clientFactory).getGinjector().getMainActivity();
      mainActivity.setPlace((AdminPlace)place);
      currentActivity = mainActivity;
    }
    /*
    if (currentActivity != null) {
      ActivityUtils.setCurrentActivity(currentActivity);
    }
    */
    return currentActivity;
  }

  @Override
  public Place getDefaultPlace() {
    return new AdminPlace();
  }

  @Override
  public String getHistoryName() {
    return "adminMVP";
  }

}
