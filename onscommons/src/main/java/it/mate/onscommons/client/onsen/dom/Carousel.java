package it.mate.onscommons.client.onsen.dom;

import it.mate.phgcommons.client.utils.PhgUtils;

import com.google.gwt.core.client.JavaScriptObject;

public class Carousel extends JavaScriptObject {

  protected Carousel() { }
  
  public final void refresh() {
    PhgUtils.log("refreshing carousel controller");
    try {
      refreshImpl();
    } catch (Exception ex) {
      PhgUtils.log("refresh error!");
    }
  }
  
  protected final native void refreshImpl() /*-{
    this.refresh();    
  }-*/;
  
  public final int getActiveCarouselItemIndex() {
    return getActiveCarouselItemIndexImpl();
  }
  
  protected final native int getActiveCarouselItemIndexImpl() /*-{
    return this.getActiveCarouselItemIndex();    
  }-*/;

}
