package it.mate.tickler.server.services;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
    response.setContentType("text/html");
    List<List<Object>> values = adapter.test();
    if (values == null || values.size() == 0) {
      response.getWriter().println("<p>No data found</p>");
    } else {
      response.getWriter().println("<table>");
      for (List row : values) {
        response.getWriter().println("<tr>");
        response.getWriter().println(String.format("<td>%s</td><td>(%s)</td><td>%s</td><td>(%s)</td>", row.get(0), row.get(0).getClass(), row.get(4), row.get(4).getClass()));
        response.getWriter().println("</tr>");
      }
      response.getWriter().println("</table>");
    }
  }
  
}
