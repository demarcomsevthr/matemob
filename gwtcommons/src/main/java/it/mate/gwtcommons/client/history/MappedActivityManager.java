package it.mate.gwtcommons.client.history;

import it.mate.gwtcommons.client.mvp.BaseActivity;
import it.mate.gwtcommons.client.utils.GwtUtils;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.web.bindery.event.shared.EventBus;
//import com.google.gwt.event.shared.EventBus;

public class MappedActivityManager extends ActivityManager {
  
  private ActivityMapper mapper;
  
  private Activity previousActivity = null;
  
  public static final boolean USE_ACTIVITY_MAPPER_WRAPPER = true;

  public MappedActivityManager(ActivityMapper mapper, EventBus eventBus) {
    super(mapper, eventBus);
    this.mapper = mapper;
  }
  
  @Override
  public void onPlaceChange(PlaceChangeEvent event) {
    GwtUtils.log(getClass(), hashCode(), "onPlaceChange", "mapper = " + mapper);
    GwtUtils.log(getClass(), hashCode(), "onPlaceChange", "newPlace = " + event.getNewPlace());
    Activity activity = null;
    if (mapper instanceof ActivityMapperWrapper) {
      ActivityMapperWrapper wrapper = (ActivityMapperWrapper)mapper;
      activity = wrapper.getRealActivity(event.getNewPlace());
    } else {
      activity = mapper.getActivity(event.getNewPlace());
    }
    if (activity != null) {
      
      if (previousActivity != null && previousActivity instanceof BaseActivity) {
        BaseActivity previousBaseActivity = (BaseActivity)previousActivity;
        previousBaseActivity.onDispose();
      }
      
      previousActivity = activity;

      // 10/03/2012
      // trucco per impedire che la mapper.getActivity crei una seconda istanza della stessa activity
      // (nella super viene richiamata la mapper.getActivity)
      // (vedi it.mate.econyx.client.activities.mapper.BodyActivityMapper.getActivity)
      if (mapper instanceof BaseActivityMapper) {
        BaseActivityMapper baseActivityMapper = (BaseActivityMapper)mapper;
        baseActivityMapper.setActivityAlreadyInstantiated(activity);
        super.onPlaceChange(event);
        baseActivityMapper.setActivityAlreadyInstantiated(null);
      } else {
        super.onPlaceChange(event);
      }
      
    }
  }
  
  public static class ActivityMapperWrapper implements ActivityMapper {
    
    private ActivityMapper realMapper;
    
    private Activity realActivity;
    
    public ActivityMapperWrapper(ActivityMapper realMapper) {
      super();
      this.realMapper = realMapper;
    }
    
    public Activity getRealActivity(Place place) {
      realActivity = realMapper.getActivity(place);
      return realActivity;
    }

    @Override
    public Activity getActivity(Place place) {
      return realActivity;
    }
    
    @Override
    public String toString() {
      return realMapper.toString();
    }
    
  }
  
  /*
  public class FakeActivityMapper implements ActivityMapper {
    private Activity activity;
    public Activity getActivity() {
      return activity;
    }
    public void setActivity(Activity activity) {
      this.activity = activity;
    }
    @Override
    public Activity getActivity(Place place) {
      return activity;
    }
  }

  private ActivityMapper fakeActivityMapper;
  private ActivityMapper getNewFakeActivityMapper() {
    this.fakeActivityMapper = new FakeActivityMapper();
    return fakeActivityMapper;
  }
  */
  
}
