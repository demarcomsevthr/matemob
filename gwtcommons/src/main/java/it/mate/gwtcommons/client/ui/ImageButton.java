package it.mate.gwtcommons.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.Image;

public class ImageButton extends ButtonBase {
  
  public ImageButton() {
    super(Document.get().createPushButtonElement());
    setStyleName("gwt-Button");
  }
  
  public ImageButton(Image image, SafeHtml html) {
    this();
    setImage(image);
    setHTML(html);
  }

  public void setUrl(String url) {
    setImage(new Image(url));
  }
  
  public void setText(String text) {
    setHTML(text);
  }
  
  public void setImage(Image image) {
    String styles = image.getElement().getAttribute("style");
    styles += ";vertical-align:middle;";
    image.getElement().setAttribute("style", styles);
    DOM.insertBefore(getElement(), image.getElement(), DOM.getFirstChild(getElement()));
  }
  
  Element span = null;
  
  public void setHTML(String html) {
    if (span != null) {
      DOM.removeChild(getElement(), span);
    }
    span = DOM.createElement("span");
    span.setInnerHTML(html);
    String styles = span.getAttribute("style");
    styles += ";vertical-align:middle;";
    span.setAttribute("style", styles);
    DOM.appendChild(getElement(), span);
  }
  
}
