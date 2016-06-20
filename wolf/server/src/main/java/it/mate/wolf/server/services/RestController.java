package it.mate.wolf.server.services;

import it.mate.wolf.server.model.AgentStatus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RestController {
  
  private static Logger logger = Logger.getLogger(RestController.class);
  
  @Autowired private RemoteAdapter adapter;

  @PostConstruct
  public void onPostConstruct() {
    logger.debug("initialized " + this);
    logger.debug("adapter = " + adapter);
  }

  @RequestMapping ("/scheduledChecks")
  public void scheduledChecks(HttpServletResponse response) throws Exception {
    logger.debug("adapter scheduled checks start");
    adapter.scheduledChecks();
    response.getOutputStream().print("Adapter scheduled checks started");
  }
  
  /**
   * Example: http://localhost:8080/rest/test
   */
  @RequestMapping ("/test")
  public void test(HttpServletResponse response) throws Exception {
    logger.debug("rest test");
    response.getOutputStream().print("Adapter test: " + adapter.test());
  }
  
  @RequestMapping ("/getAgentStatus")
  public void getAgentStatus(HttpServletResponse response) throws Exception {
    logger.debug("rest getAgentStatus");
    response.setContentType("text/html");
    response.getWriter().println("<h1>Agent Status</h1>");
    AgentStatus status = adapter.getAgentStatus();
    if (status == null) {
      response.getWriter().println("<p>Agent status is null</p>");
    } else {
      response.getWriter().println(String.format("<p>User agent = %s</p>", status.getUserAgent()));
      response.getWriter().println(String.format("<p>Agent status = %s</p>", status.getStatus()));
      response.getWriter().println(String.format("<p>Last access = %s</p>", dateToString(status.getLastAccess(), "Europe/Rome") ));
    }
  }
  
  private String dateToString(Date date, String timeZone) {
    Calendar calendar = new GregorianCalendar();
    calendar.setTimeInMillis(date.getTime());
    DateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss.SSS z");
    formatter.setCalendar(calendar);
    formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
    return formatter.format(calendar.getTime());    
  }
  
  @RequestMapping ("/setAgentStatus/{agentStatus}")
  public void setAgentStatus(HttpServletRequest request, HttpServletResponse response, @PathVariable String agentStatus) throws Exception {
    String userAgent = request.getHeader("User-Agent");
    logger.debug("adapter setAgentStatus");
    logger.debug("userAgent="+userAgent);
    logger.debug("agentStatus="+agentStatus);
    //response.setContentType("text/html");
    response.getWriter().println("Received Agent Status");
    response.getWriter().println(String.format("User Agent = %s", userAgent));
    response.getWriter().println(String.format("Agent Status = %s", agentStatus));
  }
  
  @RequestMapping (method=RequestMethod.POST, value="/postAgentStatus")
  public void postAgentStatus(HttpServletRequest request, HttpServletResponse response, @RequestBody String agentStatus) throws Exception {
    String userAgent = request.getHeader("User-Agent");
    logger.debug("adapter setAgentStatus");
    logger.debug("userAgent="+userAgent);
    logger.debug("agentStatus="+agentStatus);
    
    AgentStatus status = new AgentStatus();
    status.setStatus(agentStatus);
    status.setUserAgent(userAgent);
    status.setLastAccess(new Date());
    adapter.setAgentStatus(status);
    
    //response.setContentType("text/html");
    response.getWriter().println("Received Agent Status");
    response.getWriter().println(String.format("User Agent = %s", userAgent));
    response.getWriter().println(String.format("Agent Status = %s", agentStatus));
  }
  
}
