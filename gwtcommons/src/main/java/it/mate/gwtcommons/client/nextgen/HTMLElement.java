package it.mate.gwtcommons.client.nextgen;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;

public class HTMLElement extends Element {
  
  protected HTMLElement() {

  }

  public final native JavaScriptObject addEventListenerImpl(String event, EventListener listener) /*-{

  }-*/;
  

}
