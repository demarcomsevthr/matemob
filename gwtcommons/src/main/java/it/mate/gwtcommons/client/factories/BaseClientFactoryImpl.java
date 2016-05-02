package it.mate.gwtcommons.client.factories;

import it.mate.gwtcommons.client.history.BaseActivityMapper;
import it.mate.gwtcommons.client.history.MappedActivityManager;
import it.mate.gwtcommons.client.history.MappedPlaceHistoryHandler;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.gwtcommons.shared.utils.PropertiesHolder;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
//import com.google.gwt.event.shared.HandlerRegistration;
//import com.google.gwt.event.shared.EventBus;

public abstract class BaseClientFactoryImpl <G extends CommonGinjector> implements BaseClientFactory<G> {

  protected G ginjector = null;
  
  private Place currentPlace;
  
  private AbstractCustomClientFactory customClientFactory;
  
  protected abstract G createGinjector();
  
//private Map<String, String> serverProperties;
  
  private static class HistoryHandlerRegistration {
    MappedPlaceHistoryHandler handler;
    HandlerRegistration registration;
  }
  
  private static Map<String, HistoryHandlerRegistration> historyHandlerRegistrationMap = new HashMap<String, BaseClientFactoryImpl.HistoryHandlerRegistration>();
  
  private static Map<String, MappedActivityManager> activityManagerMap = new HashMap<String, MappedActivityManager>();
  
  public BaseClientFactoryImpl() {
    this.ginjector = createGinjector();
  }
  
  public void initMvp(SimplePanel panel, PlaceHistoryMapper historyMapper, BaseActivityMapper activityMapper) {
    initMvp(panel, historyMapper, activityMapper, null);
  }

  public void initMvp(SimplePanel panel, PlaceHistoryMapper historyMapper, BaseActivityMapper activityMapper, Place defaultPlace) {
    if (defaultPlace == null) {
      defaultPlace = activityMapper.getDefaultPlace();
    }
    GwtUtils.log(getClass(), "initMvp", "*************************************************************");
    GwtUtils.log(getClass(), "initMvp", "activityMapper = " + activityMapper);
    GwtUtils.log(getClass(), "initMvp", "historyName = " + activityMapper.getHistoryName());
    assert ginjector != null : "initMvp: ginjector is null";
    initActivityManager(panel, activityMapper);
    MappedPlaceHistoryHandler historyHandler;
    HistoryHandlerRegistration historyHandlerRegistration = historyHandlerRegistrationMap.get(activityMapper.getHistoryName());
    if (historyHandlerRegistration != null) {
      historyHandler = historyHandlerRegistration.handler;
      historyHandlerRegistration.registration.removeHandler();
    } else {
      historyHandlerRegistration = new HistoryHandlerRegistration();
      historyHandler = new MappedPlaceHistoryHandler(activityMapper.getHistoryName(), historyMapper);
      historyHandlerRegistration.handler = historyHandler;
    }
//  historyHandlerRegistration.registration = historyHandler.register(getPlaceController(), ginjector.getEventBus(), defaultPlace);
    historyHandlerRegistration.registration = historyHandler.register(getPlaceController(), ginjector.getBinderyEventBus(), defaultPlace);
    historyHandlerRegistrationMap.put(activityMapper.getHistoryName(), historyHandlerRegistration);
    historyHandler.handleCurrentHistory();
  }

  private void initActivityManager (SimplePanel panel, ActivityMapper activityMapper) {
//  EventBus eventBus = ginjector.getEventBus();
    EventBus eventBus = ginjector.getBinderyEventBus();

    String historyName = null;
    if (activityMapper instanceof BaseActivityMapper) {
      BaseActivityMapper baseActivityMapper = (BaseActivityMapper)activityMapper;
      historyName = baseActivityMapper.getHistoryName();
    }
    
    ActivityManager activityManager = null;
    if (historyName != null) {
      activityManager = activityManagerMap.get(historyName);
    }
    if (activityManager == null) {
      ActivityMapper actualActivityMapper = null;
      if (MappedActivityManager.USE_ACTIVITY_MAPPER_WRAPPER) {
        actualActivityMapper = new MappedActivityManager.ActivityMapperWrapper(activityMapper);
      } else {
        actualActivityMapper = activityMapper;
      }
      activityManager = new MappedActivityManager(actualActivityMapper, eventBus);
      if (historyName != null) {
        activityManagerMap.put(historyName, (MappedActivityManager)activityManager);
      }
    }
    
    activityManager.setDisplay(panel);
    
    GwtUtils.log(getClass(), "initActivityManager", "activityManager = " + activityManager);
    
  }
  
  public G getGinjector() {
    return ginjector;
  }
  
  public PlaceController getPlaceController() {
    return ginjector.getPlaceController();
  }
  
  public EventBus getEventBus() {
//  EventBus eventBus = ginjector.getEventBus();
    EventBus eventBus = ginjector.getBinderyEventBus();
    GwtUtils.log(getClass(), "getEventBus", "eventBus = " + eventBus);
    return eventBus;
  }
  
  public Place getCurrentPlace() {
    return currentPlace;
  }

  public void setCurrentPlace(Place place) {
   this.currentPlace = place; 
  }

  public AbstractCustomClientFactory internalGetCustomClientFactory() {
    return customClientFactory;
  }

  public void setCustomClientFactory(AbstractCustomClientFactory customClientFactory) {
    this.customClientFactory = customClientFactory;
  }
  
  public void setServerProperties(Map<String, String> serverProperties) {
//  this.serverProperties = serverProperties;
    PropertiesHolder.setProperties(serverProperties);
  }


  /********************************************************************
   * 
   *  PORTAL FACTORY
   * 
   */

  @Override
  public void initModule(Panel portalPanel) {
    throw new RuntimeException("Method initPortalMwt not implemented!");
  }

  /*
  @Override
  public String getServerProperty(String name, String defaultValue) {
//  String value = serverProperties.get(name);
//  if (value == null)
//    value = defaultValue;
//  return value;
    return PropertiesHolder.getPropertyString(name, defaultValue);
  }
  
  @Override
  public String getServerProperty(String name) {
    return getServerProperty(name, null);
  }

  @Override
  public Boolean getServerPropertyBoolean(String name, Boolean defaultValue) {
    String text = getServerProperty(name, null);
    if (text == null)
      return false;
    return Boolean.parseBoolean(text);
  }
  
  @Override
  public Boolean getServerPropertyBoolean(String name) {
    return getServerPropertyBoolean(name, false);
  }
  */
  
}
