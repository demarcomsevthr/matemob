package it.mate.onscommons.client.ui;

import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.onsen.dom.SlidingMenu;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class OnsSlidingMenu extends ComplexPanel implements HasWidgets {
  
  public OnsSlidingMenu() {
    this(DOM.createElement("ons-sliding-menu"));
    getElement().setAttribute("var", "app.menu");
    getElement().setAttribute("max-slide-distance", "85%");
//  getElement().setAttribute("type", "overlay");
  }
  
  protected OnsSlidingMenu(Element elem) {
    setElement(elem);
  }
  
  @Override
  public void add(Widget widget) {
    super.add(widget, getElement());
  }

  public void attachToBody() {
    RootPanel.get().add(this);
    OnsenUi.compileElement(getElement());
  }

  public final native SlidingMenu getController() /*-{
    return $wnd.app.menu;
  }-*/;
  
}
