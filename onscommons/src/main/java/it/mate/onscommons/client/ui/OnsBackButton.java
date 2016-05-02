package it.mate.onscommons.client.ui;

import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.onscommons.client.event.HasTapHandler;
import it.mate.onscommons.client.event.TapEvent;
import it.mate.onscommons.client.event.TapHandler;
import it.mate.onscommons.client.mvp.OnsActivityManagerBase;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.utils.TransitionUtils;
import it.mate.phgcommons.client.utils.PhgUtils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class OnsBackButton extends Widget implements HasTapHandler {
  
  private HasTapHandlerImpl hasTapHandlerImpl;
  
  private boolean isSlidingMenuPresentAndIsHome = false;
  
  
  public OnsBackButton() {
    this(DOM.createElement("ons-toolbar-button"), true);
  }

  protected OnsBackButton(Element element, boolean doFadeIn) {
    
    if (OnsenUi.isNavigatorPresent() && OnsenUi.isSlidingMenuPresent()) {
      
      int actualPageCount = OnsenUi.getNavigator().getActualPageCount();
      String activePlace = OnsActivityManagerBase.IMPL.getActivePlace().getToken();
      
      PhgUtils.log("OnsBackButton CONSTRUCTOR actual navigator page count = " + actualPageCount + " - ACTIVE PLACE = " + OnsActivityManagerBase.IMPL.getActivePlace().getToken());
      
      if (actualPageCount == 0 || "home".equalsIgnoreCase(activePlace) ) {
        isSlidingMenuPresentAndIsHome = true;
      }
      
      
    }

    if (isSlidingMenuPresentAndIsHome) {
      Element icon = DOM.createElement("ons-icon");
      icon.setAttribute("icon", "fa-bars");
      element.appendChild(icon);
    } else {
      Element icon = DOM.createElement("ons-icon");
      icon.setAttribute("icon", "fa-chevron-left");
      element.appendChild(icon);
    }
    
    element.addClassName("ons-back-button");
    
    if (doFadeIn) {
      TransitionUtils.fadeIn(element, new TransitionUtils.Options().setDeferring(400).setDelay(400).setDuration(200));
    }
    
    setElement(element);
    
    addTapHandler(new TapHandler() {
      public void onTap(TapEvent event) {
        if (isSlidingMenuPresentAndIsHome) {
          OnsenUi.getSlidingMenu().toggleMenu();
        } else {
          try {
            goToPrevious();
          } catch (Exception ex) {
            PhgUtils.log(">>>>>>>>>>>>>>>>> EXCEPTION " + ex.getMessage());
            if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("page stack is empty")) {
              if (OnsenUi.isSlidingMenuPresent()) {
                OnsenUi.getSlidingMenu().toggleMenu();
              }
            }
          }
        }
      }
    });
    
  }
  
  public void setText(String text) {
    OnsButtonBase.addElementText(getElement(), text);
  }
  
  @SuppressWarnings("rawtypes")
  protected void goToPrevious() {
    Widget parent = getParent();
    while (parent != null) {
      if (parent instanceof AbstractBaseView) {
        AbstractBaseView view = (AbstractBaseView)parent;
        view.getPresenter().goToPrevious();
        break;
      }
      parent = parent.getParent();
    }
  }

  public HandlerRegistration addTapHandler(TapHandler handler) {
    if (hasTapHandlerImpl == null) {
      hasTapHandlerImpl = new HasTapHandlerImpl(this);
    }
    return hasTapHandlerImpl.addTapHandler(handler);
  }
  
}
