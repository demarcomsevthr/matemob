package it.mate.phgcommons.client.utils;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.gwtcommons.client.utils.JQuery;
import it.mate.gwtcommons.client.utils.ObjectWrapper;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.touch.HasTouchHandlers;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartHandler;

public class TouchUtils {
  
  private static String tappedStyle = "phg-TappedStyle";
  
  public static void fireClickEventFromTouchEvent(HasClickHandlers target, TouchEvent<?> touchEvent) {
    boolean touchPresent = touchEvent.getTouches() != null && touchEvent.getTouches().length() > 0;
    int pageX = touchPresent ? touchEvent.getTouches().get(0).getPageX() : ((Widget)touchEvent.getSource()).getAbsoluteLeft();
    int pageY = touchPresent ? touchEvent.getTouches().get(0).getPageY() : ((Widget)touchEvent.getSource()).getAbsoluteTop();
    NativeEvent clickEvent = Document.get().createClickEvent(0, pageX, pageY, pageX, pageY, false, false, false, false);
    DomEvent.fireNativeEvent(clickEvent, target);
  }

  public static void fireClickEventFromTapEvent(HasClickHandlers target, TapEvent tapEvent) {
    int pageX = tapEvent.getStartX();
    int pageY = tapEvent.getStartY();
    NativeEvent clickEvent = Document.get().createClickEvent(0, pageX, pageY, pageX, pageY, false, false, false, false);
    DomEvent.fireNativeEvent(clickEvent, target);
  }
  

  
  /**
   * 11/12/2013
   * 
   * Ho constatato che ogni tanto "perde" degli eventi, ovvero sembra che gli eventi vengono processati 
   * pero non scatta il rendering grafico dell'elemento.
   * Constato anche che "spostando" il focus su altri elementi nella pagina (tappandoli), 
   * viene fatto il rendering "accodato" (lo noto soprattutto nel simulatore dove i tempi sono piu "dilatati").
   * Detto cio provo a simulare programmaticamente uno spostamento di focus su un elemento invisibile della pagina.
   * Lo attivo solo per Android (su Ios non ho riscontro di questa issue).
   * 
   */

  /*
  public static void applyFocusPatch() {
    executePatch20131211(0);
  }
  */
  
  public static void applyFocusPatch() {
    GwtUtils.deferredExecution(50, new Delegate<Void>() {
      public void execute(Void element) {
        executePatch20131211(0);
      }
    });
  }
  
  private static FocusPanel focusPatch1$focusWidget;
  private static void executePatch20131211(int delay) {
    if (focusPatch1$focusWidget != null) {
      focusPatch1$focusWidget.setVisible(false);
    }
    focusPatch1$focusWidget = new FocusPanel();
    focusPatch1$focusWidget.addStyleName("phg-InvisibleTouch");
    RootPanel.get().add(focusPatch1$focusWidget);
    GwtUtils.deferredExecution(delay, new Delegate<Void>() {
      public void execute(Void element) {
        focusPatch1$focusWidget.setFocus(true);
        NativeEvent nativeEvent = null;
        if (OsDetectionUtils.isDesktop()) {
          nativeEvent = createClickEvent();
        } else {
          nativeEvent = createTouchEvent();
        }
        DomEvent.fireNativeEvent(nativeEvent, focusPatch1$focusWidget);
      }
    });
  }
  
  /*
   * ORIGINAL VERSION [TESTED 27/01/2014]
  private static FocusPanel focusPatch1$focusWidget;
  private static void executePatch20131211(int delay) {
    if (!OsDetectionUtils.isAndroid())
      return;
    if (focusPatch1$focusWidget == null) {
      focusPatch1$focusWidget = new FocusPanel();
      focusPatch1$focusWidget.setWidth("1px");
      focusPatch1$focusWidget.setHeight("1px");
      RootPanel.get().add(focusPatch1$focusWidget);
    }
    GwtUtils.deferredExecution(delay, new Delegate<Void>() {
      public void execute(Void element) {
        focusPatch1$focusWidget.setFocus(true);
        NativeEvent clickEvent = Document.get().createClickEvent(0, 0, 0, 0, 0, false, false, false, false);
        DomEvent.fireNativeEvent(clickEvent, focusPatch1$focusWidget);
      }
    });
  }
   */
  
  /************************
   * See com.google.gwt.dom.client.DOMImplStandard.createMouseEvent()
   * e http://stackoverflow.com/questions/5713393/creating-and-firing-touch-events-on-a-touch-enabled-browser
   */
  private static native NativeEvent createTouchEvent() /*-{
    var evt = $doc.createEvent('UIEvent');
    evt.initUIEvent('touchstart', true, true);  
    return evt;
  }-*/;
  
  private static NativeEvent createClickEvent() {
    return Document.get().createClickEvent(0, 0, 0, 0, 0, false, false, false, false);
  }
  
  /**
   * see http://stackoverflow.com/questions/8335834/how-can-i-hide-the-android-keyboard-using-javascript
   */
  public static void applyKeyboardPatch() {

    // 07/04/2014: adattata anche per iOS
    if (OsDetectionUtils.isAndroid()) {
      applyKeyboardPatchOnInputables(100);
    } else {
      applyKeyboardPatchOnFocusables(100);
    }
    
  }
  
