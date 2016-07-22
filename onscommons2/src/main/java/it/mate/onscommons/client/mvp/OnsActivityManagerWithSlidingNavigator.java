package it.mate.onscommons.client.mvp;

import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.places.HasToken;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.event.OnsPlaceChangeEvent;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.onsen.dom.Navigator;
import it.mate.onscommons.client.onsen.dom.NavigatorEvent;
import it.mate.onscommons.client.onsen.dom.Page;
import it.mate.onscommons.client.onsen.dom.SlidingMenuEvent;
import it.mate.onscommons.client.ui.OnsLayoutView;
import it.mate.onscommons.client.ui.OnsNavigator;
import it.mate.onscommons.client.ui.OnsSlidingMenu;
import it.mate.phgcommons.client.utils.OsDetectionUtils;
import it.mate.phgcommons.client.utils.PhgUtils;

import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.event.shared.EventBus;

public abstract class OnsActivityManagerWithSlidingNavigator extends OnsActivityManagerBase {
  
  private static final boolean CLOSE_MENU_AFTER_PUSH = Boolean.parseBoolean( PhgUtils.getLocalStorageItemForDebug("debug.OnsActivityManagerWithSlidingNavigator.closeMenuAfterPush", "false") );
  
  private static boolean allowNavigatorPoping = true;
  
  private boolean allowPagePoping = allowNavigatorPoping;
  
  private boolean navigatorInitialized = false;
  
  private Navigator navigator;
  
  private OnsSlidingMenu slidingMenu;
  
  private static boolean enableCheckAutoRefreshHome = false;
  
  private static long lastAutoRefreshHomeTime = -1;
  
  public abstract void onNavigatorInitialized(Navigator navigator);
  
  public OnsActivityManagerWithSlidingNavigator(ActivityMapper mapper, EventBus eventBus, final OnsLayoutView layoutView) {
    super(mapper, eventBus);
    RootPanel.get().add(layoutView);
    OnsenUi.compileElement(layoutView.asWidget().getElement());
    setNavigator(layoutView.getOnsNavigator());
    setSlidingMenu(layoutView.getOnsSlidingMenu());
  }
  
  @Override
  public void onPlaceChange(PlaceChangeEvent event) {
    Place newPlace = event.getNewPlace();
    
    HasToken newTokenizedPlace = (HasToken)newPlace;
    if (lastProcessedPlace != null && lastProcessedPlace.getToken().equals(newTokenizedPlace.getToken())) {
      PhgUtils.log("ON PLACE CHANGE: PLACE JUST PROCESSED (SKIP) newPlace = " + newPlace);
      slidingMenu.getController().closeMenu();
      return;
    } else {
      lastProcessedPlace = newTokenizedPlace;
    }
    
    PhgUtils.log("ON PLACE CHANGE: newPlace = " + newPlace);
    boolean preventPush = setActivePanelFromTemplate(newPlace);
    PhgUtils.log("STARTING ACTIVITY FOR " + newPlace);
    super.onPlaceChange(event);
    if (!preventPush) {
      Integer insertIndex = null;
      if (event instanceof OnsPlaceChangeEvent) {
        OnsPlaceChangeEvent onsEvent = (OnsPlaceChangeEvent)event;
        insertIndex = onsEvent.getInsertIndex();
      }
      pushPage(newPlace, insertIndex);
    } else {
      Element pageElement = navigator.getCurrentPage().getPageElement();
      OnsenUi.compileElement(pageElement);
    }
    
  }
  
  @Override
  protected Page getCurrentPage() {
    return navigator.getCurrentPage();
  }
  
