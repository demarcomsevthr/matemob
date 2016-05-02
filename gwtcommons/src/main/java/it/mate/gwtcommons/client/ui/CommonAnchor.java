package it.mate.gwtcommons.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;

public class CommonAnchor extends Anchor {

  public CommonAnchor() {
    super();
    addStyleDependentName("commonAnchor");
  }
  
  public CommonAnchor(String html) {
    this();
    setHTML(html);
  }
  
  public void setBackgroundModuleImage(String url) {
    setBackgroundImage(GWT.getModuleBaseURL() + url);
  }
  
  public void setBackgroundImage(String url) {
    DOM.setStyleAttribute(getElement(), "background", "url('"+ url + "') no-repeat scroll 0 50% transparent");
  }
  
}
