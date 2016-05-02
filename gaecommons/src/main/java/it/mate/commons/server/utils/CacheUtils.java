package it.mate.commons.server.utils;

import it.mate.commons.server.dao.JdoDao;
import it.mate.commons.server.model.CacheableEntity;
import it.mate.commons.server.model.HasKey;
import it.mate.gwtcommons.shared.utils.PropertiesHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

/**
 * 
 * @author marcello
 * 
 * 
 *
 * CHANGES LOG
 * 
 * 21/05/2012
 *   In memcache le entry vengono serializzate, se metto un ds perdo le proprietà transient (vedi unownedrelationships)
 *   quindi in cache ci devono finire i tx
 *   quindi nella put trasformo in tx class (definita sull'annotation) e mi salvo la classe del ds di provenienza
 *   per fare la trasformazione al contrario nella get
 * 
 *
 */


public class CacheUtils {

  // 22/04/2013
  // NB: ho limitato i logging tramite log4j configuration
  private static Logger logger = Logger.getLogger(CacheUtils.class);
  
  private static Map<Object, Object> instanceCache;
  
  private static List<Key> missedInstKeys = new ArrayList<Key>();
  private static List<Key> missedMemcKeys = new ArrayList<Key>();
  
  private static final String MASTER_INDEX_KEY = "masterIndex";
  
  @SuppressWarnings("unchecked")
  public static Object get (Object key) {
    Object result = null;
    if (key != null) {
      result = getInstCache().get(KeyUtils.castToString(key));
      if (result == null) {
        if (!missedInstKeys.contains(key)) {
          logger.debug(String.format("INSTCACHE MISSED entity with key %s", KeyUtils.formatToString(key)));
          missedInstKeys.add(KeyUtils.castToKey(key));
        }
        result = getMemCache().get(KeyUtils.castToString(key));
        if (result == null) {
          if (!missedMemcKeys.contains(key)) {
            logger.debug(String.format("MEMCACHE  MISSED entity with key %s", KeyUtils.formatToString(key)));
            missedMemcKeys.add(KeyUtils.castToKey(key));
          }
        }
      }
      if (result != null && result instanceof CacheEntry) {
        CacheEntry cacheEntry = (CacheEntry)result;
        if (cacheEntry.dsClass != null) {
          result = CloneUtils.clone(cacheEntry.entity, cacheEntry.dsClass);
        }
      }
    }
    return result;
  }
  
  public static void put (Object entity) {
    if (entity != null && entity instanceof Serializable) {
      Serializable serializableEntity = (Serializable)entity;
      CacheableEntity cacheableEntityAnnotation = getCacheableEntityAnnotation(serializableEntity.getClass());
      if (entity instanceof HasKey && cacheableEntityAnnotation != null) {
        HasKey hasKeyEntity = (HasKey)entity;
        Class<?> txClass = cacheableEntityAnnotation.txClass();
        Object entityClone = entity;
        if (txClass != null) {
          entityClone = CloneUtils.clone(entity, txClass);
        }
        CacheEntry cacheEntry = new CacheEntry(entityClone, txClass, entity.getClass(), KeyUtils.castToString(hasKeyEntity.getKey()));
        if (cacheableEntityAnnotation.instanceCache()) {
          getInstCache().put(KeyUtils.castToString(hasKeyEntity.getKey()), cacheEntry);
        }
        // in mem cache sempre
        getMemCache().put(KeyUtils.castToString(hasKeyEntity.getKey()), cacheEntry);
        updateKeyInMasterIndex(hasKeyEntity.getKey());
      }
    }
  }
  
  private static void updateKeyInMasterIndex(Key key) {
    List<String> masterIndex = (List<String>)getMemCache().get(MASTER_INDEX_KEY);
    if (masterIndex == null) {
      masterIndex = new ArrayList<String>();
    }
    masterIndex.add(KeyUtils.castToString(key));
    getMemCache().put(MASTER_INDEX_KEY, masterIndex);
  }
  
  public static void purgeMasterIndex() {
    List<String> masterIndex = (List<String>)getMemCache().get(MASTER_INDEX_KEY);
    if (masterIndex == null)
      return;
    for (Iterator<String> it = masterIndex.iterator(); it.hasNext();) {
      String index = it.next();
      if (!getMemCache().contains(index)) {
        it.remove();
      }
    }
    getMemCache().put(MASTER_INDEX_KEY, masterIndex);
  }
  
