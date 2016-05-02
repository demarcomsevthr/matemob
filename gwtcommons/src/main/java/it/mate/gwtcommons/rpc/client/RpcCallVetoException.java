package it.mate.gwtcommons.rpc.client;

@SuppressWarnings("serial")
public class RpcCallVetoException extends RuntimeException {

  public RpcCallVetoException() {
    super();
  }

  public RpcCallVetoException(String message, Throwable cause) {
    super(message, cause);
  }

  public RpcCallVetoException(String message) {
    super(message);
  }

  public RpcCallVetoException(Throwable cause) {
    super(cause);
  }
  
}
