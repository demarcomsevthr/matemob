package it.mate.commons.server.dao;

import java.io.Serializable;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query;

public class LowLevelDao {
  
  
  public <E extends Serializable> Object find(FindContext<E> context) {

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
    Query query = new Query();
    
    query.setKeysOnly();
    
    return null;
  }

}
