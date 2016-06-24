package it.mate.onscommons.client.ui;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.onsen.dom.SlidingMenu;
import it.mate.phgcommons.client.utils.PhgUtils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class OnsSlidingMenu extends ComplexPanel implements HasWidgets {
  
  public OnsSlidingMenu() {
    this(DOM.createElement("ons-sliding-menu"));
    getElement().setAttribute("var", "app.menu");
    getElement().setAttribute("max-slide-distance", "85%");
//  getElement().setAttribute("type", "overlay");
  }
  
  protected OnsSlidingMenu(Element elem) {
    setElement(elem);
  }
  
  @Override
  public void add(Widget widget) {
    super.add(widget, getElement());
  }

  public void attachToBody() {
    RootPanel.get().add(this);
    OnsenUi.compileElement(getElement());
  }

  public final native SlidingMenu getController() /*-{
    return $wnd.app.menu;
  }-*/;
  
  public void setMenuPage(final String pageId) {
    OnsenUi.onAvailableElement(this, new Delegate<Element>() {
      public void execute(final Element slidingMenuElement) {
        OnsenUi.onAvailableElement(pageId, new Delegate<Element>() {
          public void execute(Element templateElement) {
            slidingMenuElement.setAttribute("menu-page", pageId);
            OnsenUi.compileElement(slidingMenuElement);
            PhgUtils.log("SETTING MENU PAGE " + pageId);
            getController().setMenuPage(pageId);;
          }
        });
      }
    });
  }
  
}
