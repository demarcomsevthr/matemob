package it.mate.testons.server.servlet;

import it.mate.commons.server.utils.LoggingUtils;
import it.mate.testons.server.services.MainAdapter;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@SuppressWarnings("serial")
public class DownloadServlet extends HttpServlet {
  
  MainAdapter adapter;
  
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
    adapter = context.getBean(MainAdapter.class);
    LoggingUtils.debug(getClass(), "initialized " + this);
    LoggingUtils.debug(getClass(), "adapter = " + adapter);
  }

  
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    try {
      String name = request.getParameter("name");
      
    } catch (Exception ex) {
      LoggingUtils.error(getClass(), ex.getMessage());
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
    }
    
    
  }
  
  
}
