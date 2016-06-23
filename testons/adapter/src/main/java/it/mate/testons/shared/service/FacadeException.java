package it.mate.testons.shared.service;

@SuppressWarnings("serial")
public class FacadeException extends Exception {

  public FacadeException() {
    super();
  }

  public FacadeException(String message, Throwable cause) {
    super(message, cause);
  }

  public FacadeException(String message) {
    super(message);
  }

}
