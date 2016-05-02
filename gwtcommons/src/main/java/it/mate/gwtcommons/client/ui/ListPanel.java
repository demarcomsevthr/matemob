package it.mate.gwtcommons.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class ListPanel extends ComplexPanel {
  
  public ListPanel() {
    Element div = DOM.createDiv();
    setElement(div);
  }

  public void add(Widget widget) {
    super.add(widget, getElement());
  }
  
}
