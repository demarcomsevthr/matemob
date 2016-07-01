package it.mate.testons.client.factories;

import it.mate.testons.client.activities.MainActivity;
import it.mate.testons.client.activities.mapper.MainActivityMapper;
import it.mate.testons.client.places.MainPlace;
import it.mate.testons.client.places.MainPlaceHistoryMapper;
import it.mate.testons.client.ui.theme.CustomTheme;
import it.mate.testons.client.view.LayoutView;
import it.mate.testons.shared.service.RemoteFacadeAsync;
import it.mate.gwtcommons.client.factories.BaseClientFactoryImpl;
import it.mate.gwtcommons.client.history.BaseActivityMapper;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.mvp.OnsActivityManagerWithNavigator;
import it.mate.onscommons.client.mvp.OnsActivityManagerWithSlidingMenu;
import it.mate.onscommons.client.mvp.OnsActivityManagerWithSlidingNavigator;
import it.mate.onscommons.client.mvp.OnsMvpUtils;
import it.mate.onscommons.client.onsen.dom.Navigator;
import it.mate.onscommons.client.ui.theme.DefaultTheme;
import it.mate.phgcommons.client.place.PlaceControllerWithHistory;
import it.mate.phgcommons.client.utils.OsDetectionUtils;
import it.mate.phgcommons.client.utils.PhgUtils;
import it.mate.phgcommons.client.utils.callbacks.VoidCallback;

import java.util.Map;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.gwtphonegap.client.PhoneGapAvailableEvent;
import com.googlecode.gwtphonegap.client.PhoneGapAvailableHandler;
import com.googlecode.gwtphonegap.client.PhoneGapTimeoutEvent;
import com.googlecode.gwtphonegap.client.PhoneGapTimeoutHandler;

public class AppClientFactoryImpl extends BaseClientFactoryImpl<AppGinjector> implements AppClientFactory {
  
  private static boolean USE_SLIDE_MENU_LAYOUT = false;

  private static boolean USE_SLIDE_NAVIGATOR_LAYOUT = true;

  private PlaceHistoryMapper placeHistoryMapper;
  
  private PlaceController placeController;
  
  private PhoneGap phoneGap;
  
  private Map<String, String> nativeProperties;

  private RemoteFacadeAsync remoteFacade = null;
  
  
  
  @Override
  public void initModule(final Panel modulePanel) {
    this.phoneGap = GWT.create(PhoneGap.class);
    phoneGap.addHandler(new PhoneGapAvailableHandler() {
      @Override
      public void onPhoneGapAvailable(PhoneGapAvailableEvent event) {
        PhgUtils.log("PHONEGAP AVAILABLE EVENT");
        initDisplay(modulePanel);
      }
    });
    phoneGap.addHandler(new PhoneGapTimeoutHandler() {
      @Override
      public void onPhoneGapTimeout(PhoneGapTimeoutEvent event) {
        Window.alert("cannot load phonegap");
      }
    });
    phoneGap.initializePhoneGap();
  }
  
  private void initDisplay(Panel modulePanel) {
    
    PhgUtils.setDefaultExceptionHandler(null);

    initLog();
    
    GwtUtils.setMobileOptimizations(true);
    
    DefaultTheme.Impl.get().css().ensureInjected();
    
    CustomTheme.Instance.get().css().ensureInjected();

    if (OsDetectionUtils.isIOs()) {
      PhgUtils.log("setting resize handler");
      PhgUtils.addResizeHandler(new VoidCallback() {
        public void handle() {
//        PhgUtils.reloadApp();
        }
      });
    }

    createDisplay();
  
  }

