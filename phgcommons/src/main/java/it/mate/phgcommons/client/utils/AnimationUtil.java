package it.mate.phgcommons.client.utils;

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.animation.client.AnimationScheduler.AnimationCallback;
import com.google.gwt.dom.client.Element;

public class AnimationUtil {
  
  public static interface Handler {
    boolean handleAnimation(long startTime, long currentTime);
  }
  
  public static void doAnimation(final Handler handler) {
    doAnimation(handler, null);
  }
  
  public static void doAnimation(final Handler handler, final Element element) {
    final long startTime = System.currentTimeMillis();
    AnimationCallback animationCallback = new AnimationCallback() {
      public void execute(double currentTime) {
        if (handler.handleAnimation(startTime, (long)currentTime)) {
          AnimationScheduler.get().requestAnimationFrame(this, element);
        }
      }
    };
    animationCallback.execute(startTime);
  }

}
