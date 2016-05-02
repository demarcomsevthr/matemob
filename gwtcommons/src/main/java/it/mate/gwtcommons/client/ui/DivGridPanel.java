package it.mate.gwtcommons.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class DivGridPanel extends ComplexPanel {
  
  protected Element containerElement;

  public DivGridPanel() {
    containerElement = createContainerElement();
    setElement(containerElement);
  }
  
  protected Element createContainerElement() {
    return DOM.createDiv();
  }

  @Override
  public void add(Widget widget) {
    Element cellDivElem = DOM.createDiv();
    containerElement.appendChild(cellDivElem);
    super.add(widget, cellDivElem);
  }

}
