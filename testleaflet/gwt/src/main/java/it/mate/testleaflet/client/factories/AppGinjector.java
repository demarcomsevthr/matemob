package it.mate.testleaflet.client.factories;

import it.mate.gwtcommons.client.factories.CommonGinModule;
import it.mate.gwtcommons.client.factories.CommonGinjector;
import it.mate.phgcommons.client.utils.Dao;
import it.mate.testleaflet.client.activities.mapper.MainActivityMapper;
import it.mate.testleaflet.client.view.HomeView;
import it.mate.testleaflet.client.view.MenuView;
import it.mate.testleaflet.client.view.SettingsView;
import it.mate.testleaflet.shared.service.RemoteFacadeAsync;

import com.google.gwt.inject.client.GinModules;

@GinModules ({CommonGinModule.class, AppGinModule.class})
public interface AppGinjector extends CommonGinjector {
  
  public MainActivityMapper getMainActivityMapper();
  
  public RemoteFacadeAsync getRemoteFacade();
  
  public Dao getMainDao();
  
  public MenuView getMenuView();
  
  public HomeView getHomeView();
  
  public SettingsView getSettingsView();
  
}
