package it.mate.testons.shared.model.utils;

import java.util.List;

public class ModelUtils {

  public static boolean equals(Object v1, Object v2) {
    if (v1 == null && v2 == null) {
      return true;
    } else if (v1 == null && v2 != null) {
      return false;
    } else if (v1 != null && v2 == null) {
      return false;
    } else {
      if (v1 instanceof List && v2 instanceof List) {
        List<?> l1 = (List)v1;
        List<?> l2 = (List)v2;
        if (l1.size() != l2.size()) {
          return false;
        }
        for (int it = 0; it < l1.size(); it++) {
          if (!l1.get(it).equals(l2.get(it))) {
            return false;
          }
        }
        return true;
      }
      return v1.equals(v2);
    }
  }
  
}
