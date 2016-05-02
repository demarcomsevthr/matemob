package it.mate.commons.server.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;

public class XStreamUtils {
  
  private XStreamAppengine xstream;
  
  public XStreamUtils() {
    xstream = new XStreamAppengine(new PureJavaReflectionProvider());
  }

  public XStreamUtils(HierarchicalStreamDriver driver) {
    xstream = new XStreamAppengine(new PureJavaReflectionProvider(), driver);
  }

  public XStream getXStream() {
    return xstream;
  }
  
  public String parseGraph(Object graph) {
    return parseGraph(graph, null);
  }
  
  public String parseGraph(Object graph, String encoding) {
    String xml = null;
    if (encoding != null) {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      try {
        Writer writer = new OutputStreamWriter(baos, encoding);
        xstream.toXML(graph, writer);
        xml = baos.toString(encoding);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      xml = xstream.toXML(graph);
    }
    return xml;
  }
  
  public Object buildGraph(File xmlFile) {
    Object graph = xstream.fromXML(xmlFile);
    return graph;
  }
  
  public Object buildGraph(String xml) {
    Object graph = xstream.fromXML(xml);
    return graph;
  }
  
  public void registerConverter(Converter converter) {
    xstream.registerConverter(converter);
  }

}
