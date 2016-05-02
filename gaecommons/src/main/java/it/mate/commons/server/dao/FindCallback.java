package it.mate.commons.server.dao;

import java.io.Serializable;

public abstract class FindCallback <E extends Serializable> {

  public abstract void processResultsInTransaction(E entity);
  
}
