package it.mate.testons.server.servlet;

import it.mate.commons.server.utils.LoggingUtils;
import it.mate.testons.server.services.AdapterUtil;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class InitServlet extends HttpServlet {

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    AdapterUtil.initContext(config.getServletContext());
    LoggingUtils.debug(getClass(), "initialized " + this);
  }
  
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      String oper = request.getParameter("oper");
      
      if ("initTimbri".equals(oper)) {
        LoggingUtils.debug(getClass(), "requested initTimbri operation");
      }
      
      
    } catch (Exception ex) {
      LoggingUtils.error(getClass(), ex.getMessage());
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
    }
  }

}
