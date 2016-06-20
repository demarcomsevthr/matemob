package it.mate.wolf.agent.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

  private static Logger logger = Logger.getLogger(LoggingRequestInterceptor.class);

  private boolean readResponseBody = false;

  public LoggingRequestInterceptor(boolean readResponseBody) {
    this.readResponseBody = readResponseBody;
  }

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    traceRequest(request, body);
    ClientHttpResponse response = execution.execute(request, body);
    traceRespons(response);
    return response;
  }

  private void traceRequest(HttpRequest request, byte[] body) throws IOException {
    logger.debug("===========================request begin================================================");
    logger.debug("URI : " + request.getURI());

    HttpHeaders headers = request.getHeaders();
    for (String key : headers.keySet()) {
      headers.get(key);
      List<String> values = headers.get(key);
      for (String value : values) {
        logger.debug(String.format("Header.%s : %s", key, value));
      }
    }
    
    logger.debug("Method : " + request.getMethod());
    logger.debug("Request Body : " + new String(body, "UTF-8"));
    logger.debug("==========================request end================================================");
  }

  private void traceRespons(ClientHttpResponse response) throws IOException {
    StringBuilder inputStringBuilder = new StringBuilder();
    if (readResponseBody) {
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"));
      String line = bufferedReader.readLine();
      while (line != null) {
        inputStringBuilder.append(line);
        inputStringBuilder.append('\n');
        line = bufferedReader.readLine();
      }
    }
    logger.debug("============================response begin==========================================");
    logger.debug("status code: " + response.getStatusCode());
    logger.debug("status text: " + response.getStatusText());
    logger.debug("Response Body : " + inputStringBuilder.toString());
    logger.debug("=======================response end=================================================");
  }

}
