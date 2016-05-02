package it.mate.phgcommons.client.ui;

import it.mate.gwtcommons.client.ui.SimpleContainer;
import it.mate.gwtcommons.client.ui.Spacer;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.phgcommons.client.utils.EventUtils;
import it.mate.phgcommons.client.utils.PhgDialogUtils;
import it.mate.phgcommons.client.utils.PhgUtils;
import it.mate.phgcommons.client.utils.Time;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndHandler;

public class TimePickerDialog {
  
  public static class Options {
    private String title = "Set a time";
    private int top = -1;
    private int left = -1;
    private int width = 300;
    private int height = 374;
    private Delegate<Time> onCloseDelegate;
    private boolean modal = true;
    private boolean glassEnabled = true;
    public Options setTitle(String title) {
      this.title = title;
      return this;
    }
    public Options setTop(int top) {
      this.top = top;
      return this;
    }
    public Options setLeft(int left) {
      this.left = left;
      return this;
    }
    public Options setWidth(int width) {
      this.width = width;
      return this;
    }
    public Options setHeight(int height) {
      this.height = height;
      return this;
    }
    public Options setOnClose(Delegate<Time> onCloseDelegate) {
      this.onCloseDelegate = onCloseDelegate;
      return this;
    }
    public Options setModal(boolean modal) {
      this.modal = modal;
      return this;
    }
    public Options setGlassEnabled(boolean glassEnabled) {
      this.glassEnabled = glassEnabled;
      return this;
    }
  }
  
  private PopupPanel popup;
  
  private Options options;
  
  private Time currentTime = new Time(0, 0);
  
  private int currentDigitPosition = 0;
  
  private TouchHTML digits[] = new TouchHTML[4];
  
  private HandlerRegistration modalHandlerRegistration;
  
  private TouchHTML ampmBtn;
  
  public TimePickerDialog() {
    this(null, null);
  }
  
  public TimePickerDialog(Time initialTime) {
    this(null, initialTime);
  }
  
  public TimePickerDialog(Options options) {
    this(options, null);
  }
  
  public TimePickerDialog(Options options, Time initialTime) {
    if (initialTime != null) {
      this.currentTime = initialTime;
    }
    this.options = options != null ? options : new Options();
    initUI();
    setTime(currentTime);
  }
  
  public void setTime(Time time) {
    this.currentTime = time;
    renderTime();
  }
  
