package it.mate.testleaflet.client.view;

import it.mate.testleaflet.client.view.OrderListView.Presenter;
import it.mate.testleaflet.shared.model.Order;
import it.mate.testleaflet.shared.model.OrderItem;
import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.mvp.BasePresenter;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

public class OrderListView extends AbstractBaseView<Presenter> {
  
  public interface Presenter extends BasePresenter {
    public void goToOrderEdit(Order order);
  }
  
  public interface ViewUiBinder extends UiBinder<Widget, OrderListView> { }
  
  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
  
  @UiField FlexTable resultsTable;

  public OrderListView() {
    initUI();
  }
  
  protected void initUI() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  @Override
  public void setModel(Object model, String tag) {
    if (model instanceof List) {
      List<Order> orders = (List<Order>)model;
      populateTable(orders);
    }
  }
  
  private void populateTable(List<Order> orders) {
    
    int row = 0;
    
    for (final Order order : orders) {
      
      order.getItems();
      
      ClickHandler handler = new ClickHandler() {
        public void onClick(ClickEvent event) {
          getPresenter().goToOrderEdit(order);
        }
      };
      
      for (OrderItem item : order.getItems()) {
        
        Anchor aCodice = new Anchor(order.getCodice());
        aCodice.addClickHandler(handler);
        resultsTable.setWidget(row, 0, aCodice);
        
        Anchor aTimbro = new Anchor(item.getTimbro().getNome());
        aTimbro.addClickHandler(handler);
        resultsTable.setWidget(row, 1, aTimbro);
        
        row++;
        
      }
      
    }
    
  }

}
