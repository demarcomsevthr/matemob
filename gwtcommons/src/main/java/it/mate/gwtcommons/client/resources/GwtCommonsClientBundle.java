package it.mate.gwtcommons.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface GwtCommonsClientBundle extends ClientBundle {
  
  public static class Util {
    protected static GwtCommonsClientBundle impl;
    public static GwtCommonsClientBundle init() {
      if (impl == null) {
        impl = GWT.create(GwtCommonsClientBundle.class);
      }
      return impl;
    }
    public static GwtCommonsClientBundle initJQuery() {
      Util.injectScript(init().jqueryJs());
      return impl;
    }
    protected static void injectScript(TextResource resource) {
      ScriptInjector.fromString(resource.getText()).setWindow(ScriptInjector.TOP_WINDOW).inject();
    }
  }
  
  @Source("jquery/jquery-1.12.4.min.js")
  TextResource jqueryJs();

}
