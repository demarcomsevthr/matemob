package it.mate.phgcommons.client.ui;

import it.mate.gwtcommons.client.ui.SimpleContainer;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.phgcommons.client.ui.ph.PhTextBox;
import it.mate.phgcommons.client.utils.PhgUtils;

import java.util.Date;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndHandler;

public abstract class SimpleDatepickerDialog {

  private static final String[] MONTH_NAMES_EN = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
  private static final String[] MONTH_NAMES_IT = {"Gen", "Feb", "Mar", "Apr", "Mag", "Giu", "Lug", "Ago", "Set", "Ott", "Nov", "Dic"};

  private Date selectedDate;
  
  private PopupPanel popup;
  
  private String baseStylename = "phg-BasicDatepicker";

  TouchHTML dateBox;
  PhTextBox dayBox;
  PhTextBox monthBox;
  PhTextBox yearBox;

  public SimpleDatepickerDialog() {
    this(new Date());
  }
  
  public SimpleDatepickerDialog(Date initialDate) {
    this.selectedDate = CalendarUtil.copyDate(initialDate);
    this.selectedDate.setHours(0);
    this.selectedDate.setMinutes(0);
    this.selectedDate.setSeconds(0);
  }
  
  public void show() {
    GwtUtils.deferredExecution(new Delegate<Void>() {
      public void execute(Void element) {
        initUI();
      }
    });
  }
  
  public void hide() {
    if (popup != null) {
      popup.hide();
      popup = null;
    }
  }
  
  public abstract void onDone(Date selectedDate);
  
