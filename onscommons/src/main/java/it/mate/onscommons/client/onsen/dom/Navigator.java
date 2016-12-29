package it.mate.onscommons.client.onsen.dom;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.phgcommons.client.utils.PhgUtils;
import it.mate.phgcommons.client.utils.callbacks.JSOCallback;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;

public class Navigator extends JavaScriptObject {

  private static String pushAnimation = "fade";
  
  protected Navigator() { }
  
  public static void setPushAnimation(String pushAnimation) {
    Navigator.pushAnimation = pushAnimation;
  }
  
  public final void pushPage(String pageId) {
    pushPageInternal(pageId, null, null);
  }
  
  public final void pushPage(String pageId, Delegate<Void> onTransitionEndDelegate) {
    pushPageInternal(pageId, null, onTransitionEndDelegate);
  }
  
  public final void pushPage(String pageId, String pageHTML, final Delegate<Void> onTransitionEndDelegate) {
    pushPageInternal(pageId, pageHTML, onTransitionEndDelegate);
  }

  // TODO [ONS2]
  protected final void pushPageInternal(String pageId, String pageHTML, final Delegate<Void> onTransitionEndDelegate) {
    Options options = Options.create();
    if (pushAnimation != null) {
      options.setAnimation(pushAnimation);
    }
    options.setOnTransitionEnd(PhgUtils.createJsCallback(new JSOCallback() {
      public void handle(JavaScriptObject jso) {
        GwtUtils.deferredExecution(new Delegate<Void>() {
          public void execute(Void element) {
            OnsenUi.fadeInCurrentPage();
          }
        });
        if (onTransitionEndDelegate != null) {
          onTransitionEndDelegate.execute(null);
        }
      }
    }));
    if (pageHTML != null) {
      options.setPageHTML(pageHTML);
    }
    
    if (OnsenUi.isVersion2()) {
      if (pageHTML == null) {
        PhgUtils.logWithStackTrace("PUSHING PAGE VER 2" + pageId);
        Element pageElement = GwtUtils.getElementById(pageId);
        PhgUtils.log("PUSHING PAGE VER 2 - " + pageElement);
      }
    }
    
    PhgUtils.log("PUSHING PAGE " + pageId);
    pushPageImpl(pageId, options);
  }
  
  protected final native void pushPageImpl(String pageId, Options options) /*-{
    this.pushPage(pageId, options);    
  }-*/;
  
  public final void insertPage(int index, String pageId) {
    Options options = Options.create();
    options.setHoge("hoge");
    PhgUtils.log("INSERTING PAGE " + pageId + " AT " + index);
    insertPageImpl(index, pageId, options);
  }
  
  protected final native void insertPageImpl(int index, String pageId, Options options) /*-{
    this.insertPage(index, pageId, options);    
  }-*/;
  
  public final void popPage() {
    popPageImpl();
  }
  
  protected final native void popPageImpl() /*-{
    this.popPage();    
  }-*/;
  
  public final Page getCurrentPage() {
    return getCurrentPageImpl();
  }

  // TODO [ONS2]
  protected final native Page getCurrentPageImpl() /*-{
    if (typeof this.topPage !== "undefined") {
      return this.topPage;
    } else {
      return this.getCurrentPage();
    }
  }-*/;
  
  public final void resetToPage(String pageId) {
    PhgUtils.log("RESET TO PAGE " + pageId);
    Options options = Options.create();
    resetToPageImpl(pageId, options);
  }

  public final void resetToPage(String pageId, String pageHTML) {
    if (OnsenUi.isVersion2()) {
      PhgUtils.log(">>>>>>>>>>> [ONS2] > RESET TO PAGE " + pageId);
      PhgUtils.log("clear from page " + pageId);
      clearFromPage2PatchImpl(pageId);
      PhgUtils.log("push page " + pageId);
      pushPageInternal(pageId, pageHTML, null);
    } else {
      PhgUtils.log("RESET TO PAGE " + pageId);
      Options options = Options.create();
      if (pageHTML != null) {
        options.setPageHTML(pageHTML);
      }
      resetToPageImpl(pageId, options);
    }
  }

  protected final native void replacePageImpl(String pageId, Options options) /*-{
    this.replacePage(pageId, options);
  }-*/;

  protected final native void clearFromPage2PatchImpl(String pageId) /*-{
    var startPos = 0;
    for (var it = 0; it < this.pages.length; it++) {
      if (this.pages[it].name === pageId) {
        startPos = it;
        break;
      }
    }
    while (this.pages.length > startPos) {
      this.pages[startPos]._destroy();
    }
  }-*/;

