package it.mate.wolf.agent.adapter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import it.mate.wolf.adapter.model.AgentStatus;
import it.mate.wolf.agent.utils.LoggingRequestInterceptor;
import it.mate.wolf.agent.utils.PropertiesHolder;

public class AgentAdapterImpl implements AgentAdapter {

  private static Logger logger = Logger.getLogger(AgentAdapterImpl.class);

  private AgentStatus localStatus;

  protected AgentStatus getLocalStatus() {
    if (localStatus == null) {
      localStatus = new AgentStatus();
    }
    return localStatus;
  }

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
  public void sendAgentStatus() throws Exception {
    String serverurl = PropertiesHolder.getString("agent.serverurl");
    String method = PropertiesHolder.getString("agent.status.method", "/rest/postAgentStatusObject");

    AgentStatus agentStatus = getLocalStatus();
    agentStatus.setStatus("ON");
    agentStatus.setHostname(getHostname());
    agentStatus.setIp(getAllIps());
    agentStatus.setTemperature(getTemperature());
    agentStatus.setMemory(getMemory());

    logger.debug("Sending agent status");
    String url = serverurl + method;
    RestTemplate rt = new RestTemplate();

    if (PropertiesHolder.getBoolean("agent.status.trace", false)) {
      addLoggingRequestInterceptor(rt, false);
    }

    AgentStatus responseStatus = rt.postForObject(url, agentStatus, AgentStatus.class, new HashMap<String, String>());
    logger.debug("Received: " + responseStatus);

    if (responseStatus.getCommand() != null) {
      if (AgentStatus.COMMAND_WOL.equalsIgnoreCase(responseStatus.getCommand())) {
        logger.debug("EXECUTING COMMAND " + responseStatus.getCommand());
        sendMagicPacket();
        getLocalStatus().setLastCommand(responseStatus.getCommand());
        getLocalStatus().setLastCommandTime(new Date());
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
        // logger.debug(String.format("found ip=%s canonical=%s", ip,
        // address.getCanonicalHostName()));
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

  public void sendMagicPacket() throws Exception {
    String destIp = PropertiesHolder.getString("agent.sendMagicPacket.destIp", "255.255.255.0");
    String macAddr = PropertiesHolder.getString("agent.sendMagicPacket.macAddr", "90E6BA327769");
    int port = Integer.parseInt(PropertiesHolder.getString("agent.sendMagicPacket.port", "9"));
    if (destIp == null || destIp.trim().length() == 0 || macAddr == null || macAddr.trim().length() == 0) {
      logger.debug("SendMagicPacket disabled");
    } else {
      sendMagicPacket(destIp, macAddr, port);
    }
  }
  
  protected void sendMagicPacket(String destIp, String macAddr, int port) throws Exception {
    logger.debug(String.format("Sending magic packet to %s %s", destIp, macAddr));
    byte[] macAddrBytes = hexStringToByteArray(macAddr);
    final int REPETITIONS = 16;
    byte[] buffer = new byte[(REPETITIONS + 1) * 6];
    
    for (int i = 0; i < 6; i++)
      buffer[i] = (byte) 0xFF;
    for (int i = 1; i <= REPETITIONS; i++)
      for (int j = 0; j < 6; j++)
        buffer[i * 6 + j] = macAddrBytes[j];
    
    InetAddress address = InetAddress.getByName(destIp);
    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
    DatagramSocket datagramSocket = new DatagramSocket();
    datagramSocket.send(packet);
    
    logger.debug(String.format("Sent magic packet to %s %s", destIp, macAddr));
    datagramSocket.close();
  }

  protected byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
    }
    return data;
  }

  private void addLoggingRequestInterceptor(RestTemplate rt, boolean readResponseBody) {
    ClientHttpRequestInterceptor ri = new LoggingRequestInterceptor(readResponseBody);
    List<ClientHttpRequestInterceptor> ris = new ArrayList<ClientHttpRequestInterceptor>();
    ris.add(ri);
    rt.setInterceptors(ris);
  }
  
  public void testException() throws Exception {
    throw new RuntimeException("Prova runtime exception");
  }
  
  public void setLastException(Exception ex) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ex.printStackTrace(new PrintStream(baos));
    String text = new String(baos.toByteArray());
    getLocalStatus().setLastException(text);
    getLocalStatus().setLastExceptionTime(new Date());
  }

}
