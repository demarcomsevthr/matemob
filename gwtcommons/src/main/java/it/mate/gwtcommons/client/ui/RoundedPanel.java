package it.mate.gwtcommons.client.ui;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.SimplePanel;

public class RoundedPanel extends SimplePanel {
  
  private Element containerElement;
  
  public RoundedPanel() {
    this(null);
  }

  public RoundedPanel(Element elem) {
    super(Document.get().createDivElement());
    getElement().setClassName("gwt-RoundedPanel");
    DivElement div = createInnerDiv(getElement(), "lb");
    div = createInnerDiv(div, "rb");
    div = createInnerDiv(div, "bb");
    div = createInnerDiv(div, "blc");
    div = createInnerDiv(div, "brc");
    div = createInnerDiv(div, "tb");
    div = createInnerDiv(div, "tlc");
    div = createInnerDiv(div, "trc");
    this.containerElement = createInnerDiv(div, "content");
    if (elem != null) {
      DOM.appendChild((com.google.gwt.user.client.Element)this.containerElement, (com.google.gwt.user.client.Element)elem);
    }
  }
  
  private DivElement createInnerDiv(Element outer, String id) {
    DivElement div = Document.get().createDivElement();
    outer.appendChild(div);
//  div.setClassName(getStylePrimaryName()+"-"+id);
    div.setClassName(id);
    return div;
  }
  
  @Override
  protected com.google.gwt.user.client.Element getContainerElement() {
    return (com.google.gwt.user.client.Element)containerElement;
  }

  /*

  private Widget widget;
  
  @Override
  public void setWidget(Widget w) {
    if (w == widget) {
      return;
    }
    
    if (w != null) {
      w.removeFromParent();
    }
    
    if (widget != null) {
      remove(widget);
    }
    
    widget = w;
    
    if (w != null) {
      DOM.appendChild((com.google.gwt.user.client.Element)this.containerElement, widget.getElement());
    }
      
  }
  
  */

}
