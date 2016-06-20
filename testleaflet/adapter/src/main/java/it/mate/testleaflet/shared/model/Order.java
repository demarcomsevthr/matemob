package it.mate.testleaflet.shared.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface Order extends Serializable {
  
  public final static int STATE_IN_CART = 0;

  public final static int STATE_SENT = 1;

  public final static int STATE_RECEIVED = 2;

  public final static int STATE_PREVIEW_IN_PROGRESS = 3;

  public final static int STATE_PREVIEW_AVAILABLE = 4;

  public final static int STATE_PREVIEW_PAYED = 5;

  public final static int STATE_WORK_IN_PROGRESS = 6;

  public final static int STATE_CLOSE = 7;

  public final static int STATE_SHIPED = 8;
  
  public final static int STATE_DEFAULT = STATE_IN_CART;
  

  public Integer getId();

  public void setId(Integer id);

  public String getCodice();

  public void setCodice(String codice);

  public void setItems(List<OrderItem> items);

  public List<OrderItem> getItems();

  public Integer getState();

  public void setState(Integer state);

  public void setRemoteId(String remoteId);

  public String getRemoteId();

  public void setAccount(Account account);

  public Account getAccount();

  public void setLastUpdate(Date lastUpdate);

  public Date getLastUpdate();

  
  /** UPDATE STATES
   * 
    null >> stato iniziale
    
    N >> server.adapter.saveOrder
    
    U >> server.adapter.checkForUpdates (legge ordini|messaggi in stato N, li aggiorna a stato U)
    
    V >> (solo sul device) l'utente ha visualizzato l'aggiornamento
    
  */
  
  public void setUpdateState(String updateState);

  public String getUpdateState();

  public void setCreated(Date created);

  public Date getCreated();
  
}
