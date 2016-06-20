package it.mate.testleaflet.client.view;

import it.mate.testleaflet.client.view.TimbroDetailView.Presenter;
import it.mate.testleaflet.shared.model.Timbro;
import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.onscommons.client.event.TapEvent;
import it.mate.onscommons.client.ui.OnsLabel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class TimbroDetailView extends AbstractBaseView<Presenter> {

  public interface Presenter extends BasePresenter {
//  public void goToOrderItemEditView(Timbro timbro);
  }

  public interface ViewUiBinder extends UiBinder<Widget, TimbroDetailView> { }

  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
  
  @UiField Panel wrapperPanel;
  @UiField OnsLabel headerLbl;
  
  Timbro timbro;
  
  public TimbroDetailView() {
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
    if (model instanceof Timbro) {
      this.timbro = (Timbro)model;
      headerLbl.setText(timbro.getNome());
    }
  }

  @UiHandler("btnCompose")
  public void onBtnCompose(TapEvent event) {
//  getPresenter().goToOrderItemEditView(timbro);
  }
  
}
