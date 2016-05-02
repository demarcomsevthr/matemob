package it.mate.gwtcommons.rpc.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter.ResponseReader;
import com.google.gwt.user.client.rpc.impl.RpcStatsContext;
import com.google.gwt.user.client.rpc.impl.Serializer;

public abstract class RpcServiceInterceptor extends RemoteServiceProxy {
  
  protected class RequestContext {
    String serviceEntryPoint;
    String methodName;
    Request request;
    Response response;
    public RequestContext(String serviceEntryPoint, String methodName, Request request, Response response) {
      this.serviceEntryPoint = serviceEntryPoint;
      this.methodName = methodName;
      this.request = request;
      this.response = response;
    }
    public String getServiceEntryPoint() {
      return serviceEntryPoint;
    }
    public String getMethodName() {
      if (methodName != null && methodName.contains(".")) {
        methodName = methodName.substring(methodName.indexOf(".") + 1);
      }
      return methodName;
    }
    public Request getRequest() {
      return request;
    }
    public Response getResponse() {
      return response;
    }
  }
  
  protected abstract boolean onCreateRequest(RequestContext context);
  
  protected abstract boolean onResponseReceived(RequestContext context);
  
  protected abstract boolean onError(RequestContext context, Throwable exception);
  

  public RpcServiceInterceptor(String moduleBaseURL, String remoteServiceRelativePath, String serializationPolicyName, Serializer serializer) {
    super(moduleBaseURL, remoteServiceRelativePath, serializationPolicyName, serializer);
  }

  @Override
  protected <T> RequestCallback doCreateRequestCallback(ResponseReader responseReader, final String methodName, RpcStatsContext statsContext,
      AsyncCallback<T> callback) {

    final RequestCallback originalRequestCallback = super.doCreateRequestCallback(responseReader, methodName, statsContext, 
        wrapAsyncCallback(callback, methodName, null));
    RequestCallback wrappedRequestCallback = new RequestCallback() {
      @Override
      public void onResponseReceived(Request request, Response response) {
        if (RpcServiceInterceptor.this.onResponseReceived(new RequestContext(getServiceEntryPoint(), methodName, null, response))) {
          originalRequestCallback.onResponseReceived(request, response);
        }
      }
      @Override
      public void onError(Request request, Throwable exception) {
        if (RpcServiceInterceptor.this.onError(new RequestContext(getServiceEntryPoint(), methodName, request, null), exception)) {
          originalRequestCallback.onError(request, exception);
        }
      }
    };
    return wrappedRequestCallback;
  }

  @Override
  protected <T> Request doInvoke(ResponseReader responseReader, String methodName, RpcStatsContext statsContext, String requestData,
      AsyncCallback<T> callback) {
    
    boolean onCreateRequestContinue = onCreateRequest(new RequestContext(getServiceEntryPoint(), methodName, null, null));
    if (onCreateRequestContinue) {
      return super.doInvoke(responseReader, methodName, statsContext, requestData, callback);
    } else {
      callback.onFailure(new RpcCallVetoException());
      return null;
    }

  }
  
  private <T> AsyncCallback<T> wrapAsyncCallback (final AsyncCallback<T> originalAsyncCallback, final String methodName, final Request request) {
    return new AsyncCallback<T>() {
      public void onFailure(Throwable caught) {
        if (RpcServiceInterceptor.this.onError(new RequestContext(getServiceEntryPoint(), methodName, request, null), caught)) {
          originalAsyncCallback.onFailure(caught);
        }
      }
      public void onSuccess(T result) {
        originalAsyncCallback.onSuccess(result);
      }
    };
  }
  
}
