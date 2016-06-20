package it.mate.testleaflet.server.model;

import it.mate.commons.server.model.HasKey;
import it.mate.testleaflet.shared.model.DevInfo;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
@PersistenceCapable (detachable="true")
public class DevInfoDs implements DevInfo, HasKey, Serializable {
  
  @PrimaryKey
  @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
  Key id;

  @Persistent
  String os;
  
  @Persistent
  String layout;
  
  @Persistent
  String devName;

  @Persistent
  String phgVersion;
  
  @Persistent
  String platform;
  
  @Persistent
  String devUuid;
  
  @Persistent
  String devVersion;
  
  @Persistent
  Date created;
  
  @Persistent
  String devIp;

  @Override
  public String toString() {
    return "DevInfoDs [id=" + id + ", os=" + os + ", layout=" + layout + ", devName=" + devName + ", phgVersion=" + phgVersion + ", platform=" + platform
        + ", devUuid=" + devUuid + ", devVersion=" + devVersion + ", created=" + created + ", devIp=" + devIp + "]";
  }

  public Key getKey() {
    return id;
  }

  public void setId(String id) {
    this.id = id != null ? KeyFactory.stringToKey(id) : null;
  }

  public String getId() {
    return id != null ? KeyFactory.keyToString(id) : null;
  }

  public String getOs() {
    return os;
  }

  public void setOs(String os) {
    this.os = os;
  }

  public String getLayout() {
    return layout;
  }

  public void setLayout(String layout) {
    this.layout = layout;
  }

  public String getDevName() {
    return devName;
  }

  public void setDevName(String devName) {
    this.devName = devName;
  }

  public String getPhgVersion() {
    return phgVersion;
  }

  public void setPhgVersion(String phgVersion) {
    this.phgVersion = phgVersion;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public String getDevUuid() {
    return devUuid;
  }

  public void setDevUuid(String devUuid) {
    this.devUuid = devUuid;
  }

  public String getDevVersion() {
    return devVersion;
  }

  public void setDevVersion(String devVersion) {
    this.devVersion = devVersion;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public String getDevIp() {
    return devIp;
  }

  public void setDevIp(String devIp) {
    this.devIp = devIp;
  }

}
