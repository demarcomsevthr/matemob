package it.mate.onscommons.client.onsen.dom;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.phgcommons.client.utils.PhgUtils;
import it.mate.phgcommons.client.utils.callbacks.JSOCallback;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class Navigator extends JavaScriptObject {

  private static String pushAnimation = "fade";
  
  protected Navigator() { }
  
  public static void setPushAnimation(String pushAnimation) {
    Navigator.pushAnimation = pushAnimation;
  }
  
  public final void pushPage(String pageId) {
    pushPage(pageId, null, null);
  }
  
  public final void pushPage(String pageId, Delegate<Void> onTransitionEndDelegate) {
    pushPage(pageId, null, onTransitionEndDelegate);
  }
  
  protected final void pushPage(String pageId, Options options, final Delegate<Void> onTransitionEndDelegate) {
    if (options == null) {
      options = Options.create();
    }
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

  protected final native Page getCurrentPageImpl() /*-{
    return this.getCurrentPage();    
  }-*/;
  
  public final void resetToPage(String pageId) {
    PhgUtils.log("RESET TO PAGE " + pageId);
    Options options = Options.create();
    resetToPageImpl(pageId, options);
  }

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

  protected final native JavaScriptObject getPagesImpl() /*-{
    return this.getPages();    
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
    PhgUtils.log("CURRENT PAGE NAME = " + getCurrentPage().getName());
    PhgUtils.log("CURRENT PAGE INDEX = " + getCurrentPage().getIndex());
  }
  
}
