package it.mate.testleaflet.shared.model;

import java.io.Serializable;
import java.util.Date;

public interface Message extends Serializable {

  public Integer getOrderId();

  public void setOrder(Order order);

  public Order getOrder();

  public void setText(String text);

  public String getText();

  public void setData(Date data);

  public Date getData();

  public void setId(Integer id);

  public Integer getId();

  public void setOrderItemId(Integer orderItemId);

  public Integer getOrderItemId();

  public void setRemoteId(String remoteId);

  public String getRemoteId();

}
