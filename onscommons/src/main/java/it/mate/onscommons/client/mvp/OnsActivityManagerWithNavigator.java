package it.mate.onscommons.client.mvp;

import it.mate.gwtcommons.client.places.HasToken;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.event.OnsPlaceChangeEvent;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.onsen.dom.NavigatorEvent;
import it.mate.onscommons.client.onsen.dom.Page;
import it.mate.phgcommons.client.utils.PhgUtils;

import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.dom.client.Element;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.web.bindery.event.shared.EventBus;

public class OnsActivityManagerWithNavigator extends OnsActivityManagerBase {
  
  private boolean allowPagePoping = false;
  
  public OnsActivityManagerWithNavigator(ActivityMapper mapper, EventBus eventBus) {
    super(mapper, eventBus);
  }
  
  @Override
  public void onPlaceChange(PlaceChangeEvent event) {
    Place newPlace = event.getNewPlace();
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
      Element pageElement = OnsenUi.getNavigator().getCurrentPage().getPageElement();
      OnsenUi.compileElement(pageElement);
    }
  }
  
  @Override
  protected Page getCurrentPage() {
    return OnsenUi.getNavigator().getCurrentPage();
  }
  
  private void pushPage(Place newPlace, Integer insertIndex) {
    compileActivePanel();
    HasToken hasToken = (HasToken)newPlace;
    String newToken =  hasToken.getToken();
    putPlace(newPlace);
    boolean pagePushed = false;
    Page currentPage = OnsenUi.getNavigator().getCurrentPage();
    if (currentPage != null) {
      String currentPageName = currentPage.getName();
      if (!newToken.equals(currentPageName)) {
        if (insertIndex != null) {
          OnsenUi.getNavigator().log("BEFORE INSERT PAGE");
          OnsenUi.getNavigator().insertPage(insertIndex, newToken);
          GwtUtils.deferredExecution(new Delegate<Void>() {
            public void execute(Void element) {
              allowPagePoping = true;
              OnsenUi.getNavigator().popPage();
            }
          });
        } else {
          OnsenUi.getNavigator().pushPage(newToken);
        }
        pagePushed = true;
      }
    } else {
      OnsenUi.getNavigator().pushPage(newToken);
      pagePushed = true;
    }
    if (!pagePushed) {
      OnsenUi.getNavigator().resetToPage(newToken);
    }
  }
  
  protected void setAfterPagePushHandler() {
    OnsenUi.getNavigator().onAfterPagePush(new Delegate<NavigatorEvent>() {
      public void execute(NavigatorEvent event) {
        Page enteringPage = event.getEnterPage();
        if (enteringPage != null) {
          String enteringPageName = enteringPage.getName();
          PhgUtils.log("AFTER PUSH PAGE " + enteringPageName);
          OnsenUi.getNavigator().log("NAVIGATOR PAGE");
        }
      }
    });
  }
  
  protected void setBeforePagePopHandler() {
    OnsenUi.getNavigator().onBeforePagePop(new Delegate<NavigatorEvent>() {
      public void execute(NavigatorEvent event) {
        if (allowPagePoping) {
          allowPagePoping = false;
          PhgUtils.log("CONTINUE POPING");
          OnsenUi.getNavigator().log("BEFORE POPING");
          return;
        }
        
        int index = OnsenUi.getNavigator().getCurrentPage().getIndex() - 1;
        Page prevPage = OnsenUi.getNavigator().getPages().get(index);
        String prevPageName = prevPage.getName();
        PhgUtils.log("PREV PAGE NAME = " + prevPageName);
        PhgUtils.log("DESTROYING PAGE " + prevPage);
        prevPage.destroy();

        PhgUtils.log("CANCELING POP EVENT");
        event.cancel();
        OnsenUi.getNavigator().log("AFTER DESTROY PAGE");
        Place prevPlace = getPlace(prevPageName);
        PhgUtils.log("GOING TO PLACE " + prevPlace);
        eventBus.fireEvent(new OnsPlaceChangeEvent(prevPlace, index));
      }
    });
  }
  
}
