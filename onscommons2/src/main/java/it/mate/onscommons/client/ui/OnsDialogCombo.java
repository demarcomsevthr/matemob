package it.mate.onscommons.client.ui;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.gwtcommons.client.utils.StringUtils;
import it.mate.onscommons.client.event.NativeGestureEvent;
import it.mate.onscommons.client.event.NativeGestureHandler;
import it.mate.onscommons.client.event.OnsEventUtils;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.utils.OnsDialogUtils;
import it.mate.phgcommons.client.utils.OsDetectionUtils;
import it.mate.phgcommons.client.utils.PhgUtils;
import it.mate.phgcommons.client.utils.callbacks.JSOCallback;

import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;

public abstract class OnsDialogCombo {
  
  private OnsDialog dialog;
  
  private String carouselId;
  
  private int carouselActiveIndex;
  
  private int currentItemIndex = -1;
  
  private int itemCount;
  
  private ItemDelegate itemDelegate;
  
  private List<Item> items = null;
  
  private int loadedItemsBefore = 10; 
  
  private int loadedItemsAfter = 20; 
  
  private boolean lazyLoading = false;
  
  public static interface ItemDelegate {
    public Item getItem(int index);
  }
  
  public static class Item {
    private SafeHtml html;
    private String value;
    public Item(SafeHtml html, String value) {
      this.html = html;
      this.value = value;
    }
    public SafeHtml getHtml() {
      return html;
    }
    public String getValue() {
      return value;
    }
  }

  public OnsDialogCombo(List<Item> items) {
    this(items, items.size(), null, 0, items.size());
  }
  
  public OnsDialogCombo(int itemCount, ItemDelegate itemDelegate) {
    this(null, itemCount, itemDelegate, 0, itemCount);
  }
  
  public OnsDialogCombo(int loadedItemsBefore, int loadedItemsAfter, ItemDelegate itemDelegate) {
    this(null, (loadedItemsBefore + loadedItemsAfter), itemDelegate, loadedItemsBefore, loadedItemsAfter);
  }
  
  protected void setLazyLoading(boolean lazyLoading) {
    this.lazyLoading = lazyLoading;
  }
  
  protected void setInitialIndex(int initialIndex) {
    this.currentItemIndex = initialIndex;
  }
  
  protected OnsDialogCombo(List<Item> items, int itemCount, ItemDelegate itemDelegate, int loadedItemsBefore, int loadedItemsAfter) {
    this.items = items;
    this.itemCount = itemCount;
    this.itemDelegate = itemDelegate;
    if (loadedItemsBefore > -1) {
      this.loadedItemsBefore = loadedItemsBefore; 
    } else {
      this.loadedItemsBefore = itemCount / 2; 
    }
    if (loadedItemsAfter > -1) {
      this.loadedItemsAfter = loadedItemsAfter; 
    } else {
      this.loadedItemsAfter = itemCount / 2; 
    }
    createDialog();
  }
  
  protected OnsDialogCombo() { }
  
  public abstract void onItemSelected(Item item);
  
  private void createDialog() {
    GwtUtils.deferredExecution(100, new Delegate<Void>() {
      public void execute(Void element) {
        carouselId = OnsenUi.createUniqueElementId();
        String html = "<ons-page>";
        String itemHeight="12%";
        if (OsDetectionUtils.isTabletPortrait()) {
          itemHeight = "8%";
        }
        html += "<ons-carousel id='"+carouselId+"' var='onsComboDialogCarousel' direction='vertical' class='ons-combo-dialog-carousel' item-height='"+itemHeight+"' swipeable>";
        // il primo item vuoto serve per And4.2
        html += "<ons-carousel-item>";
        html += "</ons-carousel-item>";
        html += "</ons-carousel>";
        html += "</ons-page>";
        dialog = OnsDialogUtils.createDialog(html, true, null, "ons-combo-dialog");
        populateCarousel();
      }
    });
  }
  
  public static String getDialogHeight() {
    int height = Window.getClientHeight() - Window.getClientHeight() / 6;
    return ""+height+"px";
  }

  public static String getDialogWidth() {
    int width = 280;
    if (OsDetectionUtils.isTabletPortrait()) {
      width = 400;
    }
    return ""+width+"px";
  }

  private void populateCarousel() {
    OnsenUi.onAvailableElement(carouselId, new Delegate<Element>() {
      public void execute(Element carouselElement) {
        carouselElement.setInnerHTML("");
        if (items != null) {
          for (int index = 0; index < items.size(); index++) {
            Item item = items.get(index);
            carouselElement.appendChild(createCarouselItemElement(item, index));
          }
        } else {
          for (int index = 0; index < itemCount; index++) {
            Item item = itemDelegate.getItem(index - loadedItemsBefore);
            carouselElement.appendChild(createCarouselItemElement(item, null));
          }
        }
        GwtUtils.deferredExecution(100, new Delegate<Void>() {
          public void execute(Void element) {
            if (currentItemIndex < 0) {
              currentItemIndex = 0;
              setCarouselActiveIndex(loadedItemsBefore);
            } else {
              setCarouselActiveIndex(currentItemIndex);
            }
            refreshCarousel();
            if (lazyLoading) {
              setOnPostchange(new JSOCallback() {
                public void handle(JavaScriptObject event) {
                  onPostchange(event);
                }
              });
            }
            dialog.applyAntiFontBlurCorrection();
          }
        });
      }
    });
  }
  
