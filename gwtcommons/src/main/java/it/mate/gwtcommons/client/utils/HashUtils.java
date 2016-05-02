package it.mate.gwtcommons.client.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

public class HashUtils {

  private static String previousHash = "";
  
  private static List<Delegate<String>> delegates = new ArrayList<Delegate<String>>();
  
  private static Timer timer;
  
  public static void addHashChangeHandler(final Delegate<String> delegate) {
    if (timer == null) {
      timer = GwtUtils.createTimer(50, new Delegate<Void>() {
        public void execute(Void element) {
          String currentHash = Window.Location.getHash();
          if (currentHash != null && currentHash.startsWith("#")) {
            currentHash = currentHash.substring(1);
          }
          if (!previousHash.equals(currentHash)) {
            previousHash = currentHash;
            fireHashChangeHandlers(currentHash);
          }
        }
      });
    }
    delegates.add(delegate);
  }
  
  private static void fireHashChangeHandlers(String newHash) {
    for (Delegate<String> delegate : delegates) {
      delegate.execute(newHash);
    }
  }
  
  public static boolean hasTokens(String hash) {
    return (hash.indexOf(',') > -1);
  }
  
  public static String getPageCodeFromHash(String hash) {
    if (hasTokens(hash)) {
      String[] tokens = hash.split(",");
      for (String token : tokens) {
        if (token.startsWith("page=")) {
          return token.substring(5);
        }
      }
    }
    return hash;
  }
  
  public static String getArticleCodeFromHash(String hash) {
    if (hasTokens(hash)) {
      String[] tokens = hash.split(",");
      for (String token : tokens) {
        if (token.startsWith("article=")) {
          return token.substring(5);
        }
      }
    }
    return hash;
  }
  
}
