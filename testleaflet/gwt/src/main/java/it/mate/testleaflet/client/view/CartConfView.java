package it.mate.testleaflet.client.view;

import it.mate.testleaflet.client.activities.Tags;
import it.mate.testleaflet.client.view.CartConfView.Presenter;
import it.mate.testleaflet.shared.model.Order;
import it.mate.testleaflet.shared.model.OrderItem;
import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.gwtcommons.client.ui.Spacer;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.event.TapEvent;
import it.mate.onscommons.client.event.TapHandler;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.ui.OnsButton;
import it.mate.onscommons.client.ui.OnsList;
import it.mate.onscommons.client.ui.OnsListItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class CartConfView extends AbstractBaseView<Presenter> {

  public interface Presenter extends BasePresenter {
    public void saveOrderOnServer(Order order, Delegate<Order> delegate);
    public void goToHomeView();
  }

  public interface ViewUiBinder extends UiBinder<Widget, CartConfView> { }

  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
  
  @UiField OnsList list;
  
  OnsButton confermaBtn = null;
  
  private Order order;
  
  public CartConfView() {
    initUI();
  }

  private void initUI() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  @Override
  public void setModel(Object model, String tag) {
    if (model instanceof Order) {
      this.order = (Order)model;
      populateList();
    } else if (Tags.SAVE_ORDER_ON_PROGRESS.equals(tag)) {
      saveOrder();
    }
  }
  
  private void populateList() {
    OnsenUi.suspendCompilations();
    list.add(createConfermaItem());
    OnsenUi.refreshCurrentPage();
  }
  
  private OnsListItem createConfermaItem() {
    OnsListItem listItem = null;
    if (confermaBtn == null) {
      listItem = new OnsListItem();
      listItem.add(new Spacer("1px", "0.5em"));
      
      double importo = 0;
      for (OrderItem orderItem : order.getItems()) {
        importo +=  orderItem.getQuantity() * orderItem.getTimbro().getPrezzo();
      }
      Label importoLbl = new Label("Importo totale: " + GwtUtils.formatDecimal(importo, 2) +" €");
      listItem.add(importoLbl);
      
      listItem.add(new Spacer("1px", "2em"));
      
      confermaBtn = new OnsButton();
      confermaBtn.setText("CONFERMA");
      confermaBtn.addTapHandler(new TapHandler() {
        public void onTap(TapEvent event) {
          saveOrder();
        }
      });
      listItem.add(confermaBtn);
      listItem.add(new Spacer("1px", "0.5em"));
      OnsButton annullaBtn = new OnsButton();
      annullaBtn.setText("ANNULLA");
      annullaBtn.addTapHandler(new TapHandler() {
        public void onTap(TapEvent event) {
          getPresenter().goToHomeView();
        }
      });
      listItem.add(annullaBtn);
      listItem.add(new Spacer("1px", "0.5em"));
    }
    return listItem;
  }
  
  private void saveOrder() {
    if (order == null) {
      return;
    }
    getPresenter().saveOrderOnServer(order, new Delegate<Order>() {
      public void execute(Order element) {
        getPresenter().goToHomeView();
      }
    });
  }
  
}
