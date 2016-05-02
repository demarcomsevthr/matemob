package it.mate.gwtcommons.shared.services;

@SuppressWarnings("serial")
public class ServiceException extends RuntimeException {

  public ServiceException() {
    super();
  }

  public ServiceException(String message) {
    super(message);
  }

  public ServiceException(Throwable cause) {
    super(cause);
  }
  
}
