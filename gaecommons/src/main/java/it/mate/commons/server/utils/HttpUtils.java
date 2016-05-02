package it.mate.commons.server.utils;

import javax.servlet.http.HttpServletRequest;

public class HttpUtils {
  
  public static String getContextUrl(HttpServletRequest request) {
    StringBuffer result = new StringBuffer();
    result.append(request.getScheme());
    result.append("://");
    result.append(request.getServerName());
    if (request.getServerPort() != 80) {
      result.append(":" + request.getServerPort());
    }
    if (request.getContextPath() != null && request.getContextPath().length() > 0) {
      result.append("/");
      result.append(request.getContextPath());
    }
    return result.toString();
  }

}
