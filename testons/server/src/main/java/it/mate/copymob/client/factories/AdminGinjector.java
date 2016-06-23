package it.mate.testons.client.factories;

import it.mate.testons.client.activities.AdminActivity;
import it.mate.testons.client.activities.AdminActivityMapper;
import it.mate.testons.client.view.AccountListView;
import it.mate.testons.client.view.HomeView;
import it.mate.testons.client.view.OrderEditView;
import it.mate.testons.client.view.OrderListView;
import it.mate.testons.shared.service.AdminFacadeAsync;
import it.mate.gwtcommons.client.factories.CommonGinModule;
import it.mate.gwtcommons.client.factories.CommonGinjector;

import com.google.gwt.inject.client.GinModules;

@GinModules ({CommonGinModule.class, AdminGinModule.class})
public interface AdminGinjector extends CommonGinjector{

  public AdminFacadeAsync getAdminFacade();
  
  public AdminActivityMapper getAdminActivityMapper();
  
  public AdminActivity getMainActivity();
  
  public HomeView getHomeView();
  
  public OrderListView getOrderListView();
  
  public OrderEditView getOrderEditView();
  
  public AccountListView getAccountListView();
  
}
