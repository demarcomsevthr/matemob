package it.mate.commons.server.model.utils;

import it.mate.commons.server.dao.Dao;
import it.mate.commons.server.dao.FindCallback;
import it.mate.commons.server.dao.FindContext;
import it.mate.commons.server.dao.JdoDao;
import it.mate.commons.server.model.HasKey;
import it.mate.commons.server.utils.DynamicInvocationUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class OneToManyAdapterSupport <OWNER extends Serializable, ITEM extends Serializable> {

  private Dao dao;
  
  private String getItemsMethodName;
  
  private String setItemsMethodName;

  private String initializeItemsMethodName;

  private String getAttachedItemMethodName;
  
  private String setAttachedItemMethodName;

  public OneToManyAdapterSupport(Dao dao, String getItemsMethodName, String setItemsMethodName, String initializeItemsMethodName,
      String getAttachedItemMethodName, String setAttachedItemMethodName) {
    super();
    this.dao = dao;
    this.getItemsMethodName = getItemsMethodName;
    this.setItemsMethodName = setItemsMethodName;
    this.initializeItemsMethodName = initializeItemsMethodName;
    this.getAttachedItemMethodName = getAttachedItemMethodName;
    this.setAttachedItemMethodName = setAttachedItemMethodName;
  }

  public void onBeforeCreate(OWNER detachedEntity) {
    try {
      List<ITEM> detachedItems = (List<ITEM>) DynamicInvocationUtils.invokeMethod(detachedEntity, getItemsMethodName);
      if (detachedItems != null) {
        List<ITEM> attachedItems = new ArrayList<ITEM>();
        for (ITEM item : detachedItems) {
          if (item instanceof HasKey) {
            HasKey itemWithKey = (HasKey)item;
            if (itemWithKey.getKey() == null) {
              item = dao.create(item);
            }
          }
          attachedItems.add(item);
        }
        DynamicInvocationUtils.invokeMethod(detachedEntity, setItemsMethodName, attachedItems);
      }
    } catch (Exception ex) {
      throw new RelationException(ex);
    }
  }
  
  
  public void onBeforeUpdate(OWNER detachedEntity) {
    try {
      List<ITEM> detachedItems = (List<ITEM>) DynamicInvocationUtils.invokeMethod(detachedEntity, getItemsMethodName);
      if (detachedItems == null || detachedItems.size() == 0) {
        DynamicInvocationUtils.invokeMethod(detachedEntity, setItemsMethodName, findAttachedItemsByEntityId(detachedEntity));
      }
    } catch (Exception ex) {
      throw new RelationException(ex);
    }
  }
  
  public void onBeforeDelete(OWNER detachedEntity) {
    try {
      List<ITEM> attachedItems = findAttachedItemsByEntityId(detachedEntity);
      if (JdoDao.isSuppressExceptionThrowOnInternalFind() && attachedItems == null) {
        return;
      }
      for (ITEM attachedItem : attachedItems) {
        dao.delete(attachedItem);
      }
    } catch (Exception ex) {
      throw new RelationException(ex);
    }
  }
  
  
  public OWNER onUpdateItem(OWNER attachedEntity, ITEM detachedItem) {
    try {
      ITEM attachedItem = (ITEM)DynamicInvocationUtils.invokeMethod(attachedEntity, getAttachedItemMethodName, detachedItem);
      if (attachedItem == null) {
        attachedItem = dao.create(detachedItem);
        attachedItem = (ITEM)DynamicInvocationUtils.invokeMethod(attachedEntity, setAttachedItemMethodName, attachedItem);
        attachedEntity = dao.update(attachedEntity);
      } else {
        attachedItem = (ITEM)DynamicInvocationUtils.invokeMethod(attachedEntity, setAttachedItemMethodName, detachedItem);
        attachedItem = dao.update(attachedItem);
      }
    } catch (Exception ex) {
      throw new RelationException(ex);
    }
    return attachedEntity;
  }
  
  
  private List<ITEM> findAttachedItemsByEntityId (OWNER entity) throws Exception {
    Class<OWNER> ownerDsClass = (Class<OWNER>)entity.getClass();
    if (entity instanceof HasKey) {
      HasKey detachedEntityWithKey = (HasKey)entity;
      
      FindContext<OWNER> context = new FindContext<OWNER>(ownerDsClass);
      context.setId(detachedEntityWithKey.getKey());
      context.setCacheDisabled(true);
      
      context.setCallback(new FindCallback<OWNER>() {
        public void processResultsInTransaction(OWNER attachedOwner) {
          try {
            DynamicInvocationUtils.invokeMethod(attachedOwner, initializeItemsMethodName);
          } catch (Exception ex) {
            throw new RelationException(ex);
          }
        }
      });
      
      OWNER attachedEntity = dao.findWithContext(context);
      if (JdoDao.isSuppressExceptionThrowOnInternalFind() && attachedEntity == null) {
        return null;
      }
      List<ITEM> attachedItemList = (List<ITEM>) DynamicInvocationUtils.invokeMethod(attachedEntity, getItemsMethodName);
      return attachedItemList;
    } else {
      throw new IllegalArgumentException("entity must implement HasKey interface");
    }
  }
  

}
