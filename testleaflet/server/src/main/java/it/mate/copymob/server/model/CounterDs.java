package it.mate.testleaflet.server.model;

import it.mate.commons.server.model.HasKey;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
@PersistenceCapable (detachable="true", cacheable="false")
public class CounterDs implements HasKey, Serializable {

  @PrimaryKey
  @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
  Key id;

  @Persistent (defaultFetchGroup="true")
  Long value;
  
  @Override
  public String toString() {
    return "CounterDs [id=" + id + ", value=" + value + "]";
  }

  public Key getKey() {
    return id;
  }
  
  public String getId() {
    return id != null ? KeyFactory.keyToString(id) : null;
  }

  public Long getValue() {
    return value;
  }

  public void setValue(Long value) {
    this.value = value;
  }
  
}
