package it.mate.testleaflet.client.view;

import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.onscommons.client.event.TapEvent;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.ui.OnsToolbarButton;
import it.mate.testleaflet.client.logic.AppEvent;
import it.mate.testleaflet.client.view.HomeView.Presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;

public class HomeView extends AbstractBaseView<Presenter> {
  
  public interface Presenter extends BasePresenter {
    public void goToSettingsView();
    public void showMenu();
    public void addAppEventHandler(AppEvent.Handler handler);
  }

  public interface ViewUiBinder extends UiBinder<Widget, HomeView> { }

  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
  
//@UiField Label homeLbl;
  @UiField OnsToolbarButton btnMenu;
  
  public HomeView() {
    initUI();
  }

  private void initProvidedElements() {

  }
  
  private void initUI() {
    initProvidedElements();
    initWidget(uiBinder.createAndBindUi(this));
    btnMenu.setVisible(OnsenUi.isSlidingMenuPresent());
  }
  
  @Override
  public void setModel(Object model, String tag) {
    
  }

  @UiHandler("btnMenu")
  public void onBtnMenu(TapEvent event) {
    getPresenter().showMenu();
  }
  
}
