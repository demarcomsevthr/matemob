package it.mate.phgcommons.client.ui;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.phgcommons.client.utils.TouchUtils;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.HasTapHandlers;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.dom.client.event.touch.HasTouchHandlers;
import com.googlecode.mgwt.dom.client.event.touch.TouchCancelHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartHandler;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.widget.Button;
import com.googlecode.mgwt.ui.client.widget.touch.TouchWidget;

/**
 * 
 * Use it.mate.phgcommons.client.ui.TouchButton instead
 *
 */
public class SmartButton extends Composite implements HasClickHandlers, HasTouchHandlers, HasTapHandlers, 
      HasText, HasTag {

  private Widget impl;
  
  private final boolean isAndroid = MGWT.getOsDetection().isAndroid();
  
  private boolean changeColorOnTap = false;
  
  private int revertOriginalColorDelay = -1;
  
  private String originalColor;
  
  private String tag;
  
  private boolean rounded = true;
  
  private String colorOnTap = "white";
  
  public SmartButton() {
    
    MGWTStyle.getTheme().getMGWTClientBundle().getButtonCss().ensureInjected();    
    
    if (isAndroid) {
//    impl = new Anchor();
//    impl = new TouchHTML();
      impl = new TouchAnchor();
    } else {
      impl = new Button();
    }
    
    initWidget(impl);

    setStyleName("mgwt-SmartButton");
    addStyleName("mgwt-Button");
    setRounded(rounded);
    originalColor = getElement().getStyle().getColor();
    if (originalColor == null || "".equals(originalColor)) {
      originalColor = "black";
    }
    
    if (MGWT.getOsDetection().isAndroid()) {
      changeColorOnTap = true;
      revertOriginalColorDelay = 100; 
    }

    addChangeColorOnTap();

  }
  
  public void setRounded(boolean rounded) {
    this.rounded = rounded;
    if (rounded) {
      addStyleName("mgwt-Button-round");
    } else {
      removeStyleName("mgwt-Button-round");
    }
  }
  
  public void setRounded(String text) {
    setRounded(Boolean.parseBoolean(text));
  }
  
  private void addChangeColorOnTap() {
    if (changeColorOnTap) {
      addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          SmartButton.this.getElement().getStyle().setColor("white");
          if (revertOriginalColorDelay > 0) {
            GwtUtils.deferredExecution(revertOriginalColorDelay, new Delegate<Void>() {
              public void execute(Void element) {
                setOriginalColor();
              }
            });
          }
        }
      });
    }
  }
  
  public void setOriginalColor() {
    GwtUtils.deferredExecution(new Delegate<Void>() {
      public void execute(Void element) {
        SmartButton.this.getElement().getStyle().clearColor();
        SmartButton.this.getElement().getStyle().clearBackgroundColor();
      }
    });
  }
  
  public void setChangeColorOnTap(boolean changeColorOnClick) {
    this.changeColorOnTap = changeColorOnClick;
    addChangeColorOnTap();
  }
  
  public SmartButton(String html) {
    this();
    setHTML(html);
  }
  
  public void setText(String text) {
    setHTML(text);
  }
  
  public String getText() {
    return getElement().getInnerHTML();
  }

  public void setHTML(String html) {
    getElement().setInnerHTML(html);
  }
  
  public void setRound(String round) {
    // do nothing
  }

  @Override
  public HandlerRegistration addClickHandler(ClickHandler handler) {
    if (isAndroid) {
      return getAndroidImpl().addClickHandler(handler);
    } else {
      return getTouchWidgetImpl().addTouchStartHandler(new TouchStartHandler() {
        public void onTouchStart(TouchStartEvent event) {
          TouchUtils.fireClickEventFromTouchEvent(SmartButton.this, event);
        }
      });
    }
  }
  
  @Override
  public HandlerRegistration addTapHandler(TapHandler handler) {
    return getTouchWidgetImpl().addTapHandler(handler);
  }

  @Override
  public HandlerRegistration addTouchStartHandler(final TouchStartHandler handler) {
    return getTouchWidgetImpl().addTouchStartHandler(handler);
  }

  @Override
  public HandlerRegistration addTouchEndHandler(final TouchEndHandler handler) {
    return getTouchWidgetImpl().addTouchEndHandler(handler);
  }

  @Override
  public HandlerRegistration addTouchHandler(final TouchHandler handler) {
    return getTouchWidgetImpl().addTouchHandler(handler);
  }
  
  @Override
  public HandlerRegistration addTouchMoveHandler(TouchMoveHandler handler) {
    return getTouchWidgetImpl().addTouchMoveHandler(handler);
  }

  @Override
  public HandlerRegistration addTouchCancelHandler(TouchCancelHandler handler) {
    return getTouchWidgetImpl().addTouchCancelHandler(handler);
  }
  
  private TouchAnchor getAndroidImpl() {
    if (isAndroid) {
      return (TouchAnchor)impl;
    } else {
      return null;
    }
  }
  
  private TouchWidget getTouchWidgetImpl() {
    return (TouchWidget)impl;
  }
  
  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }
  
}
