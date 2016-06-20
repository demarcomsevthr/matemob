package it.mate.testleaflet.shared.model.impl;

import it.mate.testleaflet.shared.model.Message;
import it.mate.testleaflet.shared.model.Order;
import it.mate.gwtcommons.shared.rpc.IsMappable;
import it.mate.gwtcommons.shared.rpc.RpcMap;

import java.util.Date;

@SuppressWarnings("serial")
public class MessageTx implements Message, IsMappable {

  private Integer id;
  
  private Date data;
  
  private String text;
  
  private Order order;
  
  private Integer orderItemId;
  
  private String remoteId;
  
  @Override
  public RpcMap toRpcMap() {
    RpcMap map = new RpcMap();
    
    map.putField("id", id);
    map.putField("data", data);
    map.putField("text", text);
    map.putField("orderItemId", orderItemId);
    map.putField("remoteId", remoteId);
    
    /*
    map.put("id", id);
    map.put("data", data);
    map.put("text", text);
    map.put("orderItemId", orderItemId);
    map.put("remoteId", remoteId);
    */
    
    return map;
  }

  @Override
  public MessageTx fromRpcMap(RpcMap map) {
    
    this.id = map.getField("id");
    this.data = map.getField("data");
    this.text = map.getField("text");
    this.orderItemId = map.getField("orderItemId");
    this.remoteId = map.getField("remoteId");
    
    /*
    this.id = (Integer)map.get("id");
    this.data = (Date)map.get("data");
    this.text = (String)map.get("text");
    this.orderItemId = (Integer)map.get("orderItemId");
    this.remoteId = (String)map.get("remoteId");
    */
    
    return this;
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

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }
  
  public Integer getOrderId() {
    return order != null ? order.getId() : null;
  }
  
  public void setOrderId(Integer id) {
    this.order = id != null ? new OrderTx(id) : null;
  }

  public Integer getOrderItemId() {
    return orderItemId;
  }

  public void setOrderItemId(Integer orderItemId) {
    this.orderItemId = orderItemId;
  }

  public String getRemoteId() {
    return remoteId;
  }

  public void setRemoteId(String remoteId) {
    this.remoteId = remoteId;
  }
  
}
