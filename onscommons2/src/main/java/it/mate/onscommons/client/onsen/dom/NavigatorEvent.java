package it.mate.onscommons.client.onsen.dom;

import it.mate.gwtcommons.client.utils.GwtUtils;

import com.google.gwt.core.client.JavaScriptObject;

public class NavigatorEvent extends JavaScriptObject {

  protected NavigatorEvent() { }

  public final Page getCurrentPage() {
    return GwtUtils.getJsPropertyJso(this, "currentPage").cast();
  }
  
  public final Page getEnterPage() {
    return GwtUtils.getJsPropertyJso(this, "enterPage").cast();
  }
  
  public final Page getLeavePage() {
    return GwtUtils.getJsPropertyJso(this, "leavePage").cast();
  }
  
  public final native void cancel() /*-{
    this.cancel();
  }-*/;
  
}
