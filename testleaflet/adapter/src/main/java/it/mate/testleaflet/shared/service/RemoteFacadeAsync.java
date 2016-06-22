package it.mate.testleaflet.shared.service;

import it.mate.gwtcommons.shared.rpc.RpcMap;

import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RemoteFacadeAsync {

  void getServerTime(AsyncCallback<Date> callback);

  void sendDevInfo(RpcMap map, AsyncCallback<RpcMap> callback);

  void checkConnection(AsyncCallback<Boolean> callback);

  void saveAccount(RpcMap entity, AsyncCallback<RpcMap> callback);

}
