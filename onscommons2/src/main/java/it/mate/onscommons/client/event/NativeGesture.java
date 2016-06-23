package it.mate.onscommons.client.event;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;

public class NativeGesture extends JavaScriptObject {

  protected NativeGesture() {

  }
  
  public final String toMyString() {
    return "NativeGesture [getBubbles()=" + getBubbles() 
        + ", eventPhase=" + getEventPhase()
        + ", type=" + getType()
        + ", pageX=" + getPageX()
        + ", pageY=" + getPageY()
        + ", target=" + getTarget()
        + "]";
  }

  public final native boolean getBubbles() /*-{
    return this.bubbles;
  }-*/;
  
  public final native int getEventPhase() /*-{
    return this.eventPhase;
  }-*/;

  public final native String getType() /*-{
    return this.type;
  }-*/;

  public final native Element getTarget() /*-{
    return this.target;
  }-*/;

  public final native int getPageX() /*-{
    return this.gesture.touches[0].pageX;
  }-*/;

  public final native int getPageY() /*-{
    return this.gesture.touches[0].pageY;
  }-*/;


}
