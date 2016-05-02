package it.mate.phgcommons.client.ui;

import it.mate.phgcommons.client.ui.ph.PhCheckBox;
import it.mate.phgcommons.client.utils.MgwtDialogs;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndHandler;
import com.googlecode.mgwt.ui.client.dialog.PopinDialog;

public class TouchCombo extends TouchHTML implements HasClickHandlers, HasValue<String>, HasChangeHandlers {
  
  private PopinDialog itemsDialog;
  
  private List<Item> items = new ArrayList<Item>();
  
  private int selectedItem = -1;
  
  private String value;
  
  private List<ValueChangeHandler<String>> valueChangeHandlers = new ArrayList<ValueChangeHandler<String>>();
  
  private static final String ARROW_HTML = "<span class='phg-TouchCombo-arrow'><img src='main/images/arrowdown-32-2.png'/></span>";
//private static final String ARROW_HTML = "<span class='phg-TouchCombo-arrow'></span>";
  
  private String itemWidth = "8em";
  
  public TouchCombo() {
    this("");
  }
  
  public TouchCombo(String html) {
    this(SafeHtmlUtils.fromTrustedString(html));
  }
  
  public TouchCombo(SafeHtml html) {
    super(new HTML(html));
    addStyleName("phg-TouchCombo");
    addArrowElem();
    
    addTouchEndHandler(new TouchEndHandler() {
      public void onTouchEnd(TouchEndEvent event) {
        showItemsDialog();
      }
    });
    
  }
  
  public void setItemWidth(String itemWidth) {
    this.itemWidth = itemWidth;
  }
  
  public void addItem(String value, String description, boolean selected) {
    setItem(value, description, selected);
  }
  
  public void setItem(String value, String description, boolean selected) {
//  LogUtil.log("setItem: value = "+value+", description = "+description+", selected = " + selected);
    for (Item item : items) {
      if (item.value.equals(value)) {
        item.description = description;
        if (this.value != null && this.value.equals(value)) {
          setText(item.description);
        }
        return;
      }
    }
    Item item = new Item(value, description);
    items.add(item);
    if (this.value == null) {
      if (selected) {
        setValue(value, false);
      }
    } else {
      if (this.value.equals(value)) {
        setText(item.description);
      }
    }
  }
  
  @Override
  public void setValue(String value, boolean fireEvents) {
    this.value = value;
    for (Item item : items) {
      if (item.value.equals(value)) {
        selectedItem = items.indexOf(item);
        setText(item.description);
        if (fireEvents) {
          SelectedChangeEvent.fire(this, value);
        }
      }
    }
  }
  
  public List<Item> getItems() {
    return items;
  }
  
  private void showItemsDialog() {
    
    VerticalPanel itemsContainer = new VerticalPanel();
    itemsContainer.addStyleName("phg-TouchCombo-ItemsDialog");

    for (int it = 0; it < items.size(); it++) {
      HorizontalPanel row = new HorizontalPanel();
      final Item item = items.get(it);
      TouchHTML itemHtml = new TouchHTML(item.description);
      row.add(itemHtml);
      final PhCheckBox itemCheck = new PhCheckBox();
      itemCheck.setCircle(true);
      if (it == selectedItem) {
        itemCheck.setValue(true);
      }
      TouchEndHandler closeHandler = new TouchEndHandler() {
        public void onTouchEnd(TouchEndEvent event) {
          itemCheck.setValue(true);
          itemsDialog.hide();
          itemsDialog = null;
          selectedItem = items.indexOf(item);
          setValue(item.value);
        }
      };
      itemCheck.addTouchEndHandler(closeHandler);
      row.add(itemCheck);
      itemsContainer.add(row);
      itemHtml.addStyleName("phg-TouchCombo-ItemsDialog-item");
      itemHtml.setWidth(itemWidth);
      if (it < (items.size() - 1)) {
        row.addStyleName("phg-TouchCombo-ItemsDialog-item-middle");
      }
      itemHtml.addTouchEndHandler(closeHandler);
    }
  
    // 17/06/2014
//  TouchCombo.this.itemsDialog = MgwtDialogs.popin(null, itemsContainer);
    TouchCombo.this.itemsDialog = MgwtDialogs.popin(null, itemsContainer, true);
    
    itemsContainer.getParent().getElement().getStyle().setBackgroundColor("transparent");
    itemsContainer.getParent().getElement().getStyle().setBorderWidth(0, Unit.PX);
    
  }

  @Override
  public void setHtml(SafeHtml html) {
    super.setHtml(html);
    addArrowElem();
  }
  
  private void addArrowElem() {
    Element parentElem = getElement();
    parentElem.setInnerHTML(parentElem.getInnerHTML() + ARROW_HTML);
  }
  
  public static class Item {
    String value;
    String description;
    public Item(String value, String description) {
      this.value = value;
      this.description = description;
    }
    public String getDescription() {
      return description;
    }
    public void setDescription(String description) {
      this.description = description;
    }
  }
  
  
  @Override
  public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<String> handler) {
    this.valueChangeHandlers.add(handler);
    HandlerRegistration registration = new HandlerRegistration() {
      public void removeHandler() {
        valueChangeHandlers.remove(handler);
      }
    };
    return registration;
  }

  @Override
  public HandlerRegistration addChangeHandler(ChangeHandler handler) {
    return addDomHandler(handler, ChangeEvent.getType());
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public void setValue(String value) {
    setValue(value, true);
  }

  @Override
  public void fireEvent(GwtEvent<?> event) {
    if (event instanceof SelectedChangeEvent) {
      SelectedChangeEvent valueChangeEvent = (SelectedChangeEvent)event;
      for (ValueChangeHandler<String> handler : valueChangeHandlers) {
        handler.onValueChange(valueChangeEvent);
      }
    } else {
      super.fireEvent(event);
    }
  }
  
  public static class SelectedChangeEvent extends ValueChangeEvent<String> {

    public static <S extends HasValueChangeHandlers<String> & HasHandlers> void fire(S source, String value) {
      source.fireEvent(new SelectedChangeEvent(value));
    }
    
    public static <S extends HasValueChangeHandlers<String> & HasHandlers> void fireIfNotEqualDates(S source, String oldValue, String newValue) {
      if (ValueChangeEvent.shouldFire(source, oldValue, newValue)) {
        source.fireEvent(new SelectedChangeEvent(newValue));
      }
    }

    protected SelectedChangeEvent(String value) {
      super(value);
    }

  }
  
  
}
