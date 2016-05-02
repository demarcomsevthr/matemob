package it.mate.commons.server.services;

@SuppressWarnings("serial")
public class AdapterException extends RuntimeException {

  public AdapterException(String message, Throwable cause) {
    super(message, cause);
  }

  public AdapterException(String message) {
    super(message);
  }

  public AdapterException(Throwable cause) {
    super(cause);
  }
  
}
