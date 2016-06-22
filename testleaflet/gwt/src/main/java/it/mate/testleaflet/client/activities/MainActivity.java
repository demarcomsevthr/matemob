package it.mate.testleaflet.client.activities;

import it.mate.gwtcommons.client.factories.BaseClientFactory;
import it.mate.gwtcommons.client.mvp.BaseView;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.mvp.OnsAbstractActivity;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.phgcommons.client.utils.PhgUtils;
import it.mate.testleaflet.client.factories.AppClientFactory;
import it.mate.testleaflet.client.logic.AppEvent;
import it.mate.testleaflet.client.places.MainPlace;
import it.mate.testleaflet.client.view.HomeView;
import it.mate.testleaflet.client.view.LayoutView;
import it.mate.testleaflet.client.view.MenuView;
import it.mate.testleaflet.client.view.SettingsView;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;

@SuppressWarnings("rawtypes")
public class MainActivity extends OnsAbstractActivity implements 
    LayoutView.Presenter, MenuView.Presenter, HomeView.Presenter, SettingsView.Presenter
  {
  
  private MainPlace place;
  
  private BaseView view;
  
  public MainActivity(BaseClientFactory clientFactory, MainPlace place) {
    this.place = place;
  }
  
  private void initApp() {

  }
  
  @Override
  @SuppressWarnings("unchecked")
  public void start(AcceptsOneWidget panel, EventBus eventBus) {
    
    if (place.getToken().equals(MainPlace.HOME)) {
//    PhgUtils.setDesktopDebugBorder(384, 682); // LG G3 5.5' RATIO (1440x2560)
//    PhgUtils.setDesktopDebugBorder(384, 568); // NEXUS 4 4.7' RATIO (768 x 1280)
    }
    
    if (place.getToken().equals(MainPlace.HOME)) {
      this.view = AppClientFactory.IMPL.getGinjector().getHomeView();
    }
    
    if (place.getToken().equals(MainPlace.MENU)) {
      this.view = AppClientFactory.IMPL.getGinjector().getMenuView();
    }
    
    if (place.getToken().equals(MainPlace.SETTINGS)) {
      this.view = AppClientFactory.IMPL.getGinjector().getSettingsView();
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
  public void showMenu() {
    OnsenUi.getSlidingMenu().toggleMenu();
  }

  
  @Override
  public void addAppEventHandler(AppEvent.Handler handler) {
    AppClientFactory.IMPL.getBinderyEventBus().addHandler(AppEvent.TYPE, handler);
  }
  
  protected void fireAppStateChangeEvent(AppEvent event) {
    AppClientFactory.IMPL.getBinderyEventBus().fireEvent(event);
  }

}