  // TODO [ONS2]
  private void pushPage(Place newPlace, Integer insertIndex) {
    
    compileActivePanel();
    
    HasToken hasToken = (HasToken)newPlace;
    String newToken =  hasToken.getToken();
    putPlace(newPlace);
    
    PhgUtils.log("------------------------------------");
    PhgUtils.log("BEFORE PUSH PAGE " + newToken);
    navigator.log("NAVIGATOR PAGE");
    PhgUtils.log("------------------------------------");
    
    boolean pagePushed = false;
    
    Page currentPage = navigator.getCurrentPage();
    
    Delegate<Void> onPushTransitionEndDelegate = null;
    if (CLOSE_MENU_AFTER_PUSH) {
      onPushTransitionEndDelegate = new Delegate<Void>() {
        public void execute(Void element) {
          PhgUtils.log("onPushTransitionEndDelegate: closing menu");
          slidingMenu.getController().closeMenu();
        }
      };
    } else {
      slidingMenu.getController().closeMenu();
    }
    
    if (currentPage != null) {
      String currentPageName = currentPage.getName();
      if (!newToken.equals(currentPageName)) {
        if (insertIndex != null) {
          navigator.log("BEFORE INSERT PAGE");
          navigator.insertPage(insertIndex, newToken);
          GwtUtils.deferredExecution(new Delegate<Void>() {
            public void execute(Void element) {
              allowPagePoping = true;
              navigator.popPage();
            }
          });
        } else {
          boolean found = false;
          for (int it = 0; it < navigator.getPages().length(); it++) {
            Page page = navigator.getPages().get(it);
            String pageName = page.getName();
            if (pageName != null && ( pageName.equals(newToken) || pageName.trim().length() == 0)) {
              found = true;
              if (OnsenUi.isVersion2()) {
                navigator.resetToPage(newToken, getActivePanelInnerHtml());
              } else {
                navigator.resetToPage(newToken);
              }
              PhgUtils.log("------------------------------------");
              PhgUtils.log("AFTER RESET PAGE " + newToken);
              navigator.log("NAVIGATOR PAGE");
              PhgUtils.log("------------------------------------");
            }
          }
          
          if (!found) {
            checkAutoRefreshHome(null, newToken);
            if ("home".equalsIgnoreCase(newToken)) {
              if (OnsenUi.isVersion2()) {
                navigator.resetToPage(newToken, getActivePanelInnerHtml());
              } else {
                navigator.resetToPage(newToken);
              }
            } else {
              if (OnsenUi.isVersion2()) {
                PhgUtils.log("push page version 2");
                navigator.pushPage(newToken, getActivePanelInnerHtml(), onPushTransitionEndDelegate);
              } else {
                navigator.pushPage(newToken, onPushTransitionEndDelegate);
              }
            }
          }
          
        }
        pagePushed = true;
      }
    } else {
      
      if (OnsenUi.isVersion2()) {
        PhgUtils.log("push page version 2");
        navigator.pushPage(newToken, getActivePanelInnerHtml(), onPushTransitionEndDelegate);
        pagePushed = true;
      } else {
        navigator.pushPage(newToken, onPushTransitionEndDelegate);
        pagePushed = true;
      }
      
    }
    
    if (!pagePushed) {
      if (OnsenUi.isVersion2()) {
        navigator.resetToPage(newToken, getActivePanelInnerHtml());
      } else {
        navigator.resetToPage(newToken);
      }
    }
    
  }
  
  protected void setAfterPagePushHandler() {
    if (!navigatorInitialized) {
      GwtUtils.deferredExecution(new Delegate<Void>() {
        public void execute(Void element) {
          setAfterPagePushHandler();
        }
      });
    } else {
      navigator.onAfterPagePush(new Delegate<NavigatorEvent>() {
        public void execute(NavigatorEvent event) {
          PhgUtils.log("------------------------------------");
          PhgUtils.log("AFTER PUSH PAGE");
          Page enteringPage = event.getEnterPage();
          if (enteringPage != null) {
            String enteringPageName = enteringPage.getName();
            PhgUtils.log("ENTERING PAGE = " + enteringPageName);
            PhgUtils.log("setting current view in entering page");
            enteringPage.setView(OnsenUi.getCurrentView());
            onShowPage("ENTERING PAGE", enteringPage, false);
          }
          navigator.log("NAVIGATOR PAGES");
          PhgUtils.log("------------------------------------");
        }
      });
      navigator.onAfterPagePop(new Delegate<NavigatorEvent>() {
        public void execute(NavigatorEvent event) {
          PhgUtils.log("------------------------------------");
          PhgUtils.log("AFTER POP PAGE");
          Page enteringPage = event.getEnterPage();
          if (enteringPage != null) {
            String enteringPageName = enteringPage.getName();
            PhgUtils.log("ENTERING PAGE = " + enteringPageName);
            onShowPage("ENTERING PAGE", enteringPage, false);
          }
          navigator.log("NAVIGATOR PAGE");
          PhgUtils.log("------------------------------------");
        }
      });
      
      slidingMenu.getController().onMenuClose(new Delegate<SlidingMenuEvent>() {
        public void execute(SlidingMenuEvent element) {
          PhgUtils.log("------------------------------------");
          PhgUtils.log("AFTER CLOSE MENU");
          Page currentPage = navigator.getCurrentPage();
          if (currentPage != null) {
            String currentPageName = currentPage.getName();
            PhgUtils.log("ENTERING PAGE = " + currentPageName);
            onShowPage("ENTERING PAGE", currentPage, true);
          }
          PhgUtils.log("------------------------------------");
        }
      });
      
    }
  }
  