  protected final native void resetToPageImpl(String pageId, Options options) /*-{
    this.resetToPage(pageId, options);    
  }-*/;
  
  public final int getActualPageCount() {
    int count = 0;
    JsArray<Page> pages = getPages();
    for (int it = 0; it < pages.length(); it++) {
      Page page = pages.get(it);
      if (page.getName() != null && page.getName().trim().length() > 0) {
        count++;
      }
    }
    return count;
  }
  
  public final JsArray<Page> getPages() {
    return getPagesImpl().cast();
  }

  // TODO [ONS2]
  protected final native JavaScriptObject getPagesImpl() /*-{
    if (typeof this.getPages !== "undefined") {
      return this.getPages();
    } else {
      return this.pages;
    }
  }-*/;
  
  public final native int getPageCount() /*-{
    if (typeof this.getPages !== "undefined") {
      return this.getPages().length;
    } else if (typeof this.pages !== "undefined") {
      return this.pages.length;
    } else {
      return 0;
    }
  }-*/;

  public final void onAfterPagePush(final Delegate<NavigatorEvent> delegate) {
    onAfterPagePushImpl(new JSOCallback() {
      public void handle(JavaScriptObject jso) {
        delegate.execute((NavigatorEvent)jso.cast());
      }
    });
  }
  
  protected final native void onAfterPagePushImpl(JSOCallback callback) /*-{
    var jsCallback = $entry(function(event) {
      callback.@it.mate.phgcommons.client.utils.callbacks.JSOCallback::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(event);
    });
    this.on('postpush', jsCallback);    
  }-*/;

  public final void onAfterPagePop(final Delegate<NavigatorEvent> delegate) {
    onAfterPagePopImpl(new JSOCallback() {
      public void handle(JavaScriptObject jso) {
        delegate.execute((NavigatorEvent)jso.cast());
      }
    });
  }
  
  protected final native void onAfterPagePopImpl(JSOCallback callback) /*-{
    var jsCallback = $entry(function(event) {
      callback.@it.mate.phgcommons.client.utils.callbacks.JSOCallback::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(event);
    });
    this.on('postpop', jsCallback);    
  }-*/;

  public final void onBeforePagePop(final Delegate<NavigatorEvent> delegate) {
    onBeforePagePopImpl(new JSOCallback() {
      public void handle(JavaScriptObject jso) {
        delegate.execute((NavigatorEvent)jso.cast());
      }
    });
  }
  
  protected final native void onBeforePagePopImpl(JSOCallback callback) /*-{
    var jsCallback = $entry(function(event) {
      callback.@it.mate.phgcommons.client.utils.callbacks.JSOCallback::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(event);
    });
    this.on('prepop', jsCallback);    
  }-*/;
  
  public final void onBeforePagePush(final Delegate<NavigatorEvent> delegate) {
    onBeforePagePushImpl(new JSOCallback() {
      public void handle(JavaScriptObject jso) {
        delegate.execute((NavigatorEvent)jso.cast());
      }
    });
  }
  protected final native void onBeforePagePushImpl(JSOCallback callback) /*-{
    var jsCallback = $entry(function(event) {
      callback.@it.mate.phgcommons.client.utils.callbacks.JSOCallback::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(event);
    });
    this.on('prepush', jsCallback);    
  }-*/;
  
  public final void resetHistory() {
    PhgUtils.log("resetting navigator history");
    Page currentPage = getCurrentPage();
    resetToPage(currentPage.getName());
  }

  public final void log(String prompt) {
    JsArray<Page> pages = getPages();
    PhgUtils.log("NAVIGATOR ACTUAL PAGE COUNT = " + getActualPageCount());
    for (int it = 0; it < pages.length(); it++) {
      Page page = pages.get(it);
      String pageName = page.getName();
      PhgUtils.log( prompt + " - " + it + " - " + pageName);
    }
    Page currentPage = getCurrentPage();
    if (currentPage != null) {
      PhgUtils.log("CURRENT PAGE NAME = " + getCurrentPage().getName());
      PhgUtils.log("CURRENT PAGE INDEX = " + getCurrentPage().getIndex());
    } else {
      PhgUtils.log("CURRENT PAGE IS NULL");
    }
  }
  
  public final void resetAllPages() {
    PhgUtils.log("resetting all navigator pages");
    resetAllPagesImpl();
  }

  protected final native void resetAllPagesImpl() /*-{
    while (this.pages.length > 0) {
      this.pages[0]._destroy();
    }
  }-*/;

}
