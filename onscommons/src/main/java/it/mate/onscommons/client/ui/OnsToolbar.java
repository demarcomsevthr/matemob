package it.mate.onscommons.client.ui;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.onscommons.client.onsen.OnsenUi;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class OnsToolbar extends HTMLPanel {

  private final static String TAG_NAME = "ons-toolbar";
  
  private static boolean waitingButtonVisible = false;
  
  private static Element toolbarElement = null;
  
  public OnsToolbar() {
    this(TAG_NAME, "");
  }
  
  public OnsToolbar(String html) {
    this(TAG_NAME, html);
  }
  
  protected OnsToolbar(String tag, String html) {
    super(tag, html);
    getElement().addClassName("ons-toolbar");
    OnsenUi.ensureId(getElement());
    createWaitingIcon();
    toolbarElement = getElement();
  }

  @Override
  public void add(Widget widget) {
    super.add(widget, getElement());
  }
  
  private static Element toolbarWaitingDiv = null;
  
  private void createWaitingIcon() {
    
    OnsenUi.onAvailableElement(this, new Delegate<Element>() {
      public void execute(Element toolbarElement) {
        if (toolbarWaitingDiv == null) {
          Element waitingDiv = DOM.createDiv();
          waitingDiv.setClassName("navigation-bar ons-waiting-div");

          Element waitingIco = DOM.createElement("img");
          waitingIco.addClassName("ons-waiting-div-icon");
          waitingIco.setAttribute("src", GWT.getModuleBaseURL() + "images/preloader1.gif");
          
          waitingDiv.appendChild(waitingIco);
          
          toolbarWaitingDiv = waitingDiv;
          
          setWaitingButtonVisible(false);
          
          RootPanel.getBodyElement().appendChild(waitingDiv);
        }
      }
    });

    /*
    OnsenUi.onAttachedElement(this, new Delegate<Element>() {
      public void execute(Element toolbarElement) {
        Element waitingDiv = DOM.createDiv();
        waitingDiv.setClassName("right");
        Element waitingBtn = DOM.createElement("ons-toolbar-button");
        waitingBtn.setClassName("ons-toolbar-button-waiting");
        waitingBtn.setAttribute("disabled", "");
        waitingBtn.setAttribute("var", "_onsToolbarButtonWaiting");
        waitingBtn.getStyle().setOpacity(0);
        Element waitingIco = DOM.createElement("img");
        waitingIco.addClassName("ons-toolbar-button-waiting-icon");
        waitingIco.setAttribute("src", GWT.getModuleBaseURL() + "images/preloader1.gif");
        waitingBtn.appendChild(waitingIco);
        waitingDiv.appendChild(waitingBtn);
        toolbarElement.appendChild(waitingDiv);
      }
    });
    */
    
  }
  
  public static void setWaitingButtonVisible(boolean visible) {
    waitingButtonVisible = visible;
    if (toolbarWaitingDiv != null) {
      if (visible) {
        toolbarWaitingDiv.getStyle().setOpacity(1);
        toolbarWaitingDiv.getStyle().setZIndex(999999);
        toolbarWaitingDiv.getStyle().setHeight(40, Unit.PX);
      } else {
        toolbarWaitingDiv.getStyle().setOpacity(0);
        toolbarWaitingDiv.getStyle().setZIndex(-1);
        toolbarWaitingDiv.getStyle().setHeight(0, Unit.PX);
      }
    } else {
      setWaitingButtonVisibleImpl(visible ? "1" : "0");
    }
  }
  
  public static boolean isWaitingButtonVisible() {
    return waitingButtonVisible;
  }
  
  private static native void setWaitingButtonVisibleImpl (String opacity) /*-{
    if (typeof $wnd._onsToolbarButtonWaiting != 'undefined') {
      $wnd._onsToolbarButtonWaiting._element[0].style.opacity = opacity;
    }
  }-*/;
  
  public static Element getGlobalToolbarElement() {
    return toolbarElement;
  }

}
