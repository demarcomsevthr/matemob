package it.mate.wolf.agent.adapter;


public interface AgentAdapter {
  
  public void getAgentStatus();
  
  public void sendAgentStatusText() throws Exception;

  public void sendAgentStatus() throws Exception;
  
  public void sendMagicPacket() throws Exception;

}
