package it.mate.testons.server.model;

import it.mate.commons.server.model.HasKey;
import it.mate.testons.shared.model.Message;
import it.mate.testons.shared.model.Order;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
@PersistenceCapable (detachable="true")
public class MessageDs implements Message, HasKey {

  @PrimaryKey
  @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
  Key remoteId;
  
  @Persistent
  private Integer id;
  
  @Persistent
  private Date data;
  
  @Persistent
  private String text;
  
  private Order order;
  
  private Integer orderItemId;
  
  public Key getKey() {
    return remoteId;
  }
  
  public String getRemoteId() {
    return remoteId != null ? KeyFactory.keyToString(remoteId) : null;
  }
  
  public void setRemoteId(String remoteId) {
    this.remoteId = remoteId != null ? KeyFactory.stringToKey(remoteId) : null;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Date getData() {
    return data;
  }

  public void setData(Date data) {
    this.data = data;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Integer getOrderId() {
    return null;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public Integer getOrderItemId() {
    return orderItemId;
  }

  public void setOrderItemId(Integer orderItemId) {
    this.orderItemId = orderItemId;
  }

}
