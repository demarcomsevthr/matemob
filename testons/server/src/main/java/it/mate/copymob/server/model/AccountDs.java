package it.mate.testons.server.model;

import it.mate.commons.server.model.HasKey;
import it.mate.testons.shared.model.Account;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
@PersistenceCapable (detachable="true")
public class AccountDs implements HasKey, Account {

  @PrimaryKey
  @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
  Key id;
  
  @Persistent
  private String email;
  
  @Persistent
  private String name;
  
  @Persistent
  private String password;
  
  @Persistent (dependentKey="false", defaultFetchGroup="true")
  private Key devInfoId;
  
  @Persistent
  private String authDomain;
  
  @Persistent
  private String federatedIdentity;
  
  @Persistent
  private String pushNotifRegId;
  
  
  @Override
  public String toString() {
    return "AccountDs [id=" + id + ", email=" + email + ", name=" + name + ", password=" + password + ", devInfoId=" + devInfoId + ", authDomain=" + authDomain
        + ", federatedIdentity=" + federatedIdentity + "]";
  }

  public Key getKey() {
    return id;
  }
  
  public String getId() {
    return id != null ? KeyFactory.keyToString(id) : null;
  }

  public void setId(String id) {
    this.id = id != null ? KeyFactory.stringToKey(id) : null;
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

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getDevInfoId() {
    return devInfoId != null ? KeyFactory.keyToString(devInfoId) : null;
  }

  public void setDevInfoId(String devInfoId) {
    this.devInfoId = devInfoId != null ? KeyFactory.stringToKey(devInfoId) : null;
  }

  public String getAuthDomain() {
    return authDomain;
  }

  public void setAuthDomain(String authDomain) {
    this.authDomain = authDomain;
  }

  public String getFederatedIdentity() {
    return federatedIdentity;
  }

  public void setFederatedIdentity(String federatedIdentity) {
    this.federatedIdentity = federatedIdentity;
  }

  public String getPushNotifRegId() {
    return pushNotifRegId;
  }

  public void setPushNotifRegId(String pushNotifRegId) {
    this.pushNotifRegId = pushNotifRegId;
  }
  
}
