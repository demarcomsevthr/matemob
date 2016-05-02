package it.mate.gwtcommons.client.utils;

import it.mate.gwtcommons.shared.model.CloneablePropertyMissingException;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({ "unchecked", "serial" })
public class CollectionPropertyClientUtil <I extends Serializable, D extends I> extends AbstractList<I> implements Serializable {

  private transient List<D> items = new ArrayList<D>();
  
  private transient Class<D> descendantClass;
  
  public CollectionPropertyClientUtil() {  }
  
  public CollectionPropertyClientUtil(List<D> items, Class<D> descendantClass) {
    if (items != null)
      this.items = items;
    this.descendantClass = descendantClass;
  }
  
  public static <I extends Serializable, D extends I> List<I> cast (List<D> itemsToWrap, Class<D> descendantClass) {
    return new CollectionPropertyClientUtil<I, D>(itemsToWrap, descendantClass);
  }
  
  public static <I extends Serializable, D extends I> List<D> clone (List<I> itemsToClone, Class<D> descendantClass) {
    CollectionPropertyClientUtil<I, D> wrapper = new CollectionPropertyClientUtil<I, D>(new ArrayList<D>(), descendantClass);
    if (itemsToClone != null) {
      for (I itemToClone : itemsToClone) {
        if (isInstanceOf(itemToClone, descendantClass)) {
          wrapper.items.add((D)itemToClone);
        } else {
          throw new CloneablePropertyMissingException(itemToClone);
        }
      }
    } else {
      wrapper.items = null;
    }
    return wrapper.items;
  }
  
  public List<D> getItems() {
    return items;
  }

  public int size() {
    return items.size();
  }
  public I get(int index) {
    return items.get(index);
  }
  public I set(int index, I element) {
    if (element == null) {
      throw new IllegalArgumentException("null");
    }
    if (isInstanceOf(element, descendantClass)) {
      return items.set(index, (D)element);
    } else {
      throw new IllegalArgumentException(element.getClass().getName());
    }
  }
  public boolean add(I element) {
    if (element == null) {
      throw new IllegalArgumentException("null");
    }
    if (isInstanceOf(element, descendantClass)) {
      return items.add((D)element);
    } else {
      throw new IllegalArgumentException(element.getClass().getName());
    }
  }
  public boolean remove(Object o) {
    return items.remove(o);
  }
  public I remove(int index) {
    return items.remove(index);
  }
  
  
  @SuppressWarnings("rawtypes")
  private static boolean isInstanceOf(Object inst, Class<?> type) {
    if (inst.getClass().equals(type)) {
      return true;
    }
    Class supercl = inst.getClass().getSuperclass();
    while (supercl != null) {
      if (supercl.equals(type)) {
        return true;
      }
      supercl = supercl.getSuperclass();
      if (Object.class.equals(supercl))
        supercl = null;
    }
    return false;
  }
  
  
}
