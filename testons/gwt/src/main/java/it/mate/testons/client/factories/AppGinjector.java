package it.mate.testons.client.factories;

import it.mate.gwtcommons.client.factories.CommonGinModule;
import it.mate.gwtcommons.client.factories.CommonGinjector;
import it.mate.testons.client.activities.mapper.MainActivityMapper;
import it.mate.testons.client.view.DynListView;
import it.mate.testons.client.view.HomeView;
import it.mate.testons.client.view.MenuView;
import it.mate.testons.client.view.SettingsView;
import it.mate.testons.shared.service.RemoteFacadeAsync;

import com.google.gwt.inject.client.GinModules;

@GinModules ({CommonGinModule.class, AppGinModule.class})
public interface AppGinjector extends CommonGinjector {
  
  public MainActivityMapper getMainActivityMapper();
  
  public RemoteFacadeAsync getRemoteFacade();
  
  public MenuView getMenuView();
  
  public HomeView getHomeView();
  
  public SettingsView getSettingsView();
  
  public DynListView getDynListView();
  
}
