package it.mate.phgcommons.client.ui;

import it.mate.phgcommons.client.ui.ph.HasModel;
import it.mate.phgcommons.client.utils.TouchUtils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HTML;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndHandler;
import com.googlecode.mgwt.ui.client.widget.touch.TouchWidget;

public class TouchHTML extends TouchWidget implements HasClickHandlers, HasModel, HasTag {
  
  private String tooltip;
  
  private Object model;
  
  private String tag;
  
  public TouchHTML() {
    this("");
  }
  
  public TouchHTML(String html) {
    this(SafeHtmlUtils.fromTrustedString(html));
  }
  
  public TouchHTML(SafeHtml html) {
    this(new HTML(html));
  }
  
  public TouchHTML(String html, TouchEndHandler handler) {
    this(SafeHtmlUtils.fromTrustedString(html));
    addTouchEndHandler(handler);
  }
  
  protected TouchHTML(HTML html) {
    setElement(html.getElement());
    addStyleName("phg-TouchHTML");
    addTouchEndHandler(new TouchEndHandler() {
      public void onTouchEnd(TouchEndEvent event) {
        TouchUtils.applyFocusPatch();
      }
    });
    TouchUtils.addTappedStyleHandlers(this);
  }
  
  public void setTooltip(String tooltip) {
    this.tooltip = "<span class='phg-tooltip'>"+tooltip+"</span>";
    setHtml(this.tooltip);
  }
  
  public void setHtml(String html) {
    setHtml(SafeHtmlUtils.fromTrustedString(html));
  }
  
  public void setHTML(SafeHtml html) {
    setHtml(html);
  }
  
  public void setText(String text) {
    setHtml(SafeHtmlUtils.fromTrustedString(text));
  }

  public void setHtml(SafeHtml html) {
    Element elem = getElement();
    elem.setInnerSafeHtml(html);
  }
  
  public HandlerRegistration addClickHandler(ClickHandler handler) {
    return addTapHandler(new TapHandler() {
      public void onTap(TapEvent event) {
        TouchUtils.fireClickEventFromTapEvent(TouchHTML.this, event);
      }
    });
  }

  public Object getModel() {
    return model;
  }

  public HasModel setModel(Object model) {
    this.model = model;
    return this;
  }

  @Override
  public String getTag() {
    return tag;
  }

  @Override
  public void setTag(String tag) {
    this.tag = tag;
  }

}
