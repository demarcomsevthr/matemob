package it.mate.wolf.server.services;

import it.mate.wolf.adapter.model.AgentStatus;



public interface RemoteAdapter {
  
  public String sendDevInfo(String os, String layout, String devName, String phgVersion, String platform, String devUuid, String devVersion, String devIp);  

  public void scheduledChecks();
  
  public String test();
  
  public void setAgentStatus(AgentStatus agentStatus);
  
  public AgentStatus getAgentStatus();
  
}
