package it.mate.onscommons.client.onsen.dom;

import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.onsen.OnsenUi;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;

public class Page extends JavaScriptObject {

  protected Page() { }
  
  public final String getName() {
    return GwtUtils.getJsPropertyString(this, "name");
  }
  
  // TODO [ONS2]
  public final Element getPageElement() {
    if (OnsenUi.isVersion2()) {
      Element pageElement = (Element)this.cast();
      return pageElement;
    } else {
      JavaScriptObject pageElement = GwtUtils.getJsPropertyJso(this, "element");
      Element pageContextElement = GwtUtils.getJsPropertyJso(pageElement, "context").cast();
      return pageContextElement;
    }
  }
  
  // TODO [ONS2]
  public final Element getInnerPageElement() {
    if (OnsenUi.isVersion2()) {
      Element pageElement = getPageElement();
      NodeList<Element> nodeList = pageElement.getElementsByTagName("div");
      for (int it = 0; it < nodeList.getLength(); it++) {
        Element innerElem = nodeList.getItem(it);
        if (innerElem.getClassName().contains("page__content")) {
          return innerElem;
        }
      }
    } else {
      Element pageElement = getPageElement();
      NodeList<Element> nodeList = pageElement.getElementsByTagName("div");
      for (int it = 0; it < nodeList.getLength(); it++) {
        Element innerElem = nodeList.getItem(it);
        if (innerElem.getClassName().contains("ons-page-inner")) {
          return innerElem;
        }
      }
    }
    return null;
  }
  
  public final Integer getIndex() {
    JsArray<Page> pages = OnsenUi.getNavigator().getPages();
    for (int it = 0; it < pages.length(); it++) {
      Page page = pages.get(it);
      if (page.getName().equals(this.getName())) {
        return it;
      }
    }
    return null;
  }
  
  public final native void destroy() /*-{
    this.destroy();    
  }-*/;
  
  public final void setView(AbstractBaseView view) {
    getInnerPageElement().setPropertyObject("_view", view);
  }
  
  public final AbstractBaseView getView() {
    return (AbstractBaseView)getInnerPageElement().getPropertyObject("_view");
  }
  
}
