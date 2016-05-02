package it.mate.gwtcommons.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class BulletPanel extends ComplexPanel {
  
  private Element ulElement;
  
  public BulletPanel() {
    ulElement = DOM.createElement("ul").cast();
    setElement(ulElement);
  }

  @Override
  public void add(Widget widget) {
    Element liElem = DOM.createElement("li").cast();
    ulElement.appendChild(liElem);
    addOnItem(widget, liElem);
  }
  
  public void addOnItem(Widget widget, Element liElem) {
    super.add(widget, liElem);
  }
  
}