  private Element createCarouselItemElement(Item item, Integer index) {
    String id = OnsenUi.createUniqueElementId();
    Element carouselItemElem = DOM.createElement("ons-carousel-item");
    carouselItemElem.setId(id);
    carouselItemElem.setAttribute("data-value", item.getValue());
    if (index != null) {
      carouselItemElem.setAttribute("data-item-index", ""+index);
    }
    carouselItemElem.setInnerHTML(item.getHtml().asString());
    OnsenUi.onAvailableElement(id, new Delegate<Element>() {
      public void execute(Element carouselItemElement) {
        OnsEventUtils.addTouchStartHandler(carouselItemElement, new NativeGestureHandler() {
          public void on(NativeGestureEvent event) {
            event.getTarget().setPropertyInt("lastX", event.getTarget().getAbsoluteLeft());
            event.getTarget().setPropertyInt("lastY", event.getTarget().getAbsoluteTop());
          }
        });
        OnsEventUtils.addTouchEndHandler(carouselItemElement, new NativeGestureHandler() {
          public void on(NativeGestureEvent event) {
            int lastX = event.getTarget().getPropertyInt("lastX");
            int lastY = event.getTarget().getPropertyInt("lastY");
            if (lastX == event.getTarget().getAbsoluteLeft() && lastY == event.getTarget().getAbsoluteTop()) {
              String dataValue = event.getTarget().getAttribute("data-value");
              String dataItemIndex = event.getTarget().getAttribute("data-item-index");
              PhgUtils.log("SELECTED ITEM: " + event.getTarget());
              PhgUtils.log("SELECTED ITEM DATA-VALUE: " + dataValue);
              PhgUtils.log("SELECTED ITEM INDEX: " + dataItemIndex);
              dialog.hide();
              if (StringUtils.isNumber(dataItemIndex) && items != null) {
                int index = Integer.parseInt(dataItemIndex);
                if (index < items.size()) {
                  onItemSelected(items.get(index));
                } else {
                  onItemSelected(new Item(SafeHtmlUtils.fromString(event.getTarget().getInnerHTML()), dataValue));
                }
              } else {
                onItemSelected(new Item(SafeHtmlUtils.fromString(event.getTarget().getInnerHTML()), dataValue));
              }
            }
          }
        });
      }
    });
    return carouselItemElem;
  }
  
  private void onPostchange(JavaScriptObject event) {
    PhgUtils.log("OnPostchange: event {"+
        " carousel="+ GwtUtils.getJsPropertyObject(event, "carousel") +
        ", activeIndex="+ GwtUtils.getJsPropertyInt(event, "activeIndex") +
        ", lastActiveIndex="+ GwtUtils.getJsPropertyInt(event, "lastActiveIndex") +
        ", carouselActiveIndex="+ carouselActiveIndex
        +"}");
    final int newActiveIndex = GwtUtils.getJsPropertyInt(event, "activeIndex");
    if (newActiveIndex != carouselActiveIndex) {
      OnsenUi.onAvailableElement(carouselId, new Delegate<Element>() {
        public void execute(Element carouselElement) {
          if (newActiveIndex < carouselActiveIndex) {
            int diff = carouselActiveIndex - newActiveIndex;
            for (int it = 0; it < diff; it++) {
              currentItemIndex -= 1;
              Item item = itemDelegate.getItem(currentItemIndex - loadedItemsBefore);
              carouselElement.insertFirst(createCarouselItemElement(item, null));
              carouselElement.getLastChild().removeFromParent();
            }
          } else if (newActiveIndex > carouselActiveIndex) {
            int diff = newActiveIndex - carouselActiveIndex;
            for (int it = 0; it < diff; it++) {
              currentItemIndex += 1;
              Item item = itemDelegate.getItem(currentItemIndex + loadedItemsAfter - 1);
              carouselElement.appendChild(createCarouselItemElement(item, null));
              carouselElement.getFirstChild().removeFromParent();
            }
          }
          setCarouselActiveIndex(loadedItemsBefore);
          refreshCarousel();
        }
      });
    }
  }
  
  protected final native void refreshCarousel() /*-{
    $wnd.onsComboDialogCarousel.refresh();
  }-*/;
  
  protected final native void setOnPostchange(JSOCallback onPostchange) /*-{
    var jsOnPostchange = $entry(function(e) {
      onPostchange.@it.mate.phgcommons.client.utils.callbacks.JSOCallback::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(e);
    });
    $wnd.onsComboDialogCarousel.on("postchange", jsOnPostchange);
  }-*/;
  
  protected void setCarouselActiveIndex(int index) {
    carouselActiveIndex = index;
    _setActiveIndex(carouselActiveIndex);
  }
    
  protected final native void _setActiveIndex(int index) /*-{
    @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)("setting active index = " + index);
    $wnd.onsComboDialogCarousel.setActiveCarouselItemIndex(index);
  }-*/;
  
}
