package it.mate.testleaflet.shared.service;

import it.mate.gwtcommons.shared.rpc.RpcMap;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RemoteFacadeAsync {

  void getServerTime(AsyncCallback<Date> callback);

  void sendDevInfo(RpcMap map, AsyncCallback<RpcMap> callback);

  void checkConnection(AsyncCallback<Boolean> callback);

  void getTimbri(AsyncCallback<List<RpcMap>> callback);

  void saveAccount(RpcMap entity, AsyncCallback<RpcMap> callback);

  void saveOrder(RpcMap entity, AsyncCallback<RpcMap> callback);

  void findOrdersByAccount(String accountId, Date lastUpdate, AsyncCallback<List<RpcMap>> callback);

  void checkForUpdates(String accountId, AsyncCallback<RpcMap> callback);

}
