package it.mate.gwtcommons.client.utils;

public abstract class DelegateWithModel<T, M> implements Delegate<T> {

  protected M model;

  public DelegateWithModel(M model) {
    this.model = model;
  }

  protected M model() {
    return model;
  }
  
}
