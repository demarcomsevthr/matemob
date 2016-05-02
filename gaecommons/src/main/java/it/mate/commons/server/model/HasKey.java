package it.mate.commons.server.model;

import com.google.appengine.api.datastore.Key;

public interface HasKey {
  
  Key getKey();
  
}
