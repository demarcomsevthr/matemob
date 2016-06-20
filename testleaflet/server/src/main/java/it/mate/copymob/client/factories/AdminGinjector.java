package it.mate.testleaflet.client.factories;

import it.mate.testleaflet.client.activities.AdminActivity;
import it.mate.testleaflet.client.activities.AdminActivityMapper;
import it.mate.testleaflet.client.view.AccountListView;
import it.mate.testleaflet.client.view.HomeView;
import it.mate.testleaflet.client.view.OrderEditView;
import it.mate.testleaflet.client.view.OrderListView;
import it.mate.testleaflet.shared.service.AdminFacadeAsync;
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
