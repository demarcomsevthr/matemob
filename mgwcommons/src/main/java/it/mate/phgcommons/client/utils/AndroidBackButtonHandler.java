package it.mate.phgcommons.client.utils;

import it.mate.gwtcommons.client.utils.Delegate;

import com.googlecode.mgwt.ui.client.MGWT;

public class AndroidBackButtonHandler {
  
  private static Delegate<String> delegate = null;
  
  public static void start() {
    if (MGWT.getOsDetection().isAndroid()) {
      AndroidBackButtonHandler.applyImpl();
    }
  }
  
  public static void setDelegate(Delegate<String> delegate) {
    AndroidBackButtonHandler.delegate = delegate;
  }
  
  private static native void applyImpl() /*-{
    $doc.addEventListener("backbutton", function(e) {
      var loc = "";
//    loc = $wnd.location;
      @it.mate.phgcommons.client.utils.AndroidBackButtonHandler::fireDelegate(Ljava/lang/String;)(loc);
    });
  }-*/;
  
  private static void fireDelegate(String location) {
    if (AndroidBackButtonHandler.delegate != null) {
      AndroidBackButtonHandler.delegate.execute(location);
    }
  }

}
