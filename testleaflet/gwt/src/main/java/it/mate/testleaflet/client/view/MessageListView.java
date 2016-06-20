package it.mate.testleaflet.client.view;

import it.mate.testleaflet.client.view.MessageListView.Presenter;
import it.mate.testleaflet.shared.model.Message;
import it.mate.testleaflet.shared.model.Order;
import it.mate.testleaflet.shared.model.OrderItem;
import it.mate.testleaflet.shared.model.impl.MessageTx;
import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.event.TapEvent;
import it.mate.onscommons.client.event.TapHandler;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.ui.OnsCssPopover;
import it.mate.onscommons.client.ui.OnsList;
import it.mate.onscommons.client.ui.OnsListItem;
import it.mate.onscommons.client.ui.OnsScroller;
import it.mate.onscommons.client.ui.OnsTextArea;
import it.mate.phgcommons.client.utils.PhgUtils;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class MessageListView extends AbstractBaseView<Presenter> {

  public interface Presenter extends BasePresenter {
    public void showMenu();
    public void saveOrderItemOnDevice(OrderItem orderItem, Delegate<Order> delegate);
  }

  public interface ViewUiBinder extends UiBinder<Widget, MessageListView> { }

  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
  
  @UiField Panel wrapperPanel;
  @UiField OnsList list;
  @UiField OnsScroller scroller;
  @UiField OnsTextArea txtNewMessage;

  OrderItem orderItem;
  List<Message> messages;
  
  public MessageListView() {
    initUI();
  }

  private void initProvidedElements() {

  }

  private void initUI() {
    initProvidedElements();
    initWidget(uiBinder.createAndBindUi(this));
    if (OnsenUi.isSlidingMenuPresent()) {
      OnsenUi.getSlidingMenu().setSwipeable(false);
    }
  }
  
  @Override
  public void onDetachView() {
    super.onDetachView();
    if (OnsenUi.isSlidingMenuPresent()) {
      OnsenUi.getSlidingMenu().setSwipeable(true);
    }
  }
  
  @Override
  @SuppressWarnings("unchecked")
  public void setModel(Object model, String tag) {
    if (model instanceof List) {
      messages = (List<Message>)model;
      populateList();
    } else if (model instanceof OrderItem) { 
      orderItem = (OrderItem)model;
      messages = orderItem.getMessages();
      
      PhgUtils.log("MESSAGE LIST VIEW --> " + orderItem);
      
      populateList();
    }
  }

  /*
  @UiHandler("btnMenu")
  public void onBtnMenu(TapEvent event) {
    getPresenter().showMenu();
  }
  */
  
  private void populateList() {
    if (messages != null) {
      for (final Message message : messages) {
        OnsListItem item = new OnsListItem();
        item.addStyleName("app-ons-list-item-message");

        String html = "";
        html += "<table>";
        html += "<tr>";
        html += "<td>";
        html += "<div class='app-ons-list-item-message-content'>";
        html += message.getText();
        html += "</div>";
        html += "</td>";
        html += "</tr>";
        html += "</table>";
        
        OnsCssPopover popover = new OnsCssPopover(html, "left");
        item.add(popover);
        
        item.addTapHandler(new TapHandler() {
          public void onTap(TapEvent event) {
            PhgUtils.log("tapped message " + message.getId());
          }
        });
        
        list.add(item);
        
        
      }
      
      GwtUtils.deferredExecution(500, new Delegate<Void>() {
        public void execute(Void element) {
          scroller.compile();
//        list.compile();
        }
      });
      
    }
  }
  
  
  @UiHandler("btnSend")
  public void onBtnSend(TapEvent event) {
    if (orderItem != null) {
      Message message = new MessageTx();
      message.setOrderItemId(orderItem.getId());
      message.setData(new Date());
      message.setText(txtNewMessage.getValue());
      List<Message> messages = orderItem.getMessages();
      messages.add(message);
      orderItem.setMessages(messages);
      getPresenter().saveOrderItemOnDevice(orderItem, new Delegate<Order>() {
        public void execute(Order order) {
          getPresenter().goToPrevious();
        }
      });
    }
  }
  
}
