package it.mate.onscommons.client.ui;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.event.NativeEventUtils;
import it.mate.phgcommons.client.utils.PhgUtils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;


public class OnsTextArea extends OnsTextBoxBase {
  
  public OnsTextArea() {
    super(DOM.createTextArea(), null, "ons-textarea");
    addStyleName("textarea");
  }
  
  public void setResizeable(String value) {
    Delegate<Element> delegate = new Delegate<Element>() {
      public void execute(final Element element) {
        GwtUtils.deferredExecution(new Delegate<Void>() {
          public void execute(Void dd) {
            PhgUtils.log("textarea scrollheight = "+element.getScrollHeight());
            element.getStyle().setHeight(element.getScrollHeight(), Unit.PX);
          }
        });
      }
    };
    NativeEventUtils.addEventListenerDelegate(this, "blur", delegate);
    NativeEventUtils.addEventListenerDelegate(this, "keyup", delegate);
    NativeEventUtils.addEventListenerDelegate(this, "change", delegate);
  }
  
}
