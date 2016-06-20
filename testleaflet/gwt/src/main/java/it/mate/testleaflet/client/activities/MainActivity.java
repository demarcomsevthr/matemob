package it.mate.testleaflet.client.activities;

import it.mate.testleaflet.client.factories.AppClientFactory;
import it.mate.testleaflet.client.logic.AppEvent;
import it.mate.testleaflet.client.logic.MainDao;
import it.mate.testleaflet.client.logic.TimbriUtils;
import it.mate.testleaflet.client.places.MainPlace;
import it.mate.testleaflet.client.view.AccountEditView;
import it.mate.testleaflet.client.view.CartConfView;
import it.mate.testleaflet.client.view.CartListView;
import it.mate.testleaflet.client.view.CategorieListView;
import it.mate.testleaflet.client.view.HomeView;
import it.mate.testleaflet.client.view.LayoutView;
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
import it.mate.testleaflet.shared.model.Account;
import it.mate.testleaflet.shared.model.Categoria;
import it.mate.testleaflet.shared.model.Order;
import it.mate.testleaflet.shared.model.OrderItem;
import it.mate.testleaflet.shared.model.Timbro;
import it.mate.testleaflet.shared.model.impl.AccountTx;
import it.mate.testleaflet.shared.model.impl.DevInfoTx;
import it.mate.testleaflet.shared.model.impl.OrderItemTx;
import it.mate.testleaflet.shared.model.impl.OrderTx;
import it.mate.gwtcommons.client.factories.BaseClientFactory;
import it.mate.gwtcommons.client.mvp.BaseView;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.gwtcommons.client.utils.ObjectWrapper;
import it.mate.gwtcommons.shared.rpc.RpcMap;
import it.mate.gwtcommons.shared.rpc.ValueConstructor;
import it.mate.onscommons.client.event.TapEvent;
import it.mate.onscommons.client.event.TapHandler;
import it.mate.onscommons.client.mvp.OnsAbstractActivity;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.ui.HasTapHandlerImpl;
import it.mate.onscommons.client.ui.OnsButton;
import it.mate.onscommons.client.ui.OnsDialog;
import it.mate.onscommons.client.ui.OnsToolbar;
import it.mate.onscommons.client.utils.OnsDialogUtils;
import it.mate.phgcommons.client.plugins.FileSystemPlugin;
import it.mate.phgcommons.client.plugins.ImagePickerPlugin;
import it.mate.phgcommons.client.plugins.PushNotification;
import it.mate.phgcommons.client.plugins.PushPlugin;
import it.mate.phgcommons.client.utils.OsDetectionUtils;
import it.mate.phgcommons.client.utils.PhgUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.web.bindery.event.shared.EventBus;

