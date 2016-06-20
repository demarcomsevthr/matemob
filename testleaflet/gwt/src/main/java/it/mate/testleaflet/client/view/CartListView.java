package it.mate.testleaflet.client.view;

import it.mate.testleaflet.client.view.CartListView.Presenter;
import it.mate.testleaflet.shared.model.Order;
import it.mate.testleaflet.shared.model.OrderItem;
import it.mate.testleaflet.shared.model.Timbro;
import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.gwtcommons.client.ui.Spacer;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.event.TapEvent;
import it.mate.onscommons.client.event.TapHandler;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.ui.OnsButton;
import it.mate.onscommons.client.ui.OnsHorizontalPanel;
import it.mate.onscommons.client.ui.OnsLabel;
import it.mate.onscommons.client.ui.OnsList;
import it.mate.onscommons.client.ui.OnsListItem;
import it.mate.phgcommons.client.utils.PhgUtils;

import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CartListView extends AbstractBaseView<Presenter> {

  public interface Presenter extends BasePresenter {
    public void goToOrderItemEditView(OrderItem orderItem);
    public void goToHomeView();
    public void goToCartConfView(Order order);
  }

  public interface ViewUiBinder extends UiBinder<Widget, CartListView> { }

  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
  
  @UiField Panel wrapperPanel;
  @UiField OnsList cartList;
  
  private Order order;
  
  public CartListView() {
    initUI();
  }

  private void initUI() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  @Override
  public void setModel(Object model, String tag) {
    if (model == null) {
      OnsLabel emptyLbl = new OnsLabel("Il carrello è vuoto");
      emptyLbl.addStyleName("app-cart-empty-lbl");
      cartList.add(emptyLbl);
      OnsenUi.refreshCurrentPage();
    } else if (model instanceof Order) {
      this.order = (Order)model;
      populateList();
    }
  }
  
  private void populateList() {
    OnsenUi.suspendCompilations();
    Iterator<OrderItem> it = order.getItems().iterator();
    while (it.hasNext()) {
      final OrderItem orderItem = it.next();
      TapHandler orderItemTapHandler = new TapHandler() {
        public void onTap(TapEvent event) {
          getPresenter().goToOrderItemEditView(orderItem);
        }
      };
      Timbro timbro = orderItem.getTimbro();
      OnsListItem listItem = new OnsListItem();
      
      OnsHorizontalPanel rowPanel = new OnsHorizontalPanel();
      rowPanel.setAddDirect(true);
      
      String html = "<img src='"+ timbro.getImageData() +"' class='app-cart-item-img'/>";
      HTML imgHtml = new HTML(html);
      OnsenUi.addTapHandler(imgHtml, orderItemTapHandler);
      rowPanel.add(imgHtml);
      
      OnsLabel nameLbl = new OnsLabel(timbro.getNome());
      OnsenUi.addTapHandler(nameLbl, orderItemTapHandler);
      nameLbl.addStyleName("app-cart-item-name");
      rowPanel.add(nameLbl);
      
      rowPanel.add(new Spacer("2em"));
      
      rowPanel.add(createDetailPanel(orderItem));
//    rowPanel.add(qtaPanel);
      
      OnsHorizontalPanel actionsPanel = new OnsHorizontalPanel();
      actionsPanel.setAddDirect(true);
      OnsButton editBtn = new OnsButton("");
      editBtn.addStyleName("app-cart-btn-edit");
      editBtn.setIcon("fa-edit");
      editBtn.setModifier("quiet");
      editBtn.addTapHandler(orderItemTapHandler);
      actionsPanel.add(editBtn);
      
      rowPanel.add(actionsPanel);
      
      listItem.add(rowPanel);
      
      cartList.add(listItem);

    }
    
    OnsenUi.refreshCurrentPage();
    
  }
  
  private VerticalPanel createDetailPanel(final OrderItem orderItem) {
    
    VerticalPanel detailPanel = new VerticalPanel();
    detailPanel.addStyleName("app-cart-detail-panel");
    
    final OnsLabel prezzoBox = new OnsLabel("Prezzo: ");
    prezzoBox.addStyleName("app-cart-prezzo-label");
    
    OnsHorizontalPanel qtaPanel = new OnsHorizontalPanel();
    qtaPanel.addStyleName("app-cart-qta-panel");
    qtaPanel.setAddDirect(true);

    OnsLabel qtaLabel = new OnsLabel("Qtà:");
    qtaLabel.addStyleName("app-cart-item-label");
    
    final OnsLabel qtaBox = new OnsLabel();
    setQta(qtaBox, prezzoBox, orderItem);
    
    OnsButton plusBtn = new OnsButton("");
    plusBtn.addStyleName("app-cart-btn-plus");
    plusBtn.setIcon("fa-plus-circle");
    plusBtn.setModifier("quiet");
    plusBtn.addTapHandler(new TapHandler() {
      public void onTap(TapEvent event) {
        PhgUtils.log("plus");
        double qta = orderItem.getQuantity();
        qta ++;
        orderItem.setQuantity(qta);
        setQta(qtaBox, prezzoBox, orderItem);
      }
    });
    
    OnsButton minusBtn = new OnsButton();
    minusBtn.getElement().removeClassName("ons-button");
    minusBtn.addStyleName("app-cart-btn-minus");
    minusBtn.setIcon("fa-minus-circle");
    minusBtn.setModifier("quiet");
    minusBtn.addTapHandler(new TapHandler() {
      public void onTap(TapEvent event) {
        double qta = orderItem.getQuantity();
        if (qta > 0) {
          qta --;
          orderItem.setQuantity(qta);
          setQta(qtaBox, prezzoBox, orderItem);
        }
      }
    });
    
    qtaPanel.add(qtaLabel);
    qtaPanel.add(plusBtn);
    qtaPanel.add(qtaBox);
    qtaPanel.add(minusBtn);
    
    detailPanel.add(qtaPanel);
    
    detailPanel.add(prezzoBox);
    
    return detailPanel;
  }
  
  private void setQta(OnsLabel qtaBox, OnsLabel prezzoBox, OrderItem orderItem) {
    qtaBox.setText(GwtUtils.formatDecimal(orderItem.getQuantity(), 0));
    prezzoBox.setText("Prezzo: " + GwtUtils.formatDecimal(orderItem.getQuantity() * orderItem.getTimbro().getPrezzo(), 2) + "€");
  }
  
  @UiHandler("btnGo")
  public void onBtnGo(TapEvent event) {
    getPresenter().goToCartConfView(order);
    /*
    getPresenter().saveOrderOnServer(order, new Delegate<Order>() {
      public void execute(Order element) {
        getPresenter().goToHomeView();
      }
    });
    */
  }
  
}
