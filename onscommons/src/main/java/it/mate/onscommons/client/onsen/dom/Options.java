package it.mate.onscommons.client.onsen.dom;

import it.mate.gwtcommons.client.utils.GwtUtils;

import com.google.gwt.core.client.JavaScriptObject;

public class Options extends JavaScriptObject {
  
  protected Options() { }
  
  public static Options create() {
    return JavaScriptObject.createObject().cast();
  }
  
  public final void setHoge(String value) {
    GwtUtils.setJsPropertyString(this, "hoge", value);
  }
  
  public final void setCloseMenu(boolean value) {
    GwtUtils.setJsPropertyBool(this, "closeMenu", value);
  }
  
  public final void setAnimation(String value) {
    GwtUtils.setJsPropertyString(this, "animation", value);
  }
  
  public final void setOnTransitionEnd(JavaScriptObject value) {
    GwtUtils.setJsPropertyJso(this, "onTransitionEnd", value);
  }
  
}
