package it.mate.testons.client.view;

import it.mate.testons.client.view.OrderEditView.Presenter;
import it.mate.testons.shared.model.Order;
import it.mate.testons.shared.model.OrderItem;
import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class OrderEditView extends AbstractBaseView<Presenter> {
  
  public interface Presenter extends BasePresenter {
    public void updateOrder(Order order, Delegate<Order> delegate);
  }
  
  public interface ViewUiBinder extends UiBinder<Widget, OrderEditView> { }
  
  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
  
  @UiField FlexTable flexTable;
  @UiField ListBox lbxOrderStates;
  
  private Order order;
  
  public OrderEditView() {
    initUI();
  }
  
  protected void initUI() {
    initWidget(uiBinder.createAndBindUi(this));
    
    lbxOrderStates.addItem("Ricevuto", ""+Order.STATE_RECEIVED);
    lbxOrderStates.addItem("Preventivo in elaborazione", ""+Order.STATE_PREVIEW_IN_PROGRESS);
    lbxOrderStates.addItem("Preventivo disponibile", ""+Order.STATE_PREVIEW_AVAILABLE);
    lbxOrderStates.addItem("Pagato", ""+Order.STATE_PREVIEW_PAYED);
    lbxOrderStates.addItem("In lavorazione", ""+Order.STATE_WORK_IN_PROGRESS);
    lbxOrderStates.addItem("Spedito", ""+Order.STATE_SHIPED);
    lbxOrderStates.addItem("Chiuso", ""+Order.STATE_CLOSE);
    
  }

  @Override
  public void setModel(Object model, String tag) {
    if (model instanceof Order) {
      this.order = (Order)model;
      updateOrderState(order);
      populateTable(order);
    }
  }
  
  private void updateOrderState(Order order) {
    String orderStateCode = order.getState().toString();
    for (int it = 0; it < lbxOrderStates.getItemCount(); it++) {
      if (lbxOrderStates.getValue(it).equals(orderStateCode) ) {
        lbxOrderStates.setSelectedIndex(it);
      }
    }
  }
  
  private void populateTable(Order order) {
    int row = 0;
    for (OrderItem item : order.getItems()) {
      OrderItemView itemView = new OrderItemView();
      itemView.setPresenter((OrderItemView.Presenter)getPresenter());
      itemView.setModel(item);
      itemView.setOrder(order);
      flexTable.setWidget(row, 0, itemView.asWidget());
      row++;
    }
  }
  
  private Order flushOrder() {
    
    String stateCode = lbxOrderStates.getValue(lbxOrderStates.getSelectedIndex());
    Integer newState = Integer.parseInt(stateCode);
    order.setState(newState);
    
    return order;
  }

  @UiHandler ("btnUpdate")
  public void onBtnUpdate(ClickEvent event) {
    getPresenter().updateOrder(flushOrder(), new Delegate<Order>() {
      public void execute(Order element) {
        GwtUtils.log("ORDINE SALVATO!");
      }
    });
  }

}
