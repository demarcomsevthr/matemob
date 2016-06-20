package it.mate.testleaflet.shared.model.impl;

import it.mate.testleaflet.shared.model.OrderItemRow;
import it.mate.gwtcommons.shared.rpc.IsMappable;
import it.mate.gwtcommons.shared.rpc.RpcMap;

@SuppressWarnings("serial")
public class OrderItemRowTx implements OrderItemRow, IsMappable {
  
  private Integer id;
  
  private Integer orderItemId;
  
  private String text;
  
  private Boolean bold;
  
  private Integer size = 10;
  
  private String fontFamily;
  
  private String remoteId;
  
  private Boolean italic;
  
  private Boolean underline;
  
  private String align;
  
  
  
  
  public OrderItemRowTx() {

  }
  
  public OrderItemRowTx(String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return "OrderItemRowTx [id=" + id + ", orderItemId=" + orderItemId + ", text=" + text + ", bold=" + bold + "]";
  }

  @Override
  public RpcMap toRpcMap() {
    RpcMap map = new RpcMap();
    map.putField("id", id);
    map.putField("orderItemId", orderItemId);
    map.putField("text", text);
    map.putField("bold", bold);
    map.putField("size", size);
    map.putField("fontFamily", fontFamily);
    map.putField("remoteId", remoteId);
    map.putField("italic", italic);
    map.putField("underline", underline);
    map.putField("align", align);
    return map;
  }

  @Override
  public OrderItemRowTx fromRpcMap(RpcMap map) {
    this.id = map.getField("id");
    this.orderItemId = map.getField("orderItemId");
    this.text = map.getField("text");
    this.bold = map.getField("bold");
    this.size = map.getField("size");
    this.fontFamily = map.getField("fontFamily");
    this.remoteId = map.getField("remoteId");
    this.italic = map.getField("italic");
    this.underline = map.getField("underline");
    this.align = map.getField("align");
    return this;
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
    return bold != null ? bold : false;
  }

  public void setBold(Boolean bold) {
    this.bold = bold;
  }

  public Integer getSize() {
    return size != null ? size : 0;
  }

  public void setSize(Integer size) {
    this.size = size != null ? size : 0;
  }

  public String getFontFamily() {
    return fontFamily;
  }

  public void setFontFamily(String fontFamily) {
    this.fontFamily = fontFamily;
  }

  public String getRemoteId() {
    return remoteId;
  }

  public void setRemoteId(String remoteId) {
    this.remoteId = remoteId;
  }

  public Boolean getItalic() {
    return italic != null ? italic : false;
  }

  public void setItalic(Boolean italic) {
    this.italic = italic;
  }

  public Boolean getUnderline() {
    return underline != null ? underline : false;
  }

  public void setUnderline(Boolean underline) {
    this.underline = underline;
  }

  public String getAlign() {
    return align != null ? align : "center";
  }

  public void setAlign(String align) {
    this.align = align;
  }
  
}
