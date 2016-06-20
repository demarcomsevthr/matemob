package it.mate.testleaflet.client.utils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;

public class AnimitUtils {

  protected final static double CONTROLBAR_DURATION = 0.2;
  
  public static native void showComposeControlbar(Element elem, int x0, int y0, int x1, int y1, String width, String height) /*-{
    $wnd.animit(elem)
      .queue({
        css: {
          display: 'initial'
        },
        duration: 0
      })
      .queue({
        css: @it.mate.testleaflet.client.utils.AnimitUtils::hiddenStyleControlbar(II)(x0, y0),
        duration: 0
      })
      .queue({
        css: @it.mate.testleaflet.client.utils.AnimitUtils::visibleStyleControlbar(IILjava/lang/String;Ljava/lang/String;)(x1, y1, width, height),
        duration: @it.mate.testleaflet.client.utils.AnimitUtils::CONTROLBAR_DURATION
      })
      .play();
  }-*/;
  
  public static native void hideComposeControlbar(Element elem, int x0, int y0, int x1, int y1, String width, String height) /*-{
    $wnd.animit(elem)
      .queue({
        css: @it.mate.testleaflet.client.utils.AnimitUtils::visibleStyleControlbar(IILjava/lang/String;Ljava/lang/String;)(x1, y1, width, height),
        duration: 0
      })
      .queue({
        css: @it.mate.testleaflet.client.utils.AnimitUtils::hiddenStyleControlbar(II)(x0, y0),
        duration: @it.mate.testleaflet.client.utils.AnimitUtils::CONTROLBAR_DURATION
      })
      .queue({
        css: {
          display: 'none'
        },
        duration: 0
      })
      .play();
  }-*/;
  
  protected static native JavaScriptObject hiddenStyleControlbar(int x0, int y0) /*-{
    return { 
      left: x0+'px',
      top: y0+'px',
      width: 0,
      height: 0
    };
  }-*/;
  
  protected static native JavaScriptObject visibleStyleControlbar(int x1, int y1, String width, String height) /*-{
    return { 
      left: x1+'px',
      top: y1+'px',
      width: width,
      height: height
    };
  }-*/;
  
}
