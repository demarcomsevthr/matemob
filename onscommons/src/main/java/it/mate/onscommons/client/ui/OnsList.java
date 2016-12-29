package it.mate.onscommons.client.ui;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.utils.TransitionUtils;
import it.mate.phgcommons.client.utils.PhgUtils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class OnsList extends HTMLPanel {

  private final static String TAG_NAME = "ons-list";
  
  private Element listElement;
  
  private static boolean doLog = true;
  
  private int addRequestsNumber = 0;
  
  // costruito programmaticamente
  public OnsList() {
    this(TAG_NAME, "", false);
  }
  
  // costruito negli ui.xml
  public OnsList(String html) {
    this(TAG_NAME, html, true);
  }
  
  protected OnsList(String tag, String html, boolean fromUiBinder) {
    super(tag, html);
    if (doLog) PhgUtils.log("COSTRUTTORE " + tag + " " + html);
    getElement().addClassName(TAG_NAME);
    OnsenUi.ensureId(getElement());
    if (OnsenUi.isVersion2()) {
      if (fromUiBinder) {
        getElement().setAttribute("modifier", "noborder");
      }
    }
  }

  @Override
  public void add(final Widget widget) {
    
    if (doLog) PhgUtils.log("ADD WIDGET " + widget);
    
    super.add(widget, getElement());
    String id = getElement().getId();
    GwtUtils.onAvailable(id, new Delegate<Element>() {
      public void execute(Element listElem) {
        listElement = listElem;
        Element itemElem = widget.getElement();
        if (doLog) PhgUtils.log("Adding to OnsList element " + itemElem);
        listElem.appendChild(itemElem);
        OnsenUi.compileElement(listElem);
        OnsenUi.compileElement(itemElem);
      }
    });
    
  }
  
  
  public void add(final Widget widget, final Delegate<Element> delegate) {
    if (widget == null) {
      return;
    }
    addRequestsNumber ++;
    super.add(widget, getElement());
    OnsenUi.onAvailableElement(this, new Delegate<Element>() {
      public void execute(Element listElement) {
        OnsList.this.listElement = listElement;
        Element itemElem = widget.getElement();
        if (doLog) PhgUtils.log("Adding to OnsList element " + itemElem);
        listElement.appendChild(itemElem);
        OnsenUi.compileElement(listElement);
        OnsenUi.compileElement(itemElem);
        GwtUtils.deferredExecution(100, new Delegate<Void>() {
          public void execute(Void element) {
            addRequestsNumber --;
            if (delegate != null && addRequestsNumber <= 0) {
              delegate.execute(OnsList.this.listElement);
            }
          }
        });
      }
    });
  }
  
  public void insert(final Widget widget, final int beforeIndex) {
    String id = getElement().getId();
    GwtUtils.onAvailable(id, new Delegate<Element>() {
      public void execute(Element listElem) {
        listElement = listElem;
        Element itemElem = widget.getElement();
        Node beforeChild = listElem.getChild(beforeIndex);
        listElem.insertBefore(itemElem, beforeChild);
        OnsenUi.compileElement(listElem);
        OnsenUi.compileElement(itemElem);
      }
    });
  }
  
  public int getItemCount() {
    return listElement.getChildCount();
  }
  
  public void setModifier(final String modifier) {
    OnsenUi.onAttachedElement(this, new Delegate<Element>() {
      public void execute(Element element) {
        element.setAttribute("modifier", modifier);
      }
    });
  }

  public void compile() {
    GwtUtils.onAvailable(getElement().getId(), new Delegate<Element>() {
      public void execute(Element listElement) {
        OnsenUi.compileElement(listElement);
      }
    });
  }
  
  public void setAnimation(String animation) {
    TransitionUtils.fadeIn(getElement(), TransitionUtils.parseAttributeValue(animation).setDelay(0));
  }
  
  public void clear() {
    clear(null);
  }
  
  public void clear(final Delegate<Element> delegate) {
    OnsenUi.onAvailableElement(this, new Delegate<Element>() {
      public void execute(Element onsListElement) {
        onsListElement.removeAllChildren();
        if (delegate != null) {
          delegate.execute(onsListElement);
        }
      }
    });
  }
  
  public static void setDoLog(boolean doLog) {
    OnsList.doLog = doLog;
  }
  
}
