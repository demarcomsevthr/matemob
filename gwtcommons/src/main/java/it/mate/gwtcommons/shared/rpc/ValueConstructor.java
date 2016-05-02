package it.mate.gwtcommons.shared.rpc;

import java.io.Serializable;

public interface ValueConstructor <V extends IsMappable> extends Serializable {
  public V newInnstance();
}
