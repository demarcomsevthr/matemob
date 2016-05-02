package it.mate.commons.server.dao;

@SuppressWarnings("serial")
public class DataNotFoundException extends RuntimeException {

  public DataNotFoundException() {
    super();
  }

  public DataNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public DataNotFoundException(String message) {
    super(message);
  }

  public DataNotFoundException(Throwable cause) {
    super(cause);
  }
  
}
