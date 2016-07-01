package it.mate.testons.client.view;

import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.onscommons.client.event.TapEvent;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.ui.OnsToolbarButton;
import it.mate.onscommons.client.utils.OnsDialogUtils;
import it.mate.phgcommons.client.utils.PhgUtils;
import it.mate.testons.client.logic.AppEvent;
import it.mate.testons.client.view.HomeView.Presenter;

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
    public void goToDynListView();
  }

  public interface ViewUiBinder extends UiBinder<Widget, HomeView> { }

  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
  
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
    PhgUtils.log("btnMenu.tapped");
    getPresenter().showMenu();
  }
  
  @UiHandler("btnDialog")
  public void onBtnDialog(TapEvent event) {
    OnsDialogUtils.alert("Un dialog!");
  }
  
  @UiHandler("btnDynList")
  public void onBtnDynList(TapEvent event) {
    getPresenter().goToDynListView();
  }
  
  @UiHandler("testBtn")
  public void onTestBtn(TapEvent event) {
    OnsDialogUtils.alert("Test btn!");
  }
  
}
