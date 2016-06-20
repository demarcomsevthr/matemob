package it.mate.wolf.server.services;

import it.mate.wolf.adapter.model.AgentStatus;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class RemoteAdapterImpl implements RemoteAdapter {
  
  private static Logger logger = Logger.getLogger(RemoteAdapterImpl.class);
  
  private static AgentStatus agentStatus;
  
  @PostConstruct
  public void postConstruct() {
    logger.debug("initialized " + this);
  }

  @Override
  public void scheduledChecks() {

  }

  @Override
  public String sendDevInfo(String os, String layout, String devName, String phgVersion, String platform, String devUuid, String devVersion, String devIp) {
    return null;
  }
  
  public String test() {
    return "Server ON. Rev. DEPLOY TEST 3. Current time is " + new Date();
  }
  
  public void setAgentStatus(AgentStatus agentStatus) {
    RemoteAdapterImpl.agentStatus = agentStatus;
  }
  
  public AgentStatus getAgentStatus() {
    return RemoteAdapterImpl.agentStatus;
  }
  
}
