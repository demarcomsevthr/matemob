package it.mate.gwtcommons.client.mvp;

import com.google.gwt.user.client.ui.IsWidget;

public interface BaseView <A extends BasePresenter> extends IsWidget {
  
  public void setPresenter(A activity);
  
  public void setModel(Object model, String tag);

  public void setModel(Object model);

  // 23/11/2012 per le AdminTabPanel
  public void setWidth(String width);
  
  public void setHeight(String height);
  
}