  private void initLog() {
    // ATTENZIONE: il log in production mode funziona solo dopo questo settaggio
    GwtUtils.setEnableLogInProductionMode(true);
    String traceActive = PhgUtils.getLocalStorageItem(AppClientFactory.KEY_TRACE_ACTIVE);
    if ("true".equals(traceActive)) {
      PhgUtils.log("***********    TRACE ENABLED   *************");
      PhgUtils.startTrace();
    }
    PhgUtils.log("***********    STARTING NEW APP INSTANCE   ***********");
    GwtUtils.logEnvironment(getClass(), "onModuleLoad");
    PhgUtils.logEnvironment();
    /*
    PhgUtils.log("AppProperties.extendedVersion = "+AppProperties.IMPL.extendedVersion());
    PhgUtils.log("AppConstants.versionNumber = "+AppProperties.IMPL.versionNumber());
    */
  }
  
  private void createDisplay() {
    initMvp(null, new MainActivityMapper(this));
  }
  
  @Override
  public void initMvp(SimplePanel panel, final BaseActivityMapper activityMapper) {
    
    if (USE_SLIDE_MENU_LAYOUT) {
      new OnsActivityManagerWithSlidingMenu(activityMapper, getBinderyEventBus(), new MainPlace(MainPlace.MENU));
      OnsMvpUtils.initMvp(this, activityMapper, new MainPlace());
    } else {
      if (USE_SLIDE_NAVIGATOR_LAYOUT) {
        LayoutView layoutView = new LayoutView();
        layoutView.setPresenter(new MainActivity(this, new MainPlace()));
        
        //TODO [29/05/2015]
        // sebbene piu' performante devo ritorgliere il navigator poping
        // perche' in casi come OrderItemEdit --> OrderItemCompose --> OrderItemEdit
        // non va bene (deve fare un refresh dell'order item modificato)
        OnsActivityManagerWithSlidingNavigator.setAllowNavigatorPoping(false);
        
        new OnsActivityManagerWithSlidingNavigator(activityMapper, getBinderyEventBus(), layoutView) {
          public void onNavigatorInitialized(Navigator navigator) {
            OnsMvpUtils.initMvp(AppClientFactory.IMPL, activityMapper, new MainPlace());
          }
        };
      } else {
        new OnsActivityManagerWithNavigator(activityMapper, getBinderyEventBus());
        OnsMvpUtils.initMvp(this, activityMapper, new MainPlace());
      }
    }
    
    
  }
  
  @Override
  protected AppGinjector createGinjector() {
    return GWT.create(AppGinjector.class);
  }
  
  @Override
  public PlaceHistoryMapper getPlaceHistoryMapper() {
    if (placeHistoryMapper == null)
      placeHistoryMapper = GWT.create(MainPlaceHistoryMapper.class);
    return placeHistoryMapper;
  }
  
  @Override
  public com.google.gwt.event.shared.EventBus getEventBus() {
    throw new RuntimeException("Cannot use com.google.gwt.event.shared.EventBus in mgwt app, use instead injector.getBinderyEventBus");
  }
  
  public com.google.web.bindery.event.shared.EventBus getBinderyEventBus() {
    return getGinjector().getBinderyEventBus();
  }
  
  @Override
  public PlaceController getPlaceController() {
    if (placeController == null) {
      placeController = new PlaceControllerWithHistory(getGinjector().getBinderyEventBus(), new PlaceController.DefaultDelegate());
//    placeController = getGinjector().getPlaceController();
    }
    return placeController;
  }
  
  @Override
  public PhoneGap getPhoneGap() {
    return phoneGap;
  }

  @Override
  public String getNativeProperty(String name, String defValue) {
    String value = null;
    if (value == null) {
      value = PhgUtils.getWindowSetting(name);
    }
    if (value == null && nativeProperties != null) {
      value = nativeProperties.get(name);
    }
    if (value == null) {
      value = defValue;
    }
    return value;
  }
  
  @Override
  public boolean getNativeProperty(String name, boolean defValue) {
    String value = getNativeProperty(name, null);
    if (value == null)
      return defValue;
    return Boolean.parseBoolean(value);
  }
  
  @Override
  public RemoteFacadeAsync getRemoteFacade() {
    if (remoteFacade == null) {
      remoteFacade = getGinjector().getRemoteFacade();
    }
    return remoteFacade;
  }

}
