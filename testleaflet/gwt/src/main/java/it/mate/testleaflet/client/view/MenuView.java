package it.mate.testleaflet.client.view;

import it.mate.testleaflet.client.view.MenuView.Presenter;
import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.onscommons.client.event.TapEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;

public class MenuView extends AbstractBaseView<Presenter> {

  public interface Presenter extends BasePresenter {
    public void goToHomeView();
    public void goToSettingsView();
    public void goToTimbriListView();
    public void goToMessageListView();
    public void goToCategorieListView();
    public void goToOrderListView();
    public void goToCartListView();
  }

  public interface ViewUiBinder extends UiBinder<Widget, MenuView> { }

  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
  
  public MenuView() {
    initUI();
  }

  private void initProvidedElements() {

  }

  private void initUI() {
    initProvidedElements();
    initWidget(uiBinder.createAndBindUi(this));
  }
  
  @Override
  public void setModel(Object model, String tag) {

  }

  @UiHandler("btnSettings")
  public void onBtnSettings(TapEvent event) {
    getPresenter().goToSettingsView();
  }
  
  @UiHandler("btnTimbri")
  public void onBtnTimbri(TapEvent event) {
    getPresenter().goToCategorieListView();
  }
  
  @UiHandler("btnMessaggi")
  public void onBtnMessaggi(TapEvent event) {
    getPresenter().goToMessageListView();
  }
  
  @UiHandler("btnOrdini")
  public void onBtnOrdini(TapEvent event) {
    getPresenter().goToOrderListView();
  }
  
  @UiHandler("btnCart")
  public void onBtnCart(TapEvent event) {
    getPresenter().goToCartListView();
  }
  
}
