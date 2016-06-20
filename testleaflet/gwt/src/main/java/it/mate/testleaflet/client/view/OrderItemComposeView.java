package it.mate.testleaflet.client.view;

import it.mate.testleaflet.client.utils.AnimitUtils;
import it.mate.testleaflet.client.view.OrderItemComposeView.Presenter;
import it.mate.testleaflet.shared.model.Order;
import it.mate.testleaflet.shared.model.OrderItem;
import it.mate.testleaflet.shared.model.OrderItemRow;
import it.mate.testleaflet.shared.model.impl.OrderItemRowTx;
import it.mate.testleaflet.shared.model.impl.OrderItemTx;
import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.gwtcommons.client.ui.Spacer;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.gwtcommons.client.utils.ObjectWrapper;
import it.mate.onscommons.client.event.OnsEventUtils;
import it.mate.onscommons.client.event.TapEvent;
import it.mate.onscommons.client.event.TapHandler;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.ui.OnsButton;
import it.mate.onscommons.client.ui.OnsDialog;
import it.mate.onscommons.client.ui.OnsHorizontalPanel;
import it.mate.onscommons.client.ui.OnsList;
import it.mate.onscommons.client.ui.OnsListItem;
import it.mate.onscommons.client.ui.OnsScroller;
import it.mate.onscommons.client.ui.OnsTextBox;
import it.mate.onscommons.client.utils.OnsDialogUtils;
import it.mate.phgcommons.client.utils.PhgUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class OrderItemComposeView extends AbstractBaseView<Presenter> {

  public interface Presenter extends BasePresenter {
    public void saveOrderItemOnDevice(OrderItem item, Delegate<Order> delegate);
  }

  public interface ViewUiBinder extends UiBinder<Widget, OrderItemComposeView> { }

  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
  
  @UiField Panel wrapperPanel;
  
  @UiField OnsList rowsPanel;
  @UiField OnsScroller scroller;
  @UiField Panel controlbar;
  @UiField Spacer viewfinder;
  
//@UiField OnsDialog fontSizeDialog;
//@UiField OnsDialog fontFamilyDialog;
  
  private OrderItem item;
  
  private boolean controlbarVisible = false;
  
  private Element lastTappedElement;
  
  private List<OnsTextBox> textboxes = new ArrayList<OnsTextBox>();

  private int selectedRowIndex;
  
  private JavaScriptObject overallEventListener = null;
  
  private static final String CONTROLBAR_VISIBLE_WIDTH = "17.6em";
  private static final String CONTROLBAR_VISIBLE_HEIGHT =  "2.2em";
  private static final int CONTROLBAR_VISIBLE_MARGIN_TOP =  4;
  
  public OrderItemComposeView() {
    initUI();
  }

  private void initProvidedElements() {

  }

  private void initUI() {
    initProvidedElements();
    initWidget(uiBinder.createAndBindUi(this));
    OnsenUi.ensureId(controlbar.getElement());
    OnsenUi.ensureId(rowsPanel.getElement());
    OnsenUi.ensureId(viewfinder.getElement());
  }

  @Override
  public void setModel(Object model, String tag) {
    if (model instanceof OrderItem) {
      item = (OrderItem)model;
      PhgUtils.log("editing item " + item);
      for (OrderItemRow row : item.getRows()) {
        PhgUtils.log("   with row " + row);
      }
      if (item.getRows().size() == 0) {
        item.getRows().add(new OrderItemRowTx(""));
      }
      List<OnsListItem> listItems = new ArrayList<>();
      for (int it = 0; it < item.getRows().size(); it++) {
        OrderItemRow row = item.getRows().get(it);
        listItems.add(createRowItem(row));
      }
      iterateListItemsForRendering(listItems.iterator(), new Delegate<Void>() {
        public void execute(Void element) {
          rowsPanel.add(createLastRowItem());
        }
      });
    } else {
      // FOR DEBUG
      item = new OrderItemTx(null);
      OrderItemRow row = new OrderItemRowTx("");
      item.getRows().add(row);
      rowsPanel.add(createRowItem(row));
      rowsPanel.add(createLastRowItem());
    }
  }
  
  private void iterateListItemsForRendering(final Iterator<OnsListItem> it, final Delegate<Void> delegate) {
    if (it.hasNext()) {
      OnsListItem listItem = it.next();
      rowsPanel.add(listItem, new Delegate<Element>() {
        public void execute(Element element) {
          iterateListItemsForRendering(it, delegate);
        }
      });
    } else {
      delegate.execute(null);
    }
  }
  
  private OnsListItem createRowItem(OrderItemRow row) {
    final int index = textboxes.size();
    OnsHorizontalPanel rowpanel = new OnsHorizontalPanel();
    rowpanel.setAddDirect(true);
    rowpanel.setWidth("100%");
    OnsTextBox textbox = new OnsTextBox();
    textbox.addStyleName("app-edit-text");
    textbox.setText(row.getText());
    if (row.getSize() != null && row.getSize() > 0) {
      textbox.getElement().getStyle().setFontSize(row.getSize() / 10, Unit.EM);
    }
    if (row.getBold()) {
      textbox.getElement().addClassName("app-bold");
    }
    if (row.getItalic()) {
      textbox.getElement().addClassName("app-italic");
    }
    if (row.getUnderline()) {
      textbox.getElement().addClassName("app-underline");
    }
    GwtUtils.setJsPropertyString(textbox.getElement().getStyle(), "text-align", row.getAlign()); ;
    rowpanel.add(textbox);
    final OnsButton controlBtn = new OnsButton();
    controlBtn.addStyleName("app-edit-btn-cfg");
    controlBtn.setIconWhenAvailable("fa-bars");
    controlBtn.addTapHandler(new TapHandler() {
      public void onTap(TapEvent event) {
        switchControlbar(GwtUtils.getElement(controlBtn), index);
      }
    });
    rowpanel.add(controlBtn);
    textboxes.add(textbox);
    OnsListItem item = new OnsListItem();
    item.add(rowpanel);
    return item;
  }
  
  private OnsListItem createLastRowItem() {
    OnsHorizontalPanel rowpanel = new OnsHorizontalPanel();
    rowpanel.setWidth("100%");
    rowpanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
    final OnsButton addBtn = new OnsButton();
    addBtn.addStyleName("app-edit-btn-cfg");
    addBtn.setIcon("fa-plus-square");
    addBtn.setText("");
    addBtn.addTapHandler(new TapHandler() {
      public void onTap(TapEvent event) {
        OrderItemRow row = new OrderItemRowTx("");
        item.getRows().add(row);
        rowsPanel.insert(createRowItem(row), rowsPanel.getItemCount() - 1);
      }
    });
    rowpanel.add(addBtn);
    OnsListItem item = new OnsListItem();
    item.add(rowpanel);
    return item;
  }
  
  private OrderItem flushModel() {
    for (int it = 0; it < textboxes.size(); it++) {
      OnsTextBox textbox = textboxes.get(it);
      boolean lastRow = it == (textboxes.size() - 1);
      if (!lastRow || textbox.getText().trim().length() > 0) {
        if (item.getRows().size() <= it)
          item.getRows().add(new OrderItemRowTx());
        item.getRows().get(it).setText(textbox.getText());
      }
    }
    for (int it = item.getRows().size() - 1; it > 0; it--) {
      OrderItemRow row = item.getRows().get(it);
      if (row.getText().trim().length() == 0) {
        item.getRows().remove(it);
      } else {
        break;
      }
    }
    return item;
  }

  @UiHandler("btnSave")
  public void onBtnSave(TapEvent event) {
    getPresenter().saveOrderItemOnDevice(flushModel(), new Delegate<Order>() {
      public void execute(Order element) {
        getPresenter().goToPrevious();
      }
    });
  }

  @UiHandler("btnEdtBold")
  public void onBtnEdtBold(TapEvent event) {
    showTargetPosition(event.getTargetElement());
    OrderItemRow row = item.getRows().get(selectedRowIndex);
    row.setBold(!row.getBold());
    if (row.getBold()) {
      textboxes.get(selectedRowIndex).getElement().addClassName("app-bold");
    } else {
      textboxes.get(selectedRowIndex).getElement().replaceClassName("app-bold", "");
    }
  }
  
  @UiHandler("btnEdtItalic")
  public void onBtnEdtItalic(TapEvent event) {
    showTargetPosition(event.getTargetElement());
    OrderItemRow row = item.getRows().get(selectedRowIndex);
    row.setItalic(!row.getItalic());
    if (row.getItalic()) {
      textboxes.get(selectedRowIndex).getElement().addClassName("app-italic");
    } else {
      textboxes.get(selectedRowIndex).getElement().replaceClassName("app-italic", "");
    }
  }
  
  @UiHandler("btnEdtUnderline")
  public void onBtnEdtUnderline(TapEvent event) {
    showTargetPosition(event.getTargetElement());
    OrderItemRow row = item.getRows().get(selectedRowIndex);
    row.setUnderline(!row.getUnderline());
    if (row.getUnderline()) {
      textboxes.get(selectedRowIndex).getElement().addClassName("app-underline");
    } else {
      textboxes.get(selectedRowIndex).getElement().replaceClassName("app-underline", "");
    }
  }
  
  @UiHandler("btnEdtAlignLeft")
  public void onBtnEdtAlignLeft(TapEvent event) {
    showTargetPosition(event.getTargetElement());
    setItemAlign("left");
  }
  
  @UiHandler("btnEdtAlignCenter")
  public void onBtnEdtAlignCenter(TapEvent event) {
    showTargetPosition(event.getTargetElement());
    setItemAlign("center");
  }
  
  @UiHandler("btnEdtAlignRight")
  public void onBtnEdtAlignRight(TapEvent event) {
    showTargetPosition(event.getTargetElement());
    setItemAlign("right");
  }
  
  private void setItemAlign(String align) {
    OrderItemRow row = item.getRows().get(selectedRowIndex);
    row.setAlign(align);
    GwtUtils.setJsPropertyString(textboxes.get(selectedRowIndex).getElement().getStyle(), "text-align", align); ;
  }
  
  @UiHandler("btnEdtSize")
  public void onBtnEdtSize(TapEvent event) {
//  showFontSizeDialog();
    createFontSizeDialog();
  }
  
  private void createFontSizeDialog() {
    final ObjectWrapper<OnsDialog> dialog = new ObjectWrapper<OnsDialog>();
    String html = "";
    html += "<ons-list>";
    html += createFontSizeDialogItem(8, dialog);
    html += createFontSizeDialogItem(9, dialog);
    html += createFontSizeDialogItem(10, dialog);
    html += createFontSizeDialogItem(11, dialog);
    html += createFontSizeDialogItem(12, dialog);
    String id = OnsenUi.createUniqueElementId();
    html += "<ons-list-item><ons-button id='"+id+"' class='ons-button ng-isolate-scope button effeckt-button slide-left'>Annulla</ons-button></ons-list-item>";
    OnsenUi.addTapHandler(id, new TapHandler() {
      public void onTap(TapEvent event) {
        dialog.get().hide();
      }
    });
    html += "</ons-list>";
    dialog.set(OnsDialogUtils.createDialog(html));
  }
  
  private String createFontSizeDialogItem(final int size, final ObjectWrapper<OnsDialog> dialog) {
    OrderItemRow row = item.getRows().get(selectedRowIndex);
    int currentRowSize = row.getSize();
    boolean checked = size == currentRowSize;
    String id = OnsenUi.createUniqueElementId();
    String html = "<ons-list-item id='"+id+"'><input type='radio' "+ (checked ? "checked='true'" : "") +" />"+size+" points</ons-list-item>";
    OnsenUi.addTapHandler(id, new TapHandler() {
      public void onTap(TapEvent event) {
        setItemSize(size);
        dialog.get().hide();
      }
    });
    return html;
  }

  /*
  private void showFontSizeDialog() {
    fontSizeDialog.show();
    GwtUtils.deferredExecution(500, new Delegate<Void>() {
      public void execute(Void element) {
        setItemSizeChecked();
      }
    });
  }
  private void setItemSizeChecked() {
    PhgUtils.log("setItemSizeChecked --1--");
    OrderItemRow row = item.getRows().get(selectedRowIndex);
    int size = row.getSize();
    if (size >= 8) {
      PhgUtils.log("setItemSizeChecked --2--");
      String btnFontSizeId = "btnFntSize" + size;
      setRadioChecked(btnFontSizeId);
    } else {
      setRadioChecked("");
    }
  }
  @UiHandler("fontSizeCancelBtn")
  public void onFontSizeCancelBtn(TapEvent event) {
    fontSizeDialog.hide();
  }
  @UiHandler({"btnFntSize8", "btnFntSize9", "btnFntSize10", "btnFntSize11", "btnFntSize12"})
  public void onBtnFntSize(TapEvent event) {
    setItemSize(Integer.parseInt(((OnsListItem)event.getTargetWidget()).getValue()));
    fontSizeDialog.hide();
  }
  */
  
  private void setItemSize(double size) {
    OrderItemRow row = item.getRows().get(selectedRowIndex);
    row.setSize((int)size);
    textboxes.get(selectedRowIndex).getElement().getStyle().setFontSize(size / 10, Unit.EM);
  }
  
  @UiHandler("btnEdtFont")
  public void onBtnEdtFont(TapEvent event) {
//  showFontFamilyDialog();
    createFontFamilyDialog();
  }
  
  private void createFontFamilyDialog() {
    final ObjectWrapper<OnsDialog> dialog = new ObjectWrapper<OnsDialog>();
    String html = "";
    html += "<ons-list>";
    html += createFontFamilyDialogItem("Georgia, serif", "Georgia", dialog);
    html += createFontFamilyDialogItem("'Palatino Linotype', 'Book Antiqua', Palatino, serif", "Palatino", dialog);
    html += createFontFamilyDialogItem("'Times New Roman', Times, serif", "Times New Roman", dialog);
    html += createFontFamilyDialogItem("Arial, Helvetica, sans-serif", "Arial", dialog);
    String id = OnsenUi.createUniqueElementId();
    html += "<ons-list-item><ons-button id='"+id+"' class='ons-button ng-isolate-scope button effeckt-button slide-left'>Annulla</ons-button></ons-list-item>";
    OnsenUi.addTapHandler(id, new TapHandler() {
      public void onTap(TapEvent event) {
        dialog.get().hide();
      }
    });
    html += "</ons-list>";
    dialog.set(OnsDialogUtils.createDialog(html));
  }
  
  private String createFontFamilyDialogItem(final String fontFamily, final String fontFamilyDesc, final ObjectWrapper<OnsDialog> dialog) {
    OrderItemRow row = item.getRows().get(selectedRowIndex);
    String currentRowFontFamily = row.getFontFamily();
    boolean checked = fontFamily.equals(currentRowFontFamily);
    String id = OnsenUi.createUniqueElementId();
    String html = "<ons-list-item id='"+id+"'><input type='radio' "+ (checked ? "checked='true'" : "") +" />"+fontFamilyDesc+"</ons-list-item>";
    OnsenUi.addTapHandler(id, new TapHandler() {
      public void onTap(TapEvent event) {
        setFontFamily(fontFamily);
        dialog.get().hide();
      }
    });
    return html;
  }
  
  /*
  private void showFontFamilyDialog() {
    fontFamilyDialog.show();
    GwtUtils.deferredExecution(new Delegate<Void>() {
      public void execute(Void element) {
        setFontFamilyChecked();
      }
    });
  }
  private void setFontFamilyChecked() {
    OrderItemRow row = item.getRows().get(selectedRowIndex);
    String family = row.getFontFamily();
    if (family == null) {
      setRadioChecked("");
    } else if (family.toLowerCase().contains("georgia")) {
      setRadioChecked("btnFntFamGeorgia");
    } else if (family.toLowerCase().contains("palatino")) {
      setRadioChecked("btnFntFamPalatino");
    } else if (family.toLowerCase().contains("times")) {
      setRadioChecked("btnFntFamTimes");
    } else if (family.toLowerCase().contains("arial")) {
      setRadioChecked("btnFntFamArial");
    }
  }
  @UiHandler("fontFamCancelBtn")
  public void onFontFamCancelBtn(TapEvent event) {
    fontFamilyDialog.hide();
  }
  @UiHandler({"btnFntFamGeorgia", "btnFntFamPalatino", "btnFntFamTimes"})
  public void onBtnFntFamily(TapEvent event) {
    setFontFamily(((OnsListItem)event.getTargetWidget()).getValue());
    fontFamilyDialog.hide();
  }
  */

  /*
  private void setRadioChecked(String id) {
    NodeList<Element> elements = GwtUtils.getElementsByTagName("input");
    for (int it = 0; it < elements.getLength(); it++) {
      Element inputElem = elements.getItem(it);
      if ("radio".equalsIgnoreCase(inputElem.getAttribute("type"))) {
        if (id.equals(inputElem.getId())) {
          inputElem.setAttribute("checked", "true");
        } else {
          inputElem.removeAttribute("checked");
        }
      }
    }
  }
  */
  
  private void setFontFamily(String value) {
    OrderItemRow row = item.getRows().get(selectedRowIndex);
    row.setFontFamily(value);
    GwtUtils.setJsPropertyString(textboxes.get(selectedRowIndex).getElement().getStyle(), "fontFamily", value);
  }
  
  private void showTargetPosition(Element targetElement) {
    int tx = targetElement.getAbsoluteLeft();
    int ty = targetElement.getAbsoluteTop();
    PhgUtils.log("target.left = " + tx + " target.top = " + ty);
    GwtUtils.getElement(viewfinder).getStyle().setLeft(tx, Unit.PX);
    GwtUtils.getElement(viewfinder).getStyle().setTop(ty, Unit.PX);
    GwtUtils.getElement(viewfinder).getStyle().setZIndex(99);
  }
  
  private void switchControlbar(final Element tappedElement, int index) {
    PhgUtils.log(GwtUtils.getElementOffset(tappedElement));
    int x0 = GwtUtils.getElementOffsetLeft(tappedElement);
    int y0 = GwtUtils.getElementOffsetTop(tappedElement) - CONTROLBAR_VISIBLE_MARGIN_TOP;
    int x1 = 5;
    int y1 = y0;
    if (controlbarVisible && isLastTappedElement(tappedElement)) {
      PhgUtils.log("hiding crontrolbar");
      AnimitUtils.hideComposeControlbar(GwtUtils.getElement(controlbar), x0, y0, x1, y1, CONTROLBAR_VISIBLE_WIDTH, CONTROLBAR_VISIBLE_HEIGHT);
      if (overallEventListener != null) {
        OnsEventUtils.removeEventListener(overallEventListener);
        overallEventListener = null;
      }
    } else {
      selectedRowIndex = index;
      PhgUtils.log("showing crontrolbar");
      AnimitUtils.showComposeControlbar(GwtUtils.getElement(controlbar), x0, y0, x1, y1, CONTROLBAR_VISIBLE_WIDTH, CONTROLBAR_VISIBLE_HEIGHT);
      lastTappedElement = tappedElement;
      GwtUtils.deferredExecution(new Delegate<Void>() {
        public void execute(Void element) {
          overallEventListener = OnsEventUtils.addOverallEventListener(new Delegate<Element>() {
            public void execute(Element element) {
              if (OnsEventUtils.isContained(element, controlbar.getElement().getId())) {
                //nothing
              } else {
                switchControlbar(tappedElement, -1);
              }
            }
          });
        }
      });
    }
    controlbarVisible = !controlbarVisible;
  }
  
  private boolean isLastTappedElement(Element tappedElement) {
    return lastTappedElement == null || lastTappedElement.getId().equals(tappedElement.getId());
  }

}
