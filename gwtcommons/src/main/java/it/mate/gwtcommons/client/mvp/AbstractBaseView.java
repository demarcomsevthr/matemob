package it.mate.gwtcommons.client.mvp;

import com.google.gwt.user.client.ui.Composite;

public abstract class AbstractBaseView <A extends BasePresenter> extends Composite implements BaseView<A> {

  private A presenter;

  public A getPresenter() {
    return presenter;
  }

  public void setPresenter(A presenter) {
    this.presenter = presenter;
  }
  
  public void setModel(Object model) {
    setModel(model, null);
  }
  
  public void onDetachView() {
    
  }
  
  public void onShowView() {
    
  }
  
}
