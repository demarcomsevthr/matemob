package it.mate.phgcommons.client.utils;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class ZIndexPatch {
  
  public static void apply() {
    new Timer() {
      public void run() {
        Document.get().getBody().getStyle().setZIndex(1);
        RootPanel.get().setVisible(true);
      }
    }.scheduleRepeating(500);
  }
  
  
  public static void apply(final Widget widget) {
    new Timer() {
      public void run() {
        widget.getElement().getStyle().setZIndex(1);
        widget.getElement().getStyle().setVisibility(Visibility.VISIBLE);
        widget.setVisible(true);
      }
    }.schedule(200);
  }

}
