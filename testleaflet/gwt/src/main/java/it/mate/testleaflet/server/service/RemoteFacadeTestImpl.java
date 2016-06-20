package it.mate.testleaflet.server.service;

import it.mate.testleaflet.shared.service.FacadeException;
import it.mate.testleaflet.shared.service.RemoteFacade;
import it.mate.gwtcommons.shared.rpc.RpcMap;

import java.util.Date;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings({"serial"})
public class RemoteFacadeTestImpl extends RemoteServiceServlet implements RemoteFacade {

  private final boolean LOCALTEST = false;
  
  private static Logger logger = Logger.getLogger(RemoteFacadeTestImpl.class);
  
  private RemoteFacade remoteFacade = null;
  
  private final String REMOTE_SERVICE_RELATIVE_PATH = ".remoteFacade";
  
  private String moduleBaseUrl;
  
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    moduleBaseUrl = LOCALTEST ? "http://127.0.0.1:8080/main/" : "https://testleafletsrv.appspot.com/main/"; 
    remoteFacade = (RemoteFacade)SyncProxy.newProxyInstance(RemoteFacade.class, moduleBaseUrl, REMOTE_SERVICE_RELATIVE_PATH);
    logger.debug("initialized " + this);
    logger.debug("moduleBaseUrl = " + moduleBaseUrl);
  }
  
  @Override
  public Date getServerTime() {
    logger.debug("calling " + moduleBaseUrl);
    return remoteFacade.getServerTime();
  }
  
  @Override
  public RpcMap sendDevInfo(RpcMap map) {
    logger.debug("calling " + moduleBaseUrl);
    return remoteFacade.sendDevInfo(map);
  }

  @Override
  public RpcMap saveAccount(RpcMap entity) {
    logger.debug("calling " + moduleBaseUrl);
    return remoteFacade.saveAccount(entity);
  }
  
  @Override
  public Boolean checkConnection() {
    logger.debug("calling " + moduleBaseUrl);
    return remoteFacade.checkConnection();
  }

  @Override
  public List<RpcMap> getTimbri() throws FacadeException {
    logger.debug("calling " + moduleBaseUrl);
    return remoteFacade.getTimbri();
  }

  @Override
  public RpcMap saveOrder(RpcMap entity) {
    logger.debug("calling " + moduleBaseUrl);
    return remoteFacade.saveOrder(entity);
  }

  @Override
  public List<RpcMap> findOrdersByAccount(String accountId, Date lastUpdate) throws FacadeException {
    logger.debug("calling " + moduleBaseUrl);
    return remoteFacade.findOrdersByAccount(accountId, lastUpdate);
  }

  @Override
  public RpcMap checkForUpdates(String accountId) throws FacadeException {
    logger.debug("calling " + moduleBaseUrl);
    return remoteFacade.checkForUpdates(accountId);
  }

}
