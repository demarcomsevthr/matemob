package it.mate.phgcommons.client.utils;

import it.mate.gwtcommons.client.factories.BaseClientFactory;
import it.mate.gwtcommons.client.factories.CommonGinjector;
import it.mate.gwtcommons.client.history.BaseActivityMapper;
import it.mate.gwtcommons.client.utils.GwtUtils;

import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.mgwt.mvp.client.AnimatableDisplay;
import com.googlecode.mgwt.mvp.client.AnimatingActivityManager;
import com.googlecode.mgwt.mvp.client.Animation;
import com.googlecode.mgwt.mvp.client.AnimationMapper;

public class MvpPhoneUtils {
  
  public static void initMvp(BaseClientFactory<? extends CommonGinjector> clientFactory, SimplePanel panel, String historyId, BaseActivityMapper activityMapper) {
    CommonGinjector ginjector = clientFactory.getGinjector();
    PlaceHistoryMapper historyMapper = clientFactory.getPlaceHistoryMapper();
    initActivityManager(ginjector, panel, activityMapper);
//  MappedPlaceHistoryHandler historyHandler = new MappedPlaceHistoryHandler(historyId != null ? historyId : activityMapper.getHistoryName(), historyMapper);
    PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
    historyHandler.register(clientFactory.getPlaceController(), ginjector.getBinderyEventBus(), activityMapper.getDefaultPlace());
    historyHandler.handleCurrentHistory();
  }
  
  private static void initActivityManager (CommonGinjector ginjector, SimplePanel activityPanel, ActivityMapper activityMapper) {
    EventBus eventBus = ginjector.getBinderyEventBus();
    AnimatableDisplay display = GWT.create(AnimatableDisplay.class);
    DefaultAnimationMapper animationMapper = new DefaultAnimationMapper();
    AnimatingActivityManager activityManager = new AnimatingActivityManager(activityMapper, animationMapper, eventBus);
    activityManager.setDisplay(display);
    activityPanel.add(display);
    GwtUtils.log(MvpPhoneUtils.class, "initActivityManager", "activityManager = " + activityManager);
  }
  
  public static class DefaultAnimationMapper implements AnimationMapper {
    @Override
    public Animation getAnimation(Place oldPlace, Place newPlace) {
      // 18/12/2012 - DA APPROFONDIRE
      // commentata perchè causa la creazione di un div che disabilita tutto
//    return Animation.FADE;
      return null;
    }
  }
  
  
  /*
  public static HandlerRegistration addPlaceChangeHandler(EventBus eventBus, PlaceChangeEvent.Handler handler) {
    return eventBus.addHandler(PlaceChangeEvent.TYPE, handler);
  }
  */

}
