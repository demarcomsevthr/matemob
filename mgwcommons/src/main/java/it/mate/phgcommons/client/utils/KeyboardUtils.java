package it.mate.phgcommons.client.utils;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.gwtcommons.client.utils.JQuery;
import it.mate.phgcommons.client.ui.TouchButton;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndHandler;



public class KeyboardUtils {
  
  private static final String FOCUSED_ELEMENT_KEY = "phgFocusedElement";
  
  private static TouchButton doneButton;

  private static Element focusedElement = null;
  
  private static Timer focusTimer = null;
  
  private static int stopFocusRequested = -1;
  
  private static String doneButtonText = "Finito";
  
  private static List<Delegate<Element>> focusDelegates = new ArrayList<Delegate<Element>>();
  
  private static Delegate<Element> doneBtnFocusDelegate = null;
  
  private static int lastTop, lastLeft;
  
  
  private static boolean isDoneButtonAddonApplicable() {
    if ("en".equals(PhgUtils.getAppLocalLanguage())) {
      return false;
    }
    if (OsDetectionUtils.isIOs() && PhgUtils.getDeviceVersion().startsWith("6")) {
      return true;
    }
    if (OsDetectionUtils.isDesktop()) {
      return true;
    }
    return false;
  }
  
  
  public static void enableDoneButtonAddon() {
    
    if (isDoneButtonAddonApplicable()) {
      // continue
    } else {
      return;
    }
    
    PhgUtils.log("enabling keyboard done button surrogate");
    
    if (doneBtnFocusDelegate == null) {
      
      doneBtnFocusDelegate = new Delegate<Element>() {
        public void execute(Element focusedElement) {
          if (stopFocusRequested > 0) {
            // cicli di attesa
            PhgUtils.log("stop focus requested");
            stopFocusRequested --;
            hideDoneButton();
            return;
          }
          if (isValidInputElement(focusedElement)) {
            KeyboardUtils.focusedElement = focusedElement;
            String value = focusedElement.getAttribute(FOCUSED_ELEMENT_KEY);
            if (value == null || "".equals(value) || doneButton == null) {
              PhgUtils.log("focusedElement = " + focusedElement);
              focusedElement.setAttribute(FOCUSED_ELEMENT_KEY, "true");
              showDoneButton();
            }
            if (doneButton != null) {
              int top = focusedElement.getParentElement().getAbsoluteTop();
              if (OsDetectionUtils.isIOs()) {
                top -= 20;
              } else {
                top -= 3;
              }
              int left = focusedElement.getParentElement().getAbsoluteRight() - doneButton.getOffsetWidth();
              if (top > 0 && left > 0) {
                if (lastTop != top || lastLeft != left) {
                  lastTop = top;
                  lastLeft = left;
                }
                doneButton.getElement().getStyle().setTop(top, Unit.PX);
                doneButton.getElement().getStyle().setLeft(left, Unit.PX);
              }
            }
          } else {
            hideDoneButton();
          }
        }
      };
      
      addFocusDelegate(doneBtnFocusDelegate);
      
    }

    GwtUtils.deferredExecution(new Delegate<Void>() {
      public void execute(Void element) {
        JQuery.select(":input").blur(new Delegate<Element>() {
          public void execute(Element bluredElement) {
            PhgUtils.log("bluredElement = " + bluredElement);
            bluredElement.setAttribute(FOCUSED_ELEMENT_KEY, "");
            hideDoneButton();
          }
        });
      }
    });
    
  }
  
  public static boolean isValidInputElement(Element element) {
    boolean isValid = false;
    if (element == null) {
      return isValid;
    }
    isValid = element.getNodeName().equalsIgnoreCase("input");
    if (isValid) {
      isValid = isValid && !DOM.getElementPropertyBoolean((com.google.gwt.user.client.Element)element, "readOnly");
    }
    return isValid;
  }
  
  public static void disableDoneButtonAddon() {
    if (doneBtnFocusDelegate != null) {
      removeFocusDelegate(doneBtnFocusDelegate);
      doneBtnFocusDelegate = null;
    }
  }
  
  public static void addFocusDelegate(Delegate<Element> delegate) {
    if (OsDetectionUtils.isIOs() || OsDetectionUtils.isDesktop()) {
      // continue
    } else {
      return;
    }
    focusDelegates.add(delegate);
    if (focusTimer == null) {
      focusTimer = GwtUtils.createTimer(200, new Delegate<Void>() {
        public void execute(Void element) {
          Element focusedElement = getActiveElementImpl();
          
          for (Delegate<Element> delegate : focusDelegates) {
            delegate.execute(focusedElement);
          }
          
        }
      });
    }
  }
  
  public static void removeFocusDelegate(Delegate<Element> delegate) {
    focusDelegates.remove(delegate);
    if (focusDelegates.size() == 0) {
      focusTimer.cancel();
    }
  }
  
  private static void showDoneButton() {
    hideDoneButton();
    if (stopFocusRequested > 0) {
      return;
    }
    doneButton = new TouchButton();
    doneButton.setStyleName("mgwt-Button phg-DoneButton");
    doneButton.setText(doneButtonText);
    doneButton.addTouchEndHandler(new TouchEndHandler() {
      public void onTouchEnd(TouchEndEvent event) {
        stopFocusRequested = 5;
        hideDoneButton();
        GwtUtils.deferredExecution(new Delegate<Void>() {
          public void execute(Void element) {
            if (focusedElement != null) {
              JQuery.withElement(focusedElement).blur();
            }
          }
        });
        GwtUtils.deferredExecution(200, new Delegate<Void>() {
          public void execute(Void element) {
            TouchUtils.applyFocusPatch();
          }
        });
      }
    });
    RootPanel.get().add(doneButton);
  }
  
  private static void hideDoneButton() {
    if (doneButton != null) {
      doneButton.setVisible(false);
      doneButton.removeFromParent();
      doneButton = null;
    }
  }
  
  public static void setDoneButtonText(String doneButtonText) {
    KeyboardUtils.doneButtonText = doneButtonText;
  }
  
  private static native Element getActiveElementImpl() /*-{
    return $doc.activeElement;
  }-*/;


}
