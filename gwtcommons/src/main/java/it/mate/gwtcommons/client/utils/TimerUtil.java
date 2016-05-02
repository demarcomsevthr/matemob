package it.mate.gwtcommons.client.utils;

import com.google.gwt.user.client.Timer;


public class TimerUtil {
  
  private Timer timer;
  
  public TimerUtil (int periodMillis, final Delegate delegate) {
    this.timer = new Timer() {
      public void run() {
        //GwtUtils.log("-- run timer TimerUtil");
        if (delegate.canCancel()) {
          timer.cancel();
        }
      }
    };
    timer.scheduleRepeating(periodMillis);
  }
  
  public interface Delegate {
    boolean canCancel();
  }

}
