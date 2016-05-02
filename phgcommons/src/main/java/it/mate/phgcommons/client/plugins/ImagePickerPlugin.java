package it.mate.phgcommons.client.plugins;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.phgcommons.client.utils.JSONUtils;
import it.mate.phgcommons.client.utils.PhgUtils;
import it.mate.phgcommons.client.utils.callbacks.JSOCallback;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

public class ImagePickerPlugin {
  
  /**
   * 
   * DOCUMENTAZIONE
   * 
   * https://github.com/wymsee/cordova-imagePicker
   * 
   * 
   * 
   */
  
  
  public static native boolean isInstalled () /*-{
    return typeof ($wnd.imagePicker) != 'undefined';
  }-*/;
  
  public static class Options {
    Integer maximumImagesCount;
    Integer width;
    Integer height;
    Integer quality;
    public Options setMaximumImagesCount(Integer maximumImagesCount) {
      this.maximumImagesCount = maximumImagesCount;
      return this;
    }
    public Options setWidth(Integer width) {
      this.width = width;
      return this;
    }
    public Options setHeight(Integer height) {
      this.height = height;
      return this;
    }
    public Options setQuality(Integer quality) {
      this.quality = quality;
      return this;
    }
  }

  public static void getPictures(Options options, final Delegate<List<String>> delegate) {
    PhgUtils.log("getting pictures");
    JavaScriptObject jsOptions = JavaScriptObject.createObject();
    if (options.maximumImagesCount != null) {
      GwtUtils.setJsPropertyInteger(jsOptions, "maximumImagesCount", options.maximumImagesCount);
    }
    if (options.width != null) {
      GwtUtils.setJsPropertyInteger(jsOptions, "width", options.width);
    }
    if (options.height != null) {
      GwtUtils.setJsPropertyInteger(jsOptions, "height", options.height);
    }
    if (options.quality != null) {
      GwtUtils.setJsPropertyInteger(jsOptions, "quality", options.quality);
    }
    getPicturesImpl(jsOptions, new JSOCallback() {
      public void handle(JavaScriptObject jso) {
        PhgUtils.log("getPictures - success - " + JSONUtils.stringify(jso));
        List<String> urls = new ArrayList<String>();
        JsArrayString results = jso.cast();
        if (results.length() > 0) {
          for (int it = 0; it < results.length(); it++) {
            urls.add(results.get(it));
          }
        }
        delegate.execute(urls);
      }
    }, new JSOCallback() {
      public void handle(JavaScriptObject error) {
        PhgUtils.log("getPictures - failure - " + JSONUtils.stringify(error));
      }
    });
  }
  
  private static native void getPicturesImpl(JavaScriptObject options, JSOCallback success, JSOCallback failure) /*-{
    var jsSuccess = $entry(function(results) {
      success.@it.mate.phgcommons.client.utils.callbacks.JSOSuccess::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(results);
    });
    var jsError = $entry(function(error) {
      failure.@it.mate.phgcommons.client.utils.callbacks.JSOSuccess::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(error);
    });
    $wnd.imagePicker.getPictures(jsSuccess, jsError, options); 
  }-*/;

}
