package it.mate.onscommons.client.ui;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.event.HasTapHandler;
import it.mate.onscommons.client.event.TapHandler;
import it.mate.onscommons.client.onsen.OnsenUi;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public abstract class OnsButtonBase extends Widget implements HasTapHandler {
  
  private HasTapHandlerImpl hasTapHandlerImpl;
  
  private String text;
  
  protected OnsButtonBase(String tagName) {
    this(DOM.createElement(tagName), tagName);
  }
  
  protected OnsButtonBase(Element element, String className) {
    if (className != null && className.trim().length() > 0) {
      element.addClassName(className);
    }
    setElement(element);
    OnsenUi.ensureId(element);
    hasTapHandlerImpl = new HasTapHandlerImpl(this);
  }
  
  public void setText(String text) {
    addElementText(getElement(), text);
    this.text = text;
  }
  
  public void setTextDirect(String text) {
    getElement().setInnerHTML(text);
    this.text = text;
  }
  
  protected static void addElementText(Element widgetElement, String text) {
    if (OnsenUi.isAddDirectWithPlainHtml()) {
      OnsenUi.appendInnerHtml(widgetElement, text);
    } else {
      Element attachedElement = GwtUtils.getElementById(widgetElement.getId());
      if (attachedElement == null) {
        String innerHtml = widgetElement.getInnerHTML();
        innerHtml = innerHtml + text;
        widgetElement.setInnerHTML(innerHtml);
      } else {
        attachedElement.setInnerHTML(text);
      }
    }
  }
  
  public void setTextWhenAvailable(final String text) {
    if (OnsenUi.isAddDirectWithPlainHtml()) {
      getElement().setInnerText(text);
    } else {
      OnsenUi.onAvailableElement(getElement(), new Delegate<Element>() {
        public void execute(Element element) {
          element.setInnerText(text);
        }
      });
    }
  }
  
  public String getText() {
    return text;
  }

  public HandlerRegistration addTapHandler(final TapHandler handler) {
    return hasTapHandlerImpl.addTapHandler(handler);
  }
  
  public void setDisabled(boolean disabled) {
    if (OnsenUi.isAddDirectWithPlainHtml()) {
      getElement().setAttribute("disabled", "");
    } else {
      OnsenUi.onAttachedElement(this, new Delegate<Element>() {
        public void execute(Element element) {
          element.setAttribute("disabled", "");
        }
      });
    }
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
  
  public void setExcludeFromPageRefresh(String excludeFromPageRefresh) {
    getElement().setAttribute(OnsenUi.EXCLUDE_FROM_PAGE_REFRESH_ATTR, "true");
  }
  
}
