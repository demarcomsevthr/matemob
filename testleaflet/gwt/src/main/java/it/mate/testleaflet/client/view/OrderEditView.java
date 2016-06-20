package it.mate.testleaflet.client.view;

import it.mate.testleaflet.client.view.OrderEditView.Presenter;
import it.mate.testleaflet.shared.model.Order;
import it.mate.testleaflet.shared.model.OrderItem;
import it.mate.testleaflet.shared.model.Timbro;
import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.gwtcommons.client.ui.Spacer;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.event.TapEvent;
import it.mate.onscommons.client.event.TapHandler;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.ui.OnsButton;
import it.mate.onscommons.client.ui.OnsHorizontalPanel;
import it.mate.onscommons.client.ui.OnsLabel;
import it.mate.onscommons.client.ui.OnsList;
import it.mate.onscommons.client.ui.OnsListItem;

import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class OrderEditView extends AbstractBaseView<Presenter> {

  public interface Presenter extends BasePresenter {
    public void saveOrderOnServer(Order order, Delegate<Order> delegate);
    public void goToOrderItemEditView(OrderItem orderItem);
    public void goToHomeView();
  }

  public interface ViewUiBinder extends UiBinder<Widget, OrderEditView> { }

  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
  

  @UiField OnsList itemList;
  
  private Order order;
  
  public OrderEditView() {
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
      itemList.add(new OnsLabel("ORDINE VUOTO!"));
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
      
      OnsHorizontalPanel qtaPanel = new OnsHorizontalPanel();
      qtaPanel.setAddDirect(true);

      final OnsLabel qtaFld = new OnsLabel();
      setQtaLbl(qtaFld, orderItem.getQuantity());

      qtaPanel.add(qtaFld);
      
      rowPanel.add(qtaPanel);
      
      OnsHorizontalPanel actionsPanel = new OnsHorizontalPanel();
      actionsPanel.setAddDirect(true);
      
      OnsButton editBtn = new OnsButton("");
      editBtn.addStyleName("app-order-item-btn-edit");
      editBtn.setIcon("fa-bars");
      editBtn.setModifier("quiet");
      editBtn.addTapHandler(orderItemTapHandler);
      actionsPanel.add(editBtn);
      
      OnsenUi.onAvailableElement(editBtn, new Delegate<Element>() {
        public void execute(Element editBtnElement) {
          Element tdElement = editBtnElement.getParentElement();
          tdElement.getStyle().setPosition(Position.ABSOLUTE);
          tdElement.getStyle().setRight(0, Unit.PX);
          tdElement.getStyle().setPaddingTop(2, Unit.PCT);
          tdElement.getStyle().setPaddingRight(2, Unit.PCT);
        }
      });
      
      rowPanel.add(actionsPanel);
      
      listItem.add(rowPanel);
      
      itemList.add(listItem);

    }
    
    OnsenUi.refreshCurrentPage();
    
  }
  
  private void setQtaLbl(OnsLabel lbl, double qta) {
    lbl.setText(GwtUtils.formatDecimal(qta, 0));
  }
  
}
