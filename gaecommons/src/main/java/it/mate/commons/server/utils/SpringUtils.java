package it.mate.commons.server.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.type.filter.AssignableTypeFilter;

public class SpringUtils {

  public static <B> List<Class<? extends B>> scanPackageClasses (String basePackageNameFilter, Class<? extends B> baseClassFilter) throws Exception {
    ClassPathScanningCandidateComponentProvider scanningProvider = new ClassPathScanningCandidateComponentProvider(true);
    scanningProvider.addIncludeFilter(new AssignableTypeFilter(baseClassFilter));
    Set<BeanDefinition> beanDefinitions = scanningProvider.findCandidateComponents(basePackageNameFilter);
    List<Class<? extends B>> results = new ArrayList<Class<? extends B>>();
    for (BeanDefinition beanDefinition : beanDefinitions) {
      Class<? extends B> subClass = (Class<? extends B>)Class.forName(beanDefinition.getBeanClassName(), false, scanningProvider.getClass().getClassLoader());
      results.add(subClass);
    }
    return results;
  }
  
  public static ApplicationContext getStringApplicationContext(String definitionText) {
    ByteArrayResource resource = new ByteArrayResource(definitionText.getBytes());
    GenericXmlApplicationContext context = new GenericXmlApplicationContext(resource);
    return context;
  }
  
  public static Resource getClassPathResource(String path) {
    return new ClassPathResource(path);
  }
  
}
