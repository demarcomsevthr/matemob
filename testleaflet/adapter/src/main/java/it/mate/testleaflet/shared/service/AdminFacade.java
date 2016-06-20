package it.mate.testleaflet.shared.service;

import it.mate.testleaflet.shared.model.Account;
import it.mate.testleaflet.shared.model.Order;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath(".adminFacade")
public interface AdminFacade extends RemoteService {
  
  public List<Order> findAllOrders() throws FacadeException;
  
  public Order findOrderById(String id) throws FacadeException;
  
  public Order saveOrder(Order order) throws FacadeException;
  
  public List<Account> findAllAccounts() throws FacadeException;
  
  public void sendPushNotification(Account account, String message, String regId) throws FacadeException;
  
}
