package it.mate.commons.server.model.utils;

import it.mate.commons.server.dao.Dao;
import it.mate.commons.server.dao.FindCallback;
import it.mate.commons.server.dao.FindContext;
import it.mate.commons.server.dao.JdoDao;
import it.mate.commons.server.model.HasKey;
import it.mate.commons.server.utils.DynamicInvocationUtils;

import java.io.Serializable;

@SuppressWarnings("unchecked")
public class OneToOneAdapterSupport <OWNER extends Serializable, ITEM extends Serializable> {

  private Dao dao;
  
  private String getItemMethod;
  
  private String setItemMethod;

  private String initializeItemMethod;

  /*
  private String getAttachedItemMethod;
  
  private String setAttachedItemMethod;
  */

  public OneToOneAdapterSupport(Dao dao, String getItemMethod, String setItemMethod) {
    this(dao, getItemMethod, setItemMethod, null);
  }

  public OneToOneAdapterSupport(Dao dao, String getItemMethod, String setItemMethod, String initializeItemMethod
      /* ,String getAttachedItemMethod, String setAttachedItemMethod */) {
    super();
    this.dao = dao;
    this.getItemMethod = getItemMethod;
    this.setItemMethod = setItemMethod;
    this.initializeItemMethod = initializeItemMethod;
    /*
    this.getAttachedItemMethod = getAttachedItemMethod;
    this.setAttachedItemMethod = setAttachedItemMethod;
    */
  }

  public void onBeforeCreate(OWNER detachedEntity) {
    try {
      ITEM detachedItem = (ITEM) DynamicInvocationUtils.invokeMethod(detachedEntity, getItemMethod);
      if (detachedItem != null) {
        ITEM attachedItem = null;
        if (detachedItem instanceof HasKey) {
          HasKey detachedItemWithKey = (HasKey)detachedItem;
          if (detachedItemWithKey.getKey() == null) {
            attachedItem = dao.create(detachedItem);
          }
        }
        DynamicInvocationUtils.invokeMethod(detachedEntity, setItemMethod, attachedItem);
      }
    } catch (Exception ex) {
      throw new RelationException(ex);
    }
  }
  
  
  public void onBeforeUpdate(OWNER detachedEntity) {
    try {
      ITEM detachedItem = (ITEM) DynamicInvocationUtils.invokeMethod(detachedEntity, getItemMethod);
      if (detachedItem == null) {
        DynamicInvocationUtils.invokeMethod(detachedEntity, setItemMethod, findAttachedItemByEntityId(detachedEntity));
      }
    } catch (Exception ex) {
      throw new RelationException(ex);
    }
  }
  
  public void onBeforeDelete(OWNER detachedEntity) {
    try {
      ITEM attachedItem = findAttachedItemByEntityId(detachedEntity);
      if (JdoDao.isSuppressExceptionThrowOnInternalFind() && attachedItem == null) {
        return;
      }
      dao.delete(attachedItem);
    } catch (Exception ex) {
      throw new RelationException(ex);
    }
  }

  /*
  public OWNER onUpdateItem(OWNER attachedEntity, ITEM detachedItem) {
    try {
      ITEM attachedItem = (ITEM)DynamicInvocationUtils.invokeMethod(attachedEntity, getAttachedItemMethod, detachedItem);
      if (attachedItem == null) {
        attachedItem = dao.create(detachedItem);
        attachedItem = (ITEM)DynamicInvocationUtils.invokeMethod(attachedEntity, setAttachedItemMethod, attachedItem);
        attachedEntity = dao.update(attachedEntity);
      } else {
        attachedItem = (ITEM)DynamicInvocationUtils.invokeMethod(attachedEntity, setAttachedItemMethod, detachedItem);
        attachedItem = dao.update(attachedItem);
      }
    } catch (Exception ex) {
      throw new RelationException(ex);
    }
    return attachedEntity;
  }
  */
  
  private ITEM findAttachedItemByEntityId (OWNER entity) throws Exception {
    Class<OWNER> ownerDsClass = (Class<OWNER>)entity.getClass();
    if (entity instanceof HasKey) {
      HasKey detachedEntityWithKey = (HasKey)entity;
      
      FindContext<OWNER> context = new FindContext<OWNER>(ownerDsClass);
      context.setId(detachedEntityWithKey.getKey());
      context.setCacheDisabled(true);
      
      if (initializeItemMethod != null) {
        context.setCallback(new FindCallback<OWNER>() {
          public void processResultsInTransaction(OWNER attachedOwner) {
            try {
              DynamicInvocationUtils.invokeMethod(attachedOwner, initializeItemMethod);
            } catch (Exception ex) {
              throw new RelationException(ex);
            }
          }
        });
      }
      
      OWNER attachedEntity = dao.findWithContext(context);
      if (JdoDao.isSuppressExceptionThrowOnInternalFind() && attachedEntity == null) {
        return null;
      }
      ITEM attachedItem = (ITEM) DynamicInvocationUtils.invokeMethod(attachedEntity, getItemMethod);
      return attachedItem;
    } else {
      throw new IllegalArgumentException("entity must implement HasKey interface");
    }
  }
  

}
