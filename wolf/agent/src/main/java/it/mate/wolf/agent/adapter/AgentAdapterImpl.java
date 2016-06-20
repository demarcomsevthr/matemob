package it.mate.wolf.agent.adapter;

import it.mate.wolf.adapter.model.AgentStatus;
import it.mate.wolf.agent.utils.LoggingRequestInterceptor;
import it.mate.wolf.agent.utils.PropertiesHolder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

public class AgentAdapterImpl implements AgentAdapter {

  private static Logger logger = Logger.getLogger(AgentAdapterImpl.class);
  
  private String lastCommand;

  @Override
  public void getAgentStatus() {
    String serverurl = PropertiesHolder.getString("agent.serverurl");
    String testMethod = PropertiesHolder.getString("agent.test.method", "/rest/getAgentStatusObject");
    String url = serverurl + testMethod;
    logger.debug("Agent is runnig");
    logger.debug("Server url=" + serverurl);
    logger.debug("Test url=" + url);
    RestTemplate rt = new RestTemplate();
    if (PropertiesHolder.getBoolean("agent.test.trace", false)) {
      addLoggingRequestInterceptor(rt, false);
    }
    AgentStatus response = rt.getForObject(url, AgentStatus.class);
    logger.debug("Received: " + response);
  }

  @Override
  public void sendAgentStatusText() throws Exception {
    String serverurl = PropertiesHolder.getString("agent.serverurl");
    String method = PropertiesHolder.getString("agent.status.method", "/rest/postAgentStatusObject");
    String status = "";
    status = "hostname=" + getHostname();
    status += "|ips=" + getAllIps();
    status += "|" + getTemperature();
    status += "|memory=" + getMemory();
    logger.debug("Sending agent status");
    String url = serverurl + method;
    Map<String, String> params = new HashMap<String, String>();
    RestTemplate rt = new RestTemplate();

    if (PropertiesHolder.getBoolean("agent.status.trace", false)) {
      addLoggingRequestInterceptor(rt, false);
    }
    
    AgentStatus response = rt.postForObject(url, status, AgentStatus.class, params);
    logger.debug("Received: " + response);

  }
  
  @Override
  public void sendAgentStatus() throws Exception {
    String serverurl = PropertiesHolder.getString("agent.serverurl");
    String method = PropertiesHolder.getString("agent.status.method", "/rest/postAgentStatusObject");
    
    /*
    String status = "";
    status = "hostname=" + getHostname();
    status += "|ips=" + getAllIps();
    status += "|" + getTemperature();
    status += "|memory=" + getMemory();
    */
    
    AgentStatus request = new AgentStatus();
    request.setStatus("ON" + (lastCommand != null ? (";" + lastCommand) : ""));
    request.setHostname(getHostname());
    request.setIp(getAllIps());
    request.setTemperature(getTemperature());
    request.setMemory(getMemory());
    
    logger.debug("Sending agent status");
    String url = serverurl + method;
    RestTemplate rt = new RestTemplate();

    if (PropertiesHolder.getBoolean("agent.status.trace", false)) {
      addLoggingRequestInterceptor(rt, false);
    }
    
    AgentStatus response = rt.postForObject(url, request, AgentStatus.class, new HashMap<String, String>());
    logger.debug("Received: " + response);
    
    if (response.getCommand() != null) {
      if (AgentStatus.COMMAND_WOL.equalsIgnoreCase(response.getCommand())) {
        logger.debug("TODO: EXECUTING COMMAND " + response.getCommand());
        lastCommand = "WOL AT " + new Date();
        resetAgentCommand();
      }
    }

  }
  
  protected void resetAgentCommand() {
    String serverurl = PropertiesHolder.getString("agent.serverurl");
    String method = PropertiesHolder.getString("agent.resetCommand.method", "/rest/setAgentCommand");
    logger.debug("Sending reset agent command");
    String url = serverurl + method + "/noop";
    RestTemplate rt = new RestTemplate();
    if (PropertiesHolder.getBoolean("agent.status.trace", false)) {
      addLoggingRequestInterceptor(rt, false);
    }
    String response = rt.getForObject(url, String.class);
    logger.debug("Received: " + response);
  }
  
  public String getHostname() throws IOException {
    InetAddress addr;
    addr = InetAddress.getLocalHost();
    return addr.getHostName();
  }

  public String getAllIps() throws IOException {
    String ips = "";
    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
    while (interfaces.hasMoreElements()) {
      NetworkInterface interf = (NetworkInterface) interfaces.nextElement();
      Enumeration<InetAddress> addresses = interf.getInetAddresses();
      while (addresses.hasMoreElements()) {
        InetAddress address = (InetAddress) addresses.nextElement();
        String ip = address.getHostAddress();
        if (ip.contains(":"))
          continue;
//      logger.debug(String.format("found ip=%s canonical=%s", ip, address.getCanonicalHostName()));
        if (ips.length() > 0)
          ips += "^";
        ips += ip;
      }
    }
    return ips;
  }

  public String getTemperature() throws IOException, InterruptedException {
    String result = "temp=unknown";
    String command = PropertiesHolder.getString("agent.temperature.command");
    if (command == null || command.trim().length() == 0) {
      return result;
    }
    Runtime rt = Runtime.getRuntime();
    Process proc = rt.exec(command);
    proc.waitFor();
    if (proc != null) {
      InputStream is = proc.getInputStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      String line = null;
      while ((line = br.readLine()) != null) {
        logger.debug("temp output = " + line);
        result = line;
      }

    }
    return result;
  }

  public String getMemory() throws IOException {
    String result = "";
    Runtime rt = Runtime.getRuntime();
    result += "free=" + rt.freeMemory();
    result += "^max=" + rt.maxMemory();
    result += "^total=" + rt.totalMemory();
    return result;
  }

  public void sendMagicPacket(byte[] mac) throws Exception {

    byte[] buffer = new byte[17 * 6];
    for (int i = 0; i < 6; i++)
      buffer[i] = (byte)0xFF;
    
    for (int i = 1; i <= 16; i++)
      for (int j = 0; j < 6; j++)
          buffer[i * 6 + j] = mac[j];    
    
    InetAddress address = InetAddress.getByName("255.255.255.0");
    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 9);
    DatagramSocket datagramSocket = new DatagramSocket();
    datagramSocket.send(packet);
    System.out.println(InetAddress.getLocalHost().getHostAddress());
    datagramSocket.close();

  }
  
  private void addLoggingRequestInterceptor(RestTemplate rt, boolean readResponseBody) {
    ClientHttpRequestInterceptor ri = new LoggingRequestInterceptor(readResponseBody);
    List<ClientHttpRequestInterceptor> ris = new ArrayList<ClientHttpRequestInterceptor>();
    ris.add(ri);
    rt.setInterceptors(ris);
  }

}
