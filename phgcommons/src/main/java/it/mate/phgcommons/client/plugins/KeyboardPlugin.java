package it.mate.phgcommons.client.plugins;


/**
 * 
 * SEE ALSO:
 * 
 * https://github.com/martinmose/cordova-keyboard/
 *
 */

public class KeyboardPlugin {
  
  public static native boolean isInstalled () /*-{
    return typeof ($wnd.Keyboard) != 'undefined';
  }-*/;
  
  public static void hideFormAccessoryBar() {
    hideFormAccessoryBarImpl(true);
  }
  
  protected static native void hideFormAccessoryBarImpl(boolean flag) /*-{
    $wnd.Keyboard.hideFormAccessoryBar(flag);
  }-*/;

}
