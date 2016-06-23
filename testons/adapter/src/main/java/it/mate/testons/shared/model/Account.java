package it.mate.testons.shared.model;

import java.io.Serializable;

public interface Account extends Serializable {
  
  public void setName(String nickname);

  public String getName();

  public void setEmail(String email);

  public String getEmail();

  public void setId(String userId);

  public String getId();

  public void setDevInfoId(String devInfoId);

  public String getDevInfoId();

  public void setPassword(String password);

  public String getPassword();

  public void setPushNotifRegId(String pushNotifRegId);

  public String getPushNotifRegId();

}
