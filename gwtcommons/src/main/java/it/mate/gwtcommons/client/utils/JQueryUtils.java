package it.mate.gwtcommons.client.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;

/**
 * 07/10/2013
 * 
 * SEE ALSO:
 * 
 *   it.mate.gwtcommons.client.utils.JQuery
 *
 */

public class JQueryUtils {
  
  public static JsArray<Element> select(String selector) {
    JsArray<Element> elements = selectImpl(selector);
    if (elements != null && elements.length() == 0) {
      elements = null;
    }
    return elements;
  }
  
  public static Element selectElement(String selector) {
    return selectFirst(selector);
  }
  
  public static List<Element> selectList(String selector) {
    List<Element> result = new ArrayList<Element>();
    JsArray<Element> array = select(selector);
    for (int it = 0; it < array.length(); it++) {
      result.add(array.get(it));
    }
    return result;
  }
  
  @SuppressWarnings("rawtypes")
  public static Element selectFirst(String selector) {
    Object results = selectImpl(selector);
    if (results != null && results instanceof JsArray) {
      JsArray jsArray = (JsArray)results;
      if (jsArray.length() > 0) {
        Element firstElement = (Element)jsArray.get(0);
        return firstElement;
      }
    }
    return null;
  }
 
  private static native JsArray<Element> selectImpl(String selector) /*-{
    return $wnd.$(selector);
  }-*/;

}
