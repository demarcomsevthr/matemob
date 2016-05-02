package it.mate.commons.server.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtils {
  
  public static List<Field> getAllPrivateFields(Class<?> entityClass, List<Field> previousFields) {
    List<Field> resultFields;
    if (previousFields != null) {
      resultFields = previousFields;
    } else {
      resultFields = new ArrayList<Field>();
    }
    Field[] fields = entityClass.getDeclaredFields();
    for (Field field : fields) {
      resultFields.add(field);
    }
    Class<?> superclass = entityClass.getSuperclass();
    if (superclass != null) {
      return getAllPrivateFields(superclass, resultFields);
    } else {
      return resultFields;
    }
  }
  
  public static Method getMethodByName(Class<?> entityClass, String methodName) throws Exception {
    return entityClass.getDeclaredMethod(methodName);
  }

}
