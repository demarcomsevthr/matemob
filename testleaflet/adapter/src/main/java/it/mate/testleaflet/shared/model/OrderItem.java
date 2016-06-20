package it.mate.testleaflet.shared.model;

import java.io.Serializable;
import java.util.List;

public interface OrderItem extends Serializable {

  public Integer getId();

  public void setId(Integer id);

  public void setQuantity(Double quantity);

  public Double getQuantity();

  public void setTimbroId(Integer timbroId);

  public Integer getTimbroId();

  public void setOrderId(Integer orderId);

  public Integer getOrderId();
  
  public List<OrderItemRow> getRows();

  public void setRows(List<OrderItemRow> rows);

  public void setTimbro(Timbro timbro);

  public Timbro getTimbro();

  public boolean isInCart();

  public void setInCart(Boolean inCart);

  public Boolean getInCart();

  public void setRemoteId(String remoteId);

  public String getRemoteId();

  public void setMessages(List<Message> messages);

  public List<Message> getMessages();

  public void setPreviewImage(String previewImage);

  public String getPreviewImage();

  public void setCustomerImage(String customerImage);

  public String getCustomerImage();

}
