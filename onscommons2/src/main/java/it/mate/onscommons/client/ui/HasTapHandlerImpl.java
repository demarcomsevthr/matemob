package it.mate.onscommons.client.ui;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.event.HasTapHandler;
import it.mate.onscommons.client.event.TapEvent;
import it.mate.onscommons.client.event.TapHandler;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.phgcommons.client.utils.OsDetectionUtils;
import it.mate.phgcommons.client.utils.PhgUtils;
import it.mate.phgcommons.client.utils.callbacks.JSOCallback;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

public class HasTapHandlerImpl {
  
  private final static String EVENT_NAME = getTapEventName(); 
  
  private static final String getTapEventName() {
    if (OsDetectionUtils.isDesktop()) {
      return "click";
    }
    if (OsDetectionUtils.isAndroid() && PhgUtils.getDeviceVersion().startsWith("4.0")) {
      return "touchend";
    }
    return "touchend";
  }
  
  private List<TapHandler> tapHandlers = new ArrayList<TapHandler>();
  
  private List<HandlerRegistration> tapHandlerRegistrations = new ArrayList<HandlerRegistration>();
  
  private JavaScriptObject jsEventListener = null;
  
  private HasTapHandler targetWidget;
  
  private Element targetElement;
  
  private String targetElementId = null;
  
  private static boolean allHandlersDisabled = false;
  
  private static boolean useDocEventListener = false;
  
  private static boolean doLog = false;
  
  public HasTapHandlerImpl(HasTapHandler target) {
    if (doLog) PhgUtils.log("");
    if (doLog) PhgUtils.log("new instance with target " + target);
    this.targetWidget = target;
    this.targetElement = ((Widget)targetWidget).getElement();
  }
  
  public HasTapHandlerImpl(Element target) {
    if (doLog) PhgUtils.log("new instance with element " + target);
    this.targetElement = target;
  }
  
  public HasTapHandlerImpl(String elementId) {
    if (doLog) PhgUtils.log("new instance with elementId " + elementId);
    this.targetElementId = elementId;
  }
  
  private boolean isTargetWidgetChildOfDialog() {
    if (targetWidget == null) {
      return false;
    } else {
      return isChildOfDialog(((Widget)targetWidget).getParent());
    }
  }
  
  private boolean isChildOfDialog(Widget targetWidget) {
    if (targetWidget == null) {
      return false;
    }
    if (targetWidget instanceof OnsDialog) {
      return true;
    }
    return isChildOfDialog(targetWidget.getParent());
  }
  
  public static void setUseDocEventListener(boolean useDocEventListener) {
    if (doLog) PhgUtils.log("USE DOC EVENT LISTENER = " + useDocEventListener);
    HasTapHandlerImpl.useDocEventListener = useDocEventListener;
  }
  
  private String getTargetElementLog() {
//  Element targetElement = ((Widget)targetWidget).getElement();
    return targetElement.getNodeName().toLowerCase() + " " + targetElement.getId();
  }
  
  public HandlerRegistration addTapHandler(final TapHandler handler) {

    if (doLog) PhgUtils.log("ADD TAP HANDLER --1-- " + handler);
    
    if (targetElement != null) {
      OnsenUi.ensureId(targetElement);
    }
    
    this.tapHandlers.add(handler);
    
    if (jsEventListener == null) {
      
      if (doLog) PhgUtils.log("ADD TAP HANDLER --2-- ");
      
      if (targetElement != null) {
        targetElementId = targetElement.getId();
      }
      
      GwtUtils.onAvailable(targetElementId, new Delegate<Element>() {
        public void execute(/* final */ Element availableElement) {
          
          if (doLog) PhgUtils.log("ADD TAP HANDLER --3-- targetElementId available - " + availableElement);
          
          boolean isInDialog = isTargetWidgetChildOfDialog();
          
          if (isInDialog || useDocEventListener || targetElement == null) {
            
            if (doLog) PhgUtils.log("ADD TAP HANDLER --3-- SETTING DOC EVENT LISTENER");
            jsEventListener = addEventListenerDocImpl(availableElement.getId(), EVENT_NAME, onTapCallback(availableElement));
            
          } else {
            
            if (doLog) PhgUtils.log("ADD TAP HANDLER --3-- SETTING ELEMENT EVENT LISTENER");
            jsEventListener = addEventListenerElemImpl(availableElement, EVENT_NAME, onTapCallback(availableElement));
            
          }
        }
      });

    }
    HandlerRegistration registration = new HandlerRegistration() {
      public void removeHandler() {
        tapHandlers.remove(handler);
        if (tapHandlers.size() == 0) {
          removeEventListenerImpl(EVENT_NAME, jsEventListener);
          jsEventListener = null;
        }
      }
    };
    tapHandlerRegistrations.add(registration);
    return registration;
  }
  
