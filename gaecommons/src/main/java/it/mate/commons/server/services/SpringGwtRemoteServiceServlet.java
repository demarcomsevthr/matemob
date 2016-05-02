package it.mate.commons.server.services;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class SpringGwtRemoteServiceServlet extends RemoteServiceServlet {

  private static Logger logger = Logger.getLogger(SpringGwtRemoteServiceServlet.class);

  @Override
  public String processCall(String payload) throws SerializationException {
    try {
      Object handler = getBean(getThreadLocalRequest());
      RPCRequest rpcRequest = RPC.decodeRequest(payload, handler.getClass(), this);
      onAfterRequestDeserialized(rpcRequest);
      logger.debug("Invoking " + handler.getClass().getName() + "." + rpcRequest.getMethod().getName());
      return RPC.invokeAndEncodeResponse(handler, rpcRequest.getMethod(), rpcRequest.getParameters(), rpcRequest.getSerializationPolicy());
    } catch (IncompatibleRemoteServiceException ex) {
      log("An IncompatibleRemoteServiceException was thrown while processing this call.", ex);
      return RPC.encodeResponseForFailure(null, ex);
    }
  }

  protected Object getBean(HttpServletRequest request) {
    String service = getService(request);
    Object bean = getBean(service);
    if (!(bean instanceof RemoteService)) {
      throw new IllegalArgumentException("Spring bean is not a GWT RemoteService: " + service + " (" + bean + ")");
    }
    logger.debug("Bean for service " + service + " is " + bean);
    return bean;
  }

  protected String getService(HttpServletRequest request) {
    String url = request.getRequestURI();
    String service = url.substring(url.lastIndexOf("/") + 1);
    logger.debug("Service for URL " + url + " is " + service);
    return service;
  }

  protected Object getBean(String name) {
    WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
    if (applicationContext == null) {
      throw new IllegalStateException("No Spring web application context found");
    }
    if (!applicationContext.containsBean(name)) {
      throw new IllegalArgumentException("Spring bean not found: " + name);
    }
    return applicationContext.getBean(name);
  }

}
