package it.mate.phgcommons.client.plugins;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.phgcommons.client.utils.AbstractPluginWrapper;
import it.mate.phgcommons.client.utils.OsDetectionUtils;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;

public class NativePropertiesPlugin extends AbstractPluginWrapper {

  public static void getProperties(final Delegate<Map<String, String>> delegate) {
    if (OsDetectionUtils.isDesktop() || OsDetectionUtils.isIOs()) {
      delegate.execute(new HashMap<String, String>());
    } else {
      execString("NativePropertiesPlugin", "getPropertiesAsString", null, new Delegate<String>() {
        public void execute(String results) {
          Map<String, String> properties = new HashMap<String, String>();
          String[] propTokens = results.split("\\|");
          for (String propToken : propTokens) {
            String[] tokens = propToken.split("=");
            String name = tokens[0];
            String value = tokens[1];
            properties.put(name, value);
          }
          delegate.execute(properties);
        }
      });
    }
  }
  
  /*
  public static void getProperties(final Delegate<Map<String, String>> delegate) {
    exec("NativePropertiesPlugin", "getProperties", null, new Delegate<JavaScriptObject>() {
      public void execute(JavaScriptObject results) {
        Map<String, String> properties = new HashMap<String, String>();
        JsArray<JsEntry> array = results.cast();
        for (int it = 0; it < array.length(); it++) {
          String name = array.get(it).getName();
          String value = array.get(it).getValue();
          properties.put(name, value);
        }
        delegate.execute(properties);
      }
    });
  }
  */
  
  protected static class JsEntry extends JavaScriptObject {
    protected JsEntry() { }
    public final String getName() {
      return (String)GwtUtils.getJsPropertyObject(this, "name");
    }
    public final String getValue() {
      return (String)GwtUtils.getJsPropertyObject(this, "value");
    }
  }
  
  
}
