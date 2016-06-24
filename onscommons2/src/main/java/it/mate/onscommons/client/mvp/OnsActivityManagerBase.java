package it.mate.onscommons.client.mvp;

import it.mate.gwtcommons.client.places.HasToken;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.onsen.OnsenReadyHandler;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.onsen.dom.Page;
import it.mate.onscommons.client.ui.OnsTemplate;
import it.mate.phgcommons.client.utils.PhgUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.dom.client.Element;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

public abstract class OnsActivityManagerBase extends ActivityManager {

  protected EventBus eventBus;

  private static Panel activePanel;
  
  private static String activePanelId = "";
  
  private List<OnsTemplate> templates;
  
  private Map<String, Place> placesMap = new HashMap<String, Place>();
  
  public static OnsActivityManagerBase IMPL;

  protected HasToken lastProcessedPlace;
  
  protected HasToken activePlace;
  
  public OnsActivityManagerBase(ActivityMapper mapper, EventBus eventBus) {
    super(mapper, eventBus);
    this.eventBus = eventBus;
    if (!OnsenUi.isInitialized()) {
      OnsenUi.initializeOnsen(new OnsenReadyHandler() {
        public void onReady() {
          PhgUtils.log("ONSEN READY");
        }
      });
    }
    setAfterPagePushHandler();
    setBeforePagePopHandler();
    setDisplay(new SimplePanel());
    IMPL = this;
  }

  protected abstract void setAfterPagePushHandler();
  
  protected abstract void setBeforePagePopHandler();
  
  protected abstract Page getCurrentPage();
  
  public static Panel getActivePanel() {
    return activePanel;
  }

  protected static void setActivePanel(Panel panel, String id) {
    activePanel = panel;
    activePanelId = id;
  }
  
  protected boolean setActivePanelFromTemplate(Place newPlace) {
    boolean preventPush = false;
    HasToken hasToken = (HasToken)newPlace;
    this.activePlace = hasToken;
    String newToken = hasToken.getToken();
    if (getCurrentPage() != null && newToken.equals(getCurrentPage().getName())) {
      setActivePanelFromCurrentPage(newPlace);
      preventPush = true;
    } else {
      OnsTemplate template = getTemplateByPlace(hasToken);
      template.clear();
      setActivePanel(template, newToken);
    }
    return preventPush;
  }
  
  protected OnsTemplate getTemplateByPlace(HasToken place) {
    if (templates == null) {
      templates = new ArrayList<OnsTemplate>();
    }
    String token = place.getToken();
    for (OnsTemplate template : templates) {
      if (template.getToken().equals(token)) {
        return template;
      }
    }
    OnsTemplate template = new OnsTemplate(token);
    templates.add(template);
    return template;
  }
  
  private void setActivePanelFromCurrentPage(Place newPlace) {
    HasToken hasToken = (HasToken)newPlace;
    String newToken = hasToken.getToken();
    if (getCurrentPage() == null) {
      return;
    }
    Element innerElem = getCurrentPage().getInnerPageElement();
    ElementWrapperPanel wrapper = new ElementWrapperPanel(innerElem);
    String currentPageName = getCurrentPage().getName();
    PhgUtils.log("WRAPPING CURRENT PAGE " + currentPageName);
    setActivePanel(wrapper, newToken);
  }
  
  public static class ElementWrapperPanel extends ComplexPanel implements AcceptsOneWidget {
    protected ElementWrapperPanel(Element elem) {
      setElement(elem);
    }
    @Override
    public void setWidget(IsWidget w) {
      add(w);
    }
    @Override
    public void add(Widget child) {
      add(child, getElement());
    }
  }
  
  protected Element getActivePanelChildElement() {
    Element activePanelChildElement = null;
    if (activePanel != null && activePanel.getElement() != null && activePanel.getElement().getChildCount() > 0) {
      activePanelChildElement = (Element)activePanel.getElement().getChild(0);
    }
    return activePanelChildElement;
  }
  
  private String activePanelInnerHtml = null;
  
  protected String getActivePanelInnerHtml() {
    return activePanelInnerHtml;
  }
  
  // TODO [ONS2]
  protected Element compileActivePanel() {
    
    Element activePanelElement = activePanel.getElement();
    
    PhgUtils.log("compileActivePanel#1 activePanelId=" + activePanelId);
    PhgUtils.log("compileActivePanel#1 activePanel=" + activePanel);
    
    if (OnsenUi.isVersion2()) {
      
      String activePanelHTML = GwtUtils.getOuterHtml(activePanel.getElement());
      activePanelInnerHtml = activePanel.getElement().getInnerHTML();
      OnsenUi.setCacheTemplate(activePanelId, activePanelHTML);
      
    } else {
      if (!PhgUtils.isReallyAttached(activePanelId)) {
        try {
          RootPanel.get().remove(activePanel);
        } catch (Exception ex) { }
        PhgUtils.log("ADDING PANEL TO DOCUMENT - " + activePanel.getElement());
        RootPanel.get().add(activePanel);
      }
      if (PhgUtils.isReallyAttached(activePanelId)) {
        Element templateElem = activePanel.getElement();
        if (templateElem != null) {
          OnsenUi.compileElement(templateElem);
        }
      }
    }
    
    return activePanelElement;
    
  }
  
  protected void putPlace(Place place) {
    HasToken hasToken = (HasToken)place;
    String token = hasToken.getToken();
    placesMap.put(token, place);
  }
  
  protected Place getPlace(String token) {
    return placesMap.get(token);
  }
  
  public void setLastProcessedPlace(HasToken lastProcessedPlace) {
    this.lastProcessedPlace = lastProcessedPlace;
  }
  
  public HasToken getActivePlace() {
    return activePlace;
  }
  
}
