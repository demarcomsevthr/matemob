package it.mate.onscommons.client.event;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.phgcommons.client.utils.OsDetectionUtils;
import it.mate.phgcommons.client.utils.callbacks.JSOCallback;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;

public class OnsEventUtils {

  private final static String TAP = OsDetectionUtils.isDesktop() ? "click" : "touchend"; 
  
  private static final String TOUCHSTART = OsDetectionUtils.isDesktop() ? "mousedown" : "touchstart"; 
  
  private static final String TOUCHEND = OsDetectionUtils.isDesktop() ? "mouseup" : "touchend";
  
  public static HandlerRegistration addDragStartHandler(Element element, final NativeGestureHandler handler) {
    return addDragStartHandler(element, false, handler);
  }
  
  public static HandlerRegistration addDragStartHandler(Element element, boolean attachHandlerToElement, final NativeGestureHandler handler) {
    return internalAddHandler(element, null, "dragstart", handler, attachHandlerToElement);
  }
  
  public static HandlerRegistration addDragOverHandler(Element element, final NativeGestureHandler handler) {
    return addDragOverHandler(element, false, handler);
  }
  
  public static HandlerRegistration addDragOverHandler(Element element, boolean attachHandlerToElement, final NativeGestureHandler handler) {
    return internalAddHandler(element, null, "dragover", handler, attachHandlerToElement);
  }
  
  public static HandlerRegistration addDragEndHandler(Element element, final NativeGestureHandler handler) {
    return addDragEndHandler(element, false, handler);
  }
  
  public static HandlerRegistration addDragEndHandler(Element element, boolean attachHandlerToElement, final NativeGestureHandler handler) {
    return internalAddHandler(element, null, "dragend", handler, attachHandlerToElement);
  }
  
  public static HandlerRegistration addMouseMoveHandler(Element element, final NativeGestureHandler handler) {
    return addMouseMoveHandler(element, false, handler);
  }
  
  public static HandlerRegistration addMouseMoveHandler(Element element, boolean attachHandlerToElement, final NativeGestureHandler handler) {
    return internalAddHandler(element, null, "mousemove", handler, attachHandlerToElement);
  }
  
  public static HandlerRegistration addChangeHandler(Element element, final NativeGestureHandler handler) {
    return addChangeHandler(element, false, handler);
  }
  
  public static HandlerRegistration addChangeHandler(Element element, boolean attachHandlerToElement, final NativeGestureHandler handler) {
    return internalAddHandler(element, null, "change", handler, attachHandlerToElement);
  }
  
  public static HandlerRegistration addTapHandler(Element element, final NativeGestureHandler handler) {
    return internalAddHandler(element, null, TAP, handler, true);
  }
  
  public static HandlerRegistration addTapHandler(String elementId, final NativeGestureHandler handler) {
    return internalAddHandler(null, elementId, TAP, handler, false);
  }
  
  public static HandlerRegistration addTouchStartHandler(Element element, final NativeGestureHandler handler) {
    return internalAddHandler(element, null, TOUCHSTART, handler, true);
  }
  
  public static HandlerRegistration addTouchEndHandler(Element element, final NativeGestureHandler handler) {
    return internalAddHandler(element, null, TOUCHEND, handler, true);
  }
  
  public static HandlerRegistration addHandler(Element element, String eventName, NativeGestureHandler handler) {
    return internalAddHandler(element, null, eventName, handler, true);
  }
  
  public static HandlerRegistration addHandler(Element element, String eventName, boolean attachHandlerToElement, final NativeGestureHandler handler) {
    return internalAddHandler(element, null, eventName, handler, attachHandlerToElement);
  }
  
  public static HandlerRegistration addHandler(String elementId, String eventName, final NativeGestureHandler handler) {
    return internalAddHandler(null, elementId, eventName, handler, false);
    /* 30/10/2015 - TOLTA DUPLICAZIONE DI CODICE 
    addEventListenerDocImpl(elementId, eventName, new JSOCallback() {
      public void handle(JavaScriptObject jso) {
        handler.on(new NativeGestureEvent((NativeGesture)jso.cast()));
      }
    });
    HandlerRegistration registration = new HandlerRegistration() {
      public void removeHandler() {
        
      }
    };
    return registration;
    */
  }
  
  protected static HandlerRegistration internalAddHandler(Element element, String elementId, String eventName, final NativeGestureHandler handler, boolean attachHandlerToElement) {
    if (attachHandlerToElement) {
      addEventListenerElemImpl(element, eventName, new JSOCallback() {
        public void handle(JavaScriptObject jso) {
          handler.on(new NativeGestureEvent((NativeGesture)jso.cast()));
        }
      });
    } else {
      addEventListenerDocImpl(element != null ? element.getId() : elementId, eventName, new JSOCallback() {
        public void handle(JavaScriptObject jso) {
          handler.on(new NativeGestureEvent((NativeGesture)jso.cast()));
        }
      });
    }
    HandlerRegistration registration = new HandlerRegistration() {
      public void removeHandler() {
        
      }
    };
    return registration;
  }
  
  protected static native JavaScriptObject addEventListenerDocImpl (String elemId, String eventName, JSOCallback callback) /*-{
    var jsEventListener = $entry(function(e) {
      if (@it.mate.onscommons.client.event.OnsEventUtils::isContained(Lcom/google/gwt/dom/client/Element;Ljava/lang/String;)(e.target, elemId)) {
        callback.@it.mate.phgcommons.client.utils.callbacks.JSOCallback::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(e);
      }
    });
    $doc.addEventListener(eventName, jsEventListener, false);    
    return jsEventListener;
  }-*/;
  
  protected static native JavaScriptObject addEventListenerElemImpl (Element elem, String eventName, JSOCallback callback) /*-{
    var jsEventListener = $entry(function(e) {
      if (@it.mate.onscommons.client.event.OnsEventUtils::isContained(Lcom/google/gwt/dom/client/Element;Ljava/lang/String;)(e.target, elem.id)) {
        callback.@it.mate.phgcommons.client.utils.callbacks.JSOCallback::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(e);
      }
    });
    elem.addEventListener(eventName, jsEventListener, false);
    return jsEventListener;
  }-*/;
  
  public static native boolean isContained(Element elem, String containerId) /*-{
  do {
    if (typeof elem == 'undefined' || elem == null) {
      break;
    }
    if (elem.id == containerId) {
      return true;
    }
  } while(elem = elem.parentElement);
  return false;
}-*/;

  public static JavaScriptObject addOverallEventListener (final Delegate<Element> delegate) {
    return addOverallEventListenerImpl(TAP, new JSOCallback() {
      public void handle(JavaScriptObject jso) {
        delegate.execute((Element)jso.cast());
      }
    });
  }
  
  public static void removeEventListener (JavaScriptObject jsEventListener) {
    removeEventListenerImpl(TAP, jsEventListener);
  }
  
  protected static native JavaScriptObject addOverallEventListenerImpl (String eventName, JSOCallback callback) /*-{
    var jsEventListener = $entry(function(e) {
      callback.@it.mate.phgcommons.client.utils.callbacks.JSOCallback::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(e.target);
    });
    $doc.addEventListener(eventName, jsEventListener, false);    
    return jsEventListener;
  }-*/;
  
  protected static native void removeEventListenerImpl(String eventName, JavaScriptObject jsEventListener) /*-{
    $doc.removeEventListener(eventName, jsEventListener);    
  }-*/;

}
