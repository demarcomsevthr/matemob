package com.google.gwt.dom.client;

import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.phgcommons.client.utils.PhgUtils;


public class DOMImplWebkitPatched extends DOMImplWebkit {
  
  public DOMImplWebkitPatched() {
    super();
    PhgUtils.log("----- USING " + DOMImplWebkitPatched.class);
  }
  
  @Override
  public Element createElement(Document doc, String tag) {
    Element element = super.createElement(doc, tag);
    OnsenUi.ensureId(element);
    OnsenUi.initializeElementCompileAttr(element);
    return element;
  }
  
}