  private void initUI() {
    
    popup = new PopupPanel();
    popup.addStyleName(baseStylename);
    
    SimpleContainer container = new SimpleContainer();
    
    HorizontalPanel header = new HorizontalPanel();
    header.addStyleName(baseStylename+"-header");
    container.add(header);
    
    dateBox = new TouchHTML();
    dateBox.addStyleName(baseStylename+"-dateBox");
    header.add(dateBox);
    
    HorizontalPanel center = new HorizontalPanel();
    center.addStyleName(baseStylename+"-center");
    container.add(center);
    
    SimpleContainer centerLeft = new SimpleContainer();
    centerLeft.addStyleName(baseStylename+"-centerLeft");
    center.add(centerLeft);
    
    VerticalPanel centerLeftPanel = new VerticalPanel();
    centerLeft.add(centerLeftPanel);
    TouchHTML addDayBox = new TouchHTML("+");
    addDayBox.addStyleName(baseStylename+"-spinControl");
    addDayBox.addTouchEndHandler(createSpinHandler(DAY, +1));
    centerLeftPanel.add(addDayBox);
    dayBox = new PhTextBox();
    dayBox.addStyleName(baseStylename+"-dayBox");
    dayBox.setType("number");
    dayBox.addChangeHandler(new ChangeHandler() {
      public void onChange(ChangeEvent event) {
        Integer dd = dayBox.getValueAsInteger();
        if (dd != null && dd >= 1 && dd <= 31) {
          selectedDate.setDate(dd);
        }
        showSelectedDate();
      }
    });
    centerLeftPanel.add(dayBox);
    TouchHTML subDayBox = new TouchHTML("-");
    subDayBox.addStyleName(baseStylename+"-spinControl");
    subDayBox.addTouchEndHandler(createSpinHandler(DAY, -1));
    centerLeftPanel.add(subDayBox);
    
    SimpleContainer centerMiddle = new SimpleContainer();
    centerMiddle.addStyleName(baseStylename+"-centerMiddle");
    center.add(centerMiddle);
    
    VerticalPanel centerMiddlePanel = new VerticalPanel();
    centerMiddle.add(centerMiddlePanel);
    
    TouchHTML addMonthBox = new TouchHTML("+");
    addMonthBox.addStyleName(baseStylename+"-spinControl");
    addMonthBox.addTouchEndHandler(createSpinHandler(MONTH, +1));
    centerMiddlePanel.add(addMonthBox);
    monthBox = new PhTextBox();
    monthBox.addStyleName(baseStylename+"-monthBox");
    monthBox.setReadOnly(true);
    centerMiddlePanel.add(monthBox);
    TouchHTML subMonthBox = new TouchHTML("-");
    subMonthBox.addStyleName(baseStylename+"-spinControl");
    subMonthBox.addTouchEndHandler(createSpinHandler(MONTH, -1));
    centerMiddlePanel.add(subMonthBox);
    
    
    SimpleContainer centerRight = new SimpleContainer();
    centerRight.addStyleName(baseStylename+"-centerRight");
    center.add(centerRight);
    
    VerticalPanel centerRightPanel = new VerticalPanel();
    centerRight.add(centerRightPanel);
    
    TouchHTML addYearBox = new TouchHTML("+");
    addYearBox.addStyleName(baseStylename+"-spinControl");
    addYearBox.addTouchEndHandler(createSpinHandler(YEAR, +1));
    centerRightPanel.add(addYearBox);
    yearBox = new PhTextBox();
    yearBox.addStyleName(baseStylename+"-yearBox");
    yearBox.setType("number");
    yearBox.addChangeHandler(new ChangeHandler() {
      public void onChange(ChangeEvent event) {
        Integer yy = yearBox.getValueAsInteger();
        if (yy != null && yy >= 1900) {
          selectedDate.setYear(yy - 1900);
        }
        showSelectedDate();
      }
    });
    centerRightPanel.add(yearBox);
    TouchHTML subYearBox = new TouchHTML("-");
    subYearBox.addStyleName(baseStylename+"-spinControl");
    subYearBox.addTouchEndHandler(createSpinHandler(YEAR, -1));
    centerRightPanel.add(subYearBox);
    
    
    HorizontalPanel bottom = new HorizontalPanel();
    bottom.addStyleName(baseStylename+"-bottom");
    container.add(bottom);
    
    TouchHTML doneBtn = new TouchHTML(PhgUtils.isAppLocalLanguageIT()?"FINITO":"DONE");
    doneBtn.addStyleName(baseStylename+"-doneBtn");
    doneBtn.addTouchEndHandler(new TouchEndHandler() {
      public void onTouchEnd(TouchEndEvent event) {
        onDone(selectedDate);
        hide();
      }
    });
    bottom.add(doneBtn);
    
    TouchHTML cancelBtn = new TouchHTML(PhgUtils.isAppLocalLanguageIT()?"Annulla":"Cancel");
    cancelBtn.addStyleName(baseStylename+"-cancelBtn");
    cancelBtn.addTouchEndHandler(new TouchEndHandler() {
      public void onTouchEnd(TouchEndEvent event) {
        hide();
      }
    });
    bottom.add(cancelBtn);
    
    showSelectedDate();
    
    popup.add(container);
    popup.center();
    
  }
  
  private static final int DAY = 0;
  private static final int MONTH = 1;
  private static final int YEAR = 2;
  
  private TouchEndHandler createSpinHandler(final int type, final int amount) {
    return new TouchEndHandler() {
      public void onTouchEnd(TouchEndEvent event) {
        if (type == DAY) {
          CalendarUtil.addDaysToDate(selectedDate, amount);
        } else if (type == MONTH) {
          CalendarUtil.addMonthsToDate(selectedDate, amount);
        } else if (type == YEAR) {
          CalendarUtil.addMonthsToDate(selectedDate, 12 * amount);
        }
        showSelectedDate();
      }
    };
  }
  
  private void showSelectedDate() {
    dateBox.setHtml(GwtUtils.dateToString(selectedDate, "dd/MM/yyyy"));
    dayBox.setValue(GwtUtils.dateToString(selectedDate, "dd"));
    yearBox.setValue(GwtUtils.dateToString(selectedDate, "yyyy"));
    int mm = selectedDate.getMonth();
    if (PhgUtils.isAppLocalLanguageIT()) {
      monthBox.setValue(MONTH_NAMES_IT[mm]);
    } else {
      monthBox.setValue(MONTH_NAMES_EN[mm]);
    }
  }
  
  
}
