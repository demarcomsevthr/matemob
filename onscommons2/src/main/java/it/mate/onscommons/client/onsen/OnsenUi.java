package it.mate.onscommons.client.onsen;

import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.gwtcommons.client.utils.ObjectWrapper;
import it.mate.onscommons.client.event.OnsPlaceChangeEvent;
import it.mate.onscommons.client.event.TapHandler;
import it.mate.onscommons.client.onsen.dom.Navigator;
import it.mate.onscommons.client.onsen.dom.SlidingMenu;
import it.mate.onscommons.client.ui.HasTapHandlerImpl;
import it.mate.onscommons.client.ui.OnsList;
import it.mate.onscommons.client.ui.OnsNavigator;
import it.mate.onscommons.client.ui.OnsPage;
import it.mate.onscommons.client.ui.OnsSlidingMenu;
import it.mate.onscommons.client.utils.IOSPatches;
import it.mate.onscommons.client.utils.TransitionUtils;
import it.mate.phgcommons.client.place.PlaceControllerWithHistory;
import it.mate.phgcommons.client.utils.OsDetectionUtils;
import it.mate.phgcommons.client.utils.PhgUtils;
import it.mate.phgcommons.client.utils.callbacks.ElementCallback;
import it.mate.phgcommons.client.utils.callbacks.JSOCallback;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.Widget;


@SuppressWarnings("rawtypes")
public class OnsenUi {
  
  // TODO [ONS2]
  private static String onsenVersion = null;
  
  private static boolean doLog = true;
  
  private static boolean initialized = false;
  
  private static List<Delegate<Void>> initializationHandlers = new ArrayList<Delegate<Void>>();
  
  private static Navigator navigator;
  
  private static SlidingMenu slidingMenu;
  
  public final static String ANIMATION_SLIDE = "slide";
  
  public final static String ANIMATION_REVERSE_SLIDE = "reverseSlide";
  
  public final static String ANIMATION_LIFT = "lift";
  
  public final static String ANIMATION_FADE = "fade";
  
  public final static String ANIMATION_NONE = "none";
  
  public final static String ANIMATION_REVEAL = "reveal";
  
  public final static String ANIMATION_PUSH = "push";
  
  public final static String ANIMATION_NATIVE_PUSH = "nativePush";
  
  public final static String ANIMATION_NATIVE_POP = "nativePop";
  
  private static final int REFRESH_CURRENT_PAGE_DELAY = 0;
  
  private static boolean compilationSuspended;
  
  private static long lastElementCompilationTime = -1;
  
  protected static JavaScriptObject jQueryDocumentSelector = null;
  
  private static int uniqueElementId = 0;
  
  private static int fadeinDuration = 500;
  
  private static boolean removeToolbarDuringPageRefresh = true; 
  
  private static boolean preventTapHandlerWherScrollerMoves = false;
  
  public static final String EXCLUDE_FROM_PAGE_REFRESH_ATTR = "excludeFromPageRefresh";
  
  //private static boolean addDirectWithPlainHtml = false;
  
  private static boolean usePlaceControllerHistory = false;
  
  private static boolean checkReconfigurableAppModule;
  
  private static AbstractBaseView currentView;
  
  private static String currentPageId = null;
  
  private static boolean compilationSuspendedLogged = false;
  
  private final static String ELEMENT_COMPILED_ATTR = "_onsenui_element_compiled";
  
  public static void initializeOnsen(OnsenReadyHandler handler) {
    if (!initialized) {
      
      PhgUtils.log("Initializing onsen version " + onsenVersion);
      
      if (OnsenUi.isVersion2()) {
        removeToolbarDuringPageRefresh = false;
      }
      
      initialized = true;
      initOnsenImpl(handler, checkReconfigurableAppModule);
      
      for (Delegate<Void> initHandler : initializationHandlers) {
        initHandler.execute(null);
      }
      ensureJQueryDocumentSelector();
      
      if (!OsDetectionUtils.isDesktop()) {
        setPreventTapHandlerWherScrollerMoves(true);
      }
      
      if (OsDetectionUtils.isIOs()) {
        PhgUtils.log("APPLYING IOS7 HEADER PATCH");
        IOSPatches.applyIOS7HeaderBarPatch();
      }
     
    }
  }
  
