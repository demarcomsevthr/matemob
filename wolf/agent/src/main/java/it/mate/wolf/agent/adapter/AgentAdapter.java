package it.mate.wolf.agent.adapter;


public interface AgentAdapter {
  
  public void getAgentStatus();
  
  public void sendAgentStatus() throws Exception;
  
  public void sendMagicPacket() throws Exception;
  
  public void testException() throws Exception;
  
  public void setLastException(Exception ex);

}