  private void onShowPage(String logMsg, Page page, boolean tryCurrentView) {
    PhgUtils.log("onShowPage");
    String pageName = page.getName();
    PhgUtils.log(logMsg+" NAME = " + pageName);
    AbstractBaseView view = page.getView();
    if (view != null) {
      PhgUtils.log(logMsg+" VIEW CLASS = " + view.getClass());
      view.onShowView();
    } else {
      PhgUtils.log(logMsg+" VIEW IS NULL");
      if (tryCurrentView) {
        PhgUtils.log("trying current view");
        AbstractBaseView currentView = OnsenUi.getCurrentView();
        if (currentView != null) {
          currentView.onShowView();
        }
      }
    }
  }
  
  // TODO [ONS2]
  protected void setBeforePagePopHandler() {
    if (!navigatorInitialized) {
      GwtUtils.deferredExecution(new Delegate<Void>() {
        public void execute(Void element) {
          setBeforePagePopHandler();
        }
      });
    } else {
      navigator.onBeforePagePush(new Delegate<NavigatorEvent>() {
        public void execute(NavigatorEvent event) {
          checkAutoRefreshHome(event, null);
        }
      });
      navigator.onBeforePagePop(new Delegate<NavigatorEvent>() {
        public void execute(NavigatorEvent event) {
          checkAutoRefreshHome(event, null);
          if (allowPagePoping) {
            allowPagePoping = allowNavigatorPoping;
            PhgUtils.log("CONTINUE POPING");
            navigator.log("BEFORE POPING");
            lastProcessedPlace = null;
            String popinPageId = getPopinPageId(event);
            if (popinPageId != null && popinPageId.trim().length() > 0) {
              OnsenUi.setCurrentPageId(popinPageId);
            }
          } else {
            int index = navigator.getCurrentPage().getIndex() - 1;
            Page prevPage = navigator.getPages().get(index);
            String prevPageName = prevPage.getName();
            PhgUtils.log("PREV PAGE NAME = " + prevPageName);
            PhgUtils.log("DESTROYING PAGE " + prevPage);
            if (OnsenUi.isVersion1()) {
              prevPage.destroy();
            }
            PhgUtils.log("CANCELING POP EVENT");
            event.cancel();
            navigator.log("AFTER DESTROY PAGE");
            Place prevPlace = getPlace(prevPageName);
            PhgUtils.log("GOING TO PLACE " + prevPlace);
            lastProcessedPlace = null;
            eventBus.fireEvent(new OnsPlaceChangeEvent(prevPlace, index));
          }
        }
      });
    }
  }
  
  
  private String getPopinPageId(NavigatorEvent event) {
    
    Navigator navigator = GwtUtils.getJsPropertyJso(event, "navigator").cast();
    Page currentPage = GwtUtils.getJsPropertyJso(event, "currentPage").cast();
    String currentPageName = currentPage.getName();
    Page popinPage = null;
    JsArray<Page> pages = navigator.getPages();
    for (int it = 0; it < pages.length(); it++) {
      Page page = pages.get(it);
      if (page.getName().equalsIgnoreCase(currentPageName) && it > 0) {
        popinPage = pages.get(it - 1);
        break;
      }
    }

    Element popinPageElement = popinPage.getPageElement();
    String popinPageId = popinPageElement.getId();
    if (popinPageElement.getId() == null || popinPageElement.getId().trim().length() == 0) {
      NodeList<Element> innerPages = popinPageElement.getElementsByTagName("ons-page");
      if (innerPages != null) {
        for (int it = 0; it < innerPages.getLength(); it++) {
          Element innerPage = innerPages.getItem(it);
          if (innerPage.getId() != null && innerPage.getId().trim().length() > 0) {
            popinPageId = innerPage.getId();
          }
        }
      }
    }
    
    return popinPageId;
  }
  
