package it.mate.phgcommons.client.ui;

import it.mate.phgcommons.client.utils.TouchUtils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndHandler;
import com.googlecode.mgwt.ui.client.widget.touch.TouchWidget;

public class TouchAnchor extends TouchWidget implements HasClickHandlers, HasTag {
  
  private Element wrapper;
  
  private Anchor anchor;
  
  private String tag;
  
  public TouchAnchor() {
    this("");
  }
  
  public TouchAnchor(String html) {
    this(SafeHtmlUtils.fromTrustedString(html));
  }
  
  public TouchAnchor(SafeHtml html) {
    this(new Anchor(html));
  }
  
  public TouchAnchor(SafeHtml html, TapHandler handler) {
    this(html);
    addTapHandler(handler);
  }
  
  protected TouchAnchor(Anchor anchor) {
    this.anchor = anchor;
    wrapper = DOM.createDiv();
    wrapper.appendChild(anchor.getElement());
    setElement(wrapper);
    addStyleName("phg-TouchAnchor");
    addTouchEndHandler(new TouchEndHandler() {
      public void onTouchEnd(TouchEndEvent event) {
        TouchUtils.applyFocusPatch();
      }
    });
    TouchUtils.addTappedStyleHandlers(this);
  }
  
  public void setHtml(SafeHtml html) {
    Element elem = anchor.getElement();
    elem.setInnerSafeHtml(html);
  }
  
  public void setText(String text) {
    setHtml(SafeHtmlUtils.fromTrustedString(text));
  }

  public HandlerRegistration addClickHandler(ClickHandler handler) {
    return addTapHandler(new TapHandler() {
      public void onTap(TapEvent event) {
        TouchUtils.fireClickEventFromTapEvent(TouchAnchor.this, event);
      }
    });
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
