package it.mate.gwtcommons.client.ui;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;

public class WaitingCursorUtil {
  
  private static int cycle = 1;
  
  private static Timer timer = null;
  
  private static Element targetElement = null;
  
  private static String previousCursor;
  
  public static void setTargetElement(Element targetElement) {
    WaitingCursorUtil.targetElement = targetElement;
  }
  
  public static void setPreviousCursor(String previousCursor) {
    WaitingCursorUtil.previousCursor = previousCursor;
  }
  
  public static void start() {
    start("/images/commons/waiting/waiting", ".gif", 1, 8);
  }
  
  private static void start(final String basePath, final String extension, final int firstFrame, final int lastFrame) {
    if (timer == null) {
      cycle = firstFrame;
      timer = GwtUtils.createTimer(180, new Delegate<Void>() {
        public void execute(Void element) {
          if (targetElement == null)
            targetElement = RootPanel.getBodyElement();
          if (previousCursor == null)
            previousCursor = targetElement.getStyle().getCursor();
          if (previousCursor == null || "".equals(previousCursor) || previousCursor.startsWith("url"))
            previousCursor = "default";
//        String cursor = "url("+basePath+cycle+extension+"),wait";
          String cursor = "wait";
          GwtUtils.setStyleAttribute(targetElement, "cursor", cursor);
          GwtUtils.setStyleAttribute(RootPanel.getBodyElement(), "cursor", cursor);
          cycle ++;
          if (cycle > lastFrame) {
            cycle = firstFrame;
          }
        }
      });
    }
  }
  public static void stop() {
    if (timer != null)
      timer.cancel();
    timer = null;
    if (targetElement == null)
      targetElement = RootPanel.getBodyElement();
    RootPanel.getBodyElement().getStyle().setCursor(Cursor.DEFAULT);
    GwtUtils.setStyleAttribute(targetElement, "cursor", previousCursor);
    targetElement = null;
    previousCursor = null;
  }
}
