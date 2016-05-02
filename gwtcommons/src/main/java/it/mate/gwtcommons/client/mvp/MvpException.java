package it.mate.gwtcommons.client.mvp;

@SuppressWarnings("serial")
public class MvpException extends RuntimeException {

  public MvpException() {
    super();
  }

  public MvpException(String message, Throwable cause) {
    super(message, cause);
  }

  public MvpException(String message) {
    super(message);
  }

  public MvpException(Throwable cause) {
    super(cause);
  }

}