@SuppressWarnings("rawtypes")
public class MainActivity extends OnsAbstractActivity implements 
    LayoutView.Presenter, MenuView.Presenter, HomeView.Presenter, SettingsView.Presenter, TimbriListView.Presenter, TimbroDetailView.Presenter, 
    OrderItemEditView.Presenter, OrderItemComposeView.Presenter, OrderItemImageView.Presenter,
    MessageListView.Presenter, AccountEditView.Presenter,
    CartListView.Presenter, CategorieListView.Presenter,
    OrderListView.Presenter, OrderEditView.Presenter,
    CartConfView.Presenter
  {
  
  private MainPlace place;
  
  private BaseView view;
  
  private MainDao dao = (MainDao)AppClientFactory.IMPL.getGinjector().getMainDao();
  
  private Timer daoTimer;

  private static boolean checkForRemoteUpdatesInProgress = false;
  
  private static final String GCM_DEBUG_REG_ID = "APA91bH9kMBuTNn32SJho3ZqjJlManVvsd8KtM9Tp1jiwYpdQXE8DdM8FXPlVil46HhQiZCP-Rvwf2qp6XeCnD89qHqF3wWo7dfH0VFY5iuuXSm7o0OKMSaLFCvsYVOBo2iPhHMARnWO";
  
  private static final String GCM_SENDER_ID = "106218079007";
  
  private static final boolean DO_DEBUG = true;
  
  private static boolean saveOrderInProgress = false;
  
  private static OrderItem selectedOrderItem;
  
  public OrderItem getSelectedOrderItem() {
    return selectedOrderItem;
  }
  protected void setSelectedOrderItem(OrderItem selectedOrderItem) {
    MainActivity.selectedOrderItem = selectedOrderItem;
  }
  
  
  public MainActivity(BaseClientFactory clientFactory, MainPlace place) {
    this.place = place;
  }
  
  private void initApp() {
    registerPushNotifications();
    checkForRemoteUpdates(false);
  }
  
  @Override
  @SuppressWarnings("unchecked")
  public void start(AcceptsOneWidget panel, EventBus eventBus) {
    
    if (place.getToken().equals(MainPlace.HOME)) {
//    PhgUtils.setDesktopDebugBorder(384, 682); // LG G3 5.5' RATIO (1440x2560)
      PhgUtils.setDesktopDebugBorder(384, 568); // NEXUS 4 4.7' RATIO (768 x 1280)
    }
    
    daoTimer = GwtUtils.createTimer(500, new Delegate<Void>() {
      public void execute(Void element) {
        if (dao.isReady()) {
          daoTimer.cancel();
          TimbriUtils.doRun();
          initApp();
          fireAppStateChangeEvent(new AppEvent(AppEvent.DB_READY));
        }
      }
    });
    
    if (dao.isReady()) {
      initApp();
    }
    
    if (place.getToken().equals(MainPlace.HOME)) {
      testServerConnection();
      getDevInfoId(new Delegate<String>() {
        public void execute(String devInfoId) {
          PhgUtils.log("devInfoId is " + devInfoId);
        }
      });
      this.view = AppClientFactory.IMPL.getGinjector().getHomeView();
    }
    
    if (place.getToken().equals(MainPlace.MENU)) {
      this.view = AppClientFactory.IMPL.getGinjector().getMenuView();
    }
    
    if (place.getToken().equals(MainPlace.SETTINGS)) {
      this.view = AppClientFactory.IMPL.getGinjector().getSettingsView();
    }
    
    if (place.getToken().equals(MainPlace.CATEGORIE_LIST)) {
      this.view = AppClientFactory.IMPL.getGinjector().getCategorieListView();
    }
    
    if (place.getToken().equals(MainPlace.TIMBRI_LIST)) {
      this.view = AppClientFactory.IMPL.getGinjector().getTimbriListView();
    }
    
    if (place.getToken().equals(MainPlace.TIMBRO_DETAIL)) {
      this.view = AppClientFactory.IMPL.getGinjector().getTimbroDetailView();
    }
    
    if (place.getToken().equals(MainPlace.ORDER_ITEM_EDIT)) {
      this.view = AppClientFactory.IMPL.getGinjector().getOrderItemEditView();
    }
    
    if (place.getToken().equals(MainPlace.ORDER_ITEM_COMPOSE)) {
      this.view = AppClientFactory.IMPL.getGinjector().getOrderItemComposeView();
    }
    
    if (place.getToken().equals(MainPlace.ORDER_ITEM_IMAGE)) {
      this.view = AppClientFactory.IMPL.getGinjector().getOrderItemImageView();
    }
    
    if (place.getToken().equals(MainPlace.MESSAGE_LIST)) {
      this.view = AppClientFactory.IMPL.getGinjector().getMessageListView();
    }
    
    if (place.getToken().equals(MainPlace.ACCOUNT_EDIT)) {
      this.view = AppClientFactory.IMPL.getGinjector().getAccountEditView();
    }
    
    if (place.getToken().equals(MainPlace.CART_LIST)) {
      this.view = AppClientFactory.IMPL.getGinjector().getCartListView();
    }
    
    if (place.getToken().equals(MainPlace.CART_CONF)) {
      this.view = AppClientFactory.IMPL.getGinjector().getCartConfView();
    }
    
    if (place.getToken().equals(MainPlace.ORDER_LIST)) {
      this.view = AppClientFactory.IMPL.getGinjector().getOrderListView();
    }
    
    if (place.getToken().equals(MainPlace.ORDER_EDIT)) {
      this.view = AppClientFactory.IMPL.getGinjector().getOrderEditView();
    }
    
    view.setPresenter(this);
    
    panel.setWidget(view.asWidget());

    GwtUtils.deferredExecution(new Delegate<Void>() {
      public void execute(Void element) {
        PhgUtils.log("activity - setting model deferred");;
        retrieveModel();
      }
    });
    
  }
  
  private void retrieveModel() {
    if (place.getToken().equals(MainPlace.HOME)) {
//    findUpdatedOrders();
    }
    if (place.getToken().equals(MainPlace.CATEGORIE_LIST)) {
      dao.findAllCategorie(new Delegate<List<Categoria>>() {
        public void execute(List<Categoria> categorie) {
          view.setModel(categorie, "categorie");
        }
      });
    }
    if (place.getToken().equals(MainPlace.TIMBRI_LIST)) {
      if (place.getModel() instanceof Categoria) {
        final Categoria categoria = (Categoria)place.getModel();
        dao.findTimbriByCategoria(categoria.getCodice(), new Delegate<List<Timbro>>() {
          public void execute(List<Timbro> timbri) {
            
            Collections.sort(timbri, new Comparator<Timbro>() {
              public int compare(Timbro t1, Timbro t2) {
                return t1.getNome().compareTo(t2.getNome());
              }
            });
            
            view.setModel(categoria.getDescrizione(), "categoria");
            view.setModel(timbri, "timbri");
          }
        });
      } else {
        dao.findAllTimbri(new Delegate<List<Timbro>>() {
          public void execute(List<Timbro> timbri) {
            view.setModel(timbri, "timbri");
          }
        });
      }
    }
    if (place.getToken().equals(MainPlace.TIMBRO_DETAIL)) {
      view.setModel(place.getModel());
    }
    if (place.getToken().equals(MainPlace.ORDER_ITEM_EDIT)) {
      if (place.getModel() instanceof OrderItem) {
        view.setModel(place.getModel());
      } else {
        view.setModel(getSelectedOrderItem());
      }
    }
    if (place.getToken().equals(MainPlace.ORDER_ITEM_COMPOSE)) {
      if (place.getModel() instanceof OrderItem) {
        view.setModel(place.getModel());
      } else {
        if (getSelectedOrderItem() != null) {
          view.setModel(getSelectedOrderItem());
        } else {
          
          if (DO_DEBUG) {
            dao.findOrderInCart(new Delegate<List<Order>>() {
              public void execute(List<Order> results) {
                if (results != null && results.size() > 0) {
                  Order order = results.get(0);
                  view.setModel(order.getItems().get(0));
                }
              }
            });
          }
          
        }
      }
    }
    if (place.getToken().equals(MainPlace.ORDER_ITEM_IMAGE)) {
      if (place.getModel() instanceof OrderItem) {
        view.setModel(place.getModel());
      } else {
        view.setModel(getSelectedOrderItem());
      }
    }
    if (place.getToken().equals(MainPlace.ACCOUNT_EDIT)) {
      getAccount(new Delegate<Account>() {
        public void execute(Account account) {
          view.setModel(account);
        }
      });
    }
    if (place.getToken().equals(MainPlace.CART_LIST)) {
      dao.findOrderInCart(new Delegate<List<Order>>() {
        public void execute(List<Order> results) {
          if (results != null && results.size() == 1) {
            setSelectedOrderItem(null);
            view.setModel(results.get(0));
          } else {
            view.setModel(null);
          }
        }
      });
    }
    if (place.getToken().equals(MainPlace.CART_CONF)) {
      if (place.getModel() != null) {
        view.setModel(place.getModel());
      } else {
        dao.findOrderInCart(new Delegate<List<Order>>() {
          public void execute(List<Order> results) {
            if (results != null && results.size() == 1) {
              setSelectedOrderItem(null);
              view.setModel(results.get(0));
              if (saveOrderInProgress) {
                view.setModel(saveOrderInProgress, Tags.SAVE_ORDER_ON_PROGRESS);  
              }
            } else {
              view.setModel(null);
            }
          }
        });
      }
    }
    if (place.getToken().equals(MainPlace.MESSAGE_LIST)) {
      if (place.getModel() instanceof OrderItem) {
        OrderItem orderItem = (OrderItem)place.getModel();
        view.setModel(orderItem);
      } else {

      }
    }
    if (place.getToken().equals(MainPlace.ORDER_LIST)) {
      dao.findSentOrders(new Delegate<List<Order>>() {
        public void execute(List<Order> orders) {
          view.setModel(orders, "orders");
        }
      });
    }
    if (place.getToken().equals(MainPlace.ORDER_EDIT)) {
      view.setModel(place.getModel());
    }
  }
  
  @Override
  public BaseView getView() {
    return this.view;
  }

  @Override
  public void goToPrevious() {
    OnsenUi.goToPreviousPlace(AppClientFactory.IMPL.getPlaceController(), new MainPlace());
  }

  @Override
  public void goToHomeView() {
    AppClientFactory.IMPL.getPlaceController().goTo(new MainPlace(MainPlace.HOME));
  }

  @Override
  public void goToSettingsView() {
    AppClientFactory.IMPL.getPlaceController().goTo(new MainPlace(MainPlace.SETTINGS));
  }

  @Override
  public void goToTimbriListView() {
    AppClientFactory.IMPL.getPlaceController().goTo(new MainPlace(MainPlace.TIMBRI_LIST));
  }

  @Override
  public void goToTimbriListView(Categoria categoria) {
    AppClientFactory.IMPL.getPlaceController().goTo(new MainPlace(MainPlace.TIMBRI_LIST, categoria));
  }

  @Override
  public void goToCategorieListView() {
    AppClientFactory.IMPL.getPlaceController().goTo(new MainPlace(MainPlace.CATEGORIE_LIST));
  }

  @Override
  public void goToTimbroDetailView(Timbro timbro) {
    AppClientFactory.IMPL.getPlaceController().goTo(new MainPlace(MainPlace.TIMBRO_DETAIL, timbro));
  }

  @Override
  public void goToOrderItemEditView(OrderItem orderItem) {
    AppClientFactory.IMPL.getPlaceController().goTo(new MainPlace(MainPlace.ORDER_ITEM_EDIT, orderItem));
  }

  public void goToTimbroComposeView(Timbro timbro) {
    AppClientFactory.IMPL.getPlaceController().goTo(new MainPlace(MainPlace.ORDER_ITEM_COMPOSE, timbro));
  }

  @Override
  public void goToTimbroComposeView(OrderItem orderItem) {
    AppClientFactory.IMPL.getPlaceController().goTo(new MainPlace(MainPlace.ORDER_ITEM_COMPOSE, orderItem));
  }

  @Override
  public void goToOrderItemImageView(OrderItem orderItem) {
    AppClientFactory.IMPL.getPlaceController().goTo(new MainPlace(MainPlace.ORDER_ITEM_IMAGE, orderItem));
  }

  @Override
  public void goToMessageListView() {
    AppClientFactory.IMPL.getPlaceController().goTo(new MainPlace(MainPlace.MESSAGE_LIST));
  }

  @Override
  public void goToMessageListView(OrderItem orderItem) {
    AppClientFactory.IMPL.getPlaceController().goTo(new MainPlace(MainPlace.MESSAGE_LIST, orderItem));
  }

  @Override
  public void goToAccountEditView() {
    AppClientFactory.IMPL.getPlaceController().goTo(new MainPlace(MainPlace.ACCOUNT_EDIT));
  }

  @Override
  public void goToCartListView() {
    AppClientFactory.IMPL.getPlaceController().goTo(new MainPlace(MainPlace.CART_LIST));
  }

  @Override
  public void goToCartConfView(Order order) {
    AppClientFactory.IMPL.getPlaceController().goTo(new MainPlace(MainPlace.CART_CONF, order));
  }

  @Override
  public void goToOrderListView() {
    AppClientFactory.IMPL.getPlaceController().goTo(new MainPlace(MainPlace.ORDER_LIST));
  }

  @Override
  public void goToOrderEditView(Order order) {
    AppClientFactory.IMPL.getPlaceController().goTo(new MainPlace(MainPlace.ORDER_EDIT, order));
  }

  @Override
  public void showMenu() {
    OnsenUi.getSlidingMenu().toggleMenu();
  }

  @Override
  public void addTimbroToCurrentOrder(final Timbro timbro, Delegate<OrderItem> delegate) {
    
    if (delegate == null) {
      delegate = new Delegate<OrderItem>() {
        public void execute(OrderItem orderItem) {
          goToOrderItemEditView(orderItem);
        }
      };
    }
    
    final Delegate<OrderItem> fDelegate = delegate;

    dao.findOrderInCart(new Delegate<List<Order>>() {
      public void execute(List<Order> results) {
        
        if (results == null || results.size() == 0) {
          Order order = new OrderTx();
          order.setCodice("CARTORDER");
          order.setState(Order.STATE_IN_CART);
          order.getItems().add(createOrderItem(order, timbro, 1d));
          dao.saveOrder(order, new Delegate<Order>() {
            public void execute(Order order) {
              addTimbroToCurrentOrder(timbro, fDelegate);
            }
          });
          return;
        }
        
        Order order = results.get(0);
        
        for (OrderItem orderItem : order.getItems()) {
          if (orderItem.getTimbro().getId().equals(timbro.getId())) {
            
            if (orderItem.getRows() == null || orderItem.getRows().size() == 0) {
              setSelectedOrderItem(orderItem);
              fDelegate.execute(orderItem);
              return;
            }

            if (DO_DEBUG) {
              // TODO: serve solo per testarlo, poi va tolto e si va sempre in insert
              setSelectedOrderItem(orderItem);
              fDelegate.execute(orderItem);
              return;
            }
            
          }
        }

        OrderItem orderItem = createOrderItem(order, timbro, 1d);
        order.getItems().add(orderItem);
        setSelectedOrderItem(orderItem);
        fDelegate.execute(orderItem);
        return;
        
      }
    });
    
  }
  
  @Override
  public void addOrderItemToCart(final OrderItem orderItem) {
    orderItem.setInCart(true);
    saveOrderItemOnDevice(orderItem, new Delegate<Order>() {
      public void execute(Order element) {
        goToCartListView();
      }
    });
  }
  
  private OrderItem createOrderItem(Order order, Timbro timbro, double qty) {
    OrderItem item = new OrderItemTx(order);
    item.setQuantity(1d);
    item.setTimbro(timbro);
    return item;
  }

  private void ensureDevInfoId() {
    String devInfoId = getDevInfoIdFromLocalStorage();
    if (devInfoId != null)
      return;
    
    final String duringGenerateDevInfoSemaphore = "duringGenerateDevInfo";
    
    String duringGenerateDevInfo = (String)GwtUtils.getClientAttribute(duringGenerateDevInfoSemaphore);
    if (duringGenerateDevInfo != null)
      return;
    
    GwtUtils.setClientAttribute(duringGenerateDevInfoSemaphore, "true");
    
    String os = (OsDetectionUtils.isAndroid() ? "android" : OsDetectionUtils.isIOs() ? "ios" : "other");
    String layout = PhgUtils.getLayoutInfo();
    String devName = PhgUtils.getDeviceName();
    String phgVersion = PhgUtils.getDevicePhonegap();
    String platform = PhgUtils.getDevicePlatform();
    String devUuid = PhgUtils.getDeviceUuid();
    String devVersion = PhgUtils.getDeviceVersion();
    
    DevInfoTx devInfo = new DevInfoTx();
    devInfo.setOs(os);
    devInfo.setLayout(layout);
    devInfo.setDevName(devName);
    devInfo.setPhgVersion(phgVersion);
    devInfo.setPlatform(platform);
    devInfo.setDevUuid(devUuid);
    devInfo.setDevVersion(devVersion);
    
    AppClientFactory.IMPL.getRemoteFacade().sendDevInfo(devInfo.toRpcMap(), 
        new AsyncCallback<RpcMap>() {
          public void onFailure(Throwable caught) {
            GwtUtils.removeClientAttribute(duringGenerateDevInfoSemaphore);
          }
          public void onSuccess(RpcMap map) {
            if (map != null) {
              DevInfoTx devInfo = new DevInfoTx().fromRpcMap(map);
              if (devInfo.getId() != null) {
                PhgUtils.log("received devInfoId "+ devInfo.getId() +" from remote facade");
                setDevInfoIdInLocalStorage(devInfo.getId());
                GwtUtils.removeClientAttribute(duringGenerateDevInfoSemaphore);
              }
            }
          }
      });
    
  }
  
  private void testServerConnection() {
    AppClientFactory.IMPL.getRemoteFacade().getServerTime(new AsyncCallback<Date>() {
      public void onSuccess(Date result) {
        PhgUtils.log("SERVER CONNECTION SUCCESS: " + GwtUtils.dateToString(result, "dd/MM/yyyy HH:mm:ss,SSS"));
      }
      public void onFailure(Throwable caught) {
        PhgUtils.log("SERVER CONNECTION FAILURE");
      }
    });
  }

  public void getDevInfoId(final Delegate<String> delegate) {
    final ObjectWrapper<Boolean> delegateFired = new ObjectWrapper<Boolean>(false);
    GwtUtils.createTimerDelegate(500, true, new Delegate<Timer>() {
      public void execute(Timer timer) {
        ensureDevInfoId();
        String devInfoId = getDevInfoIdFromLocalStorage();
        if (devInfoId != null) {
          timer.cancel();
          if (!delegateFired.get()) {
            delegateFired.set(true);
            delegate.execute(devInfoId);
          }
        }
      }
    });
  }
  
  protected String getDevInfoIdFromLocalStorage() {
    return PhgUtils.getLocalStorageItem("devInfoId");
  }

  protected void setDevInfoIdInLocalStorage(String devInfoId) {
    PhgUtils.setLocalStorageItem("devInfoId", devInfoId);
  }
  
  @Override
  public void resetDB() {
    dao.dropDB(new Delegate<Void>() {
      public void execute(Void element) {
        PhgUtils.log("Drop finished, realoading app");
        GwtUtils.deferredExecution(500, new Delegate<Void>() {
          public void execute(Void element) {
            PhgUtils.reloadAppHome();
          }
        });
//      PhgUtils.reloadApp();
      }
    });
  }
  
  @Override
  public void saveAccount(Account account, final Delegate<Account> delegate) {
    AccountTx tx = (AccountTx)account;
    String devInfoId = getDevInfoIdFromLocalStorage();
    tx.setDevInfoId(devInfoId);
    setWaitingState(true);
    AppClientFactory.IMPL.getRemoteFacade().saveAccount(tx.toRpcMap(), new AsyncCallback<RpcMap>() {
      public void onSuccess(RpcMap rpc) {
        if (rpc == null) {
          PhgUtils.log("SAVE ACCOUNT SERVER ERROR");
          setWaitingState(false);
          OnsDialogUtils.alert("Error", "Account saving error!");
        } else {
          dao.saveAccount(new AccountTx().fromRpcMap(rpc), new Delegate<Account>() {
            public void execute(Account account) {
              setWaitingState(false);
              delegate.execute(account);
            }
          });
        }
      }
      public void onFailure(Throwable caught) {
        PhgUtils.log("SAVE ACCOUNT SERVER ERROR");
        setWaitingState(false);
        OnsDialogUtils.alert("Error", "Account saving error ("+ caught.getMessage() +")!");
      }
    });
  }
  
  private void getAccount(Delegate<Account> delegate) {
    dao.findAccount(delegate);
  }

  public void testWaitingState(boolean flag) {
    setWaitingState(flag);
  }
  
  public static void setWaitingState(boolean waiting) {
    OnsToolbar.setWaitingButtonVisible(waiting);
    HasTapHandlerImpl.setAllHandlersDisabled(waiting);
  }
  
  @Override
  public void saveOrderItemOnDevice(OrderItem orderItem, Delegate<Order> delegate) {
    PhgUtils.log("saving item " + orderItem);
    setSelectedOrderItem(orderItem);
    OrderItemTx orderItemTx = (OrderItemTx)orderItem;
    if (orderItemTx.getOrder() == null) {
      PhgUtils.log("ERRORE FATALE - order non settato!");
      PhgUtils.log("ERRORE FATALE - order non settato!");
      PhgUtils.log("ERRORE FATALE - order non settato!");
      PhgUtils.log("ERRORE FATALE - order non settato!");
      throw new IllegalArgumentException("Item senza reference all'ordine - " + orderItem);
    }
    dao.saveOrder(orderItemTx.getOrder(), delegate);
  }
  
  @Override
  public void saveOrderOnDevice(Order order, Delegate<Order> delegate) {
    dao.saveOrder(order, delegate);
  }
  
  @Override
  public void saveOrderOnServer(final Order order, final Delegate<Order> delegate) {
    
    getAccount(new Delegate<Account>() {
      public void execute(Account account) {
        if (account == null) {
          OnsDialogUtils.alert("Attenzione", "Devi registrare un account per proseguire", new Delegate<Void>() {
            public void execute(Void element) {
              saveOrderInProgress = true;
              goToAccountEditView();
            }
          });
        } else {
          setWaitingState(true);
          order.setAccount(account);
          
          if (order.getCreated() == null) {
            order.setCreated(new Date());
          }
          
          OrderTx tx = (OrderTx)order;
          
          RpcMap orderMap = tx.toRpcMap();
          
          AppClientFactory.IMPL.getRemoteFacade().saveOrder(orderMap, new AsyncCallback<RpcMap>() {
            public void onSuccess(RpcMap map) {
              Order result = new OrderTx().fromRpcMap(map);
              
              dao.saveOrder(result, new Delegate<Order>() {
                public void execute(final Order result) {
                  setWaitingState(false);
                  PhgUtils.log("SAVE ORDER RESULT >> " + result);
                  OnsDialogUtils.alert("Info", "Ordine salvato", new Delegate<Void>() {
                    public void execute(Void element) {
                      delegate.execute(result);
                    }
                  });
                }
              });
            }
            public void onFailure(Throwable caught) {
              setWaitingState(false);
              PhgUtils.log("SAVE ORDER SERVER ERROR >> TODO: DIALOG");
              OnsDialogUtils.alert("Error", "Order saving error ("+ caught.getMessage() +")!");
            }
          });
        }
      }
    });
  }
  
  @Override
  public void updateOrdersFromServer() {
    dao.findAllOrders(new Delegate<List<Order>>() {
      public void execute(List<Order> ordiniDevice) {
        if (ordiniDevice != null && ordiniDevice.size() > 0) {
          Date minLastUpdate = new Date();
          for (Order order : ordiniDevice) {
            if (order.getLastUpdate() != null && order.getLastUpdate().before(minLastUpdate)) {
              minLastUpdate = order.getLastUpdate();
            }
          }
          final Date fLastUpdate = minLastUpdate;
          getAccount(new Delegate<Account>() {
            public void execute(Account account) {
              setWaitingState(true);
              AppClientFactory.IMPL.getRemoteFacade().findOrdersByAccount(account.getId(), fLastUpdate, new AsyncCallback<List<RpcMap>>() {
                public void onSuccess(List<RpcMap> results) {
                  setWaitingState(false);
                  if (results != null) {
                    List<Order> ordiniDaAggiornare = new ArrayList<Order>();
                    for (RpcMap map : results) {
                      Order order = new OrderTx().fromRpcMap(map);
                      ordiniDaAggiornare.add(order);
                    }
                    iterateOrdersForUpdate(ordiniDaAggiornare.iterator(), new Delegate<Void>() {
                      public void execute(Void element) {
                        PhgUtils.log(">>> UPDATE COMPLETATO");
                      }
                    });
                  }
                }
                public void onFailure(Throwable caught) {
                  setWaitingState(false);
                  OnsDialogUtils.alert("Error", "Order update error ("+ caught.getMessage() +")!");
                }
              });
            }
          });
        }
      }
    });
  }
  
  private void iterateOrdersForUpdate(final Iterator<Order> it, final Delegate<Void> delegate) {
    if (it.hasNext()) {
      Order order = it.next();
      PhgUtils.log(">>> UPDATING ORDER " + order);
      dao.saveOrder(order, new Delegate<Order>() {
        public void execute(Order updatedOrder) {
          iterateOrdersForUpdate(it, delegate);
        }
      });
    } else {
      delegate.execute(null);
    }
  }
  
  protected void setPushNotificationsRegistered() {
    GwtUtils.setClientAttribute("pushNotificationsRegistered", new String("YES"));
  }
  
  protected boolean arePushNotificationsRegistered() {
    return GwtUtils.getClientAttribute("pushNotificationsRegistered") != null;
  }
  
  @Override
  public void registerPushNotifications() {
    if (arePushNotificationsRegistered()) {
      return;
    }
    getAccount(new Delegate<Account>() {
      public void execute(final Account account) {
        if (account != null) {
          if (PushPlugin.isInstalled()) {
            PhgUtils.log("Push Plugin installed");
            PushPlugin.register(GCM_SENDER_ID, new Delegate<PushNotification>() {
              public void execute(PushNotification notification) {
                if (notification.isRegistrationEvent()) {
                  savePushNotificationIdOnAccount(account, notification.getRegId());
                } else if (notification.isMessageEvent()) {
                  checkForRemoteUpdates(true);
                }
              }
            });
          } else {
            PhgUtils.log("Push Plugin NOT INSTALLED (saving debug regId)");
            savePushNotificationIdOnAccount(account, GCM_DEBUG_REG_ID);
          }
        }
      }
    });
  }
  
  private void savePushNotificationIdOnAccount(Account account, final String pushNotifRegId) {
    setPushNotificationsRegistered();
    if (!pushNotifRegId.equals(account.getPushNotifRegId())) {
      PhgUtils.log("saving pushNotRegId " + pushNotifRegId);
      account.setPushNotifRegId(pushNotifRegId);
      dao.saveAccount(account, new Delegate<Account>() {
        public void execute(Account account) {
          AccountTx atx = (AccountTx)account;
          setWaitingState(true);
          AppClientFactory.IMPL.getRemoteFacade().saveAccount(atx.toRpcMap(), new AsyncCallback<RpcMap>() {
            public void onSuccess(RpcMap result) {
              setWaitingState(false);
              PhgUtils.log("registered pushNotRegId " + pushNotifRegId);
//            OnsDialogUtils.alert("Registered push notifications");
            }
            public void onFailure(Throwable caught) {
              setWaitingState(false);
              OnsDialogUtils.alert("Error", "Order update error ("+ caught.getMessage() +")!");
            }
          });
        }
      });
    }
  }
  
  public void saveCustomerImageOnOrderItem(final OrderItem orderItem, final Delegate<OrderItem> delegate) {
    if (ImagePickerPlugin.isInstalled()) {
      ImagePickerPlugin.getPictures(new ImagePickerPlugin.Options(), new Delegate<List<String>>() {
        public void execute(List<String> results) {
          if (results != null && results.size() > 0) {
            String url = results.get(0);
            String destFile = url.substring(url.lastIndexOf("/"));
            PhgUtils.log("destFile = " + destFile);
            FileSystemPlugin.readExternalFileAsEncodedData(url, /* "image.tmp" */ destFile, new Delegate<String>() {
              public void execute(String fileContent) {
//              PhgUtils.log("fileContent: " + fileContent);
                orderItem.setCustomerImage(fileContent);
                saveOrderItemOnDevice(orderItem, new Delegate<Order>() {
                  public void execute(Order element) {
                    delegate.execute(orderItem);
                  }
                });
              }
            });
          }
        }
      });
    } else {
      PhgUtils.log("ImagePickerPlugin NOT INSTALLED");
      
      if (OsDetectionUtils.isDesktop()) {
        TimbriUtils.readFromLocalhost("http://127.0.0.1:8888/.image?name=timbro-test.jpg", new Delegate<String>() {
          public void execute(String fileContent) {
            
            fileContent = TimbriUtils.encodeImageData(fileContent, "jpeg");
            
            orderItem.setCustomerImage(fileContent);
            saveOrderItemOnDevice(orderItem, new Delegate<Order>() {
              public void execute(Order element) {
                delegate.execute(orderItem);
              }
            });
          }
        });
      }
      
    }
  }
  
  protected void setLastCheckForUpdates() {
    GwtUtils.setClientAttribute("lastCheckForUpdates", new Date());
  }
  
  protected Date getLastCheckForUpdates() {
    return (Date)GwtUtils.getClientAttribute("lastCheckForUpdates");
  }
  
  protected void findUpdatedOrders() {
    dao.findUpdatedOrders(new Delegate<List<Order>>() {
      public void execute(final List<Order> updatedOrders) {
        if (updatedOrders != null && updatedOrders.size() > 0) {
          fireAppStateChangeEvent(new AppEvent(AppEvent.UPDATED_ORDERS_AVAILABLE, updatedOrders));
          GwtUtils.deferredExecution(100, new Delegate<Void>() {
            public void execute(Void element) {
              showUpdatedOrdersDialog(updatedOrders);
            }
          });
        }
      }
    });
  }
  
  @Override
  public void addAppEventHandler(AppEvent.Handler handler) {
    AppClientFactory.IMPL.getBinderyEventBus().addHandler(AppEvent.TYPE, handler);
  }
  
  protected void fireAppStateChangeEvent(AppEvent event) {
    AppClientFactory.IMPL.getBinderyEventBus().fireEvent(event);
  }
  
  protected void checkForRemoteUpdates(final boolean immediately) {
    if (checkForRemoteUpdatesInProgress) {
      return;
    }
    checkForRemoteUpdatesInProgress = true;
    getAccount(new Delegate<Account>() {
      public void execute(Account account) {
        if (account == null) {
          return;
        }
        Date lastCheckForUpdates = getLastCheckForUpdates();
        if (immediately) {
          lastCheckForUpdates = null;
        }
        Date now = new Date();
        // check successivi ogni 10 minuti
        if (lastCheckForUpdates == null || now.getTime() > (lastCheckForUpdates.getTime() + 600000)) {
          AppClientFactory.IMPL.getRemoteFacade().checkForUpdates(account.getId(), new AsyncCallback<RpcMap>() {
            @SuppressWarnings("serial")
            public void onSuccess(RpcMap results) {
              setLastCheckForUpdates();
              List<Order> updatedOrders = results.getField("updatedOrders", new ValueConstructor<OrderTx>() {
                public OrderTx newInnstance() {
                  return new OrderTx();
                }
              });
              if (updatedOrders != null && updatedOrders.size() > 0) {
                iterateOrdersForUpdate(updatedOrders.iterator(), new Delegate<Void>() {
                  public void execute(Void element) {
                    checkForRemoteUpdatesInProgress = false;
                    PhgUtils.log("FINISH UPDATE ORDERS");
                    findUpdatedOrders();
                  }
                });
              } else {
                checkForRemoteUpdatesInProgress = false;
              }
            }
            public void onFailure(Throwable caught) {
              checkForRemoteUpdatesInProgress = false;
              OnsDialogUtils.alert("Error", "Order update error ("+ caught.getMessage() +")!");
            }
          });
        } else {
          checkForRemoteUpdatesInProgress = false;
        }
      }
    });
  }
  
  private void showUpdatedOrdersDialog(final List<Order> updatedOrders) {
    
    VerticalPanel dialogPanel = new VerticalPanel();
    dialogPanel.setWidth("100%");
    dialogPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    
    Label title = new Label("Aggiornamenti");
    title.addStyleName("app-home-alert-title");
    dialogPanel.add(title);
    
    String html = "";
    if (updatedOrders.size() == 1) {
      html = "L'ordine " + updatedOrders.get(0).getCodice() + " è stato aggiornato";
    } else if (updatedOrders.size() > 1) {
      html = "Ci sono " + updatedOrders.size() + " ordini aggiornati";
    }
    HTML messageHtml = new HTML(html);
    messageHtml.addStyleName("app-home-alert-message");
    dialogPanel.add(messageHtml);
    
    OnsButton button = new OnsButton();
    button.setText("Visualizza");
    button.addStyleName("app-home-alert-button");
    dialogPanel.add(button);
    
    final OnsDialog dialog = OnsDialogUtils.createDialog(dialogPanel, true);
    
    button.addTapHandler(new TapHandler() {
      public void onTap(TapEvent event) {
        dialog.hide();
        GwtUtils.deferredExecution(100, new Delegate<Void>() {
          public void execute(Void element) {
            goToOrderListView();
          }
        });
      }
    });
    
  }

}
