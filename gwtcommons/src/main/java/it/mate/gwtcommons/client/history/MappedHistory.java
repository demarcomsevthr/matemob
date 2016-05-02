package it.mate.gwtcommons.client.history;

import it.mate.gwtcommons.client.utils.GwtUtils;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;


public class MappedHistory {

  /*
  private static MemHistoryImpl impl;
  
  static {
    impl = GWT.create(HistoryImpl.class);
    if (!impl.init()) {
      // Set impl to null as a flag to no-op future calls.
      impl = null;

      // Tell the user.
      GWT.log("Unable to initialize the mem history subsystem");
    }
  }
  */
  private static Map<String, MappedHistoryImpl> implMap;
  
  static {
    implMap = new HashMap<String, MappedHistoryImpl>();
  }
  
  private static MappedHistoryImpl getImpl (String name) {
    MappedHistoryImpl impl = implMap.get(name);
    if (impl == null) {
      impl = new MappedHistoryImpl();
      
      GwtUtils.log(MappedHistory.class, "getImpl", "caching new MappedHistoryImpl with name " + name);
      
//  05/04/2012 
//    !!!!        ATTENZIONE       !!!!
//    FORSE E' UN BACO: null !!!
//
//    implMap.put(null, impl);
      implMap.put(name, impl);
//
      
    }
    return impl;
  }
  
  public static HandlerRegistration addValueChangeHandler(String name, ValueChangeHandler<String> handler) {
    return getImpl(name).addValueChangeHandler(handler);
  }
  
  public static void back() {
    
  }
  
  public static void fireCurrentHistoryState(String name) {
    String token = getToken(name);
    getImpl(name).fireHistoryChangedImpl(token);
  }
  
  public static void forward() {
    
  }
  
  public static String getToken(String name) {
    return getImpl(name).getToken();
  }
  
  public static void newItem(String historyToken) {
    for (String name : implMap.keySet()) {
      newItem(name, historyToken, true);
    }
  }
  
  public static void newItem(String name, String historyToken) {
    newItem(name, historyToken, true);
  }
  
  public static void newItem(String name, String historyToken, boolean issueEvent) {
    getImpl(name).newItem(historyToken, issueEvent);
  }
  
  
}
