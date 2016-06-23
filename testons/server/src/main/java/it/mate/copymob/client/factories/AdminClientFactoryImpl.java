package it.mate.testons.client.factories;

import it.mate.testons.client.activities.AdminActivityMapper;
import it.mate.testons.client.places.AdminPlaceHistoryMapper;
import it.mate.gwtcommons.client.factories.BaseClientFactoryImpl;
import it.mate.gwtcommons.client.history.BaseActivityMapper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;

public class AdminClientFactoryImpl extends BaseClientFactoryImpl<AdminGinjector> implements AdminClientFactory {

  private static PlaceHistoryMapper placeHistoryMapper;
  
  @Override
  protected AdminGinjector createGinjector() {
    return GWT.create(AdminGinjector.class);
  }
  
  @Override
  public void initModule(Panel portalPanel) {

  }
  
  @Override
  public void initMvp(SimplePanel panel, BaseActivityMapper activityMapper) {
    super.initMvp(panel, getPlaceHistoryMapper(), activityMapper);
  }

  @Override
  public void initMvp(SimplePanel panel, BaseActivityMapper activityMapper, Place defaultPlace) {
    super.initMvp(panel, getPlaceHistoryMapper(), activityMapper, defaultPlace);
  }

  @Override
  public PlaceHistoryMapper getPlaceHistoryMapper() {
    if (placeHistoryMapper == null)
      placeHistoryMapper = GWT.create(AdminPlaceHistoryMapper.class);
    return placeHistoryMapper;
  }
  
  @Override
  public AdminActivityMapper getAdminActivityMapper() {
    return new AdminActivityMapper(this);
  }

}