  public static List<CacheEntry> getAllMemCacheEntries() {
    List<CacheEntry> results = new ArrayList<CacheEntry>();
    List<String> masterIndex = (List<String>)getMemCache().get(MASTER_INDEX_KEY);
    if (masterIndex != null) {
      for (String index : masterIndex) {
        Object entry = getMemCache().get(index);
        if (entry != null && entry instanceof CacheEntry) {
          results.add((CacheEntry)entry);
        }
      }
    }
    return results;
  }
  
  public static void delete (Object entity) {
    if (entity != null && entity instanceof HasKey && entity.getClass().getAnnotation(CacheableEntity.class) != null) {
      try {
        HasKey hasKeyEntity = (HasKey)entity;
        deleteByKey(hasKeyEntity.getKey());
      } catch (Exception ex) {
        logger.error("error", ex);
      }
    }
  }
  
  public static void deleteByKey (Object key) {
    if (existsInInstCacheByKey(key)) {
      getInstCache().remove(KeyUtils.castToString(key));
    }
    if (existsInMemCacheByKey(key)) {
      getMemCache().delete(KeyUtils.castToString(key));
    }
  }
  
  @SuppressWarnings("unchecked")
  public static <E> void deleteByKeyWithCondition (Object id, Class<E> entryType, Condition<E> condition) {
    Object cachedEntry = CacheUtils.get(id);
    if (cachedEntry != null && entryType.isAssignableFrom(cachedEntry.getClass())) {
      E cachedEntity = (E)cachedEntry;
      if (condition.evaluate(cachedEntity)) {
        CacheUtils.deleteByKey(id);
      }
    }
  }
  
  private static boolean existsInMemCacheByKey (Object key) {
    return getMemCache().contains(KeyUtils.castToString(key));
  }
  
  private static boolean existsInInstCacheByKey (Object key) {
    return getInstCache().containsKey(KeyUtils.castToString(key));
  }
  
  public static void clearAll() {
    getInstCache().clear();
    getMemCache().clearAll();
  }
  
  public static CacheableEntity getCacheableEntityAnnotation(Class<? extends Serializable> entityClass) {
    CacheableEntity cacheableEntityAnnotation = entityClass.getAnnotation(CacheableEntity.class);
    if (cacheableEntityAnnotation == null) {
      List<Class<? extends Serializable>> subclasses = JdoDao.findSubClasses(entityClass);
      if (subclasses != null) {
        for (Class<? extends Serializable> subclass : subclasses) {
          CacheableEntity tmpCacheableEntityAnnotation = subclass.getAnnotation(CacheableEntity.class);
          if (tmpCacheableEntityAnnotation != null) {
            cacheableEntityAnnotation = tmpCacheableEntityAnnotation;
          }
        }
      }
    }
    return cacheableEntityAnnotation;
  }
  
  @SuppressWarnings({"serial", "rawtypes"})
  public static class CacheEntry implements Serializable {
    public Object entity; 
    public Class txClass;
    public Class dsClass;
    public String key;
    public CacheEntry(Object entity, Class txClass, Class dsClass, String key) {
      super();
      this.entity = entity;
      this.txClass = txClass;
      this.dsClass = dsClass;
      this.key = key;
    }
    public String toString() {
      return "CacheEntry [txClass=" + txClass + ", dsClass=" + dsClass + ", entity=" + entity + "]";
    }
    public Object getEntity() {
      return entity;
    }
    public String getKey() {
      return key;
    }
  }

  private static MemcacheService getMemCache() {
    return MemcacheServiceFactory.getMemcacheService();
  }
  
  private synchronized static Map<Object, Object> getInstCache() {
    if (PropertiesHolder.getBoolean("it.mate.commons.server.utils.CacheUtils.useInstanceCache", true)) {
      if (instanceCache == null) {
        instanceCache = new HashMap<Object, Object>();
      }
      return instanceCache;
    } else {
      // se voglio annullare la cache globalmente restituisco sempre una map vuota
      return new HashMap<Object, Object>();
    }
  }
  
  public static Set<Object> instKeySet () {
    return getInstCache().keySet();
  }
  
  public static Object instGet (Object key) {
    return getInstCache().get(key);
  }
  
  public static Object instPut (Object key, Object value) {
    return getInstCache().put(key, value);
  }
  
  public interface Condition <E> {
    public boolean evaluate(E cachedEntity);
  }
  
}