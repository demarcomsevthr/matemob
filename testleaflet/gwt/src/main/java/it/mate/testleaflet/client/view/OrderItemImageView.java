package it.mate.testleaflet.client.view;

import it.mate.testleaflet.client.view.OrderItemImageView.Presenter;
import it.mate.testleaflet.shared.model.OrderItem;
import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.onscommons.client.event.TapEvent;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.ui.OnsList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class OrderItemImageView extends AbstractBaseView<Presenter> {

  public interface Presenter extends BasePresenter {
    public void saveCustomerImageOnOrderItem(OrderItem orderItem, Delegate<OrderItem> delegate);
    public void goToOrderItemImageView(OrderItem orderItem);
  }

  public interface ViewUiBinder extends UiBinder<Widget, OrderItemImageView> { }

  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
  
  @UiField HTML imgHtml;
  @UiField OnsList bottomList;
  
  OrderItem orderItem;
  
  public OrderItemImageView() {
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
//  OnsenUi.suspendCompilations();
    if (model instanceof OrderItem) {
      this.orderItem = (OrderItem)model;
      if (orderItem.getCustomerImage() != null) {
        OnsenUi.onAvailableElement(imgHtml, new Delegate<Element>() {
          public void execute(Element element) {
            String html = "<img class='app-timbri-image-panel' src='"+ orderItem.getCustomerImage() +"'/>";
            element.setInnerHTML(html);
          }
        });
      }
    }
//  OnsenUi.refreshCurrentPage();
  }
  
  @UiHandler ("btnAdd")
  public void onBtnAdd(TapEvent event) {
    getPresenter().saveCustomerImageOnOrderItem(orderItem, new Delegate<OrderItem>() {
      public void execute(OrderItem orderItem) {
        getPresenter().goToOrderItemImageView(orderItem);
      }
    });
  }
  
}
