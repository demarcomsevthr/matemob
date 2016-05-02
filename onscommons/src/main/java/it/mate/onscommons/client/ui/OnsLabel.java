package it.mate.onscommons.client.ui;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.onscommons.client.event.HasTapHandler;
import it.mate.onscommons.client.event.TapHandler;
import it.mate.onscommons.client.onsen.OnsenUi;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Label;

public class OnsLabel extends Label implements HasTapHandler {

  private HasTapHandlerImpl hasTapHandlerImpl;
  
  public OnsLabel() {
    this("");
  }

  public OnsLabel(String text) {
    super(text);
    OnsenUi.ensureId(getElement());
    addStyleName("ons-label");
    hasTapHandlerImpl = new HasTapHandlerImpl(this);
  }
  
  @Override
  public void setText(final String text) {
    if (OnsenUi.isAddDirectWithPlainHtml()) {
      getElement().setInnerText(text);
    } else {
      OnsenUi.onAvailableElement(this, new Delegate<Element>() {
        public void execute(Element element) {
          element.setInnerText(text);
        }
      });
    }
  }
  
  public void setHtml(final String html) {
    if (OnsenUi.isAddDirectWithPlainHtml()) {
      getElement().setInnerHTML(html);
    } else {
      OnsenUi.onAvailableElement(this, new Delegate<Element>() {
        public void execute(Element element) {
          element.setInnerHTML(html);
        }
      });
    }
  }

  public HandlerRegistration addTapHandler(final TapHandler handler) {
    return hasTapHandlerImpl.addTapHandler(handler);
  }
  
}