  private void initUI() {
    
    popup = new PopupPanel();
    popup.addStyleName("phg-TimePickerDialog");
    popup.setWidth(options.width+"px");
    popup.setHeight(options.height+"px");
    
    if (options.glassEnabled) {
      popup.setGlassStyleName("phg-PopupPanelGlass");
      popup.setGlassEnabled(true);
    }
    
    if (options.modal) {
      makePopupModal();
    }
    
    SimpleContainer container = new SimpleContainer();
    
    HTML header = new HTML(options.title);
    header.addStyleName("phg-TimePickerDialog-Header");
    container.add(header);
    
    HorizontalPanel digitsRow = new HorizontalPanel();
    digitsRow.addStyleName("phg-TimePickerDialog-TimeRow");
    container.add(digitsRow);
    
    digitsRow.add(createDigitHtml(0, getPositionalDigit(currentTime.getHours(), 0)));
    digitsRow.add(createDigitHtml(1, getPositionalDigit(currentTime.getHours(), 1)));
    digitsRow.add(new Label(":"));
    digitsRow.add(createDigitHtml(2, getPositionalDigit(currentTime.getMinutes(), 0)));
    digitsRow.add(createDigitHtml(3, getPositionalDigit(currentTime.getMinutes(), 1)));
    
    if (Time.is12HFormat()) {
      
      //TODO
      digitsRow.add(new Spacer("0.5em"));
      digitsRow.getElement().getStyle().setPaddingLeft(12, Unit.PCT);
      
      ampmBtn = new TouchHTML(currentTime.isAM() ? "AM" : "PM");
      ampmBtn.addStyleName("phg-TimePickerDialog-AMPM");
      digitsRow.add(ampmBtn);
      ampmBtn.addTouchEndHandler(new TouchEndHandler() {
        public void onTouchEnd(TouchEndEvent event) {
          currentTime.incHours(+12);
          renderTime();
        }
      });
      
    }
    
    FlexTable keypad = new FlexTable();
    keypad.addStyleName("phg-TimePickerDialog-Keypad");
    container.add(keypad);
    
    TouchEndHandler digitHandler = new TouchEndHandler() {
      public void onTouchEnd(TouchEndEvent event) {
        Widget source = (Widget)event.getSource();
        int newDigit = getWidgetValue(source);
        if (currentDigitPosition == 0) {
          if (Time.is12HFormat()) {
            if (newDigit <= 0) {
              int hours = currentTime.getHours12();
              currentTime.setHours12(setDigitInPosition(hours, 0, newDigit));
            } else if (newDigit == 1) {
              int d1 = getWidgetValue(digits[1]);
              if (d1 <= 2) {
                int hours = currentTime.getHours12();
                currentTime.setHours12(setDigitInPosition(hours, 0, newDigit));
              } else {
                currentTime.setHours12(10);
              }
            }
            /*
            if (newDigit == 0) {
              int d1 = getWidgetValue(digits[1]);
              if (d1 <= 2) {
                
              }
              int hours = currentTime.getHours();
              currentTime.setHours(setDigitInPosition(hours, 0, newDigit));
            } else if (newDigit == 1) {
              int d1 = getWidgetValue(digits[1]);
              int hours = currentTime.getHours();
              if (currentTime.isAM()) {
                if (d1 < 2) {
                  hours = 10 + d1;
                } else if (d1 == 2) {
                  hours = 0;
                }
              } else {
                if (d1 < 2) {
                  hours = 22 + d1;
                } else if (d1 == 2) {
                  hours = 12;
                }
              }
              currentTime.setHours(hours);
            }
            */
          } else {
            if (newDigit <= 2) {
              int hours = currentTime.getHours();
              currentTime.setHours(setDigitInPosition(hours, 0, newDigit));
            }
          }
          currentDigitPosition ++;
          renderTime();
        } else if (currentDigitPosition == 1) {
          if (Time.is12HFormat()) {
            int d0 = getWidgetValue(digits[0]);
            if (d0 <= 0) {
              int hours = currentTime.getHours12();
              currentTime.setHours12(setDigitInPosition(hours, 1, newDigit));
            } else if (d0 == 1) {
              if (newDigit <= 2) {
                int hours = currentTime.getHours12();
                currentTime.setHours12(setDigitInPosition(hours, 1, newDigit));
              }
            }
            /*
            int d0 = getWidgetValue(digits[0]);
            if (d0 == 0) {
              int hours = currentTime.getHours();
              currentTime.setHours(setDigitInPosition(hours, 1, newDigit));
            } else if (d0 == 1) {
              if (newDigit <= 1) {
                if (currentTime.isAM()) {
                  currentTime.setHours(setDigitInPosition(10, 1, newDigit));
                } else {
                  currentTime.setHours(setDigitInPosition(10, 1, newDigit) + 12);
                }
              } else if (newDigit == 2) {
                if (currentTime.isAM()) {
                  currentTime.setHours(0);
                } else {
                  currentTime.setHours(12);
                }
              }
            } else if (d0 == 2) {
              if (newDigit <= 4) {
                int hours = currentTime.getHours();
                currentTime.setHours(setDigitInPosition(hours, 1, newDigit));
              }
            }
            */
          } else {
            int d0 = getWidgetValue(digits[0]);
            if (d0 <= 1) {
              int hours = currentTime.getHours();
              currentTime.setHours(setDigitInPosition(hours, 1, newDigit));
            } else if (d0 == 2) {
              if (newDigit <= 3) {
                int hours = currentTime.getHours();
                currentTime.setHours(setDigitInPosition(hours, 1, newDigit));
              }
            }
          }
          currentDigitPosition ++;
          renderTime();
        } else if (currentDigitPosition == 2) {
          if (newDigit <= 5) {
            int minutes = currentTime.getMinutes();
            currentTime.setMinutes(setDigitInPosition(minutes, 0, newDigit));
          }
          currentDigitPosition ++;
          renderTime();
        } else if (currentDigitPosition == 3) {
          int minutes = currentTime.getMinutes();
          currentTime.setMinutes(setDigitInPosition(minutes, 1, newDigit));
          currentDigitPosition = 0;
          renderTime();
        }
      }
    };
    
    TouchEndHandler minutesHandler = new TouchEndHandler() {
      public void onTouchEnd(TouchEndEvent event) {
        Widget source = (Widget)event.getSource();
        int minutes = getWidgetValue(source);
        currentTime.setMinutes(minutes);
        renderTime();
      }
    };
    
    TouchEndHandler incHandler = new TouchEndHandler() {
      public void onTouchEnd(TouchEndEvent event) {
        Widget source = (Widget)event.getSource();
        int incr = getWidgetValue(source);
        int hours = currentTime.getHours();
        if (incr == +1) {
          if (hours == 23) {
            hours = 00;
          } else {
            hours ++;
          }
        } else if (incr == -1) {
          if (hours == 00) {
            hours = 23;
          } else {
            hours --;
          }
        }
        currentTime.setHours(hours);
        renderTime();
      }
    };
    
    keypad.setWidget(0, 0, createKeypadCell("1", 1, digitHandler, null));
    keypad.setWidget(0, 1, createKeypadCell("2", 2, digitHandler, null));
    keypad.setWidget(0, 2, createKeypadCell("3", 3, digitHandler, null));
    keypad.setWidget(0, 3, createKeypadCell("15", 15, minutesHandler, "minutes"));
    keypad.setWidget(1, 0, createKeypadCell("4", 4, digitHandler, null));
    keypad.setWidget(1, 1, createKeypadCell("5", 5, digitHandler, null));
    keypad.setWidget(1, 2, createKeypadCell("6", 6, digitHandler, null));
    keypad.setWidget(1, 3, createKeypadCell("30", 30, minutesHandler, "minutes"));
    keypad.setWidget(2, 0, createKeypadCell("7", 7, digitHandler, null));
    keypad.setWidget(2, 1, createKeypadCell("8", 8, digitHandler, null));
    keypad.setWidget(2, 2, createKeypadCell("9", 9, digitHandler, null));
    keypad.setWidget(2, 3, createKeypadCell("45", 45, minutesHandler, "minutes"));
    keypad.setWidget(3, 0, createKeypadCell("-1h", -1, incHandler, "hours"));
    keypad.setWidget(3, 1, createKeypadCell("0", 0, digitHandler, null));
    keypad.setWidget(3, 2, createKeypadCell("+1h", +1, incHandler, "hours"));
    keypad.setWidget(3, 3, createKeypadCell("00", 00, minutesHandler, "minutes"));
    
    
    HorizontalPanel bottom = new HorizontalPanel();
    bottom.addStyleName("phg-TimePickerDialog-Bottom");
    container.add(bottom);
    
    TouchHTML okBtn = new TouchHTML("OK");
    okBtn.addTouchEndHandler(new TouchEndHandler() {
      public void onTouchEnd(TouchEndEvent event) {
        if (currentTime.isValidTime()) {
          hide();
          if (options.onCloseDelegate != null) {
            options.onCloseDelegate.execute(currentTime);
          }
        } else {
          PhgDialogUtils.showMessageDialog("Wrong time!", "Error");
        }
      }
    });
    okBtn.addStyleName("phg-TimePickerDialog-BottomButton");
    bottom.add(okBtn);
    
    TouchHTML cancelBtn = new TouchHTML("Cancel");
    cancelBtn.addTouchEndHandler(new TouchEndHandler() {
      public void onTouchEnd(TouchEndEvent event) {
        hide();
      }
    });
    cancelBtn.addStyleName("phg-TimePickerDialog-BottomButton");
    bottom.add(cancelBtn);
    
    
    popup.add(container);

    if (options.top == -1 && options.left == -1) {
      popup.center();
    } else {
      popup.setPopupPosition(options.left, options.top);
      popup.show();
    }
    
  }
  