  // TODO [ONS2]
  protected static native void initOnsenImpl(OnsenReadyHandler handler, boolean checkReconfigurableAppModule) /*-{

    @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)('ONSEN INIT IMPL');
    
    if (typeof $wnd.ons == "undefined") {
      @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)('WINDOW.ONS IS UNDEFINED!');
    }
      
    if (checkReconfigurableAppModule) {
      if ($wnd.ons.isReady() || $wnd.ngReconfigurableAppModule !== undefined) {
        @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)('ONSEN ALREADY BOOTSTRAPPED - CONTINUE');
        handler.@it.mate.onscommons.client.onsen.OnsenReadyHandler::onReady()();
        return;
      }
    }    

    // TODO [ONS2]
    if (typeof $wnd.ons.bootstrap !== "undefined") {
      @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)('CALLING ONSEN BOOTSTRAP');
      $wnd.ons.bootstrap();
    }
    
    @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)('CREATING ONSEN READY HANDLER');
    var jsHandler = $entry(function() {
      @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)('ONSEN READY HANDLER');
      handler.@it.mate.onscommons.client.onsen.OnsenReadyHandler::onReady()();
    });
    
    
    if (typeof $wnd._onsenui_readyHandler !== "undefined") {
      @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)('SETTING ONSEN READY USING GLOBAL CALLBACK');
      $wnd._onsenui_readyHandler_callback = jsHandler;
      $wnd.ons.ready($wnd._onsenui_readyHandler);
    } else {
      @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)('SETTING ONSEN READY USING INLINE CALLBACK');
      $wnd.ons.ready(jsHandler);
    }
    
  }-*/;
  
  public static void addInitializationHandler(Delegate<Void> handler) {
    initializationHandlers.add(handler);
  }
  
  public static boolean isInitialized() {
    return initialized;
  }
  
  public static void setCheckReconfigurableAppModule(boolean checkReconfigurableAppModule) {
    OnsenUi.checkReconfigurableAppModule = checkReconfigurableAppModule;
  }
  
  public static Navigator getNavigator() {
    if (navigator == null) {
      OnsNavigator navigator = new OnsNavigator();
      navigator.attachToBody();
      OnsenUi.navigator = navigator.getControllerSingleton();
    }
    return navigator;
  }
  
  public static void setNavigator(Navigator navigator) {
    OnsenUi.navigator = navigator;
  }

  public static boolean isNavigatorPresent() {
    return navigator != null;
  }
  
  public static void resetHistory() {
    if (navigator != null) {
      navigator.resetHistory();
    }
  }
  
  public static SlidingMenu getSlidingMenu() {
    if (slidingMenu == null) {
      OnsSlidingMenu slidingMenu = new OnsSlidingMenu();
      slidingMenu.attachToBody();
      OnsenUi.slidingMenu = slidingMenu.getController();
    }
    return slidingMenu;
  }
  
  public static void setSlidingMenu(SlidingMenu slidingMenu) {
    OnsenUi.slidingMenu = slidingMenu;
  }
  
  public static boolean isSlidingMenuPresent() {
    return slidingMenu != null;
  }
  
  public static void suspendCompilations() {
    OnsenUi.compilationSuspended = true;
    HasTapHandlerImpl.setUseDocEventListener(true);
  }
  
  public static void resumeCompilations() {
    if (doLog) PhgUtils.log("RESUME COMPILATION");
    OnsenUi.compilationSuspended = false;
    compilationSuspendedLogged = false;
  }

  public static void compileElement(Element element) {
    lastElementCompilationTime = System.currentTimeMillis();
    if (compilationSuspended) {
      if (!compilationSuspendedLogged) {
        compilationSuspendedLogged = true;
        if (doLog) PhgUtils.log("COMPILATION DISABLED");
      }
      return;
    }
    compileElement_(element);
  }
  
  public static void compileElementImmediately(Element element) {
    compileElement_(element);
  }
  
  private static void compileElement_(Element element) {
    if (element == null) {
      return;
    }
    if (element.getNodeName() == null) {
      return;
    }
//  element.setAttribute("_compiled", "false");
    removeElementAttribute(element, "_compiled");
    if (doLog) PhgUtils.log("COMPILING ELEMENT " + element);
    compileElementImpl(element);
    element.setAttribute(ELEMENT_COMPILED_ATTR, "true");
  }
  
  public static void initializeElementCompileAttr(Element element) {
    element.setAttribute(ELEMENT_COMPILED_ATTR, "false");
  }
  
  private static native void compileElementImpl(Element element) /*-{
    $wnd.ons.compile(element);
  }-*/;

  private static final native void removeElementAttribute(Element element, String name) /*-{
    element.removeAttribute(name);
  }-*/;
  
