package it.mate.onscommons.client.onsen.dom;

import it.mate.phgcommons.client.utils.PhgUtils;

import com.google.gwt.core.client.JavaScriptObject;

public class Dialog extends JavaScriptObject {

  protected Dialog() { }
  
  public final void hide() {
    hideImpl();
  }
  
  protected final native void hideImpl() /*-{
    this.hide();
  }-*/;
  
  public final void show() {
    PhgUtils.log("showing dialog");
    showImpl();
  }
  
  protected final native void showImpl() /*-{
    this.show();
  }-*/;

  public final void setCancelable(boolean value) {
    setCancelableImpl(value);
  }
  
  protected final native void setCancelableImpl(boolean value) /*-{
    this.setCancelable(value);
  }-*/;

}
