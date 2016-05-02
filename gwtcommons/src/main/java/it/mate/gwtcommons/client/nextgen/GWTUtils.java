package it.mate.gwtcommons.client.nextgen;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;

/**
 * 
 * SEE ALSO:
 *   https://www.youtube.com/watch?v=wFMD1GXR2Tg - GWT nextgen JsInterop and Web Components DemoO
 *
 */

public class GWTUtils {

  public static <T extends JavaScriptObject> T jsni(String snippet) {
    return jsniImpl(snippet).cast();
  }
  
  public static <T extends Element> T createElement(String tagName) {
    Document doc = getDocument();
    T elem = doc.createElement(tagName).cast();
    return elem;
  }
  
  public static Document getDocument() {
    return jsni("$doc");
  }
  
  public static Element getBody() {
    return jsni("$doc.body");
  }
  
  private static native JavaScriptObject jsniImpl(String snippet) /*-{
    return eval(snippet);
  }-*/;

}
