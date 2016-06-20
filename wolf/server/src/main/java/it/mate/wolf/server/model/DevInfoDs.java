package it.mate.wolf.server.model;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class DevInfoDs implements Serializable {
  
  Long id;

  String os;
  
  String layout;
  
  String devName;

  String phgVersion;
  
  String platform;
  
  String devUuid;
  
  String devVersion;
  
  Date created;
  
  String devIp;

  @Override
  public String toString() {
    return "DevInfoDs [id=" + id + ", os=" + os + ", layout=" + layout + ", devName=" + devName + ", phgVersion=" + phgVersion + ", platform=" + platform
        + ", devUuid=" + devUuid + ", devVersion=" + devVersion + ", created=" + created + ", devIp=" + devIp + "]";
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
