package it.mate.wolf.adapter.model;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class AgentStatus implements Serializable {
  
  public static final String COMMAND_WOL = "wol";
  
  private String userAgent;
  
  private String status;
  
  private Date lastAccess;
  
  private String command;
  
  private String hostname;
  
  private String ip;
  
  private String temperature;
  
  private String memory;

  @Override
  public String toString() {
    return "AgentStatus [userAgent=" + userAgent + ", status=" + status + ", lastAccess=" + lastAccess + ", command=" + command + ", hostname=" + hostname
        + ", ip=" + ip + ", temperature=" + temperature + ", memory=" + memory + "]";
  }

  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Date getLastAccess() {
    return lastAccess;
  }

  public void setLastAccess(Date lastAccess) {
    this.lastAccess = lastAccess;
  }

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getTemperature() {
    return temperature;
  }

  public void setTemperature(String temperature) {
    this.temperature = temperature;
  }

  public String getMemory() {
    return memory;
  }

  public void setMemory(String memory) {
    this.memory = memory;
  }

}
