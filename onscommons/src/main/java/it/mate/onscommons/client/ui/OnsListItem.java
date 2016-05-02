package it.mate.onscommons.client.ui;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.onscommons.client.event.HasTapHandler;
import it.mate.onscommons.client.event.TapHandler;
import it.mate.onscommons.client.onsen.OnsenUi;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;


public class OnsListItem extends HTMLPanel implements HasTapHandler, HasModel {
  
  private HasTapHandlerImpl hasTapHandlerImpl;
  
  private String value;
  
  private Object model;
  
  public OnsListItem() {
    this("");
  }

  public OnsListItem(String html) {
    super("ons-list-item", html);
    hasTapHandlerImpl = new HasTapHandlerImpl(this);
  }
  
  @Override
  public void setModel(Object model) {
    this.model = model;
  }

  @Override
  public Object getModel() {
    return model;
  }

  @Override
  public void add(Widget widget) {
    super.add(widget, getElement());
  }

  @Override
  public HandlerRegistration addTapHandler(TapHandler handler) {
    return hasTapHandlerImpl.addTapHandler(handler);
  }
  
  public void setModifier(final String modifier) {
    if (OnsenUi.isAddDirectWithPlainHtml()) {
      getElement().setAttribute("modifier", modifier);
    } else {
      OnsenUi.onAttachedElement(this, new Delegate<Element>() {
        public void execute(Element element) {
          element.setAttribute("modifier", modifier);
        }
      });
    }
  }
  
  public void setValue(String value) {
    this.value = value;
  }
  
  public String getValue() {
    return value;
  }
  
  public void addHtml(String html) {
    String innerHtml = getElement().getInnerHTML();
    getElement().setInnerHTML(innerHtml + html);
  }
  
  public void setVisible(final boolean visible) {
    if (OnsenUi.isAddDirectWithPlainHtml()) {
      setVisible(getElement(), visible);
    } else {
      OnsenUi.onAvailableElement(this, new Delegate<Element>() {
        public void execute(Element element) {
          setVisible(element, visible);
        }
      });
    }
  }
  
}
