package it.mate.testons.client.activities;

import it.mate.gwtcommons.client.factories.BaseClientFactory;
import it.mate.gwtcommons.client.mvp.BaseView;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.mvp.OnsAbstractActivity;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.phgcommons.client.utils.PhgUtils;
import it.mate.testons.client.factories.AppClientFactory;
import it.mate.testons.client.logic.AppEvent;
import it.mate.testons.client.places.MainPlace;
import it.mate.testons.client.view.DynListView;
import it.mate.testons.client.view.HomeView;
import it.mate.testons.client.view.LayoutView;
import it.mate.testons.client.view.MenuView;
import it.mate.testons.client.view.SettingsView;
import it.mate.testons.shared.model.Prestazione;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;

@SuppressWarnings("rawtypes")
public class MainActivity extends OnsAbstractActivity implements 
    LayoutView.Presenter, MenuView.Presenter, HomeView.Presenter, SettingsView.Presenter,
    DynListView.Presenter
  {
  
  private MainPlace place;
  
  private BaseView view;
  
  public MainActivity(BaseClientFactory clientFactory, MainPlace place) {
    this.place = place;
  }
  
  @Override
  @SuppressWarnings("unchecked")
  public void start(AcceptsOneWidget panel, EventBus eventBus) {
    
    if (place.getToken().equals(MainPlace.HOME)) {
      this.view = AppClientFactory.IMPL.getGinjector().getHomeView();
    }
    
    if (place.getToken().equals(MainPlace.MENU)) {
      this.view = AppClientFactory.IMPL.getGinjector().getMenuView();
    }
    
    if (place.getToken().equals(MainPlace.SETTINGS)) {
      this.view = AppClientFactory.IMPL.getGinjector().getSettingsView();
    }
    
    if (place.getToken().equals(MainPlace.DYN_LIST)) {
      this.view = AppClientFactory.IMPL.getGinjector().getDynListView();
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
    if (place.getToken().equals(MainPlace.DYN_LIST)) {
      
      GwtUtils.deferredExecution(3000, new Delegate<Void>() {
        public void execute(Void element) {
          
          List<Prestazione> prestazioni = new ArrayList<Prestazione>();
          Prestazione prz = null;
          prz = new Prestazione();
          prz.setCodice("100001");
          prz.setDescrizione("PRESTAZIONE UNO");
          prestazioni.add(prz);
          prz = new Prestazione();
          prz.setCodice("100002");
          prz.setDescrizione("PRESTAZIONE DUE");
          prestazioni.add(prz);
          prz = new Prestazione();
          prz.setCodice("100003");
          prz.setDescrizione("PRESTAZIONE TRE");
          prestazioni.add(prz);
          prz = new Prestazione();
          prz.setCodice("100004");
          prz.setDescrizione("PRESTAZIONE QUATTRO");
          prestazioni.add(prz);
          prz = new Prestazione();
          prz.setCodice("100005");
          prz.setDescrizione("PRESTAZIONE CINQUE");
          prestazioni.add(prz);
          prz = new Prestazione();
          prz.setCodice("100006");
          prz.setDescrizione("PRESTAZIONE SEI");
          prestazioni.add(prz);
          prz = new Prestazione();
          prz.setCodice("100007");
          prz.setDescrizione("PRESTAZIONE SETTE");
          prestazioni.add(prz);
          prz = new Prestazione();
          prz.setCodice("100008");
          prz.setDescrizione("PRESTAZIONE OTTO");
          prestazioni.add(prz);
          prz = new Prestazione();
          prz.setCodice("100009");
          prz.setDescrizione("PRESTAZIONE NOVE");
          prestazioni.add(prz);
          prz = new Prestazione();
          prz.setCodice("100010");
          prz.setDescrizione("PRESTAZIONE DIECI");
          prestazioni.add(prz);
          
          view.setModel(prestazioni);
          
        }
      });

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
  public void goToDynListView() {
    AppClientFactory.IMPL.getPlaceController().goTo(new MainPlace(MainPlace.DYN_LIST));
  }

  @Override
  public void showMenu() {
    PhgUtils.log("showMenu");
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
