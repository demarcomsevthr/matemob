package it.mate.commons.server.dao;

import it.mate.commons.server.model.CacheableEntity;
import it.mate.commons.server.model.HasKey;
import it.mate.commons.server.utils.CacheUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.appengine.api.datastore.Key;

@SuppressWarnings({"rawtypes", "unchecked"})
public class JdoDaoWithCache extends JdoDao {

  private static Logger logger = Logger.getLogger(JdoDaoWithCache.class);
  
  @Override
  protected <E extends Serializable> Object internalFind(FindContext<E> context) {
    
    CacheableEntity cacheableEntityAnnotation = CacheUtils.getCacheableEntityAnnotation(context.getEntityClass());
    
    boolean useCache = (cacheableEntityAnnotation != null && !context.cacheDisabled());
    
    if (useCache) {
      
      if (context.getId() != null) {
        
        Object cachedResult = CacheUtils.get(context.getId());
        if (cachedResult != null) {
          return cachedResult;
        }
        
      } else {
        
        boolean relationshipsResolutionDisabled = context.isRelationshipsResolutionDisabled();
        context.setRelationshipsResolutionDisabled(true);
        boolean keysOnly = context.keysOnly();
        context.setKeysOnly(true);
        Object results = super.internalFind(context);
        context.setRelationshipsResolutionDisabled(relationshipsResolutionDisabled);
        context.setKeysOnly(keysOnly);

        if (results != null) {
          List<E> cachedResults = new ArrayList<E>();
          List<Serializable> missingIds = new ArrayList<Serializable>();
          boolean singleResult = false;
          if (results instanceof List) {
            List<?> resultList = (List<?>)results;
            for (Object result : resultList) {
              Key key = null;
              if (result instanceof Key) {
                key = (Key)result;
              } else {
                HasKey hasKey = (HasKey)result;
                key = hasKey.getKey();
              }
              E cachedResult = (E)CacheUtils.get(key);
              if (cachedResult != null) {
                cachedResults.add(cachedResult);
              } else {
                missingIds.add(key);
              }
            }
          } else {
            singleResult = true;
            Object result = results;
            Key key = null;
            if (result instanceof Key) {
              key = (Key)result;
            } else {
              HasKey hasKey = (HasKey)result;
              key = hasKey.getKey();
            }
            E cachedResult = (E)CacheUtils.get(key);
            if (cachedResult != null) {
              cachedResults.add(cachedResult);
            } else {
              missingIds.add(key);
            }
          }
          
          if (missingIds.size() > 0) {
            for (Serializable id : missingIds) {
              FindContext<E> findByIdContext = new FindContext<E>(context.getEntityClass()).setId(id);
              E missingEntity = (E)this.internalFind(findByIdContext);
              cachedResults.add(missingEntity);
            }
          }
          
          if (singleResult) {
            if (cachedResults.size() > 0) {
              return cachedResults.get(0);
            } else {
              return null;
            }
          } else {
            return cachedResults;
          }
          
        }
        
      }
    }
    
    
    Object result = super.internalFind(context);
  
    if (useCache && context.getId() != null) {
      CacheUtils.put(result);
    }
    
    return result;
  }
  
  @Override
  public <E extends Serializable> E update(E entity) {
    E updatedEntity = super.update(entity);
    CacheUtils.put(updatedEntity);
    return updatedEntity;
  };
  
  @Override
  public <E extends Serializable> E update(Class<E> entityClass, Serializable id, UpdateCallback<E> callback) {
    E updatedEntity = super.update(entityClass, id, callback);
    Object cachedResult = CacheUtils.get(id);
    if (cachedResult != null) {
      cachedResult = callback.updateEntityValues(null, (E)cachedResult);
      CacheUtils.put(cachedResult);
    }
    return updatedEntity;
  }
  
  @Override
  public <E extends Serializable> void delete(E entity) {
    try {
      CacheUtils.delete(entity);
    } catch (Exception ex) {
      logger.error("error", ex);
    }
    super.delete(entity);
  };
  
  @Override
  protected Object resolveUnownedRelationships(Object result, FindContext context) {
    Object resolvedEntity = super.resolveUnownedRelationships(result, context);
    if (resolvedEntity != null) {
      if (context != null && !context.cacheDisabled()) {
        CacheUtils.put(resolvedEntity);
      }
    }
    return resolvedEntity;
  }
  
  public void clearCache() {
    CacheUtils.clearAll();
  }
  
}
