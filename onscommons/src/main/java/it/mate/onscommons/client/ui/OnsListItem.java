package it.mate.onscommons.client.ui;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.onscommons.client.event.HasTapHandler;
import it.mate.onscommons.client.event.TapHandler;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.phgcommons.client.utils.PhgUtils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;


public class OnsListItem extends HTMLPanel implements HasTapHandler, HasModel {
  
  private HasTapHandlerImpl hasTapHandlerImpl;
  
  private String value;
  
  private Object model;
  
  private static boolean doLog = true;
  
  // costruito programmaticamente
  public OnsListItem() {
    this("", false);
  }

  // costruito negli ui.xml
  public OnsListItem(String html) {
    this(html, true);
  }
  
  protected OnsListItem(String html, boolean fromUiBinder) {
    super("ons-list-item", getInitHtml(html));
    if (OnsenUi.isVersion2()) {
      getElement().setInnerHTML(html);
    }
    hasTapHandlerImpl = new HasTapHandlerImpl(this);

    if (OnsenUi.isVersion2()) {
      getElement().addClassName("ons-fadein");
      if (fromUiBinder) {
        getElement().setAttribute("modifier", "nodivider");
      }
    }
    
//  if (doLog) PhgUtils.log("COSTRUTTORE " + html);
    
  }
  
  private static String getInitHtml(String html) {
    if (OnsenUi.isVersion2()) {
      return "";
    } else {
      return html;
    }
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
    
    if (OnsenUi.isVersion2()) {
      getElement().setAttribute("tappable", "");
    }
    
    return hasTapHandlerImpl.addTapHandler(handler);
  }
  
  public void setModifier(final String modifier) {
    OnsenUi.onAttachedElement(this, new Delegate<Element>() {
      public void execute(Element element) {
        element.setAttribute("modifier", modifier);
      }
    });
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
    OnsenUi.onAvailableElement(this, new Delegate<Element>() {
      public void execute(Element element) {
        setVisible(element, visible);
      }
    });
  }
  
}
