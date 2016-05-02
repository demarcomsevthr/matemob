package it.mate.gwtcommons.client.ui;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class Span extends Widget {

  public Span() {
    setElement(DOM.createSpan());
  }
  
  public Span(String text) {
    this();
    setText(text);
  }
  
  public void setText(String text) {
    getSpanElement().setInnerText(text);
  }
  
  public void setHTML(SafeHtml html) {
    getSpanElement().setInnerSafeHtml(html);
  }
  
  public void setHTML(String html) {
    getSpanElement().setInnerHTML(html);
  }
  
  protected SpanElement getSpanElement() {
    return getElement().cast();
  }
  
}
