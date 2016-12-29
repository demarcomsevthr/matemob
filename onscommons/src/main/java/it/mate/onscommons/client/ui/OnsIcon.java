package it.mate.onscommons.client.ui;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.onscommons.client.onsen.OnsenUi;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class OnsIcon extends Widget {

  public OnsIcon() {
    this("ons-icon");
  }

  protected OnsIcon(String tagName) {
    this(DOM.createElement(tagName), tagName);
  }
  
  protected OnsIcon(Element element, String className) {
    if (className != null) {
      element.addClassName(className);
    }
    setElement(element);
  }
  
  public void setIcon(String icon) {
    getElement().setAttribute("icon", icon);
  }
  
  public void setSize(String size) {
    getElement().setAttribute("size", size);
  }
  
  public void setRotate(String rotate) {
    getElement().setAttribute("rotate", rotate);
  }
  
  public void setFlip(String flip) {
    getElement().setAttribute("flip", flip);
  }
  
  public void setFixedWidth(String width) {
    getElement().setAttribute("fixed-width", width);
  }
  
  public void setSpin(String spin) {
    getElement().setAttribute("spin", spin);
  }
  
  public void setOpacity(final String opacity) {
    OnsenUi.onAttachedElement(this, new Delegate<Element>() {
      public void execute(Element element) {
        element.getStyle().setOpacity(Double.parseDouble(opacity));
      }
    });
  }
  
}
