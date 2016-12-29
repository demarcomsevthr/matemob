package it.mate.onscommons.client.onsen.dom;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.ui.OnsPage;
import it.mate.phgcommons.client.utils.JSONUtils;
import it.mate.phgcommons.client.utils.PhgUtils;
import it.mate.phgcommons.client.utils.callbacks.JSOCallback;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;

public class SlidingMenu extends JavaScriptObject {
  
  protected static final double DURATION = 0.21;

  protected SlidingMenu() { }
  
  public final void setMainPage(String pageId, String animation) {
    Options options = Options.create();
    options.setCloseMenu(true);
    JavaScriptObject jsAnimation = null;
    String msg = "";
    if (animation != null) {
      if (OnsenUi.ANIMATION_NATIVE_PUSH.equals(animation)) {
        jsAnimation = getPushAnimationImpl();
        msg = " WITH NATIVE PUSH ANIMATION";
      } else if (OnsenUi.ANIMATION_NATIVE_POP.equals(animation)) {
        jsAnimation = getPopAnimationImpl();
        msg = " WITH NATIVE POP ANIMATION";
      }
    }
    PhgUtils.log("PUSHING PAGE " + pageId + " " + msg + " " + JSONUtils.stringify(options));
    setMainPageImpl(pageId, options, jsAnimation);
  }
  
  public static void getPreviousPageElement(Delegate<Element> delegate) {
    if (OnsPage.getPrevPage() != null) {
      OnsenUi.onAvailableElement(OnsPage.getPrevPage().getElement().getId(), delegate);
    }
  }
  
  protected final native void setMainPageImpl(String pageId, Options options, JavaScriptObject animation) /*-{
    if (animation != null) {
      options.callback = animation;
      @it.mate.onscommons.client.onsen.dom.SlidingMenu::hideBlackMask(Lcom/google/gwt/dom/client/Element;)(this._mainPage[0]);
    }
    this.setMainPage(pageId, options);    
  }-*/;
  
  protected static void hideBlackMask(Element mainPageElem) {
    if (mainPageElem == null) {
      return;
    }
    Element menuElem = mainPageElem.getParentElement();
    if (menuElem == null) {
      return;
    }
    for (int it = 0; it < menuElem.getChildCount(); it++) {
      Element menuChildElem = menuElem.getChild(it).cast();
      String color = menuChildElem.getStyle().getBackgroundColor();
      if ("black".equalsIgnoreCase(color)) {
        menuChildElem.getStyle().setBackgroundColor("transparent");
      }
    }
  }
  
  public final void setMenuPage(String pageId) {
    Options options = Options.create();
    PhgUtils.log("MENU PAGE " + pageId);
    setMenuPageImpl(pageId, options);
  }
  
  protected final native void setMenuPageImpl(String pageId, Options options) /*-{
    this.setMenuPage(pageId, options);    
  }-*/;
  
  public final void toggleMenu() {
    PhgUtils.log("toggleMenu");
    toggleMenuImpl();
  }
  
  protected final native void toggleMenuImpl() /*-{
    this.toggleMenu();    
  }-*/;

  public final native boolean isMenuOpened() /*-{
    return this.isMenuOpened();    
  }-*/;

  public final void closeMenu() {
    closeMenuImpl();
  }
  
  protected final native void closeMenuImpl() /*-{
    this.closeMenu();
  }-*/;

  public final void setSwipeable(boolean value) {
    setSwipeableImpl(value);
  }
  
  protected final native void setSwipeableImpl(boolean value) /*-{
    this.setSwipeable(value);    
  }-*/;
  
  
  protected final native Element getMainPageImpl() /*-{
    var menu = this;
    var mainPage = menu._mainPage[0];
    return mainPage;
  }-*/;
  
  protected static boolean firstLeavingPage = true;
  
  protected final native JavaScriptObject getPushAnimationImpl () /*-{
    var menu = this;
    var animation = function() {
      $wnd.animit(menu._mainPage[0])
        .queue({
          css: {
            transform: 'translate3D(100%, 0, 0)',
            opacity: 0
          },
          duration: 0
        })
        .queue({
          css: {
            transform: 'translate3D(0, 0, 0)',
            opacity: 1.0
          },
          duration: @it.mate.onscommons.client.onsen.dom.SlidingMenu::DURATION
        })
        .play();
    }
    return animation;
  }-*/;

  protected final native JavaScriptObject getPopAnimationImpl() /*-{
    var menu = this;
    var animation = function() {
      $wnd.animit(menu._mainPage[0])
        .queue({
          css: {
            transform: 'translate3D(-100%, 0, 0)',
          },
          duration: 0
        })
        .queue({
          css: {
            transform: 'translate3D(0, 0, 0)',
          },
          duration: @it.mate.onscommons.client.onsen.dom.SlidingMenu::DURATION
        })
        .play();
    }
    return animation;
  }-*/;
  
  
  public final void onMenuClose(final Delegate<SlidingMenuEvent> delegate) {
    onEventImpl("postclose", new JSOCallback() {
      public void handle(JavaScriptObject jso) {
        delegate.execute((SlidingMenuEvent)jso.cast());
      }
    });
  }
  
  protected final native void onEventImpl(String eventName, JSOCallback callback) /*-{
    var jsCallback = $entry(function(event) {
      callback.@it.mate.phgcommons.client.utils.callbacks.JSOCallback::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(event);
    });
    this.on(eventName, jsCallback);    
  }-*/;

}
