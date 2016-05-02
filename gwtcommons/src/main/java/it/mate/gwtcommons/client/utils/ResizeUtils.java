package it.mate.gwtcommons.client.utils;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

public class ResizeUtils {
  
  public static void setHeightRelativeToScreen (final Widget widget, final Integer percHeight) {
    GwtUtils.createTimer(100, new Delegate<Void>() {
      public void execute(Void element) {
        int screenHeight = Window.getClientHeight();
        int widgetHeight = screenHeight * percHeight / 100;
        widget.setHeight(widgetHeight + "px");
      }
    });
  }

  public static void setHeightWithFixedBottom (final Widget widget, final Integer bottomHeight) {
    GwtUtils.createTimer(100, new Delegate<Void>() {
      public void execute(Void element) {
        int screenHeight = Window.getClientHeight();
        int widgetHeight = screenHeight - bottomHeight;
        widget.setHeight(widgetHeight + "px");
      }
    });
  }

}
