package it.mate.commons.server.model.utils;

@SuppressWarnings("serial")
public class RelationException extends RuntimeException {

  public RelationException() {
    super();
  }

  public RelationException(String message, Throwable cause) {
    super(message, cause);
  }

  public RelationException(String message) {
    super(message);
  }

  public RelationException(Throwable cause) {
    super(cause);
  }
  
}
