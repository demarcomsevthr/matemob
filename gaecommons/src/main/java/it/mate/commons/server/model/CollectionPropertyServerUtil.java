package it.mate.commons.server.model;

import it.mate.gwtcommons.shared.model.CloneablePropertyMissingException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;

public class CollectionPropertyServerUtil <I extends Serializable, D extends I> {

  private List<Key> keys = new ArrayList<Key>();
  
  private List<D> items = new ArrayList<D>();
  
  private CollectionPropertyServerUtil () { }
  
  @SuppressWarnings("unchecked")
  public static <I extends Serializable, D extends I> CollectionPropertyServerUtil<I, D> clone (List<I> itemsToClone, Class<D> itemDescendantClass) {
    CollectionPropertyServerUtil<I, D> wrapper = new CollectionPropertyServerUtil<I, D>();
    if (itemsToClone != null) {
      for (I itemToClone : itemsToClone) {
        if (itemDescendantClass.isAssignableFrom(itemToClone.getClass())) {
          wrapper.items.add((D)itemToClone);
          if (itemToClone instanceof HasKey) {
            HasKey itemWithKey = (HasKey)itemToClone;
            wrapper.keys.add(itemWithKey.getKey());
          } else {
            throw new IllegalArgumentException("Item must implements HasKey");
          }
        } else {
          throw new CloneablePropertyMissingException(itemToClone);
        }
      }
    }
    return wrapper;
  }
  
  public List<D> getItems() {
    return items;
  }
  
  public List<Key> getKeys() {
    return keys;
  }
  
}
