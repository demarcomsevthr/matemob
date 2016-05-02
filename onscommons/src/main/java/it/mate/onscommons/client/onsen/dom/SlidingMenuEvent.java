package it.mate.onscommons.client.onsen.dom;

import it.mate.gwtcommons.client.utils.GwtUtils;

import com.google.gwt.core.client.JavaScriptObject;

public class SlidingMenuEvent extends JavaScriptObject {

  protected SlidingMenuEvent() { }

  public final SlidingMenu getSlidingMenu() {
    return GwtUtils.getJsPropertyJso(this, "slidingMenu").cast();
  }
  
  public final native void cancel() /*-{
    this.cancel();
  }-*/;
  
}
