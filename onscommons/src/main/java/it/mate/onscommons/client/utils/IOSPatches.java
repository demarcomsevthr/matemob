package it.mate.onscommons.client.utils;

import it.mate.phgcommons.client.utils.AnimationUtil;
import it.mate.phgcommons.client.utils.OsDetectionUtils;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.MetaElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.RootPanel;

public class IOSPatches {
  
  // 20/03/2014
  // iOS webview focus issue (http://stackoverflow.com/questions/19110144/ios7-issues-with-webview-focus-when-using-keyboard-html)
  /*
  public static void applyViewPortPatch(ViewPort viewPort) {
    if (!OsDetectionUtils.isIOs()) {
      return;
    }
    viewPort.setHeightToDeviceHeight();
  }
  */
  
  // 20/03/2014
  // vedi sopra
  public static void applyStatusBarStylePatch() {
    if (!OsDetectionUtils.isIOs()) {
      return;
    }
    MetaElement statusBarStyleElement = Document.get().createMetaElement();
    statusBarStyleElement.setName("apple-mobile-web-app-status-bar-style");
    statusBarStyleElement.setContent("black-translucent");
    Element head = Document.get().getElementsByTagName("head").getItem(0);
    head.appendChild(statusBarStyleElement);
  }

  private static long iOS7HeaderBarPatchLastCheckTime = 0;
  
  public static void applyIOS7HeaderBarPatch() {
    if (!OsDetectionUtils.isIOs()) {
      return;
    }
    AnimationUtil.doAnimation(new AnimationUtil.Handler() {
      public boolean handleAnimation(long startTime, long currentTime) {
        if (currentTime > iOS7HeaderBarPatchLastCheckTime + 1000) {
          Element body = RootPanel.getBodyElement();
          body.getStyle().setMarginTop(18, Unit.PX);
          iOS7HeaderBarPatchLastCheckTime = currentTime;
//        PhgUtils.log(">>>>>>>>>>>>>> IOSPatches::applyIOS7HeaderBarPatch");
        }
        return true;
      }
    });
  }
  
  protected static native Element getActiveElement() /*-{
    return $doc.activeElement;
  }-*/;

}
