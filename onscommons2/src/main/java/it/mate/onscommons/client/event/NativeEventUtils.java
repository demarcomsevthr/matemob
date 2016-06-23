package it.mate.onscommons.client.event;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.phgcommons.client.utils.callbacks.JSOCallback;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class NativeEventUtils {

  
  public static void addEventListenerDelegate(Widget widget, final String eventName, final Delegate<Element> delegate) {
//  PhgUtils.ensureId(widget.getElement());
    OnsenUi.ensureId(widget.getElement());
    GwtUtils.onAvailable(widget.getElement().getId(), new Delegate<Element>() {
      public void execute(Element attachedElement) {
        addEventListenerElemImpl(attachedElement, eventName, new JSOCallback() {
          public void handle(JavaScriptObject jsEvent) {
            Element eventTargetElem = GwtUtils.getJsPropertyJso(jsEvent, "target").cast();
            
            delegate.execute(eventTargetElem);
            
          }
        });
      }
    });
  }
  
  
  protected static native JavaScriptObject addEventListenerElemImpl (Element elem, String eventName, JSOCallback callback) /*-{
    var jsEventListener = $entry(function(e) {
//    @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)("RISED EVENT > TARGET " + e.target.tagName + " " + e.target.id + " " + e.target.innerHTML);
      if (@it.mate.onscommons.client.event.OnsEventUtils::isContained(Lcom/google/gwt/dom/client/Element;Ljava/lang/String;)(e.target, elem.id)) {
        callback.@it.mate.phgcommons.client.utils.callbacks.JSOCallback::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(e);
      }
    });
    elem.addEventListener(eventName, jsEventListener);    
    return jsEventListener;
  }-*/;
  

}
