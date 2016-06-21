package it.mate.wolf.agent;

import it.mate.wolf.agent.adapter.AgentAdapter;
import it.mate.wolf.agent.utils.PropertiesHolder;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AgentMain {
  
  @SuppressWarnings("resource")
  public static void main(String[] args) {
    ApplicationContext context = new ClassPathXmlApplicationContext("agent-context.xml");
    AgentAdapter adapter = context.getBean(AgentAdapter.class);
    while (true) {
      try {
        if (PropertiesHolder.getBoolean("agent.test.execute", false)) {
          adapter.getAgentStatus();
        }
        if (PropertiesHolder.getBoolean("agent.status.sendText", false)) {
          adapter.sendAgentStatusText();
        }
        if (PropertiesHolder.getBoolean("agent.status.sendJson", false)) {
          adapter.sendAgentStatus();
        }
        if (PropertiesHolder.getBoolean("agent.test.wol", false)) {
          adapter.sendMagicPacket();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      } finally {
        try {
          Thread.sleep(Integer.parseInt(PropertiesHolder.getString("agent.delay", "10000")));
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }
  }

}
