package it.mate.commons.server.dao;

import java.io.Serializable;
import java.util.List;

import com.google.appengine.api.datastore.Key;

public interface Dao {
  
  public <E extends Serializable> List<E> findAll(final Class<E> entityClass);

  public <E extends Serializable> List<E> findAll(final Class<E> entityClass, FindCallback<E> callback);

  public <E extends Serializable> E findWithContext(FindContext<E> context);
  
  public <E extends Serializable> E findById(final Class<E> entityClass, final Serializable id);

  public <E extends Serializable> E findById(final Class<E> entityClass, final Serializable id, FindCallback<E> callback);

  public <E extends Serializable> E update(E entity);

  public <E extends Serializable> E update(Class<E> entityClass, Serializable id, UpdateCallback<E> callback);
  
  public <E extends Serializable> void delete(final E entity);

  public <E extends Serializable> E create(final E entity);

  public <E extends Serializable> E findSingle(Class<E> entityClass, String filter);
  
  public <E extends Serializable> E findSingle(Class<E> entityClass, String filter, FindCallback<E> callback);
  
  /*
  public <E extends Serializable> E findSingle(Class<E> entityClass, String filter, String parameters, Object... values);
  */
  
  public <E extends Serializable> E findSingle(Class<E> entityClass, String filter, String parameters, FindCallback<E> callback, Object... values);
  
  public <E extends Serializable> E findSingle(FindContext<E> context);
  
  public <E extends Serializable> List<E> findList(FindContext<E> context);
  
  public <E extends Serializable> List<Key> findKeys(FindContext<E> context);
  
  public <E extends Serializable> List<E> findList(Class<E> entityClass, String filter, String parameters, FindCallback<E> callback, Object... values);  
  
  
  public class Utils {
    public static String buildParameters(ParameterDefinition[] params) {
      String result = null;
      for (ParameterDefinition param : params) {
        if (result == null) {
          result = "";
        } else {
          result += ", ";
        }
        result += param.type.getName() + " " + param.name;
      }
      return result;
    }
  }
  
}
