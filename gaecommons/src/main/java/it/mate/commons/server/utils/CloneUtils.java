package it.mate.commons.server.utils;

import it.mate.gwtcommons.shared.model.CloneableBean;
import it.mate.gwtcommons.shared.model.CloneableProperty;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class CloneUtils {

  private static Logger logger = Logger.getLogger(CloneUtils.class);
  
  public static <T> T clone (Object source, Class<T> targetClass) {
    if (source == null) {
      return null;
    }
    return CloneUtils.internalClone(source, targetClass, new HashMap<Integer, Object>());
  }
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static <I, T extends I> List<I> clone (List sources, Class<T> targetClass, Class<I> interfaceClass) {
    Map<Integer, Object> clonedBeans = new HashMap<Integer, Object>();
    List<T> results = new ArrayList<T>();
    for (Object source : sources) {
      results.add(CloneUtils.internalClone(source, targetClass, clonedBeans));
    }
    return (List<I>)results;
  }
  
  @SuppressWarnings("unchecked")
  private static <T> T internalClone (Object source, Class<T> targetClass, Map<Integer, Object> clonedBeans) {
    
    //27/02/2012
    if (source != null) {
      Class<?> sourceClass = source.getClass();
      CloneableBean sourceCloneableBeanAnnotation = sourceClass.getAnnotation(CloneableBean.class);
      if (sourceCloneableBeanAnnotation != null && sourceCloneableBeanAnnotation.overrideTargetClassName() != null) {
        String overrideTargetClassName = sourceCloneableBeanAnnotation.overrideTargetClassName();
        try {
          Class<?> overrideTargetClass = Class.forName(overrideTargetClassName);
          if (targetClass.isAssignableFrom(overrideTargetClass)) {
            targetClass = (Class<T>)overrideTargetClass;
          }
        } catch (ClassNotFoundException ex) {
          logger.error("error", ex);
          return null;
        }
      }
    }
    
    if (targetClass.isAssignableFrom(source.getClass())) {
      return (T)source;
    } else {
      T target;
      try {
        
        // 10/10/2012
        // clonedBeans diventa una map e mantengo i target già clonati
        // li restituisco in caso di duplicazione
        
        if (clonedBeans.containsKey(source.hashCode())) {
//        logger.debug(String.format(">>>>>> the object is already cloned in this thread >>>>>> SKIP NEW CLONING - %s", source));
          return (T)clonedBeans.get(source.hashCode());
//        return null;
        /*
        } else {
          clonedBeans.add(source.hashCode());
         */
        }
        target = targetClass.newInstance();
        
        // 10/10/2012
        // BUG FIX: la put va fatta prima di chiamare la cloneProperties, altrimenti va in loop ricorsivo!
        clonedBeans.put(source.hashCode(), target);
        
        target = internalCloneProperties(source, target, clonedBeans);
        return target;
      } catch (InstantiationException ie) {
        logger.error("error instantiating target class " + targetClass.getName(), ie);
        return null;
      } catch (Exception e) {
        return null;
      }
    }
  }
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  private static <T> T internalCloneProperties (Object source, T target, Map<Integer, Object> clonedBeans) {
    if (source != null) {
      Class sourceClass = source.getClass();
      Class targetClass = target.getClass();
      
      // 28/02/2012: per gestire le classi ereditate prendo in considerazione tutti i metodi della gerarchia, 
      //             ma filtrando solo quelli dichiarati nello stesso package della classe target
      Method[] allMethods = targetClass.getMethods();
      for (Method targetMethod : allMethods) {
        
        Class declaringClass = targetMethod.getDeclaringClass();
        boolean isSetMethod = targetMethod.getName().startsWith("set");
        boolean isPublicMethod = Modifier.isPublic(targetMethod.getModifiers());
        boolean isMethodDeclaredInSameTargetPackage = declaringClass.getPackage().equals(targetClass.getPackage());
        
        if (isSetMethod && isPublicMethod && isMethodDeclaredInSameTargetPackage) {
          try {
            Method sourceMethod = sourceClass.getMethod("get" + targetMethod.getName().substring(3));
            if (sourceMethod != null) {
              
              // prendo il valore dal source
              Object value = sourceMethod.invoke(source);
              
              if (value != null) {
                CloneableProperty cloneableProperty = targetMethod.getAnnotation(CloneableProperty.class);
                if (cloneableProperty != null) {
                  if (value instanceof Collection) {
                    Collection collection = (Collection)value;
                    List clonedCollection = new ArrayList();
                    for (Object item : collection) {
                      Object clonedItem = CloneUtils.internalClone(item, cloneableProperty.targetClass(), clonedBeans);
                      if (clonedItem != null) {
                        clonedCollection.add(clonedItem);
                      }
                    }
                    value = clonedCollection;
                  } else {
                    value = CloneUtils.internalClone(value, cloneableProperty.targetClass(), clonedBeans);
                  }
                }
              }
              
              try {
                // 21/03/2012:
                // con l'introduzione di setEntity su PortalPage, sono capitati metodi col modificatore VOLATILE
                // tali metodi risultano duplicati nella lista dei metodi: uno con le annotation così come le ho
                // dichiarate e un'altro senza annotation, quindi non gestibile; riscontro empiricamente che il duplicato "sbagliato"
                // ha il modificatore VOLATILE, per cui utilizzo questo discriminante per scartare il metodo duplicato "errato"
                // OCCORRE adesso capire se ci sono altri casi in cui i metodi "volatili" invece vanno trattati
                // METTO UNA LOG ben evidente, per monitorare questi motodi
                int modifiers = targetMethod.getModifiers();
                if (Modifier.isVolatile(modifiers)) {
                  continue;
                }
                
                // setto il valore sul target
                targetMethod.invoke(target, value);
                
              } catch (IllegalArgumentException ex) {
                logger.error(String.format("error invoking method %s on target %s with value %s", targetMethod.getName(), target, value), ex);
                throw ex;
              } catch (InvocationTargetException ex) {
                logger.error(String.format("error invoking method %s on target %s with value %s", targetMethod.getName(), target, value), ex);
                throw ex;
              }
            }
          } catch (NoSuchMethodException ex) {
            // nothing
          } catch (Exception ex) {
            logger.error("error cloning", ex);
          }
        }
      }
    }
    return target;
  }
  
}
