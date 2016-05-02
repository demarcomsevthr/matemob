package it.mate.gwtcommons.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class ContainerPanel extends ComplexPanel {
  
  private Element containerElement;

  public ContainerPanel() {
    containerElement = DOM.createDiv();
    setElement(containerElement);
  }

  public void add(Widget widget, String stylename) {
    Element childElem = DOM.createDiv();
    if (stylename != null && !"".equals(stylename)) {
      childElem.setClassName(stylename);
    }
    containerElement.appendChild(childElem);
    super.add(widget, childElem);
  }
  
  @Override
  public void add(Widget widget) {
    this.add(widget, "");
  }
  
}