  //TODO
  
  private void setNavigator(final OnsNavigator onsNavigator) {
    PhgUtils.log("setNavigator#1");
    OnsenUi.onAvailableElement(onsNavigator, new Delegate<Element>() {
      public void execute(Element navigatorElement) {
        PhgUtils.log("setNavigator#2");
        OnsenUi.compileElement(navigatorElement);
        PhgUtils.log("setNavigator#3");
        GwtUtils.deferredExecution(new Delegate<Void>() {
          public void execute(Void element) {
            PhgUtils.log("setNavigator#4");
            Navigator navigator = onsNavigator.getController();
            if (navigator == null) {
              PhgUtils.log("setNavigator#5");
              setNavigator(onsNavigator);
            } else {
              PhgUtils.log("setNavigator#6");
              OnsActivityManagerWithSlidingNavigator.this.navigator = navigator;
              OnsenUi.setNavigator(navigator);
              navigatorInitialized = true;
              onNavigatorInitialized(navigator);
            }
          }
        });
      }
    });
  }
  
  private void setSlidingMenu(OnsSlidingMenu slidingMenu) {
    this.slidingMenu = slidingMenu;
    OnsenUi.onAvailableElement(slidingMenu, new Delegate<Element>() {
      public void execute(Element element) {
        OnsenUi.setSlidingMenu(OnsActivityManagerWithSlidingNavigator.this.slidingMenu.getController());
      }
    });
  }
  
  public static void setAllowNavigatorPoping(boolean allowNavigatorPoping) {
    OnsActivityManagerWithSlidingNavigator.allowNavigatorPoping = allowNavigatorPoping;
  }
  
  private void checkAutoRefreshHome(NavigatorEvent event, String newPageName) {
    if (!enableCheckAutoRefreshHome) {
      return;
    }
    if (!OsDetectionUtils.isAndroid()) {
      return;
    }
    boolean enteringHome = false;
    if ("home".equalsIgnoreCase(newPageName)) {
      enteringHome = true;
    } else if (event != null) {
      Page enteringPage = event.getEnterPage();
      if (enteringPage != null) {
        String enteringPageName = enteringPage.getName();
        if (enteringPageName.equalsIgnoreCase("home")) {
          enteringHome = true;
        }
      } else {
        if (navigator.getCurrentPage() != null) {
          int index = navigator.getCurrentPage().getIndex() - 1;
          if (index >= 0) {
            Page prevPage = navigator.getPages().get(index);
            String prevPageName = prevPage.getName();
            if (prevPageName.equalsIgnoreCase("home")) {
              enteringHome = true;
            }
          }
        }
      }
    }
    if (enteringHome) {
      PhgUtils.log(">>>>>>>>>>>>>>>>> ENTERING HOME");
      if (lastAutoRefreshHomeTime == -1) {
        lastAutoRefreshHomeTime = System.currentTimeMillis();
      }
      long currentTime = System.currentTimeMillis();
      PhgUtils.log(">>>>>>>>>>>>>>>>> CHECK AUTO REFRESH HOME ct = " + currentTime + " lr = " + lastAutoRefreshHomeTime);
      if (currentTime > (lastAutoRefreshHomeTime + 60000)) {
        PhgUtils.log("AUTO RELOADING APP.....................................");
        GwtUtils.deferredExecution(new Delegate<Void>() {
          public void execute(Void element) {
//          PhgUtils.reloadAppHome();
            reloadAppHome();
          }
        });
      }
    }
  }
  
  private static void reloadAppHome() {
    String url = Window.Location.getHref();
    if (url.contains("#")) {
      int pos = url.indexOf("#");
      url = url.substring(0, pos);
    }
    reloadAppImpl(url);
    PhgUtils.log(">>>>>>>>>>>>>>>>> LAUNCHED NAVIGATOR.APP.RELOAD URL " + url);
  }

  private static native void reloadAppImpl(String url) /*-{
    $wnd.navigator.app.loadUrl(url);
  }-*/;
  
  public static void setEnableCheckAutoRefreshHome(boolean enableCheckAutoRefreshHome) {
    OnsActivityManagerWithSlidingNavigator.enableCheckAutoRefreshHome = enableCheckAutoRefreshHome;
  }
  
}
