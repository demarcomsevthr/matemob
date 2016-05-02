package it.mate.commons.server.utils;

import it.mate.commons.server.dao.Dao;
import it.mate.commons.server.dao.EntityRelationshipsResolverHandler;
import it.mate.commons.server.dao.FindContext;
import it.mate.commons.server.dao.PersistenceException;
import it.mate.commons.server.model.HasKey;
import it.mate.commons.server.model.UnownedRelationship;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.appengine.api.datastore.Key;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EntityRelationshipsResolver {

  // 22/04/2013
  // NB: ho limitato i logging tramite log4j configuration
  private static Logger logger = Logger.getLogger(EntityRelationshipsResolver.class);
  
  private Dao dao;
  
  private FindContext context;
  
  public EntityRelationshipsResolver(Dao dao, FindContext context) {
    this.dao = dao;
    this.context = context;
  }
  
  public Object resolveUnownedRelationships (Object entity) {
    return resolveUnownedRelationships(entity, new HashMap<Key, Object>());
  }
  
  // 17/04/2013
  // Introdotto resolvedEntities per evitare i loop ricorsivi
  // si è manifestato con l'introduzione di Produttore.getProducts (e Articolo.getProducer) 
  private Object resolveUnownedRelationships (Object entity, Map<Key, Object> resolvedEntities) {
    if (entity == null)
      return null;
    
    if (entity instanceof EntityRelationshipsResolverHandler) {
      ((EntityRelationshipsResolverHandler) entity).onBeforeResolveRelationships();
    }
    
    if (entity instanceof List) {
      List results = (List)entity;
      for (Object item : results) {
        
        /* ***********
         * 16/05/2013 >> in questo caso il controllo sulle resolvedEntities lo fa due volte 
         * (lo ripete nella chiamata innestata, quindi non risolve quando invece dovrebbe)
         * 
        if (resolvedEntities.containsKey(getKey(item))) {
          logRecursiveLoop(getKey(item));
        } else {
          resolvedEntities.put(getKey(item), item);
          resolveUnownedRelationships(item, resolvedEntities);
        }
        */
        resolveUnownedRelationships(item, resolvedEntities);
        
      }
      
    /* 16/05/2013  
    } else if (entity instanceof HasUnownedRelationships) {
      HasUnownedRelationships hasDipendencies = (HasUnownedRelationships)entity;
      hasDipendencies.resolveUnownedRelationships();
      */
      
    } else {
      if (resolvedEntities.containsKey(getKey(entity))) {
        logRecursiveLoop(getKey(entity));
      } else {
        resolvedEntities.put(getKey(entity), entity);
        resolveUnownedRelationshipsWithAnnotation(entity, resolvedEntities);
      }
    }
    return entity;
  }
  
  private void resolveUnownedRelationshipsWithAnnotation (Object entity, Map<Key, Object> resolvedEntities) {
    if (entity == null)
      return;
    
    /*
    if (entity.getClass().getSimpleName().endsWith("PortalFolderPageDs")) {
      logger.debug(">>>>>>>>>>>>>>>>> DEBUGGING");
    }
    */
    
    Field[] fields = getAllHierarchyDeclaredFieldsInSamePackage(entity.getClass());
    for (Field field : fields) {
      UnownedRelationship unownedRelationshipAnnotation = field.getAnnotation(UnownedRelationship.class);
      if (unownedRelationshipAnnotation != null) {
        Field relationshipField = field;
        String keyName = unownedRelationshipAnnotation.key();
        try {
          for (Field keyField : fields) {
            if (keyName.equals(keyField.getName())) {
              keyField.setAccessible(true);
              relationshipField.setAccessible(true);
              
              //25.02.2011
              if (Collection.class.isAssignableFrom(relationshipField.getType())) {
                Class itemClass = unownedRelationshipAnnotation.itemClass();
                if (itemClass != Void.class) {
                  Collection<Key> keys = (List<Key>)keyField.get(entity);
                  if (keys != null) {
                    List relatedEntities = new ArrayList();
                    for (Key relatedKey : keys) {
                      
                      // 18/04/2013
                      // Indirettamente questo permette di risolvere le entity correlate in maniera ricorsiva
//                    Object relatedEntity = dao.findById((Class<Serializable>)itemClass, relatedKey);
//                    Object relatedEntity = dao.findWithContext(new FindContext<Serializable>(this.context).setEntityClass(itemClass).setId(relatedKey));
                      // 20/04/2013
                      Object relatedEntity = null;
                      if (context != null && context.useContextInRelationshipsResolver()) {
                        relatedEntity = dao.findWithContext(new FindContext<Serializable>(this.context).setEntityClass(itemClass).setId(relatedKey));
                      } else {
                        relatedEntity = dao.findById((Class<Serializable>)itemClass, relatedKey);
                      }
                      
                      relatedEntities.add(relatedEntity);
                      
                    }
                    relationshipField.set(entity, relatedEntities);
                  }
                }
                
              } else {
                Key relatedKey = (Key)keyField.get(entity);
                if (relatedKey != null) {
                  
                  Object relatedEntity = null;
                  if (resolvedEntities.containsKey(relatedKey)) {
                    logRecursiveLoop(relatedKey);
                    relatedEntity = resolvedEntities.get(relatedKey);
                    relationshipField.set(entity, relatedEntity);
                  } else {
                    relatedEntity = dao.findById((Class<Serializable>)relationshipField.getType(), relatedKey);
                    if (relatedEntity == null) {
                      logger.error("ERROR", new PersistenceException(String.format("ALERT: cannot resolve unowned related entity in %s, relationship type is %s, id is %s ", entity.getClass().getName(), relationshipField.getType(), relatedKey)));
                    } else {
                      resolvedEntities.put(relatedKey, relatedEntity);
                    }
                    relationshipField.set(entity, relatedEntity);
                    resolveUnownedRelationships(relatedEntity, resolvedEntities);
                  }
                  
                } else {
                  relationshipField.set(entity, null);
                }
              }
              
              break;
            }
          }
        } catch (Exception ex) {
          logger.error("error", ex);
        }
      }
      if (Collection.class.isAssignableFrom(field.getType())) {
        try {
          Field collectionField = field;
          collectionField.setAccessible(true);
          Collection collection = (Collection)collectionField.get(entity);
          if (collection != null) {
            for (Object item : collection) {
              if (resolvedEntities.containsKey(getKey(item))) {
                logRecursiveLoop(getKey(item));
              } else {
                resolvedEntities.put(getKey(item), item);
                resolveUnownedRelationshipsWithAnnotation(item, resolvedEntities);
              }
            }
          }
        } catch (Exception ex) {
          logger.error("error", ex);
        }
      }
    }
  }
  
  private Field[] getAllHierarchyDeclaredFieldsInSamePackage(Class<?> entityClass) {
    Package entityPackage = entityClass.getPackage();
    List<Field> allFields = new ArrayList<Field>();
    Class<?> currentClass = entityClass;
    while (currentClass != null) {
      if (currentClass.getPackage().equals(entityPackage)) {
        allFields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
      }
      currentClass = currentClass.getSuperclass();
    }
    return allFields.toArray(new Field[0]);
  }
  
  private Key getKey (Object entity) {
    if (entity instanceof HasKey) {
      HasKey hasKey = (HasKey)entity;
      return hasKey.getKey();
    }
    return null;
  }

  private void logRecursiveLoop (Key key) {
    logger.debug("break recursive loop " + KeyUtils.formatToString(key));
  }
  
}
