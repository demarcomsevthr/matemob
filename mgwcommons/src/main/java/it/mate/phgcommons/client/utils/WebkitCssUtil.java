package it.mate.phgcommons.client.utils;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.impl.ScrollPanelImpl;

public class WebkitCssUtil {

  private static boolean has3d = false /* _has3d() */;

  public static void translatePct(Element el, int x, int y) {
    translate(el, x, y, "%");
  }

  public static void translatePx(Element el, int x, int y) {
    translate(el, x, y, "px");
  }

  private static void translate(Element el, int x, int y, String unit) {
    String cssText = null;
    if (has3d && !MGWT.getOsDetection().isDesktop()) {
      cssText = "translate3d(" + x+unit + ", " + y+unit + ", 0"+unit+")";
    } else {
      cssText = "translate( " + x+unit + ", " + y+unit + " )";
    }
    _translate(el, cssText);
  }

  private static native void _translate(Element el, String css)/*-{
    el.style.webkitTransform = css;
  }-*/;
  
  public static native void resetTransform(Element el) /*-{
    el.style.webkitTransform = "";
  }-*/;

  private static native boolean _has3d()/*-{
    return ('WebKitCSSMatrix' in $wnd && 'm11' in new WebKitCSSMatrix())
  }-*/;

  
  public static native void setStyleProperty(Style style, String name, String value)/*-{
    style[name] = value;
  }-*/;

  public static void moveScrollPanelY(final ScrollPanelImpl scrollPanel, final int deltaY) {
    GwtUtils.deferredExecution(100, new Delegate<Void>() {
      public void execute(Void element) {
        PhgUtils.log("scrollPanelImpl.y = " + scrollPanel.getY());
        int newY = scrollPanel.getY() - deltaY;
        Widget wrappedPanel = scrollPanel.iterator().next();
        setTransform(wrappedPanel.getElement().getStyle(), "translate3d(0px, "+ newY +"px, 0px)");
      }
    });
  }
  
  private native static void setTransform(Style style, String transform) /*-{
    style['-webkit-transform'] = transform;
  }-*/;
  
}
