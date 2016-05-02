package it.mate.commons.server.model.impl;

import it.mate.commons.server.model.HasKey;
import it.mate.gwtcommons.shared.model.PageConfig;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

@SuppressWarnings("serial")
@PersistenceCapable (detachable="true")
public class PageConfigDs implements PageConfig, HasKey {

  @PrimaryKey
  @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
  private Key id;
  
  @Persistent
  private String name;
  
  @Persistent
  private Text xml;
  
  public static PageConfigDs clone(PageConfig source) {
    if (source instanceof PageConfigDs) {
      return (PageConfigDs)source;
    } else {
      return InternalHelper.copy(source, new PageConfigDs());
    }
  }
  
  public static List<? extends PageConfig> clone (List<? extends PageConfig> sources) {
    List<PageConfigDs> targets = new ArrayList<PageConfigDs>();
    for (PageConfig source : sources) {
      targets.add(clone(source));
    }
    return targets;
  }
  
  public Key getKey() {
    return null;
  }
  
  public void forceKey(Key key) {
    
  }

  public String getId() {
    return id != null ? KeyFactory.keyToString(id) : null;
  }

  public void setId(String id) {
    this.id = id != null ? KeyFactory.stringToKey(id) : null;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getXml() {
    return xml != null ? xml.getValue() : null;
  }

  public void setXml(String xml) {
    this.xml = xml != null ? new Text(xml) : null;
  }
  
}
