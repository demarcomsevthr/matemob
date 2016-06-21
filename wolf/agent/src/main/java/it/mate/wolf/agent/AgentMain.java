package it.mate.wolf.agent;

import it.mate.wolf.agent.adapter.AgentAdapter;
import it.mate.wolf.agent.utils.PropertiesHolder;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AgentMain {
  
  private static Logger logger = Logger.getLogger(AgentMain.class);
  
  @SuppressWarnings("resource")
  public static void main(String[] args) {
    ApplicationContext context = new ClassPathXmlApplicationContext("agent-context.xml");
    AgentAdapter adapter = context.getBean(AgentAdapter.class);
    while (true) {
      try {
        if (PropertiesHolder.getString("agent.test.method") != null) {
          adapter.getAgentStatus();
        }
        if (PropertiesHolder.getString("agent.status.method") != null) {
          adapter.sendAgentStatus();
        }
        if (PropertiesHolder.getBoolean("agent.test.wol", false)) {
          adapter.sendMagicPacket();
        }
        if (PropertiesHolder.getBoolean("agent.test.exception", false)) {
          adapter.testException();
        }
      } catch (Exception ex) {
        logger.error("error", ex);
        adapter.setLastException(ex);
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
