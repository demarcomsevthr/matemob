package it.mate.gwtcommons.shared.rpc;

public interface IsMappable {
  
  public RpcMap toRpcMap();
  
  public IsMappable fromRpcMap(RpcMap map);

}
