package it.mate.gwtcommons.client.utils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;

public class JQueryEvent extends JavaScriptObject {
  
  protected JQueryEvent() { }
  
  protected static JQueryEvent create() {
    return JavaScriptObject.createObject().cast();
  }
  
  public final Element getTarget() {
    return GwtUtils.getJsPropertyJso(this, "target").cast();
  }
  
}
