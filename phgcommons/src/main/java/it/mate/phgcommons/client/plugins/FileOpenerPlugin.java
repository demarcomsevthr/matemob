package it.mate.phgcommons.client.plugins;

import it.mate.phgcommons.client.utils.callbacks.JSOFailure;
import it.mate.phgcommons.client.utils.callbacks.VoidCallback;

public class FileOpenerPlugin {

  /**
   * 
   *  DOCUMENTATION
   *  
   *    https://github.com/pwlin/cordova-plugin-file-opener2
   *    
   *    
   * 
   * 
   *  INSTALLATION
   *    
   *    cordova plugin add cordova-plugin-file-opener2
   *    
   *  
   */
  
  
  public static native boolean isInstalled () /*-{
    return typeof ($wnd.cordova.plugins.fileOpener2) != 'undefined';
  }-*/;

  
  /**
   * 
   * EXAMPLE
   * 
        filePath = '/sdcard/Download/starwars.pdf', // You can also use a Cordova-style file uri: cdvfile://localhost/persistent/Download/starwars.pdf
        fileMIMEType = 'application/pdf',
   * 
   * 
   */
  
  public static native void openFile (String filePath, String fileMIMEType, VoidCallback success, JSOFailure failure) /*-{
    var jsSuccess = $entry(function(fileEntry) {
      success.@it.mate.phgcommons.client.utils.callbacks.VoidCallback::handle()();
    });
    var jsFailure = $entry(function(error) {
      failure.@it.mate.phgcommons.client.utils.callbacks.JSOFailure::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(error);
    });
    $wnd.cordova.plugins.fileOpener2.open(
        filePath, fileMIMEType,
        { 
            error : jsFailure,
            success : jsSuccess
        }
    );    
  }-*/;
  
}