  private JSOCallback onTapCallback(final Element availableElement) {
    return new JSOCallback() {
      public void handle(JavaScriptObject jsEvent) {
        Element eventElement = GwtUtils.getJsPropertyJso(jsEvent, "target").cast();
        
        if (doLog) PhgUtils.log("ON TAP CALLBACK - " + eventElement);
        
        if (!PhgUtils.isReallyAttached(availableElement.getId())) {
          if (doLog) PhgUtils.log("ON TAP CALLBACK - REMOVING ALL HANDLERS");
          removeAllHandlers();
          return;
        }
        if (allHandlersDisabled) {
          if (doLog) PhgUtils.log("ALL HANDLERS DISABLED");
          return;
        }
        for (TapHandler tapHandler : tapHandlers) {
          tapHandler.onTap(new TapEvent(eventElement, availableElement, 0, 0, (targetWidget != null ? (Widget)targetWidget : null)));
        }
      }
    };
  }
  
  public void removeAllHandlers() {
    for (HandlerRegistration reg : tapHandlerRegistrations) {
      reg.removeHandler();
    }
  }
  
  private static void doLog(String message) {
    if (doLog) PhgUtils.log(message);
  }
  
  protected static native JavaScriptObject addEventListenerDocImpl (String elemId, String eventName, JSOCallback callback) /*-{
    var jsEventListener = $entry(function(e) {
      @it.mate.onscommons.client.ui.HasTapHandlerImpl::doLog(Ljava/lang/String;)("DOC EVENT LISTENER: EVENT ON TARGET " + e.target.tagName + " " + e.target.id);
      if (@it.mate.onscommons.client.event.OnsEventUtils::isContained(Lcom/google/gwt/dom/client/Element;Ljava/lang/String;)(e.target, elemId)) {
        callback.@it.mate.phgcommons.client.utils.callbacks.JSOCallback::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(e);
      }
    });
    $doc.addEventListener(eventName, jsEventListener, false);    
    return jsEventListener;
  }-*/;
  
  
  
  
  
  /* 04/02/2015 */
  /*
  var jsPreventEventListener = $entry(function(e) {
    e.preventDefault();
    return false;
  });
  elem.addEventListener("touchstart", jsPreventEventListener, false);    
  elem.addEventListener("touchmove", jsPreventEventListener, false);    
  */
  
  /* 31/05/2015
   * 
   * TOLGO LA CONDIZIONE isContained (SULL'ELEMENT EVENT LISTENER SIAMO GIA' SICURI CHE SIAMO SULL'ELEMENT GIUSTO!)
   * 
   * PREV:
      if (@it.mate.onscommons.client.event.OnsEventUtils::isContained(Lcom/google/gwt/dom/client/Element;Ljava/lang/String;)(e.target, elem.id)) {
        callback.@it.mate.phgcommons.client.utils.callbacks.JSOCallback::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(e);
      }
   * 
   */
  
  protected static native JavaScriptObject addEventListenerElemImpl (Element elem, String eventName, JSOCallback callback) /*-{
    var jsEventListener = $entry(function(e) {
      @it.mate.onscommons.client.ui.HasTapHandlerImpl::doLog(Ljava/lang/String;)("ELEM EVENT LISTENER: EVENT ON TARGET " + e.target.tagName + " " + e.target.id);
      callback.@it.mate.phgcommons.client.utils.callbacks.JSOCallback::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(e);
//    if (@it.mate.onscommons.client.event.OnsEventUtils::isContained(Lcom/google/gwt/dom/client/Element;Ljava/lang/String;)(e.target, elem.id)) {
//      callback.@it.mate.phgcommons.client.utils.callbacks.JSOCallback::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(e);
//    }
    });
    elem.addEventListener(eventName, jsEventListener, false);
    return jsEventListener;
  }-*/;
  
  protected static native void removeEventListenerImpl(String eventName, JavaScriptObject jsEventListener) /*-{
    $doc.removeEventListener(eventName, jsEventListener);    
  }-*/;
  
  private void applyOldAndroidPatch() {
    if (OsDetectionUtils.isAndroid() && PhgUtils.getDeviceVersion().startsWith("4.0")) {
      Boolean patched = (Boolean)GwtUtils.getClientAttribute("HasTapHandlerImpl_OldAndroidPatch");
      if (patched == null) {
        GwtUtils.setClientAttribute("HasTapHandlerImpl_OldAndroidPatch", true);
        preventTouchEventDefaultImpl();
      }
    }
  }

  protected static native void preventTouchEventDefaultImpl () /*-{
    @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)(">>>>>>>>> PREVENT DEFAULT ON TOUCH EVENTS");
    var jsEventListener = $entry(function(e) {
      e.preventDefault();
      return false;
    });
    $doc.addEventListener("touchstart", jsEventListener, false);    
    $doc.addEventListener("touchmove", jsEventListener, false);    
    $doc.addEventListener("touchend", jsEventListener, false);    
  }-*/;
  
  public static void setAllHandlersDisabled(boolean allHandlersDisabled) {
    if (allHandlersDisabled) {
      if (doLog) PhgUtils.log("SETTING ALL TAP HANDLERS DISABLED");
    } else {
      if (doLog) PhgUtils.log("SETTING ALL TAP HANDLERS ENABLED");
    }
    HasTapHandlerImpl.allHandlersDisabled = allHandlersDisabled;
  }
  
}