  public static void applyKeyboardPatchOnInputables(int delay) {
    applyQuickFixFocusPatchImpl(JQuery.select("input"), delay);
    applyQuickFixFocusPatchImpl(JQuery.select("textarea"), delay);
  }
  
  public static void applyKeyboardPatchOnFocusables(int delay) {
    applyQuickFixFocusPatchImpl(JQuery.select(":focus"), delay);
  }
  
  private static native void applyQuickFixFocusPatchImpl(JQuery element, int delay) /*-{
    element.attr('readonly', 'readonly'); // Force keyboard to hide on input field.
    element.attr('disabled', 'true'); // Force keyboard to hide on textarea field.
    setTimeout(function() {
        element.blur();  //actually close the keyboard
        // Remove readonly attribute after keyboard is hidden.
        element.removeAttr('readonly');
        element.removeAttr('disabled');
    }, delay);
  }-*/;
  
  public static void setDisabled(Element element) {
    setDisabled(JQuery.withElement(element));
  }

  public static native void setDisabled(JQuery element) /*-{
    element.attr('readonly', 'readonly'); // Force keyboard to hide on input field.
    element.attr('disabled', 'true'); // Force keyboard to hide on textarea field.
  }-*/;
  
  public static void setEnabled(int delay, final Element element) {
    GwtUtils.deferredExecution(delay, new Delegate<Void>() {
      public void execute(Void nil) {
        setEnabled(JQuery.withElement(element));
      }
    });
  }
  
  public static void setEnabled(Element element) {
    setEnabled(JQuery.withElement(element));
  }
  
  public static native void setEnabled(JQuery element) /*-{
    element.blur();  //actually close the keyboard
    // Remove readonly attribute after keyboard is hidden.
    element.removeAttr('readonly');
    element.removeAttr('disabled');
  }-*/;

  public static void modalDialogFocusPatchStart() {
    JQuery allInputs = JQuery.select(":input");
    for (Element elem : allInputs.toElements()) {
      setElementDisabled(elem);
    }
  }
  
  public static void modalDialogFocusPatchEnd() {
    JQuery allInputs = JQuery.select(":input");
    for (Element elem : allInputs.toElements()) {
      elem.removeAttribute("disabled");
    }
  }
  
  private static native void setElementDisabled(Element elem) /*-{
    elem.disabled = "true";
  }-*/;
  
  public static void addTouchEndHandlerPreventingScroll (HasTouchHandlers widget, final TouchEndHandler handler) {
    addTouchHandlersPreventingScroll(widget, null, handler);
    /*
    final ObjectWrapper<Integer> touchStartY = new ObjectWrapper<Integer>();
    widget.addTouchStartHandler(new TouchStartHandler() {
      public void onTouchStart(TouchStartEvent event) {
        try {
          touchStartY.set(event.getTouches().get(0).getPageY());
        } catch (Exception ex) {}
      }
    });
    widget.addTouchEndHandler(new TouchEndHandler() {
      public void onTouchEnd(TouchEndEvent event) {
        try {
          int endY = event.getChangedTouches().get(0).getPageY();
          int diffY = Math.abs((touchStartY.get() - endY));
          if (diffY < 10) {
            handler.onTouchEnd(event);
          }
        } catch (Exception ex) {}
      }
    });
    */
  }
  
  public static void addTouchHandlersPreventingScroll (HasTouchHandlers widget, final TouchStartHandler touchStartHandler, final TouchEndHandler touchEndHandler) {
    final ObjectWrapper<Integer> touchStartY = new ObjectWrapper<Integer>();
    widget.addTouchStartHandler(new TouchStartHandler() {
      public void onTouchStart(TouchStartEvent event) {
        try {
          touchStartY.set(event.getTouches().get(0).getPageY());
        } catch (Exception ex) {}
        if (touchStartHandler != null) {
          touchStartHandler.onTouchStart(event);
        }
      }
    });
    widget.addTouchEndHandler(new TouchEndHandler() {
      public void onTouchEnd(TouchEndEvent event) {
        try {
          int endY = event.getChangedTouches().get(0).getPageY();
          int diffY = Math.abs((touchStartY.get() - endY));
          if (diffY < 10) {
            if (touchEndHandler != null) {
              touchEndHandler.onTouchEnd(event);
            }
          }
        } catch (Exception ex) {}
      }
    });
  }
  
  public static void addTappedStyleHandlers(final HasTouchHandlers widget) {
    widget.addTouchStartHandler(new TouchStartHandler() {
      public void onTouchStart(TouchStartEvent event) {
        addTappedStyle(widget);
        GwtUtils.deferredExecution(150, new Delegate<Void>() {
          public void execute(Void element) {
            removeTappedStyle(widget);
          }
        });
      }
    });
  }
  
  protected static void addTappedStyle(final HasTouchHandlers widget) {
    ((UIObject)widget).addStyleName(tappedStyle);
  }
  
  protected static void removeTappedStyle(final HasTouchHandlers widget) {
    ((UIObject)widget).removeStyleName(tappedStyle);
    /*
    GwtUtils.deferredExecution(new Delegate<Void>() {
      public void execute(Void element) {
        ((UIObject)widget).removeStyleName(tappedStyle);
      }
    });
    */
  }
  
}
