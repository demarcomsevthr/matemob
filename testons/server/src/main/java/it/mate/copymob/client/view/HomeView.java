package it.mate.testons.client.view;

import it.mate.testons.client.view.HomeView.Presenter;
import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.mvp.BasePresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class HomeView extends AbstractBaseView<Presenter> {
  
  public interface Presenter extends BasePresenter {
    
  }
  
  public interface ViewUiBinder extends UiBinder<Widget, HomeView> { }
  
  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);

  public HomeView() {
    initUI();
  }
  
  protected void initUI() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  @Override
  public void setModel(Object model, String tag) {
    
  }

}
