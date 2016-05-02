package it.mate.gwtcommons.client.ui;

import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.gwtcommons.client.mvp.BaseView;

public class NotImplementedView <P extends BasePresenter> extends AbstractBaseView<P> implements BaseView<P> {

  @Override
  public void setModel(Object model, String tag) {
    
  }

}
