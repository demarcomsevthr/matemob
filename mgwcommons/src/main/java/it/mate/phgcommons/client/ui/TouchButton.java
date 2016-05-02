package it.mate.phgcommons.client.ui;

import it.mate.phgcommons.client.utils.TouchUtils;

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

public class TouchButton extends Composite implements HasClickHandlers, HasTouchHandlers, HasTapHandlers,
      HasText, HasTag {

  private Widget impl;
  
  private final boolean isAndroid = MGWT.getOsDetection().isAndroid();
  
  private String tag;
  
  private boolean rounded = false;
  
  public TouchButton() {
    
    MGWTStyle.getTheme().getMGWTClientBundle().getButtonCss().ensureInjected();    
    
    if (isAndroid) {
      impl = new TouchHTML();
    } else {
      impl = new Button();
    }
    
    initWidget(impl);

    setStyleName("mgwt-SmartButton");
    addStyleName("mgwt-Button");
    addStyleName("phg-TouchButton");
    setRounded(rounded);
    
    TouchUtils.addTappedStyleHandlers(this);

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
  
  public TouchButton(String html) {
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
          TouchUtils.fireClickEventFromTouchEvent(TouchButton.this, event);
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
  
  private TouchHTML getAndroidImpl() {
    if (isAndroid) {
      return (TouchHTML)impl;
    } else {
      return null;
    }
  }
  
  private TouchWidget getTouchWidgetImpl() {
    return (TouchWidget)impl;
  }
  
  public void setEnabled(boolean enabled) {
    if (enabled) {
      impl.removeStyleName("mgwt-SmartButton-disabled");
    } else {
      impl.addStyleName("mgwt-SmartButton-disabled");
    }
  }
  
  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }
  
}
