package it.mate.testons.server.model;

import it.mate.commons.server.model.CollectionPropertyServerUtil;
import it.mate.commons.server.model.HasKey;
import it.mate.commons.server.model.UnownedRelationship;
import it.mate.commons.server.utils.CollectionUtils;
import it.mate.testons.shared.model.Account;
import it.mate.testons.shared.model.Order;
import it.mate.testons.shared.model.OrderItem;
import it.mate.gwtcommons.shared.model.CloneableProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
@PersistenceCapable (detachable="true")
public class OrderDs implements Order, HasKey {
  
  @PrimaryKey
  @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
  Key remoteId;
  
  @Persistent
  private Integer id;
  
  @Persistent
  private String codice;

  @Persistent
  private Integer state;
  
  @Persistent (dependentKey="false", defaultFetchGroup="false")
  List<Key> itemKeys;
  @UnownedRelationship (key="itemKeys", itemClass=OrderItemDs.class)
  transient List<OrderItemDs> items;
  
  /* server per far funzionare la includedField */
  public void itemKeysTraverse() {
    CollectionUtils.traverseCollection(this.itemKeys);
  }
  
  @Persistent (dependentKey="false")
  Key accountKey;
  @UnownedRelationship (key="accountKey")
  transient AccountDs account;
  
  @Persistent
  private Date lastUpdate;
  
  @Persistent
  private String updateState;
  
  @Persistent
  private Date created;
  
  
  
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

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCodice() {
    return codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public Integer getState() {
    return state != null ? state : Order.STATE_DEFAULT;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public List<OrderItem> getItems() {
    return items != null ? new ArrayList<OrderItem>(items) : null;
  }

  @CloneableProperty (targetClass=OrderItemDs.class)
  public void setItems(List<OrderItem> items) {
    CollectionPropertyServerUtil<OrderItem, OrderItemDs> wrapper = CollectionPropertyServerUtil.clone(items, OrderItemDs.class);
    this.items = wrapper.getItems();
    this.itemKeys = wrapper.getKeys();
  }
  
  @CloneableProperty (targetClass=AccountDs.class)
  public void setAccount(Account account) {
    if (account != null) {
      this.account = (AccountDs)account;
      this.accountKey = this.account.getKey();
    } else {
      this.account = null;
      this.accountKey = null;
    }
  }

  public Account getAccount() {
    return account;
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
