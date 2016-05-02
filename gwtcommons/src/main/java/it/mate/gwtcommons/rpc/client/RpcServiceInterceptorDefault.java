package it.mate.gwtcommons.rpc.client;

import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.gwtcommons.shared.utils.PropertiesHolder;

import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.rpc.impl.Serializer;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;

public class RpcServiceInterceptorDefault extends RpcServiceInterceptor {

  private PopupPanel waitPanel = null;
  
  public RpcServiceInterceptorDefault(String moduleBaseURL, String remoteServiceRelativePath, String serializationPolicyName,
      Serializer serializer) {
    super(moduleBaseURL, remoteServiceRelativePath, serializationPolicyName, serializer);
  }

  @Override
  protected boolean onCreateRequest(RequestContext context) {
    GwtUtils.log(getClass(), "onCreateRequest", "service = "+getServiceName()+", methodName = " + context.getMethodName()+" serviceEntryPoint = "+context.getServiceEntryPoint());
    showWaitPanelIfRequired(context);
    return true;
  }

  @Override
  protected boolean onResponseReceived(RequestContext context) {
    GwtUtils.log(getClass(), "onResponseReceived", "service = "+getServiceName()+", methodName = " + context.getMethodName()+" response.statusCode = "+context.getResponse().getStatusCode());
    hideWaitPanelIfRequired(context);
    return true;
  }

  @Override
  protected boolean onError(RequestContext context, Throwable exception) {
    GwtUtils.log(getClass(), "onError", "service = "+getServiceName()+", methodName = " + context.getMethodName()+" ex.class = "+exception.getClass().getName() + " ex.message = " + exception.getMessage());
    hideWaitPanelIfRequired(context);
    if (getServicePropertyBoolean(context.getMethodName(), "vetoOnFailureCallback", false)) {
      GwtUtils.log(getClass(), "onError", "vetoOnFailureCallback is ON");
      return false;
    }
    if (exception.getMessage() != null && exception.getMessage().equals("0")) {
      return false;
    }
    return true;
  }
  
  private void showWaitPanelIfRequired (RequestContext context) {
    if (getServicePropertyBoolean(context.getMethodName(), "showWaitPanel", false)) {
      GwtUtils.log(getClass(), "showWaitPanelIfRequired", "showWaitPanel = true");
      if (waitPanel == null) {
        waitPanel = new PopupPanel();
        GwtUtils.setStyleAttribute(waitPanel, "border", "none");
        GwtUtils.setStyleAttribute(waitPanel, "background", "transparent");
        waitPanel.setGlassEnabled(false);
        waitPanel.setAnimationEnabled(true);
        Image waitingImg = new Image(UriUtils.fromTrustedString("/images/commons/transp-loading.gif"));
        waitPanel.setWidget(waitingImg);
      }
      GwtUtils.showWait(waitPanel);
    }
  }
  
  private void hideWaitPanelIfRequired (RequestContext context) {
    if (getServicePropertyBoolean(context.getMethodName(), "showWaitPanel", false)) {
      GwtUtils.hideWait();
    }
  }
  
  private String getServiceName() {
    String serviceClassname = this.getClass().getName();
    if (serviceClassname.endsWith("_Proxy"))
      serviceClassname = serviceClassname.substring(0, serviceClassname.length() - 6);
    return serviceClassname;
  }
  
  private boolean getServicePropertyBoolean(String methodName, String propName, boolean defaultValue) {
    Boolean result = null;
    if (methodName != null) {
      result = PropertiesHolder.getBooleanAllowNull(getServiceName() + "." + methodName + "." + propName);
    }
    if (result == null) {
      result = PropertiesHolder.getBooleanAllowNull(getServiceName() + "." + propName);
    }
    if (result == null) {
      result = defaultValue;
    }
    return result;
  }

}
