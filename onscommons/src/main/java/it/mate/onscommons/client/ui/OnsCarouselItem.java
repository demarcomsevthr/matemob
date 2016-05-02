package it.mate.onscommons.client.ui;

import it.mate.onscommons.client.event.HasTapHandler;
import it.mate.onscommons.client.event.NativeGestureEvent;
import it.mate.onscommons.client.event.NativeGestureHandler;
import it.mate.onscommons.client.event.TapEvent;
import it.mate.onscommons.client.event.TapHandler;
import it.mate.onscommons.client.event.OnsEventUtils;
import it.mate.onscommons.client.onsen.OnsenUi;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;


public class OnsCarouselItem extends HTMLPanel implements HasTapHandler, HasModel {
  
  private static final boolean SUSPEND_TAP_HANDLER_DURING_DRAG_OPERATIONS = true;
  
  private static final int TAP_DELAY = 600;
  
  private HasTapHandlerImpl hasTapHandlerImpl;
  
  private HandlerRegistration dragStartHandlerReg;
  
  private HandlerRegistration dragOverHandlerReg;
  
  private HandlerRegistration mouseMoveHandlerReg;
  
  private boolean duringDragOperation = false;
  
  private Object model;
  
  private long lastMovementTime = -1;
  
  public OnsCarouselItem() {
    this("");
  }

  public OnsCarouselItem(Widget widget) {
    this("");
    add(widget);
  }

  public OnsCarouselItem(String html) {
    super("ons-carousel-item", html);
    OnsenUi.ensureId(getElement());
    hasTapHandlerImpl = new HasTapHandlerImpl(this);
  }

  @Override
  public void add(Widget widget) {
    super.add(widget, getElement());
  }

  @Override
  public HandlerRegistration addTapHandler(final TapHandler handler) {

    if (SUSPEND_TAP_HANDLER_DURING_DRAG_OPERATIONS) {
      if (dragStartHandlerReg == null) {
        dragStartHandlerReg = OnsEventUtils.addDragStartHandler(getElement(), new NativeGestureHandler() {
          public void on(NativeGestureEvent event) {
            setLastMovementTime(System.currentTimeMillis());
          }
        });
      }
    }
    
    return hasTapHandlerImpl.addTapHandler(new TapHandler() {
      public void onTap(TapEvent event) {
        if (duringDragOperation) {
          duringDragOperation = false;
          return;
        }
        if (lastMovementTime > -1) {
          if (System.currentTimeMillis() < lastMovementTime + TAP_DELAY) {
            return;
          }
        }
        handler.onTap(event);
      }
    });
    
  }
  
  public void setModifier(String modifier) {
    getElement().setAttribute("modifier", modifier);
  }

  public Object getModel() {
    return model;
  }

  public void setModel(Object model) {
    this.model = model;
  }
  
  protected void setLastMovementTime(long lastMovementTime) {
    this.lastMovementTime = lastMovementTime;
  }
  
}
