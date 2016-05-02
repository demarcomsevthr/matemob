package it.mate.gwtcommons.client.history;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public class MappedHistoryImpl implements HasValueChangeHandlers<String>, HasHandlers {

  private String token;
  
  public String getToken() {
    return token;
  }
  
  protected void setToken(String token) {
    this.token = token;
  }
  
  private HandlerManager handlers = new HandlerManager(null);

  public HandlerRegistration addValueChangeHandler(
      ValueChangeHandler<String> handler) {
    return handlers.addHandler(ValueChangeEvent.getType(), handler);
  }
  
  public void fireEvent(GwtEvent<?> event) {
    handlers.fireEvent(event);
  }

  public void fireHistoryChangedImpl(String newToken) {
    ValueChangeEvent.fire(this, newToken);
  }
  
  public HandlerManager getHandlers() {
    return handlers;
  }
  
  public boolean init() {
    return true;
  }
  
  public void newItem(String historyToken, boolean issueEvent) {
    historyToken = (historyToken == null) ? "" : historyToken;
    if (!historyToken.equals(getToken())) {
      setToken(historyToken);
      if (issueEvent) {
        fireHistoryChangedImpl(historyToken);
      }
    }
  }
  
}
