package it.mate.onscommons.client.ui;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.onscommons.client.event.HasTapHandler;
import it.mate.onscommons.client.event.TapHandler;
import it.mate.onscommons.client.onsen.OnsenUi;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTML;

public class OnsHTML extends HTML implements HasTapHandler {

  private HasTapHandlerImpl hasTapHandlerImpl;
  
  public OnsHTML() {
    super();
  }

  public OnsHTML(String html) {
    super(html);
    OnsenUi.ensureId(getElement());
    addStyleName("ons-html");
    hasTapHandlerImpl = new HasTapHandlerImpl(this);
  }
  
  @Override
  public void setHTML(final String html) {
    OnsenUi.onAvailableElement(this, new Delegate<Element>() {
      public void execute(Element element) {
        element.setInnerHTML(html);
      }
    });
  }
  
  public HandlerRegistration addTapHandler(final TapHandler handler) {
    return hasTapHandlerImpl.addTapHandler(handler);
  }
  
}
