package it.mate.gwtcommons.client.utils;

import it.mate.gwtcommons.client.factories.BaseClientFactory;
import it.mate.gwtcommons.client.factories.CommonGinjector;
import it.mate.gwtcommons.client.history.BaseActivityMapper;
import it.mate.gwtcommons.client.history.MappedActivityManager;
import it.mate.gwtcommons.client.history.MappedPlaceHistoryHandler;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
//import com.google.gwt.event.shared.EventBus;
//import com.google.gwt.event.shared.HandlerRegistration;

public class MvpUtils {
  
  public static void initMvp(BaseClientFactory<? extends CommonGinjector> clientFactory, SimplePanel panel, String historyId, BaseActivityMapper activityMapper) {
    initMvp(clientFactory, panel, historyId, activityMapper, null);
  }
  
  public static void initMvp(BaseClientFactory<? extends CommonGinjector> clientFactory, SimplePanel panel, String historyId, BaseActivityMapper activityMapper, Place defaultPlace) {
    if (defaultPlace == null) {
      defaultPlace = activityMapper.getDefaultPlace();
    }
    CommonGinjector ginjector = clientFactory.getGinjector();
    PlaceHistoryMapper historyMapper = clientFactory.getPlaceHistoryMapper();
    initActivityManager(ginjector, panel, activityMapper);
    MappedPlaceHistoryHandler historyHandler = new MappedPlaceHistoryHandler(historyId != null ? historyId : activityMapper.getHistoryName(), historyMapper);
//  historyHandler.register(ginjector.getPlaceController(), ginjector.getEventBus(), defaultPlace);
    historyHandler.register(ginjector.getPlaceController(), ginjector.getBinderyEventBus(), defaultPlace);
    historyHandler.handleCurrentHistory();
  }
  
  private static void initActivityManager (CommonGinjector ginjector, SimplePanel panel, ActivityMapper activityMapper) {
    EventBus eventBus = ginjector.getBinderyEventBus();
    ActivityManager activityManager = new MappedActivityManager(activityMapper, eventBus);
    activityManager.setDisplay(panel);
    GwtUtils.log(MvpUtils.class, "initActivityManager", "activityManager = " + activityManager);
  }
  
  public static HandlerRegistration addPlaceChangeHandler(EventBus eventBus, PlaceChangeEvent.Handler handler) {
    return eventBus.addHandler(PlaceChangeEvent.TYPE, handler);
  }

}
