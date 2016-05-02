package it.mate.phgcommons.client.utils;

import com.googlecode.mgwt.ui.client.MGWT;


public class IPhoneScrollPatch {
  
  public static void apply() {
    if (MGWT.getOsDetection().isIOs()) {
      applyImpl();
    }
  }
  
  private static native void applyImpl() /*-{
    $wnd.$(':input').bind("blur", function(e) {
      $wnd.scrollTo(0,0);
      $doc.scrollTop(0);
    });;
  }-*/;
  
  
  /*
      $wnd.alert('blur');

   */

}
