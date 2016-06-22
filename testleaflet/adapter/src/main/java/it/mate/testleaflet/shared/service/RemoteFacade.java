package it.mate.testleaflet.shared.service;

import it.mate.gwtcommons.shared.rpc.RpcMap;

import java.util.Date;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath(".remoteFacade")
public interface RemoteFacade extends RemoteService {
  
  Date getServerTime();
  
  RpcMap sendDevInfo(RpcMap map);

  public RpcMap saveAccount(RpcMap entity);
  
  Boolean checkConnection();
  
}
