package it.mate.testleaflet.client.factories;

import it.mate.testleaflet.client.activities.mapper.MainActivityMapper;
import it.mate.testleaflet.client.view.AccountEditView;
import it.mate.testleaflet.client.view.CartConfView;
import it.mate.testleaflet.client.view.CartListView;
import it.mate.testleaflet.client.view.CategorieListView;
import it.mate.testleaflet.client.view.HomeView;
import it.mate.testleaflet.client.view.MenuView;
import it.mate.testleaflet.client.view.MessageListView;
import it.mate.testleaflet.client.view.OrderEditView;
import it.mate.testleaflet.client.view.OrderItemComposeView;
import it.mate.testleaflet.client.view.OrderItemEditView;
import it.mate.testleaflet.client.view.OrderItemImageView;
import it.mate.testleaflet.client.view.OrderListView;
import it.mate.testleaflet.client.view.SettingsView;
import it.mate.testleaflet.client.view.TimbriListView;
import it.mate.testleaflet.client.view.TimbroDetailView;
import it.mate.testleaflet.shared.service.RemoteFacadeAsync;
import it.mate.gwtcommons.client.factories.CommonGinModule;
import it.mate.gwtcommons.client.factories.CommonGinjector;
import it.mate.phgcommons.client.utils.Dao;

import com.google.gwt.inject.client.GinModules;

@GinModules ({CommonGinModule.class, AppGinModule.class})
public interface AppGinjector extends CommonGinjector {
  
  public MainActivityMapper getMainActivityMapper();
  
  public RemoteFacadeAsync getRemoteFacade();
  
  public Dao getMainDao();
  
  public MenuView getMenuView();
  
  public HomeView getHomeView();
  
  public SettingsView getSettingsView();
  
  public CategorieListView getCategorieListView();
  
  public TimbriListView getTimbriListView();
  
  public TimbroDetailView getTimbroDetailView();
  
  public OrderItemEditView getOrderItemEditView();
  
  public OrderItemComposeView getOrderItemComposeView();
  
  public OrderItemImageView getOrderItemImageView();
  
  public MessageListView getMessageListView();
  
  public AccountEditView getAccountEditView();
  
  public CartListView getCartListView();
  
  public OrderListView getOrderListView();
  
  public OrderEditView getOrderEditView();
  
  public CartConfView getCartConfView();
  
}
