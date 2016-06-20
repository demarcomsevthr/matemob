package it.mate.wolf.agent;

import it.mate.wolf.agent.adapter.AgentAdapter;
import it.mate.wolf.agent.utils.PropertiesHolder;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AgentMain {
  
  public static void main(String[] args) {
    ApplicationContext context = new ClassPathXmlApplicationContext("agent-context.xml");
    AgentAdapter adapter = context.getBean(AgentAdapter.class);
    while (true) {
      try {
        adapter.testAgent();
        adapter.sendAgentStatus();
      } catch (Exception ex) {
        ex.printStackTrace();
      } finally {
        try {
          Thread.sleep(Integer.parseInt(PropertiesHolder.getString("agent.delay", "5000")));
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }
  }

}