  public static void getCurrentPageElement(Delegate<Element> delegate) {
    if (OnsPage.getLastCreatedPage() == null) {
      delegate.execute(null);
    } else {
      OnsenUi.onAvailableElement(OnsPage.getLastCreatedPage().getElement().getId(), delegate);
    }
  }
  
  public static void refreshCurrentPage() {
    refreshCurrentPage(100);
  }
  
  public static void refreshCurrentPage(int delay) {
    refreshCurrentPage(delay, null);
  }
  
  public static void setCurrentPageId(String currentPageId) {
    PhgUtils.log("CURRENT PAGE ID = " + currentPageId);
    OnsenUi.currentPageId = currentPageId;
  }
  
  // TODO: REFRESH CURRENT PAGE OPTIMIZATION
  
  public static void refreshCurrentPage(final int delay, final Delegate<JavaScriptObject> delegate) {
    GwtUtils.deferredExecution(delay, new Delegate<Void>() {
      public void execute(Void element) {
        long currentTime = System.currentTimeMillis();
        if (lastElementCompilationTime > currentTime - REFRESH_CURRENT_PAGE_DELAY) {
          refreshCurrentPage(delay, delegate);
          return;
        }
        
        if (doLog) PhgUtils.log("REFRESH CURRENT PAGE");
        
        if (doLog) PhgUtils.log("LAST CREATED PAGE ID = " + OnsPage.getLastCreatedPage().getElement().getId());

        // 18/05/2015
        String actualCurrentPageId = currentPageId;
        
        if (doLog) PhgUtils.log("FINDING CURRENT PAGE WITH ID " + actualCurrentPageId);
        
        OnsenUi.onAvailableElement(actualCurrentPageId, new Delegate<Element>() {
          public void execute(final Element pageElement) {

            boolean doRefresh = false;
            if ("false".equals(pageElement.getAttribute(ELEMENT_COMPILED_ATTR))) {
              doRefresh = true;
            } else {
              if (doLog) PhgUtils.log("cerco discendenti non compilati");
              List<Element> notCompiledElements = queryElements("#"+pageElement.getId()+" *["+ELEMENT_COMPILED_ATTR+"='false']");
              if (notCompiledElements != null && notCompiledElements.size() > 0) {
                doRefresh = true;
              }
            }
            if (!doRefresh) {
              if (doLog) PhgUtils.log("CURRENT PAGE IS JUST COMPILED OR NOT CONTAINS NOT COMPILED ELEMENTS");
              return;
            }
            
            resumeCompilations();
            
            // [13/05/2015]
            // INTRODOTTA LA SEGUENTE OTTIMIZZAZIONE:
            // PRIMA DI RICOMPILARE LA PAGE RIMUOVO LA TOOLBAR E LA REINSERISCO DOPO LA COMPILAZIONE (!!!)
            
            if (doLog) PhgUtils.log(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            
            final ObjectWrapper<Element> toolbarElement = new ObjectWrapper<Element>();
            
            if (removeToolbarDuringPageRefresh) {
              if (doLog) PhgUtils.log("Rimozione toolbar element durante il refresh della pagina");
              for (int it = 0; it < pageElement.getChildCount(); it++) {
                Element pageChildElement = pageElement.getChild(it).cast();
                if (pageChildElement.getNodeName() != null && pageChildElement.getNodeName().equalsIgnoreCase("ons-toolbar")) {
                  toolbarElement.set(pageChildElement);
                  if (doLog) PhgUtils.log("REMOVING TOOLBAR ELEMENT " + toolbarElement.get());
                  toolbarElement.get().getParentElement().removeChild(toolbarElement.get());
                }
              }
            }
            
            final List<ExcludedElement> excludedElements = new ArrayList<OnsenUi.ExcludedElement>();
            if (doLog) PhgUtils.log("Ricerca elementi esclusi dal refresh");
            findAndSaveExcludedElements(pageElement, excludedElements);
            
            if (doLog) PhgUtils.log("Setting page init callback");
            addInitCallbackImpl(pageElement, new JSOCallback() {
              public void handle(JavaScriptObject event) {
                
                if (doLog) PhgUtils.log("PAGE INIT CALLBACK #" + pageElement.getId());
                
                if (toolbarElement.get() != null) {
                  if (doLog) PhgUtils.log("REINSERTING TOOLBAR ELEMENT " + toolbarElement.get());
                  pageElement.insertFirst(toolbarElement.get());
                }
                
                if (excludedElements != null) {
                  insertExcludedElements(excludedElements);
                }
                
                if (doLog) PhgUtils.log("PAGE IS COMPILED");
                
                fadeInCurrentPage();
                
                if (delegate != null) {
                  delegate.execute(event);
                }
                
              }
            });
            

            PhgUtils.log("COMPILING CURRENT PAGE #" + pageElement.getId());
            
            if (doLog) PhgUtils.log("COMPILING PAGE ELEMENT " + pageElement);
//          compileElementImpl(pageElement);
            compileElement_(pageElement);
            
            if (doLog) PhgUtils.log("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            
            GwtUtils.deferredExecution(500, new Delegate<Void>() {
              public void execute(Void element) {
                if (doLog) PhgUtils.log("Setting use doc event listener false");
                HasTapHandlerImpl.setUseDocEventListener(false);
              }
            });
           
          }
        });
      }
    });
  }
  
  public static void fadeInCurrentPage() {
    // 25/05/2015 - se le compilazioni sono sospese anche il fade in va rimandato (capitava prima in OrderItemEditView)
    if (compilationSuspended) {
      return;
    }
    JavaScriptObject results = queryElementsImpl(".ons-fadein");
    if (results != null) {
      JsArray<Element> elements = results.cast();
      for (int it = 0; it < elements.length(); it++) {
        Element fadingElement = elements.get(it);
        if (doLog) PhgUtils.log("EXECUTING FADEIN ON ELEMENT " + fadingElement);
        TransitionUtils.fadeIn(fadingElement, fadeinDuration);
      }
    }
  }
  
  protected static void ensureJQueryDocumentSelector() {
    if (jQueryDocumentSelector == null) {
      initJQueryDocumentSelector();
    }
  }
  
  protected static native void initJQueryDocumentSelector() /*-{
    @it.mate.onscommons.client.onsen.OnsenUi::jQueryDocumentSelector = $wnd.$($doc);
  }-*/;
  
  protected static native void addInitCallbackImpl(Element element, JSOCallback callback) /*-{
    var jsCallback = $entry(function(event) {
      callback.@it.mate.phgcommons.client.utils.callbacks.JSOSuccess::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(event);
    });
    var elementId = element.id;
    @it.mate.onscommons.client.onsen.OnsenUi::jQueryDocumentSelector.on('ons-page:init', '#'+elementId, jsCallback);
  }-*/;
    
  public static List<Element> queryElements(String queryString) {
    List<Element> results = new ArrayList<Element>();
    JavaScriptObject jsResults = queryElementsImpl(queryString);
    if (jsResults != null) {
      JsArray<Element> elements = jsResults.cast();
      for (int it = 0; it < elements.length(); it++) {
        Element element = elements.get(it);
        results.add(element);
      }
    }
    return results;
  }
  
  protected static native JavaScriptObject queryElementsImpl(String query) /*-{
    return $wnd.$(query);
  }-*/;

  public static void setUsePlaceControllerHistory(boolean usePlaceControllerHistory) {
    OnsenUi.usePlaceControllerHistory = usePlaceControllerHistory;
  }

  public static void goToPreviousPlace(PlaceController placeController, Place initialPlace) {
    if (OnsenUi.isNavigatorPresent() && !usePlaceControllerHistory) {
      if (doLog) PhgUtils.log("GO TO PREVIOUS PLACE USING NAVIGATOR POP");
      OnsenUi.getNavigator().popPage();
    } else {
      if (doLog) PhgUtils.log("GO TO PREVIOUS PLACE USING PLACE CONTROLLER");
      if (placeController instanceof PlaceControllerWithHistory) {
        PlaceControllerWithHistory placeControllerHistory = (PlaceControllerWithHistory)placeController;
        Place prevPlace = placeControllerHistory.getPreviousPlace();
        if (prevPlace == null) {
          prevPlace = initialPlace;
        }
        placeControllerHistory.goToWithEvent(new OnsPlaceChangeEvent(prevPlace).setAnimation(OnsenUi.ANIMATION_NATIVE_POP));
        return;
      } else {
        placeController.goTo(initialPlace);
      }
    }
  }
  
  public static Element getParentPageElement(Element childElement) {
    Element parentElement = childElement.getParentElement();
    while (parentElement != null) {
      if (parentElement.getTagName().equalsIgnoreCase("ons-page")) {
        return parentElement;
      }
      parentElement = parentElement.getParentElement();
    }
    return null;
  }
  
  protected static native void setBeforeAppendChildCallback(ElementCallback callback) /*-{
    var defAppendChild = $wnd.Element.prototype.appendChild;
    $wnd.Element.prototype.appendChild = function(){
      callback.@it.mate.phgcommons.client.utils.callbacks.ElementCallback::handle(Lcom/google/gwt/dom/client/Element;)(arguments[0]);
      defAppendChild.apply(this, arguments);
    };
  }-*/;

  /**
   * METODI DA UTILIZZARE NEI METODI SET DEI WIDGET ONS
   * 
   */
  public static void onAttachedElement(Widget widget, Delegate<Element> delegate) {
    Element attachedElement = GwtUtils.getElementById(widget.getElement().getId());
    if (attachedElement != null) {
      delegate.execute(attachedElement);
    } else {
      delegate.execute(widget.getElement());
    }
  }

  public static void onAvailableElement(Widget widget, Delegate<Element> delegate) {
    onAvailableElement(widget.getElement(), delegate);
  }
  
  public static void onAvailableElement(Element element, Delegate<Element> delegate) {
    onAvailableElement(element.getId(), delegate);
  }

  public static void onAvailableElement(String id, Delegate<Element> delegate) {
    GwtUtils.onAvailable(id, delegate);
  }

  public static String createUniqueElementId() {
    uniqueElementId ++;
    return "ons-uid-" + uniqueElementId;
  }

  public static String ensureId(Element element) {
    if (element == null) {
      return null;
    }
    if (element.getId() == null || "".equals(element.getId())) {
      element.setId(OnsenUi.createUniqueElementId());
    }
    return element.getId();
  }
  
  public static void addTapHandler(String elementId, TapHandler handler) {
    HasTapHandlerImpl impl = new HasTapHandlerImpl(elementId);
    impl.addTapHandler(handler);
  }
  
  public static void addTapHandler(Widget widget, TapHandler handler) {
    HasTapHandlerImpl impl = new HasTapHandlerImpl(widget.getElement());
    impl.addTapHandler(handler);
  }
  
  public static void setFadeinDuration(int fadeinDuration) {
    OnsenUi.fadeinDuration = fadeinDuration;
  }
  
  public static void setPreventTapHandlerWherScrollerMoves(boolean preventTapHandlerWherScrollerMoves) {
    OnsenUi.preventTapHandlerWherScrollerMoves = preventTapHandlerWherScrollerMoves;
  }
  
  public static boolean isPreventTapHandlerWherScrollerMoves() {
    return preventTapHandlerWherScrollerMoves;
  }
  
  private static class ExcludedElement {
    private Element parentElement;
    private Element element;
    private Node nextSibling;
  }
  
  private static void findAndSaveExcludedElements(Element rootElement, List<ExcludedElement> excludedElements) {

    /* [06/07/2016]
     * USO JQUERY
     */
    
    List<Element> foundExcludedElements = queryElements("#"+rootElement.getId()+" *["+EXCLUDE_FROM_PAGE_REFRESH_ATTR+"='true']");
    if (foundExcludedElements != null && foundExcludedElements.size() > 0) {
      for (Element element : foundExcludedElements) {
        PhgUtils.log("FOUND EXCLUDED ELEMENT " + element.getNodeName()+"#"+element.getId());
        ExcludedElement excludedElement = new ExcludedElement();
        excludedElement.element = element;
        excludedElement.parentElement = element.getParentElement();
        excludedElement.nextSibling = element.getNextSibling();
        excludedElements.add(excludedElement);
        element.getParentElement().removeChild(element);
      }
    }
    
    /*
    
    for (int it = 0; it < rootElement.getChildCount(); it++) {
      Element childElement = rootElement.getChild(it).cast();
      if (childElement.getNodeType() != Node.TEXT_NODE) {
        if (childElement.getNodeName() != null && childElement.getAttribute(EXCLUDE_FROM_PAGE_REFRESH_ATTR) != null && childElement.getAttribute(EXCLUDE_FROM_PAGE_REFRESH_ATTR).trim().length() > 0) {
          PhgUtils.log("FOUND EXCLUDED ELEMENT " + childElement.getNodeName()+"#"+childElement.getId());
          ExcludedElement excludedElement = new ExcludedElement();
          excludedElement.element = childElement;
          excludedElement.parentElement = childElement.getParentElement();
          excludedElement.nextSibling = childElement.getNextSibling();
          excludedElements.add(excludedElement);
          childElement.getParentElement().removeChild(childElement);
        } else {
          findAndSaveExcludedElements(childElement, excludedElements);
        }
      }
    }
    
    */
    
  }
  
  private static void insertExcludedElements(List<ExcludedElement> excludedElements) {
    for (ExcludedElement excludedElement : excludedElements) {
      Element childElement = excludedElement.element;
      PhgUtils.log("INSERTING EXCLUDED ELEMENT " + childElement.getNodeName()+"#"+childElement.getId());
      if (excludedElement.nextSibling != null) {
        excludedElement.parentElement.insertBefore(excludedElement.element, excludedElement.nextSibling);
      } else {
        excludedElement.parentElement.appendChild(excludedElement.element);
      }
    }
  }
  
  public static void setDoLog(boolean doLog) {
    OnsenUi.doLog = doLog;
    OnsList.setDoLog(doLog);
  }
  
  public static void setFadein(Widget widget) {
    widget.addStyleName("ons-fadein");
  }
  
  public static void resetFadein(Element element) {
    TransitionUtils.resetFadeIn(element);
    element.addClassName("ons-fadein");
  }
  
  /*
  public static boolean isAddDirectWithPlainHtml() {
    return false;
  }
  */
  
  public static String getPlainHtml(Element element) {
    String html = "";
    
    html += "<" + element.getNodeName().toLowerCase();
    
    boolean foundId = false;
    
    JsArrayString atts = getAttributeNames(element);
    for (int it = 0; it < atts.length(); it++) {
      String name = atts.get(it);
      String value = element.getAttribute(name);
      html += " " + name + "='" + value + "'";
      if ("id".equalsIgnoreCase(name)) {
        foundId = true;
      }
    }
    
    if (!foundId) {
      String id = element.getId();
      if (id != null && id.trim().length() > 0) {
        html += " id='" + id + "'";
      }
    }
    
    html += ">";
    
    String innerHtml = element.getInnerHTML();
    if (innerHtml != null && innerHtml.trim().length() > 0) {
      html += innerHtml;
    } else {
      String innerText = element.getInnerText();
      if (innerText != null && innerText.trim().length() > 0) {
        html += innerText;
      }
    }
    
    html += "</" + element.getNodeName().toLowerCase();
    html += ">";
    
    return html;
  }
  
  private static native JsArrayString getAttributeNames(Element elem) /*-{
    for (var i = 0, atts = elem.attributes, n = atts.length, res = []; i < n; i++){
      res.push(atts[i].nodeName);
    }
    return res;
  }-*/;
  
  public static void appendInnerHtml(Element element, String newHtml) {
    String innerHtml = element.getInnerHTML();
    innerHtml = innerHtml + newHtml;
    element.setInnerHTML(innerHtml);
  }
  
  public static void setCurrentView(AbstractBaseView currentView) {
    OnsenUi.currentView = currentView;
  }
  
  public static AbstractBaseView getCurrentView() {
    return currentView;
  }
  
  public static boolean isCurrentView(AbstractBaseView view) {
    if (OnsenUi.currentView != null) {
      return (OnsenUi.currentView.getClass().getName().equals(view.getClass().getName()));
    }
    return false;
  }
  
  
  /************************************************************************************************
   * 
   *  ONSEN 2 UPGRADE
   * 
   */
  
  // TODO [ONS2]
  protected static String getOnsenVersion() {
    if (onsenVersion == null) {
      onsenVersion = GwtUtils.getJSVar("ONSEN_VERSION", "1.0");
    }
    return onsenVersion;
  }
  
  // TODO [ONS2]
  public static boolean isVersion2() {
    return getOnsenVersion().startsWith("2.");
  }
  
  // TODO [ONS2]
  public static boolean isVersion1() {
    return getOnsenVersion().startsWith("1.");
  }
  
  // TODO [ONS2]
  public static native JavaScriptObject getInternal() /*-{
    @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)('ONSEN2: getInternal');
    return $wnd.ons._internal;
  }-*/;
  
  // TODO [ONS2]
  public static native String getCacheTemplate(String templateId) /*-{
    @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)('ONSEN2: getCacheTemplate ' + templateId);
    return $wnd.ons._internal.templateStore.get(templateId);
  }-*/;
  
  // TODO [ONS2]
  public static native void setCacheTemplate(String templateId, String templateHTML) /*-{
    @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)('ONSEN2: setCacheTemplate ' + templateId);
    return $wnd.ons._internal.templateStore.set(templateId, templateHTML);
  }-*/;
  
}
