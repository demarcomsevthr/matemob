package it.mate.testleaflet.shared.model.impl;

import it.mate.testleaflet.shared.model.Account;
import it.mate.testleaflet.shared.model.utils.ModelUtils;
import it.mate.gwtcommons.shared.rpc.IsMappable;
import it.mate.gwtcommons.shared.rpc.RpcMap;

import java.util.Date;

@SuppressWarnings("serial")
public class AccountTx implements Account, IsMappable {

  private String id;
  
  private String email;
  
  private String name;
  
  private String password;
  
  private String devInfoId;
  
  private String pushNotifRegId;
  
  private Date lastCheckForUpdates;
  
  @Override
  public RpcMap toRpcMap() {
    RpcMap map = new RpcMap();
    
    map.putField("id", id);
    map.putField("email", email);
    map.putField("name", name);
    map.putField("password", password);
    map.putField("devInfoId", devInfoId);
    map.putField("pushNotifRegId", pushNotifRegId);
    
    /*
    map.put("id", id);
    map.put("email", email);
    map.put("name", name);
    map.put("password", password);
    map.put("devInfoId", devInfoId);
    map.put("pushNotifRegId", pushNotifRegId);
    */
    return map;
  }

  @Override
  public AccountTx fromRpcMap(RpcMap map) {
    
    this.id = map.getField("id");
    this.email = map.getField("email");
    this.name = map.getField("name");
    this.password = map.getField("password");
    this.devInfoId = map.getField("devInfoId");
    this.pushNotifRegId = map.getField("pushNotifRegId");
    
    /*
    this.id = (String)map.get("id");
    this.email = (String)map.get("email");
    this.name = (String)map.get("name");
    this.password = (String)map.get("password");
    this.devInfoId = (String)map.get("devInfoId");
    this.pushNotifRegId = (String)map.get("pushNotifRegId");
    */
    return this;
  }
  
  public AccountTx() {

  }
  
  public AccountTx(Account account) {
    if (account instanceof AccountTx) {
      AccountTx that = (AccountTx)account;
      this.id = that.id;
      this.email = that.email;
      this.password = that.password;
      this.devInfoId = that.devInfoId;
    }
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (obj instanceof AccountTx) {
      AccountTx that = (AccountTx)obj;
      if (!ModelUtils.equals(this.id, that.id))
        return false;
      if (!ModelUtils.equals(this.name, that.name))
        return false;
      if (!ModelUtils.equals(this.email, that.email))
        return false;
      if (!ModelUtils.equals(this.devInfoId, that.devInfoId))
        return false;
      if (!ModelUtils.equals(this.password, that.password))
        return false;
      return true;
    }
    return super.equals(obj);
  }

  public String getId() {
    return id;
  }

  public void setId(String userId) {
    this.id = userId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String nickname) {
    this.name = nickname;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getDevInfoId() {
    return devInfoId;
  }

  public void setDevInfoId(String devInfoId) {
    this.devInfoId = devInfoId;
  }

  public String getPushNotifRegId() {
    return pushNotifRegId;
  }

  public void setPushNotifRegId(String pushNotifRegId) {
    this.pushNotifRegId = pushNotifRegId;
  }

  public Date getLastCheckForUpdates() {
    return lastCheckForUpdates;
  }

  public void setLastCheckForUpdates(Date lastCheckForUpdates) {
    this.lastCheckForUpdates = lastCheckForUpdates;
  }
  
}
