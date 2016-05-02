package it.mate.phgcommons.client.utils;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;

import com.google.gwt.core.client.JavaScriptObject;

public class MagicPacketPlugin {

  private final static String ACTION_SEND_MAGIC_PACKET = "sendMagicPacket";
  
  public static class MagicPacket extends JavaScriptObject {
    protected MagicPacket() { }
    public static MagicPacket getMagicPacket(String host, int port, String macAddr) {
      MagicPacket packet = JavaScriptObject.createObject().cast();
      packet.setHostAddr(host);
      packet.setHostPort(""+port);
      packet.setMacAddr(macAddr);
      return packet;
    }
    public final void setHostAddr(String host) {
      GwtUtils.setJsPropertyString(this, "hostAddr", host);
    }
    public final void setHostPort(String port) {
      GwtUtils.setJsPropertyString(this, "hostPort", port);
    }
    public final void setMacAddr(String mac) {
      GwtUtils.setJsPropertyString(this, "macAddr", mac);
    }
  }
  
  public static void sendMagicPacket(MagicPacket value, final Delegate<Boolean> delegate) {
    execImpl(ACTION_SEND_MAGIC_PACKET, value, new JsCallback() {
      public void execute(JavaScriptObject jso) {
        delegate.execute(true);
      }
    }, new JsCallback() {
      public void execute(JavaScriptObject jso) {
        delegate.execute(false);
      }
    });
  }

  protected static interface JsCallback {
    public void execute(JavaScriptObject jso);
  }
  
  private static native void execImpl (String action, MagicPacket value, JsCallback success, JsCallback failure) /*-{
    var jsSuccess = $entry(function(results) {
      success.@it.mate.phgcommons.client.utils.MagicPacketPlugin.JsCallback::execute(Lcom/google/gwt/core/client/JavaScriptObject;)(results);
    });
    var jsFailure = $entry(function(err) {
      failure.@it.mate.phgcommons.client.utils.MagicPacketPlugin.JsCallback::execute(Lcom/google/gwt/core/client/JavaScriptObject;)(err);
    });
    $wnd.cordova.exec(jsSuccess, jsFailure, "MagicPacketPlugin", action, [value]);
  }-*/;
  
  
}
