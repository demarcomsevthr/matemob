package it.mate.commons.server.dao;

@SuppressWarnings("serial")
public class PersistenceException extends RuntimeException {

  public PersistenceException(String message, Throwable cause) {
    super(message, cause);
  }

  public PersistenceException(String message) {
    super(message);
  }

  public PersistenceException(Throwable cause) {
    super(cause);
  }
  
}
