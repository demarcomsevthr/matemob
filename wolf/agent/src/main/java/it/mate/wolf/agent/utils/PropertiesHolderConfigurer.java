package it.mate.wolf.agent.utils;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class PropertiesHolderConfigurer extends PropertyPlaceholderConfigurer {

  private static final Logger logger = Logger.getLogger(PropertiesHolderConfigurer.class);

  @Override
  protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties properties) throws BeansException {
    PropertiesHolder.setProperties(properties);
    super.processProperties(beanFactoryToProcess, properties);
  }
  
  public static void reloadFromClassPathResource(String path) {
    reloadFromClassPathResource(path, false);
  }
  
  public static void reloadFromClassPathResource(String path, boolean cleanInstance) {
    try {
      Properties properties = new Properties();
      properties.load(SpringUtils.getClassPathResource(path).getInputStream());
      PropertiesHolder.setProperties(properties);
    } catch (IOException ex) {
      logger.error("error", ex);
    }
  }
  
}
