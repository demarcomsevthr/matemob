package it.mate.gwtcommons.client.ui;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.RootPanel;

public class SpinnerIntegerBox extends Composite implements HasValue<Integer> {
  
  public interface SpinnerResources extends ClientBundle {
    @Source("it/mate/gwtcommons/client/images/arrowDown.png")
    ImageResource arrowDown();
    @Source("it/mate/gwtcommons/client/images/arrowUp.png")
    ImageResource arrowUp();
  }
  
  IntegerBox inputBox;
  
  Integer minvalue;
  
  Integer maxvalue;
  
  int increment;
  
  private SpinnerResources images;
  
  private int mouseDownFirstTimer = 500;
  
  private int mouseDownFollowingTimer = 50;
  
  public SpinnerIntegerBox() {
    this(0);
  }
  
  public SpinnerIntegerBox(int initialValue) {
    this(initialValue, 1);
  }
    
  public SpinnerIntegerBox(int initialValue, int increment) {
    this(initialValue, increment, null);
  }
    
  public SpinnerIntegerBox(int initialValue, int increment, Integer minvalue) {
    this(initialValue, increment, minvalue, null);
  }
    
  public SpinnerIntegerBox(int initialValue, int increment, Integer minvalue, Integer maxvalue) {
    this(initialValue, increment, minvalue, maxvalue, (SpinnerResources)GWT.create(SpinnerResources.class));
  }
    
  public SpinnerIntegerBox(int initialValue, int increment, Integer minvalue, Integer maxvalue, SpinnerResources images) {
    
    this.minvalue = minvalue;
    
    this.maxvalue = maxvalue;
    
    this.increment = increment;
    
    this.images = images;
    
    inputBox = new IntegerBox();
    
    inputBox.setValue(initialValue);

    initWidget(inputBox);
    
    final Image upImage = initImage(this.images.arrowUp(), +1);
    
    final Image downImage = initImage(this.images.arrowDown(), -1);
    
    GwtUtils.createTimer(100, new Delegate<Void>() {
      public void execute(Void element) {
        Element boxElement = inputBox.getElement();
        int boxWidth = boxElement.getOffsetWidth();
        int top = boxElement.getAbsoluteTop() + 2;
        int left = boxElement.getAbsoluteLeft() + boxWidth - 18;
        setImagePosition(upImage, top, left);
        setImagePosition(downImage, top + 9, left);
      }
    });
    
  }
  
  public void addChangeHandler(ChangeHandler handler) {
    inputBox.addChangeHandler(handler);
  }
  
  public Integer getValue() {
    return inputBox.getValue();
  }
  
  public void setValue(Integer value) {
    inputBox.setValue(value);
  }
  
  public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Integer> handler) {
    return null;
  }

  public void setValue(Integer value, boolean fireEvents) {
    
  }

  public SpinnerIntegerBox setMouseDownFirstTimer(int mouseDownFirstTimer) {
    this.mouseDownFirstTimer = mouseDownFirstTimer;
    return this;
  }
  
  public SpinnerIntegerBox setMouseDownFollowingTimer(int mouseDownFollowingTimer) {
    this.mouseDownFollowingTimer = mouseDownFollowingTimer;
    return this;
  }
  
  public SpinnerIntegerBox setIncrement(int increment) {
    this.increment = increment;
    return this;
  }
  
  public SpinnerIntegerBox setMinvalue(Integer minvalue) {
    this.minvalue = minvalue;
    return this;
  }
  
  public SpinnerIntegerBox setMaxvalue(Integer maxvalue) {
    this.maxvalue = maxvalue;
    return this;
  }
  
  private void updateValue (int versus) {
    int value = inputBox.getValue();
    if (versus > 0 && (maxvalue == null || value < maxvalue)) {
      value += increment;
    } else if (versus < 0 && (minvalue == null || value > minvalue)) {
      value -= increment;
    }
    inputBox.setValue(value);
  }
  
  private Image initImage(ImageResource resource, final int versus) {
    Image image = new Image(resource);
    image.setVisible(false);
    GwtUtils.setStyleAttribute(image, "position", "absolute");
    image.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        updateValue(versus);
        cancelTimer((Image)event.getSource(), "mouseDownTimer");
      }
    });
    image.addMouseDownHandler(new MouseDownHandler() {
      public void onMouseDown(MouseDownEvent event) {
        Timer timer = new Timer() {
          public void run() {
            updateValue(versus);
            this.schedule(mouseDownFollowingTimer);
          }
        };
        Element imageElem = ((Image)event.getSource()).getElement();
        imageElem.setPropertyObject("mouseDownTimer", timer);
        timer.schedule(mouseDownFirstTimer);
      }
    });
    image.addMouseUpHandler(new MouseUpHandler() {
      public void onMouseUp(MouseUpEvent event) {
        cancelTimer((Image)event.getSource(), "mouseDownTimer");
      }
    });
    RootPanel.get().add(image);
    return image;
  }
  
  private void cancelTimer(Image image, String timerName) {
    Element imageElem = image.getElement();
    Timer timer = (Timer)imageElem.getPropertyObject(timerName);
    if (timer != null) {
      timer.cancel();
    }
  }
  
  private void setImagePosition(Image image, int top, int left) {
    GwtUtils.setStyleAttribute(image, "top", top+"px");
    GwtUtils.setStyleAttribute(image, "left", left+"px");
    image.setVisible(true);
  }
  
  public IntegerBox getInputBox() {
    return inputBox;
  }
  
}
