package it.mate.onscommons.client.ui;

import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.onsen.dom.Navigator;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class OnsNavigator extends ComplexPanel implements HasWidgets {

  public OnsNavigator() {
    this(DOM.createElement("ons-navigator"));
  }
  
  protected OnsNavigator(Element elem) {
    elem.setAttribute("var", "app.navigator");
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
  
  public void compileElement() {
    OnsenUi.compileElement(getElement());
  }

  public final native Navigator getControllerSingleton() /*-{
    return $wnd.ons.navigator;
  }-*/;

  public final native Navigator getController() /*-{
    return $wnd.app.navigator;
  }-*/;

}
