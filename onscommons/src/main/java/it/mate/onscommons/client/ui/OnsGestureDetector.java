package it.mate.onscommons.client.ui;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;


public class OnsGestureDetector extends HTMLPanel {
  
  public OnsGestureDetector() {
    this("");
  }

  public OnsGestureDetector(String html) {
    super("ons-gesture-detector", html);
  }

  @Override
  public void add(Widget widget) {
    super.add(widget, getElement());
  }

}
