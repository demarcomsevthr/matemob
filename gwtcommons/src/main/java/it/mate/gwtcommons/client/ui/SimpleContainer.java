package it.mate.gwtcommons.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class SimpleContainer extends ComplexPanel implements HasWidgets {
  
  public SimpleContainer() {
    Element div = DOM.createDiv();
    setElement(div);
    addStyleName("gwt-SimpleContainer");
  }

  public void add(Widget widget) {
    super.add(widget, getElement());
  }
  
}
