package it.mate.onscommons.client.utils;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.phgcommons.client.utils.callbacks.JSOCallback;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;

public class HammerUtils {
  
  public static void initialize(final Delegate<Void> delegate) {
    /*
    ScriptInjector.fromUrl(GWT.getModuleBaseURL()+"js/hammer.min.js").setCallback(new Callback<Void, Exception>() {
      public void onSuccess(Void result) {
        delegate.execute(result);
      }
      public void onFailure(Exception reason) {
        PhgUtils.log("ERROR LOADING HAMMER JS");
      }
    }).inject();
    */
    GwtUtils.deferredExecution(500, delegate);
  }
  
  public static void makeDraggable(final Element element, final Delegate<DragEvent> delegate) {
    jsMakeDraggableImpl(element, new JSOCallback() {
      public void handle(JavaScriptObject touch) {
        DragEvent event = new DragEvent();
        event.target = element;
        event.x = (int)GwtUtils.getJsPropertyDouble(touch, "pageX");
        event.y = (int)GwtUtils.getJsPropertyDouble(touch, "pageY");
        delegate.execute(event);
      }
    });
  }
  
  private static native void jsMakeDraggableImpl(Element element, JSOCallback callback) /*-{
    var offset = @it.mate.onscommons.client.utils.HammerUtils::getOffsetImpl(Lcom/google/gwt/dom/client/Element;D)(element, 0.5);
    var dragHandler = $entry(function(event) {
//    @it.mate.protoph.client.utils.HammerUtils::log(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)('event = ', event);
      var touchX = event.pointers[0].pageX;
      var touchY = event.pointers[0].pageY;
      element.style.left = (touchX - offset.left) + 'px';
      element.style.top = (touchY - offset.top) + 'px';
      var touch = event.pointers[0];
      callback.@it.mate.phgcommons.client.utils.callbacks.JSOCallback::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(touch);
    });
    var hammertime = new $wnd.Hammer(element);
    hammertime.on("pan", dragHandler);
    element.style.zIndex = "99999";
  }-*/;
  
  private static native void log(String prompt, JavaScriptObject jso) /*-{
    var str = @it.mate.phgcommons.client.utils.JSONUtils::stringify(Lcom/google/gwt/core/client/JavaScriptObject;)(jso);
    @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)(prompt+str);
  }-*/;

  public static class DragEvent {
    private Element target;
    private int x;
    private int y;
    public Element getTarget() {
      return target;
    }
    public int getX() {
      return x;
    }
    public int getY() {
      return y;
    }
  }

  private static native JavaScriptObject getOffsetImpl(Element obj, double factor) /*-{
    var offsetLeft = 0;
    var offsetTop = 0;
    var offsetWidth = obj.offsetWidth;
    var offsetHeight = obj.offsetHeight;
    do {
      if (!isNaN(obj.offsetLeft)) {
          offsetLeft += obj.offsetLeft;
      }
      if (!isNaN(obj.offsetTop)) {
          offsetTop += obj.offsetTop;
      }   
    } while(obj = obj.offsetParent );
    return {left: (offsetLeft + offsetWidth * factor), top: (offsetTop + offsetHeight * factor)};
  }-*/;
  
  public static void cloneElementOnPress(Element element, final ElementCallback callback) {
    jsCloneElementOnTouch(element, new ElementCallback() {
      public void handle(final Element clonedElement) {
        GwtUtils.deferredExecution(new Delegate<Void>() {
          public void execute(Void element) {
            callback.handle(clonedElement);
          }
        });
      }
    });
  }
  
  private static native void jsCloneElementOnTouch(Element element, ElementCallback callback) /*-{
    var offset = @it.mate.onscommons.client.utils.HammerUtils::getOffsetImpl(Lcom/google/gwt/dom/client/Element;D)(element, 0.5);
  
    var jsHandler = $entry(function(event) {
      var touchX = event.pointers[0].pageX;
      var touchY = event.pointers[0].pageY;
      var clonedElem = element.cloneNode(true);
      clonedElem.style.left = (touchX - offset.left + 20) + 'px';
      clonedElem.style.top = (touchY - offset.top + 20) + 'px';
      element.parentElement.appendChild(clonedElem);
      callback.@it.mate.onscommons.client.utils.HammerUtils.ElementCallback::handle(Lcom/google/gwt/dom/client/Element;)(clonedElem);
    });
    
    var hammertime = new $wnd.Hammer(element);
    hammertime.on("press", jsHandler);
    element.style.zIndex = "99999";
    
  }-*/;
  
  public interface ElementCallback {
    public void handle(Element element);
  }
  
  public static native void addDragHandlerImpl(Element element, String eventName, JSOCallback callback) /*-{
    var dragHandler = $entry(function(event) {
      var touchX = event.pointers[0].pageX;
      var touchY = event.pointers[0].pageY;
      var touch = event.pointers[0];
      callback.@it.mate.phgcommons.client.utils.callbacks.JSOCallback::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(touch);
    });
    var hammertime = new $wnd.Hammer(element);
    hammertime.on(eventName, dragHandler);
  }-*/;
  
}