  public void hide() {
    popup.hide();
    EventUtils.removeModalHandler(modalHandlerRegistration);
    PhgUtils.setSuspendUncaughtExceptionAlerts(false);
    onHide();
  }
  
  protected void onHide() {
    
  }
  
  private void renderTime() {
    
    PhgUtils.log("rendering " + currentTime);
    
    int hours = currentTime.getHours();
    
    //TODO
    if (Time.is12HFormat()) {
      ampmBtn.setHtml(currentTime.isAM() ? "AM" : "PM");
      hours = currentTime.getHours12();
    }
    
    int d0 = hours / 10;
    int d1 = hours % 10;
    int minutes = currentTime.getMinutes();
    int d2 = minutes / 10;
    int d3 = minutes % 10;
    
    setDigitHtmlValue(digits[0], d0);
    setDigitHtmlValue(digits[1], d1);
    setDigitHtmlValue(digits[2], d2);
    setDigitHtmlValue(digits[3], d3);
    for (int it = 0; it < 4; it++) {
      if (currentDigitPosition == it) {
        digits[it].addStyleName("phg-TimePickerDialog-TimeDigit-selected");
      } else {
        digits[it].removeStyleName("phg-TimePickerDialog-TimeDigit-selected");
      }
    }
  }
  
  private void setDigitHtmlValue(TouchHTML digit, int d) {
    digit.setHtml(SafeHtmlUtils.fromTrustedString(""+d));
    setWidgetValue(digit, d);
  }
  
