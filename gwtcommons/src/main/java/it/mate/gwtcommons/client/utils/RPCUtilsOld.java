package it.mate.gwtcommons.client.utils;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class RPCUtilsOld {
  
  public static void setInterceptors(ServiceDefTarget serviceDefTarget, final Interceptors interceptors) {
    serviceDefTarget.setRpcRequestBuilder(new RpcRequestBuilder() {
      private RequestCallbackInterceptors requestCallbackInterceptors;
      protected RequestBuilder doCreate(String serviceEntryPoint) {
        RequestBuilder rb = super.doCreate(serviceEntryPoint);
        interceptors.onCreateRequest(rb);
        return rb;
      }
      @Override
      protected void doFinish(RequestBuilder rb) {
        interceptors.onFinishRequest(rb);
        if (requestCallbackInterceptors != null)
          requestCallbackInterceptors.setCurrentRequestBuilder(rb);
        super.doFinish(rb);
      }
      protected void doSetCallback(RequestBuilder rb, RequestCallback callback) {
        this.requestCallbackInterceptors = new RequestCallbackInterceptors(callback, interceptors);
        super.doSetCallback(rb, requestCallbackInterceptors);
      }
    });
  }

  public static interface Interceptors {
    void onCreateRequest(RequestBuilder rb);
    void onFinishRequest(RequestBuilder rb);
    void onResponseReceived(Response response);
    boolean onError(RequestBuilder rb, Throwable exception);
  }
  
  private static class RequestCallbackInterceptors implements RequestCallback {
    private RequestCallback wrappedRequestCallback;
    private Interceptors interceptors;
    private RequestBuilder currentRequestBuilder;
    public RequestCallbackInterceptors(RequestCallback requestCallback, Interceptors interceptors) {
      this.wrappedRequestCallback = requestCallback;
      /*
      if (wrappedRequestCallback.getClass().getName().contains("RequestCallbackAdapter")) {
        GwtUtils.log(getClass(), "RequestCallbackInterceptors", "ok");
        RequestCallbackAdapter requestCallbackAdapter = (RequestCallbackAdapter)wrappedRequestCallback;
      }
      */
      this.interceptors = interceptors;
    }
    public void setCurrentRequestBuilder(RequestBuilder currentRequestBuilder) {
      this.currentRequestBuilder = currentRequestBuilder;
    }
    public void onResponseReceived(Request request, Response response) {
      interceptors.onResponseReceived(response);
      wrappedRequestCallback.onResponseReceived(request, response);
    }
    public void onError(Request request, Throwable exception) {
      if (interceptors.onError(currentRequestBuilder, exception)) {
        wrappedRequestCallback.onError(request, exception);
      }
    }
  }
  
  private static boolean rpcRequestOutstanding = false;
  
  public static void setSynchronizedInterceptor(ServiceDefTarget serviceDefTarget) {
    
    serviceDefTarget.setRpcRequestBuilder(new RpcRequestBuilder() {

      @Override
      protected RequestBuilder doCreate(String serviceEntryPoint) {
        if (rpcRequestOutstanding) {
          GwtUtils.log(getClass(), "serviceDefTarget.RpcRequestBuilder.doCreate", "rpcRequestOutstanding = TRUE >> SKIP CALL!!!!!!!!!!!!!!!");
          return null;
        }
        GwtUtils.log(getClass(), "serviceDefTarget.RpcRequestBuilder.doCreate", "serviceEntryPoint = " + serviceEntryPoint);
        return super.doCreate(serviceEntryPoint);
      }
      
      @Override
      protected void doFinish(RequestBuilder rb) {
        if (rpcRequestOutstanding) {
          GwtUtils.log(getClass(), "serviceDefTarget.RpcRequestBuilder.doFinish", "rpcRequestOutstanding = TRUE >> SKIP CALL!!!!!!!!!!!!!!!");
          return;
        }
        rpcRequestOutstanding = true;
        GwtUtils.log(getClass(), "serviceDefTarget.RpcRequestBuilder.doFinish", "rb = " + rb);
        super.doFinish(rb);
      }

      @Override
      protected void doSetCallback(RequestBuilder rb, RequestCallback callback) {
        super.doSetCallback(rb, new SynchronizedRequestCallbackWrapper(callback));
      }
      
    });
    
  }
  
  
  private static class SynchronizedRequestCallbackWrapper implements RequestCallback {
    private RequestCallback requestCallback;
    public SynchronizedRequestCallbackWrapper(RequestCallback requestCallback) {
      this.requestCallback = requestCallback;
    }
    @Override
    public void onResponseReceived(Request request, Response response) {
      rpcRequestOutstanding = false;
      GwtUtils.log(getClass(), "serviceDefTarget.RequestCallbackWrapper.onResponseReceived", "");
      requestCallback.onResponseReceived(request, response);
    }
    @Override
    public void onError(Request request, Throwable exception) {
      rpcRequestOutstanding = false;
      GwtUtils.log(getClass(), "serviceDefTarget.RequestCallbackWrapper.onError", "");
      requestCallback.onError(request, exception);
    }
  }
  

}
