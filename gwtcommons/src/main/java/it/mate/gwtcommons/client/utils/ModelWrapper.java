package it.mate.gwtcommons.client.utils;

public class ModelWrapper <M> {

  private M model;

  public M getModel() {
    return model;
  }

  public ModelWrapper(M model) {
    super();
    this.model = model;
  }

}
