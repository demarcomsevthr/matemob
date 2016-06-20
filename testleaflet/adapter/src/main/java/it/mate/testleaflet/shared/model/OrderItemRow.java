package it.mate.testleaflet.shared.model;

import java.io.Serializable;

public interface OrderItemRow extends Serializable {

  public Integer getId();

  public void setId(Integer id);

  public void setText(String text);

  public String getText();

  public void setOrderItemId(Integer orderItemId);

  public Integer getOrderItemId();

  public void setBold(Boolean bold);

  public Boolean getBold();
  
  public void setSize(Integer size);

  public Integer getSize();

  public void setFontFamily(String fontFamily);

  public String getFontFamily();

  public void setRemoteId(String remoteId);

  public String getRemoteId();

  public void setItalic(Boolean italic);

  public Boolean getItalic();

  public void setUnderline(Boolean underline);

  public Boolean getUnderline();

  public void setAlign(String align);

  public String getAlign();

}
