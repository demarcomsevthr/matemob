package it.mate.testleaflet.shared.model;

import java.io.Serializable;
import java.util.Date;

public interface DevInfo extends Serializable {
  
  public String getId();

  public void setId(String id);

  public String getOs();

  public void setOs(String os);

  public String getLayout();

  public void setLayout(String layout);

  public String getDevName();

  public void setDevName(String devName);

  public String getPhgVersion();

  public void setPhgVersion(String phgVersion);

  public String getPlatform();

  public void setPlatform(String platform);

  public String getDevUuid();

  public void setDevUuid(String devUuid);

  public String getDevVersion();

  public void setDevVersion(String devVersion);

  public Date getCreated();

  public void setCreated(Date created);

  public String getDevIp();

  public void setDevIp(String devIp);
  
}
