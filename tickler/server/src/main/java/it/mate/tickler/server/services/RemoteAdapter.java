package it.mate.tickler.server.services;

import java.util.List;


public interface RemoteAdapter {
  
  public String sendDevInfo(String os, String layout, String devName, String phgVersion, String platform, String devUuid, String devVersion, String devIp);  

  public void scheduledChecks();
  
  public List<List<Object>> test() throws Exception;
  
}
