package it.mate.testleaflet.shared.service;

import it.mate.testleaflet.shared.model.Account;
import it.mate.testleaflet.shared.model.Order;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AdminFacadeAsync {

  void findAllOrders(AsyncCallback<List<Order>> callback);

  void findOrderById(String id, AsyncCallback<Order> callback);

  void saveOrder(Order order, AsyncCallback<Order> callback);

  void findAllAccounts(AsyncCallback<List<Account>> callback);

  void sendPushNotification(Account account, String message, String regId, AsyncCallback<Void> callback);

}
