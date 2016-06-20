package it.mate.testleaflet.client.view;

import it.mate.testleaflet.client.view.OrderListView.Presenter;
import it.mate.testleaflet.shared.model.Order;
import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.gwtcommons.client.ui.Spacer;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.event.TapEvent;
import it.mate.onscommons.client.event.TapHandler;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.ui.OnsHorizontalPanel;
import it.mate.onscommons.client.ui.OnsIcon;
import it.mate.onscommons.client.ui.OnsLabel;
import it.mate.onscommons.client.ui.OnsList;
import it.mate.onscommons.client.ui.OnsListItem;
import it.mate.onscommons.client.ui.OnsVerticalPanel;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class OrderListView extends AbstractBaseView<Presenter> {

  public interface Presenter extends BasePresenter {
    public void goToHomeView();
    public void goToOrderEditView(Order order);
    public void saveOrderOnDevice(Order order, Delegate<Order> delegate);
  }

  public interface ViewUiBinder extends UiBinder<Widget, OrderListView> { }

  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
  
  @UiField OnsList orderList;
  
  private List<Order> orders;
  
  public OrderListView() {
    initUI();
  }

  private void initProvidedElements() {

  }

  private void initUI() {
    initProvidedElements();
    initWidget(uiBinder.createAndBindUi(this));
  }

  @Override
  public void setModel(Object model, String tag) {
    if (model == null) {
      OnsLabel emptyLbl = new OnsLabel("Non ci sono ordini inseriti");
      emptyLbl.addStyleName("app-cart-empty-lbl");
      orderList.add(emptyLbl);
      OnsenUi.refreshCurrentPage();
    } else if (model instanceof List) {
      this.orders = (List)model;
      populateList();
    }
  }
  
  private void populateList() {
    
    OnsenUi.suspendCompilations();
    
    for (final Order order : orders) {
      
      TapHandler orderTapHandler = new TapHandler() {
        public void onTap(TapEvent event) {
          
          if ("U".equals(order.getUpdateState())) {
            order.setUpdateState("V");
            getPresenter().saveOrderOnDevice(order, new Delegate<Order>() {
              public void execute(Order order) {
                getPresenter().goToOrderEditView(order);
              }
            });
          } else {
            getPresenter().goToOrderEditView(order);
          }
          
        }
      };
      
      OnsListItem listItem = new OnsListItem();
      listItem.setModifier("chevron");
      listItem.addTapHandler(orderTapHandler);
      
      OnsHorizontalPanel itemPanel = new OnsHorizontalPanel();
      
      OnsIcon ordIcon = new OnsIcon();
      //OnsenUi.addTapHandler(ordIcon, orderTapHandler);
      ordIcon.setIcon("fa-circle-o");
      ordIcon.addStyleName("app-order-list-item-icon");
      itemPanel.add(ordIcon);
      
      OnsVerticalPanel dataPanel = new OnsVerticalPanel();
      dataPanel.addStyleName("app-order-list-item-data-panel");
      
      OnsHorizontalPanel row1Panel = new OnsHorizontalPanel();
      row1Panel.setAddDirect(true);
      
      row1Panel.add(new Spacer("1em"));
      
      OnsLabel nameLbl = new OnsLabel("Ordine " + order.getCodice());
      //OnsenUi.addTapHandler(nameLbl, orderTapHandler);
      nameLbl.addStyleName("app-order-list-item-name");
      row1Panel.add(nameLbl);
      
      if (order.getCreated() != null) {
        OnsLabel createdLbl = new OnsLabel(GwtUtils.dateToString(order.getCreated(), "dd/MM/yyyy") );
        //OnsenUi.addTapHandler(createdLbl, orderTapHandler);
        createdLbl.addStyleName("app-order-list-item-date");
        row1Panel.add(createdLbl);
      }
      
      dataPanel.add(row1Panel);
      
      OnsHorizontalPanel row2Panel = new OnsHorizontalPanel();
      row2Panel.setAddDirect(true);
      
      row2Panel.add(new Spacer("1em"));
      
      String descStato = "";
      if (order.getState() == Order.STATE_SENT) {
        descStato = "inviato";
      } else if (order.getState() == Order.STATE_RECEIVED) {
        descStato = "registrato";
      } else if (order.getState() == Order.STATE_PREVIEW_IN_PROGRESS) {
        descStato = "preelaborazione in corso";
      } else if (order.getState() == Order.STATE_PREVIEW_AVAILABLE) {
        descStato = "immagine di controllo disponibile";
      } else if (order.getState() == Order.STATE_PREVIEW_PAYED) {
        descStato = "pagato";
      } else if (order.getState() == Order.STATE_WORK_IN_PROGRESS) {
        descStato = "in lavorazione";
      } else if (order.getState() == Order.STATE_SHIPED) {
        descStato = "spedito";
      } else if (order.getState() == Order.STATE_CLOSE) {
        descStato = "chiuso";
      }
      
      OnsLabel stateLbl = new OnsLabel("Stato: " + descStato );
      //OnsenUi.addTapHandler(stateLbl, orderTapHandler);
      stateLbl.addStyleName("app-order-list-item-state");
      if ("U".equals(order.getUpdateState())) {
        stateLbl.getElement().getStyle().setFontWeight(FontWeight.BOLD);
      }
      row2Panel.add(stateLbl);
      
      dataPanel.add(row2Panel);
      
      itemPanel.add(dataPanel);
      
      listItem.add(itemPanel);
      
      orderList.add(listItem);
      
    }

    OnsenUi.refreshCurrentPage();
    
  }
  
}
