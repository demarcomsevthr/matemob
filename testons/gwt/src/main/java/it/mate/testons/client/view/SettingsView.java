package it.mate.testons.client.view;

import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.onscommons.client.ui.OnsLabel;
import it.mate.testons.client.constants.AppProperties;
import it.mate.testons.client.view.SettingsView.Presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class SettingsView extends AbstractBaseView<Presenter> {

  public interface Presenter extends BasePresenter {
    public void goToHomeView();
  }

  public interface ViewUiBinder extends UiBinder<Widget, SettingsView> { }

  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
  
  @UiField Label counterLbl;
  @UiField OnsLabel verLbl;
  
  boolean waiting = false;
  
  public SettingsView() {
    initUI();
  }

  private void initUI() {
    initWidget(uiBinder.createAndBindUi(this));
    verLbl.setText("Version " + AppProperties.IMPL.versionNumber());
  }
  
  @Override
  public void setModel(Object model, String tag) {
    if (model instanceof String) {
      counterLbl.setText((String)model);
    }
  }
  
}
