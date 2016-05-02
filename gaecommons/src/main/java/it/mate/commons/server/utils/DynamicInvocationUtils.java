package it.mate.commons.server.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class DynamicInvocationUtils {
  
  protected static Logger logger = Logger.getLogger(DynamicInvocationUtils.class);
  
  private static Map<String, Method> methodCache = new HashMap<String, Method>();

  public static Object invokeMethod (Object instance, String methodName, Object... args) throws Exception {
    try {
      Class<?> type = instance.getClass();
      String cacheKey = type.getName()+"$"+methodName;
      if (!methodCache.containsKey(cacheKey)) {
        Method[] allMethods = type.getMethods();
        for (Method method : allMethods) {
          if (method.getName().equals(methodName)) {
            methodCache.put(cacheKey, method);
          }
        }
      }
      Method method = methodCache.get(cacheKey);
      if (method != null) {
        return method.invoke(instance, args);
      }
      /* 18/04/2013
       * (a seguito introduzione di dao.findWithContext in EntityRelationshipsResolver.resolveUnownedRelationshipsWithAnnotation)
      throw new IllegalArgumentException(String.format("method %s not found in object of type %s", methodName, type));
      */
      logger.error(String.format("method %s not found in object of type %s", methodName, type));
      return null;
    } catch (InvocationTargetException ex) {
      if (ex.getTargetException() instanceof Exception) {
        throw (Exception)ex.getTargetException();
      } else {
        throw ex;
      }
    } catch (Exception ex) {
      throw ex;
    }
  }
  
  
}
