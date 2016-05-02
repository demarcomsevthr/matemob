package it.mate.onscommons.client.ui;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.onscommons.client.onsen.OnsenUi;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;


public class OnsButton extends OnsButtonBase {
  
  public OnsButton() {
    super("ons-button");
  }
  
  public OnsButton(String classname) {
    super(DOM.createElement("ons-button"), classname);
  }
  
  public void setIcon(String icon) {
    setIcon(getElement(), icon);
  }

  public void setIconWhenAvailable(final String icon) {
    if (OnsenUi.isAddDirectWithPlainHtml()) {
      setIconDirect(icon);
    } else {
      OnsenUi.onAvailableElement(getElement(), new Delegate<Element>() {
        public void execute(Element element) {
          setIcon(element, icon);
          OnsenUi.compileElement(element);
        }
      });
    }
  }
  
  public void setIconDirect(String icon) {
    String iconHtml = "<ons-icon icon='"+icon+"'/>";
    getElement().setInnerHTML(iconHtml);
  }

  private void setIcon(Element element, String icon) {
    String iconHtml = "<ons-icon icon='"+icon+"' size='24px'/>";
    String innerHtml = element.getInnerHTML();
    innerHtml = iconHtml + innerHtml;
    element.setInnerHTML(innerHtml);
  }

}
