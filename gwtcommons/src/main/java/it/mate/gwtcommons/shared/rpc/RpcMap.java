package it.mate.gwtcommons.shared.rpc;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("serial")
public class RpcMap extends HashMap<String, Object> {

  // Escamotage per obbligare RPC a generare tutti i datatypes utilizzati nei tx
  public String dummyString;
  public Date dummyDate; 
  public Integer dummyInteger;
  public Double dummyDouble;
  public Long dummyLong; 
  public List<Void> dummyList;
  public ArrayList<Void> dummyArrayList;
  public Boolean dummyBoolean;
  public Float dummyFloat;
  public Short dummyShort;
  
  public <R> R getField(String name) {
    return getField(name, null);
  }
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public <R, V extends IsMappable> R getField(String name, ValueConstructor<V> valueConstructor) {
    
    R result = null;
    
    Object value = this.get(name);
    if (value == null) {
      return null;
    }
    
    if (value instanceof List) {
      
      List itemsValue = (List)value;
      String textResultValue = null;
      List<V> listResultValue = null;
      for (Object itemValue : itemsValue) {
        if (itemValue instanceof String) {
          if (textResultValue == null) {
            textResultValue = "";
          }
          textResultValue += itemValue;
          result = (R)textResultValue;
        } else if (itemValue instanceof RpcMap) {
          if (listResultValue == null) {
            listResultValue = new ArrayList<V>();
          }
          RpcMap itemMap = (RpcMap)itemValue;
          IsMappable newItem = valueConstructor.newInnstance();
          listResultValue.add((V)newItem.fromRpcMap(itemMap));
        } else {
          throw new IllegalArgumentException("items " + name + " are not instances of RpcMap nor String");
        }
      }
      if (textResultValue != null) {
        result = (R)textResultValue;
      } else if (listResultValue != null) {
        result = (R)listResultValue;
      }
      
    } else if (value instanceof RpcMap) {
      
      RpcMap valueMap = (RpcMap)value;
      IsMappable newItem = valueConstructor.newInnstance();
      result = (R)newItem.fromRpcMap(valueMap);
      
    } else {
      result = (R)value;
      
    }
    
    return result;
  }
  
  
  
  @SuppressWarnings("rawtypes")
  public void putField(String name, Object value) {
    if (value == null) {
      this.put(name, null);
    } else {
      if (value instanceof List) {
        setFieldList(name, (List)value);
      } else if (value instanceof IsMappable) {
        IsMappable mappableValue = (IsMappable)value;
        this.put(name, mappableValue.toRpcMap());
      } else if (value instanceof String) {
        String textValue = (String)value;
        if (textValue.length() > TEXT_CHUNK_SIZE) {
          this.put(name, longTextToList(textValue));
        } else {
          this.put(name, value);
        }
      } else {
        this.put(name, value);
      }
    }
  }
  
  @SuppressWarnings("rawtypes")
  private void setFieldList(String name, List items) {
    List<RpcMap> listMap = new ArrayList<RpcMap>();
    if (items != null) {
      for (Object itemObj : items) {
        if (itemObj instanceof IsMappable) {
          IsMappable item = (IsMappable)itemObj;
          listMap.add(item.toRpcMap());
        } else {
          throw new IllegalArgumentException("items " + name + " are not instances of IsMappable");
        }
      }
    }
    this.put(name, listMap);
  }
  
  private final static int TEXT_CHUNK_SIZE = 35536;
  
  private static List<String> longTextToList(String longText) {
    List<String> results = new ArrayList<String>();

    while (longText != null) {
      if (longText.length() > TEXT_CHUNK_SIZE) {
        results.add(longText.substring(0, TEXT_CHUNK_SIZE));
        longText = longText.substring(TEXT_CHUNK_SIZE);
      } else {
        results.add(longText);
        longText = null;
      }
    }
    return results;
  }
  
  /*
  public static String listToLongText(List<String> list) {
    String result = "";
    if (list != null) {
      for (String line : list) {
        result += line;
      }
    }
    return result;
  }
  */
  
}
