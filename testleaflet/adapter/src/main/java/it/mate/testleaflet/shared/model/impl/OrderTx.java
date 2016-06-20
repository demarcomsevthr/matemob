package it.mate.testleaflet.shared.model.impl;

import it.mate.testleaflet.shared.model.Account;
import it.mate.testleaflet.shared.model.Order;
import it.mate.testleaflet.shared.model.OrderItem;
import it.mate.gwtcommons.client.utils.CollectionPropertyClientUtil;
import it.mate.gwtcommons.shared.model.CloneableProperty;
import it.mate.gwtcommons.shared.rpc.IsMappable;
import it.mate.gwtcommons.shared.rpc.RpcMap;
import it.mate.gwtcommons.shared.rpc.ValueConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
public class OrderTx implements Order, IsMappable {
  
  private Integer id;
  
  private String remoteId;
  
  private String codice;
  
  private Integer state = Order.STATE_DEFAULT;
  
  private List<OrderItemTx> items = new ArrayList<OrderItemTx>();
  
  private AccountTx account;
  
  private Date lastUpdate;
    
  private String updateState;
  
  private Date created;
  
  
  public OrderTx() {

  }
  
  protected OrderTx(Integer id) {
    this.id = id;
  }
  
  @Override
  public String toString() {
    return "OrderTx [id=" + id + ", remoteId=" + remoteId + ", codice=" + codice + ", state=" + state 
        + ", items.size=" + items.size() 
        + ", account=" + account
        + ", lastUpdate=" + lastUpdate + ", updateState=" + updateState + ", created=" + created + "]";
  }

  @Override
  public RpcMap toRpcMap() {
    RpcMap map = new RpcMap();
    map.putField("id", id);
    map.putField("remoteId", remoteId);
    map.putField("codice", codice);
    map.putField("state", state);
    map.putField("items", items);
    map.putField("account", account);
    map.putField("lastUpdate", lastUpdate);
    map.putField("updateState", updateState);
    map.putField("created", created);
    return map;
  }

  @Override
  public OrderTx fromRpcMap(RpcMap map) {
    this.id = map.getField("id");
    this.remoteId = map.getField("remoteId");
    this.codice = map.getField("codice");
    this.state = map.getField("state");
    this.items = map.getField("items", new ValueConstructor<OrderItemTx>() {
      public OrderItemTx newInnstance() {
        return new OrderItemTx(OrderTx.this);
      }
    });
    this.account = map.getField("account", new ValueConstructor<AccountTx>() {
      public AccountTx newInnstance() {
        return new AccountTx();
      }
    });
    this.lastUpdate = map.getField("lastUpdate");
    this.updateState = map.getField("updateState");
    this.created = map.getField("created");
    return this;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
    for (OrderItem item : items) {
      item.setOrderId(id);
    }
  }

  public String getCodice() {
    return codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public List<OrderItem> getItems() {
    linkItems();
    return CollectionPropertyClientUtil.cast(items, OrderItemTx.class);
  }

  @CloneableProperty (targetClass=OrderItemTx.class)
  public void setItems(List<OrderItem> items) {
    this.items = CollectionPropertyClientUtil.clone(items, OrderItemTx.class);
    linkItems();
  }
  
  private void linkItems() {
    if (items != null) {
      for (OrderItemTx item : items) {
        item.setOrderId(this.id);
        item.setOrder(this);
      }
    }
  }

  public Integer getState() {
    return state != null ? state : Order.STATE_DEFAULT;
  }
  
  public boolean isCartOrder() {
    return new Integer(Order.STATE_IN_CART).equals(state);
  }

  public void setState(Integer state) {
    this.state = state != null ? state : 0;
  }

  public String getRemoteId() {
    return remoteId;
  }

  public void setRemoteId(String remoteId) {
    this.remoteId = remoteId;
  }

  public Account getAccount() {
    return account;
  }

  @CloneableProperty (targetClass=AccountTx.class)
  public void setAccount(Account account) {
    this.account = (AccountTx)account;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public String getUpdateState() {
    return updateState;
  }

  public void setUpdateState(String updateState) {
    this.updateState = updateState;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

}
