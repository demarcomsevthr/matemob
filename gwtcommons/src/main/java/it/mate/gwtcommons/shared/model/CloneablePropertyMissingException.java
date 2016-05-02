package it.mate.gwtcommons.shared.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CloneablePropertyMissingException extends RuntimeException {

  public CloneablePropertyMissingException() {
    super();
  }

  public CloneablePropertyMissingException(Serializable entity) {
    super("Cannot add item of type " + entity.getClass() + ", maybe forget CloneableProperty annotation?");
  }

}
