package it.mate.onscommons.client.ui;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.event.NativeGestureEvent;
import it.mate.onscommons.client.event.NativeGestureHandler;
import it.mate.onscommons.client.event.OnsEventUtils;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.utils.TransitionUtils;
import it.mate.phgcommons.client.utils.PhgUtils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class OnsScroller extends HTMLPanel {

  private final static String TAG_NAME = "ons-scroller";
  
  private boolean compiled = false;
  
  private boolean doLog = false;
  
  private Delegate<Element> onScrollerAvailableDelegate = null;
  
  public OnsScroller() {
    this(TAG_NAME, "");
  }
  
  public OnsScroller(String html) {
    this(TAG_NAME, html);
  }
  
  protected OnsScroller(String tag, String html) {
    super(tag, html);
    getElement().addClassName(TAG_NAME);
    OnsenUi.ensureId(getElement());
    OnsenUi.onAvailableElement(this, new Delegate<Element>() {
      public void execute(final Element scrollerElement) {

        // 21/05/2015: metto anche un deleay perche l'offset va preso quando tutto cio che e sopra lo scroller e stato renderizzato
        GwtUtils.deferredExecution(200, new Delegate<Void>() {
          public void execute(Void element) {
            
            if (onScrollerAvailableDelegate != null) {
              onScrollerAvailableDelegate.execute(scrollerElement);
            } else {
              int offsetTop = scrollerElement.getOffsetTop();
              if (doLog) PhgUtils.log("SCROLLER OFFSET TOP = " + offsetTop);
              if (doLog) PhgUtils.log("SETTING SCROLLER ABSOLUTE POSITION...");
              scrollerElement.getStyle().setTop(offsetTop, Unit.PX);
              scrollerElement.getStyle().setBottom(0, Unit.PX);
              scrollerElement.getStyle().setWidth(100, Unit.PCT);
              scrollerElement.getStyle().setPosition(Position.ABSOLUTE);
            }
            
            if (OnsenUi.isPreventTapHandlerWherScrollerMoves()) {
              
              OnsEventUtils.addHandler(scrollerElement, "touchmove", true, new NativeGestureHandler() {
                public void on(NativeGestureEvent event) {
                  if (doLog) PhgUtils.log("SCROLLER TOUCH MOVE - PREVENT TAP HANDLERS");
                  globalTapHandlersPreventing(true);
                }
              });
              
              OnsEventUtils.addHandler(scrollerElement, "touchend", true, new NativeGestureHandler() {
                public void on(NativeGestureEvent event) {
                  if (doLog) PhgUtils.log("SCROLLER TOUCH END - ALLOW TAP HANDLERS");
                  globalTapHandlersPreventing(false);
                }
              });
              
              OnsEventUtils.addDragStartHandler(scrollerElement, true, new NativeGestureHandler() {
                public void on(NativeGestureEvent event) {
                  if (doLog) PhgUtils.log("SCROLLER DRAG START - PREVENT TAP HANDLER");
                  globalTapHandlersPreventing(true);
                }
              });
              
              OnsEventUtils.addDragEndHandler(scrollerElement, true, new NativeGestureHandler() {
                public void on(NativeGestureEvent event) {
                  if (doLog) PhgUtils.log("SCROLLER DRAG END - ALLOW TAP HANDLER");
                  globalTapHandlersPreventing(false);
                }
              });
            }
            
          }
        });
        
        
      }
    });
  }
  
  private void globalTapHandlersPreventing(boolean prevent) {
    if (prevent) {
      HasTapHandlerImpl.setAllHandlersDisabled(true);
      // per sicurezza dopo 10 sec li riabilito
      GwtUtils.deferredExecution(10000, new Delegate<Void>() {
        public void execute(Void element) {
          HasTapHandlerImpl.setAllHandlersDisabled(false);
        }
      });
    } else {
      GwtUtils.deferredExecution(800, new Delegate<Void>() {
        public void execute(Void element) {
          HasTapHandlerImpl.setAllHandlersDisabled(false);
        }
      });
    }
  }
  

  @Override
  public void add(final Widget widget) {
    super.add(widget, getElement());
    String id = getElement().getId();
    GwtUtils.onAvailable(id, new Delegate<Element>() {
      public void execute(Element panelElement) {
        Element childElement = widget.getElement();
        panelElement.appendChild(childElement);
        if (!compiled) {
          compiled = true;
          OnsenUi.compileElement(panelElement);
        }
        OnsenUi.compileElement(childElement);
      }
    });
  }
  
  public void compile() {
    GwtUtils.onAvailable(getElement().getId(), new Delegate<Element>() {
      public void execute(Element panelElement) {
        OnsenUi.compileElement(panelElement);
      }
    });
  }
  
  public void setAnimation(String animation) {
    TransitionUtils.fadeIn(getElement(), TransitionUtils.parseAttributeValue(animation));
  }
  
  public void setOnScrollerAvailableDelegate(Delegate<Element> onScrollerAvailableDelegate) {
    this.onScrollerAvailableDelegate = onScrollerAvailableDelegate;
  }

}
