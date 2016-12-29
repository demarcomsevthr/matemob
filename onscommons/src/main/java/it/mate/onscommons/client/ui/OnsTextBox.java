package it.mate.onscommons.client.ui;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.phgcommons.client.utils.OsDetectionUtils;

import com.google.gwt.dom.client.Element;



public class OnsTextBox extends OnsTextBoxBase {
  
  public OnsTextBox() {
    super("text");
    addStyleName("text-input");
  }
  
  protected OnsTextBox(String type) {
    super(type);
  }
  
  
  public void setAutocomplete(final String autocomplete) {
    OnsenUi.onAvailableElement(this, new Delegate<Element>() {
      public void execute(Element element) {
        element.setAttribute("autocomplete", autocomplete);
      }
    });
  }

  public void disableIosSuggestions() {
    if (OsDetectionUtils.isIOs()) {
      OnsenUi.onAvailableElement(this, new Delegate<Element>() {
        public void execute(Element element) {
          element.setAttribute("autocomplete", "off");
          element.setAttribute("autocorrect", "off");
        }
      });
    }
  }

}
