package it.mate.gwtcommons.client.utils;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public abstract class RPCWrapper <T> {
  
  protected Object[] args;
  
  protected AsyncCallback<T> defaultCallback;
  
  public RPCWrapper(Object ...args) {
    this.args = args;
  }

  protected abstract void serviceCall();
  
  protected abstract void onSuccess(T result);
  
  private boolean disableAlert = false;
  
  private boolean showWaitPanel = false;
  
  private PopupPanel waitPanel = null;
  
  public void onFailure(Throwable caught) {
    if (!disableAlert)
      Window.alert(caught.getMessage());
  }
  
  public RPCWrapper<T> waitPanel() {
    return waitPanel(null);
  }
  
  public RPCWrapper<T> waitPanel(PopupPanel waitPanel) {
    showWaitPanel = true;
    this.waitPanel = waitPanel;
    return this;
  }
  
  public RPCWrapper<T> disableAlert() {
    disableAlert = true;
    return this;
  }
  
  public void doCall() {
    defaultCallback = new AsyncCallback<T>() {
      public void onFailure(Throwable caught) {
        if (showWaitPanel)
          GwtUtils.hideWait();
        RPCWrapper.this.onFailure(caught);
      }
      public void onSuccess(T result) {
        if (showWaitPanel)
          GwtUtils.hideWait();
        RPCWrapper.this.onSuccess(result);
      }
    };

    if (showWaitPanel) {
      if (waitPanel == null) {
        waitPanel = new PopupPanel();
        waitPanel.setGlassEnabled(false);
        waitPanel.setAnimationEnabled(true);
        Label lb = new Label("Attendere prego...");
        GwtUtils.setStyleAttribute(lb, "font", "bold 16px Verdana");
        GwtUtils.setStyleAttribute(lb, "color", "red");
        waitPanel.setWidget(lb);
        GwtUtils.showWait(waitPanel);
      }
    }
    
    serviceCall();
  }
  
  ////////////////////////////////////////////////////////////////////////
  //  TESTING
  
  public interface TestServiceAsync {
    void findById(String id, AsyncCallback<String> callback);
  }
  
  TestServiceAsync testService = null;
  
  public void test(String id, final Delegate<String> delegate) {
    
    testService.findById(id, new AsyncCallback<String>() {
      public void onFailure(Throwable caught) {
        Window.alert("alert");
      }
      public void onSuccess(String result) {
        delegate.execute(result);
      }
    });

    new RPCWrapper<String>(id) {
      protected void serviceCall() {
        // Example: testService.findById((String)args[0], callback);
        testService.findById((String)args[0], defaultCallback);
      }
      protected void onSuccess(String result) {
        // Example: delegate.execute(result);
        delegate.execute(result);
      }
    }.doCall();
    
    testService.findById(id, new AsyncCallback<String>() {
      public void onFailure(Throwable caught) {
        Window.alert("alert");
      }
      public void onSuccess(String result) {
        delegate.execute(result);
      }
    });
    
    new RPCWrapper<String>(id) {
      protected void serviceCall() {
        testService.findById((String)args[0], defaultCallback);
      }
      protected void onSuccess(String result) {
        delegate.execute(result);
      }
    }.waitPanel().doCall();
    
    new RPCWrapper<String>(id) {
      protected void serviceCall() {
        testService.findById((String)args[0], defaultCallback);
      }
      protected void onSuccess(String result) {
        delegate.execute(result);
      }
    }.doCall();
    
    final AsyncCallback<String> myCallback = new AsyncCallback<String>() {
      public void onSuccess(String result) {
      }
      public void onFailure(Throwable caught) {
      }
    };
    
    new RPCWrapper<String>(id) {
      protected void serviceCall() {
        testService.findById((String)args[0], defaultCallback);
      }
      protected void onSuccess(String result) {
        myCallback.onSuccess(result);
      }
    }.doCall();
    
    
  }
  

}
