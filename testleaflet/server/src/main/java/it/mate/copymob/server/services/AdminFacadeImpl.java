package it.mate.testleaflet.server.services;

import it.mate.commons.server.utils.LoggingUtils;
import it.mate.testleaflet.shared.model.Account;
import it.mate.testleaflet.shared.model.Order;
import it.mate.testleaflet.shared.service.AdminFacade;
import it.mate.testleaflet.shared.service.FacadeException;

import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class AdminFacadeImpl extends RemoteServiceServlet implements AdminFacade {

  private MainAdapter adapter = null;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    AdapterUtil.initContext(config.getServletContext());
    adapter = AdapterUtil.getMainAdapter();
  }

  @Override
  public List<Order> findAllOrders() throws FacadeException {
    try {
      LoggingUtils.debug(getClass(), "before find");
      return adapter.findAllOrders();
    } catch (Exception ex) {
      LoggingUtils.error(getClass(), "errore", ex);
      throw new FacadeException(ex.getMessage(), ex);
    }
  }

  @Override
  public Order findOrderById(String id) throws FacadeException {
    try {
      return adapter.findOrderById(id);
    } catch (Exception ex) {
      LoggingUtils.error(getClass(), "errore", ex);
      throw new FacadeException(ex.getMessage(), ex);
    }
  }
  
  public Order saveOrder(Order order) throws FacadeException {
    try {
      return adapter.saveOrder(order);
    } catch (Exception ex) {
      LoggingUtils.error(getClass(), "errore", ex);
      throw new FacadeException(ex.getMessage(), ex);
    }
  }
  
  @Override
  public List<Account> findAllAccounts() throws FacadeException {
    try {
      List<Account> accounts = adapter.findAllAccounts();
      LoggingUtils.debug(getClass(), "found " + accounts.size() + " accounts");
      return accounts;
    } catch (Exception ex) {
      LoggingUtils.error(getClass(), "errore", ex);
      throw new FacadeException(ex.getMessage(), ex);
    }
  }
  
  @Override
  public void sendPushNotification(Account account, String message, String regId) throws FacadeException {
    try {
      adapter.sendPushNotification(account, message, regId);
    } catch (Exception ex) {
      LoggingUtils.error(getClass(), "errore", ex);
      throw new FacadeException(ex.getMessage(), ex);
    }
  }

}
