package it.mate.gwtcommons.client.ui;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class IFrame extends Widget {
  
  private Element iframe;
  
  public IFrame() {
    this(null);
  }
  
  public IFrame(SafeUri uri) {
    iframe = DOM.createIFrame();
    setElement(iframe);
    if (uri != null) {
      setSrc(uri);
    }
  }
  
  public void setSrc(SafeUri uri) {
    iframe.setAttribute("src", uri.asString());
  }

}
