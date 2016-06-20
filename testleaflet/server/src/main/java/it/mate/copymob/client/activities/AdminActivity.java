package it.mate.testleaflet.client.activities;


import it.mate.testleaflet.client.factories.AdminClientFactory;
import it.mate.testleaflet.client.places.AdminPlace;
import it.mate.testleaflet.client.view.AccountListView;
import it.mate.testleaflet.client.view.HomeView;
import it.mate.testleaflet.client.view.OrderEditView;
import it.mate.testleaflet.client.view.OrderItemView;
import it.mate.testleaflet.client.view.OrderListView;
import it.mate.testleaflet.shared.model.Account;
import it.mate.testleaflet.shared.model.Order;
import it.mate.testleaflet.shared.service.AdminFacadeAsync;
import it.mate.gwtcommons.client.mvp.SingletonBaseActivity;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;

public class AdminActivity extends SingletonBaseActivity implements 
    HomeView.Presenter, 
    OrderListView.Presenter, OrderEditView.Presenter, OrderItemView.Presenter,
    AccountListView.Presenter
  {
  
  protected AdminPlace place;

  protected AdminFacadeAsync facade = AdminClientFactory.IMPL.getGinjector().getAdminFacade();
  
  private static Map<String, Delegate<AcceptsOneWidget>> startHandlers;
  
  private static Map<String, Delegate<AdminPlace>> retrieveHandlers;
  
  public AdminActivity(AdminClientFactory clientFactory) {
    super(clientFactory);
    GwtUtils.log(getClass(), "init", "instantiating " + this.hashCode());
  }
  
  public void setPlace(AdminPlace place) {
    this.place = place;
  }
  
  private Map<String, Delegate<AcceptsOneWidget>> ensureStartHandlers() {
    if (startHandlers == null) {
      startHandlers = new HashMap<String, Delegate<AcceptsOneWidget>>();
      startHandlers.put(AdminPlace.HOME, new Delegate<AcceptsOneWidget>() {
        public void execute(AcceptsOneWidget panel) {
          initView(AdminClientFactory.IMPL.getGinjector().getHomeView(), panel);
          retrieveModel();
        }
      });
      startHandlers.put(AdminPlace.ORDER_LIST, new Delegate<AcceptsOneWidget>() {
        public void execute(AcceptsOneWidget panel) {
          initView(AdminClientFactory.IMPL.getGinjector().getOrderListView(), panel);
          retrieveModel();
        }
      });
      startHandlers.put(AdminPlace.ORDER_EDIT, new Delegate<AcceptsOneWidget>() {
        public void execute(AcceptsOneWidget panel) {
          initView(AdminClientFactory.IMPL.getGinjector().getOrderEditView(), panel);
          retrieveModel();
        }
      });
      startHandlers.put(AdminPlace.ACCOUNT_LIST, new Delegate<AcceptsOneWidget>() {
        public void execute(AcceptsOneWidget panel) {
          initView(AdminClientFactory.IMPL.getGinjector().getAccountListView(), panel);
          retrieveModel();
        }
      });
    }
    return startHandlers;
  }

  private Map<String, Delegate<AdminPlace>> ensureRetrieveHandlers() {
    if (retrieveHandlers == null) {
      retrieveHandlers = new HashMap<String, Delegate<AdminPlace>>();
      retrieveHandlers.put(AdminPlace.HOME, new Delegate<AdminPlace>() {
        public void execute(AdminPlace place) { 
          getView().setModel(place.getModel());
          testServerConnection();
        }
      });
      retrieveHandlers.put(AdminPlace.ORDER_LIST, new Delegate<AdminPlace>() {
        public void execute(AdminPlace place) { 
          facade.findAllOrders(new AsyncCallback<List<Order>>() {
            public void onSuccess(List<Order> results) {
              getView().setModel(results);
            }
            public void onFailure(Throwable caught) {
              processRemoteFailure(caught);
            }
          });
        }
      });
      retrieveHandlers.put(AdminPlace.ORDER_EDIT, new Delegate<AdminPlace>() {
        public void execute(AdminPlace place) { 
          if (place.getModel() instanceof String) {
            String orderId = (String)place.getModel();
            facade.findOrderById(orderId, new AsyncCallback<Order>() {
              public void onSuccess(Order result) {
                getView().setModel(result);
              }
              public void onFailure(Throwable caught) {
                processRemoteFailure(caught);
              }
            });
          } else {
            getView().setModel(place.getModel());
          }
        }
      });
      retrieveHandlers.put(AdminPlace.ACCOUNT_LIST, new Delegate<AdminPlace>() {
        public void execute(AdminPlace place) {
          facade.findAllAccounts(new AsyncCallback<List<Account>>() {
            public void onSuccess(List<Account> results) {
              getView().setModel(results);
            }
            public void onFailure(Throwable caught) {
              processRemoteFailure(caught);
            }
          });
        }
      });
    }
    return retrieveHandlers;
  }

  @Override
  public void start(final AcceptsOneWidget panel, EventBus eventBus) {
    GwtUtils.log(getClass(), "start", "starting " + this.hashCode());
    Delegate<AcceptsOneWidget> startHandler = ensureStartHandlers().get(place.getToken());
    if (startHandler != null) {
      startHandler.execute(panel);
    } else {
      throw new IllegalStateException("RICEVUTO PLACE CON TOKEN " + place.getToken() + " NON IMPLEMENTATO");
    }
  }
  
  @Override
  public void goToOrderEdit(Order order) {
    AdminClientFactory.IMPL.getPlaceController().goTo(new AdminPlace(AdminPlace.ORDER_EDIT, order));
  }
  
  @Override
  public void goToOrderEdit(String orderId) {
    AdminClientFactory.IMPL.getPlaceController().goTo(new AdminPlace(AdminPlace.ORDER_EDIT, orderId));
  }
  
  private void retrieveModel() {
    ensureRetrieveHandlers().get(place.getToken()).execute(place);
  }
  
  private void testServerConnection() {
    /*
    facade.checkConnection(new AsyncCallback<Boolean>() {
      public void onSuccess(Boolean result) {
        GwtUtils.log("TEST SERVER CONNECTION SUCCESS");
      }
      public void onFailure(Throwable caught) {
              processFailure(caught);
      }
    });
    */
  }
  
  private void processRemoteFailure(Throwable caught) {
    GwtUtils.log("FAILURE - " + caught.getMessage());
  }
  
  public void updateOrder(Order order, final Delegate<Order> delegate) {
    facade.saveOrder(order, new AsyncCallback<Order>() {
      public void onSuccess(Order result) {
        delegate.execute(result);
      }
      public void onFailure(Throwable caught) {
        processRemoteFailure(caught);
      }
    });
  }
  
  public void sendPushNotification(Account account, String message, String regId) {
    GwtUtils.log("sending notification");
    facade.sendPushNotification(account, message, regId, new AsyncCallback<Void>() {
      public void onSuccess(Void result) {
        GwtUtils.log("sending notification success");
      }
      public void onFailure(Throwable caught) {
        GwtUtils.log("sending notification error");
      }
    });
  }
  
}
