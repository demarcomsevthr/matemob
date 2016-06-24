package it.mate.onscommons.client.mvp;

import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.mvp.BaseView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public abstract class OnsAbstractActivity extends AbstractActivity {

  @Override
  public void start(AcceptsOneWidget panel, com.google.gwt.event.shared.EventBus eventBus) {
    start((AcceptsOneWidget)OnsActivityManagerWithNavigator.getActivePanel(), (com.google.web.bindery.event.shared.EventBus) eventBus);
  }

  public void start(AcceptsOneWidget panel, com.google.web.bindery.event.shared.EventBus eventBus) {

  }
  
  @SuppressWarnings("rawtypes")
  public abstract BaseView getView();
  
  @Override
  public void onStop() {
    super.onStop();
    detachView();
  }
  
  @Override
  public void onCancel() {
    super.onCancel();
    detachView();
  }
  
  private void detachView() {
    if (getView() instanceof AbstractBaseView) {
      AbstractBaseView view = (AbstractBaseView)getView();
      view.onDetachView();
    }
  }
  
}