  private TouchHTML createDigitHtml(final int index, int value) {
    TouchHTML digit = new TouchHTML(""+value);
    digit.addStyleName("phg-TimePickerDialog-TimeDigit");
    digit.addTapHandler(new TapHandler() {
      public void onTap(TapEvent event) {
        currentDigitPosition = index;
        renderTime();
      }
    });
    setWidgetValue(digit, value);
    digits[index] = digit;
    return digit;
  }
  
  private TouchHTML createKeypadCell(String text, int value, TouchEndHandler handler, String additionalStyleName) {
    TouchHTML keypadCell = new TouchHTML(text);
    keypadCell.addStyleName("phg-TimePickerDialog-KeypadCell");
    if (additionalStyleName != null) {
      keypadCell.addStyleName("phg-TimePickerDialog-KeypadCell-" + additionalStyleName);
    }
    keypadCell.addTouchEndHandler(handler);
    setWidgetValue(keypadCell, value);
    return keypadCell;
  }
  
  private int getWidgetValue(Widget widget) {
    return widget.getElement().getPropertyInt("model");
  }
  
  private void setWidgetValue(Widget widget, int value) {
    widget.getElement().setPropertyInt("model", value);
  }
  
  private static int getPositionalDigit(int value, int pos) {
    assert value >= 00 && value <= 59;
    assert pos == 0 || pos == 1;
    if (pos == 0)
      return value / 10;
    if (pos == 1)
      return value % 10;
    return -1;
  }
  
  private static int setDigitInPosition(int value, int pos, int digit) {
    assert value >= 00 && value <= 59;
    assert pos == 0 || pos == 1;
    if (pos == 0) {
      value = (digit * 10) + (value % 10);
    } else if (pos == 1) {
      value = (value / 10) * 10 + (digit);
    }
    return value;
  }
  
  private void makePopupModal() {
    if (modalHandlerRegistration == null) {
      EventUtils.createModalHandler(new EventUtils.PanelGetter() {
        public Panel getPanel() {
          return popup;
        }
      }, new Delegate<HandlerRegistration>() {
        public void execute(HandlerRegistration registration) {
          modalHandlerRegistration = registration;
        }
      });
    }
  }
  
}
