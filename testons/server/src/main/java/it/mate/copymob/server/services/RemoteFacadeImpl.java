package it.mate.testons.server.services;

import it.mate.commons.server.utils.CloneUtils;
import it.mate.testons.shared.model.Order;
import it.mate.testons.shared.model.Timbro;
import it.mate.testons.shared.model.impl.AccountTx;
import it.mate.testons.shared.model.impl.DevInfoTx;
import it.mate.testons.shared.model.impl.OrderTx;
import it.mate.testons.shared.model.impl.TimbroTx;
import it.mate.testons.shared.service.FacadeException;
import it.mate.testons.shared.service.RemoteFacade;
import it.mate.gwtcommons.shared.rpc.RpcMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class RemoteFacadeImpl extends RemoteServiceServlet implements RemoteFacade {

  private static Logger logger = Logger.getLogger(RemoteFacadeImpl.class);
  
  private MainAdapter adapter = null;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    AdapterUtil.initContext(config.getServletContext());
    adapter = AdapterUtil.getMainAdapter();
    logger.debug("initialized " + this);
  }
  
  @Override
  public Date getServerTime() {
    return new Date();
  }
  
  @Override
  public RpcMap sendDevInfo(RpcMap map) {
    DevInfoTx devInfo = new DevInfoTx().fromRpcMap(map);
    devInfo.setDevIp(getThreadLocalRequest().getRemoteAddr());
    devInfo = (DevInfoTx)adapter.sendDevInfo(devInfo);
    return devInfo.toRpcMap();
  }

  @Override
  public RpcMap saveAccount(RpcMap entity) {
    AccountTx tx = new AccountTx().fromRpcMap(entity);
    tx = (AccountTx)adapter.saveAccount(tx);
    return tx.toRpcMap();
  }
  
  @Override
  public Boolean checkConnection() {
    return true;
  }
  
  @Override
  public List<RpcMap> getTimbri() throws FacadeException {
    try {
      List<RpcMap> results = new ArrayList<RpcMap>();
      List<Timbro> timbri = adapter.getTimbri();
      for (Timbro timbro : timbri) {
        results.add(CloneUtils.clone(timbro, TimbroTx.class).toRpcMap());
      }
      return results;
    } catch (Exception ex) {
      throw new FacadeException(ex.getMessage());
    }
  }

  @Override
  public RpcMap saveOrder(RpcMap entity) {
    OrderTx tx = new OrderTx().fromRpcMap(entity);
    tx = (OrderTx)adapter.saveOrder(tx);
    RpcMap resultMap = tx.toRpcMap();
    logger.debug("RESULT MAP = " + resultMap);
    return resultMap;
  }

  @Override
  public List<RpcMap> findOrdersByAccount(String accountId, Date lastUpdate) throws FacadeException {
    try {
      List<RpcMap> results = new ArrayList<RpcMap>();
      List<Order> orders = adapter.findOrdersByAccount(accountId, lastUpdate);
      for (Order order : orders) {
        OrderTx tx = CloneUtils.clone(order, OrderTx.class);
        results.add(tx.toRpcMap());
      }
      return results;
    } catch (Exception ex) {
      throw new FacadeException(ex.getMessage());
    }
  }
  
  @Override
  public RpcMap checkForUpdates(String accountId) throws FacadeException {
    try {
      RpcMap results = new RpcMap();
      List<Order> updatedOrders = adapter.findUpdatedOrdersByAccount(accountId);
      results.putField("updatedOrders", updatedOrders);
      return results;
    } catch (Exception ex) {
      throw new FacadeException(ex.getMessage());
    }
  }
  
}
