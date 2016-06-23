package it.mate.testons.server.model;

import it.mate.commons.server.model.HasKey;
import it.mate.testons.shared.model.OrderItemRow;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
@PersistenceCapable (detachable="true")
public class OrderItemRowDs implements OrderItemRow, HasKey {

  @PrimaryKey
  @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
  Key remoteId;
  
  @Persistent
  private Integer id;
  
  transient Integer orderItemId;
  
  @Persistent
  private String text;
  
  @Persistent
  private Boolean bold;
  
  @Persistent
  private Integer size;
  
  @Persistent
  private String fontFamily;
  
  @Persistent
  private Boolean italic;
  
  @Persistent
  private Boolean underline;
  
  @Persistent
  private String align;

  
  @Override
  public Key getKey() {
    return remoteId;
  }

  public String getRemoteId() {
    return remoteId != null ? KeyFactory.keyToString(remoteId) : null;
  }
  
  public void setRemoteId(String remoteId) {
    this.remoteId = remoteId != null ? KeyFactory.stringToKey(remoteId) : null;
  }
  
  public boolean isBold() {
    return false;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getOrderItemId() {
    return orderItemId;
  }

  public void setOrderItemId(Integer orderItemId) {
    this.orderItemId = orderItemId;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Boolean getBold() {
    return bold;
  }

  public void setBold(Boolean bold) {
    this.bold = bold;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public String getFontFamily() {
    return fontFamily;
  }

  public void setFontFamily(String fontFamily) {
    this.fontFamily = fontFamily;
  }

  public Boolean getItalic() {
    return italic;
  }

  public void setItalic(Boolean italic) {
    this.italic = italic;
  }

  public Boolean getUnderline() {
    return underline;
  }

  public void setUnderline(Boolean underline) {
    this.underline = underline;
  }
  
  public String getAlign() {
    return align;
  }

  public void setAlign(String align) {
    this.align = align;
  }
  
}
