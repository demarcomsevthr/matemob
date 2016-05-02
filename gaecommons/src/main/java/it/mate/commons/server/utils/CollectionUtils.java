package it.mate.commons.server.utils;

import java.util.Collection;
import java.util.Iterator;

public class CollectionUtils {
  
  @SuppressWarnings("unchecked")
  public static void traverseCollection (Collection collection) {
    if (collection != null) {
      for (Iterator it = collection.iterator(); it.hasNext();) {
        it.next();
      }
    }
    
  }

}
