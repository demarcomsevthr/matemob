package it.mate.gwtcommons.client.factories;

import it.mate.gwtcommons.client.history.BaseActivityMapper;
import it.mate.gwtcommons.client.mvp.CurrentPlaceHolder;

import java.util.Map;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.web.bindery.event.shared.EventBus;
//import com.google.gwt.event.shared.EventBus;

public interface BaseClientFactory <G extends CommonGinjector> extends CurrentPlaceHolder {

  public PlaceController getPlaceController();
  
  public G getGinjector();
  
  public void initMvp(SimplePanel panel, PlaceHistoryMapper historyMapper, BaseActivityMapper activityMapper);  

  public void initModule(Panel modulePanel);
  
  public EventBus getEventBus();
  
  public PlaceHistoryMapper getPlaceHistoryMapper();

  public void setCustomClientFactory(AbstractCustomClientFactory customClientFactory);

  public AbstractCustomClientFactory internalGetCustomClientFactory();

  public void setServerProperties(Map<String, String> serverProperties);
  
  /*

  public String getServerProperty(String name, String defaultValue);

  public String getServerProperty(String name);

  public Boolean getServerPropertyBoolean(String name, Boolean defaultValue);
  
  public Boolean getServerPropertyBoolean(String name);
  
  */
  
}
