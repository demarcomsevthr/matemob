package it.mate.gwtcommons.rpc.generator;

import it.mate.gwtcommons.rpc.client.RpcServiceInterceptor;

import java.util.List;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.rebind.rpc.ProxyCreator;

public class RemoteInterfaceProxyCreator extends ProxyCreator {

  private final GeneratorContext generatorContext;

  public RemoteInterfaceProxyCreator(JClassType type, GeneratorContext generatorContext) {
    super(type);
    this.generatorContext = generatorContext;
  }

  @Override
  protected Class<? extends RemoteServiceProxy> getProxySupertype() {
    Class<? extends RemoteServiceProxy> interceptorClass = RpcServiceInterceptor.class;
    Class<? extends RemoteServiceProxy> configuredInterceptor = getInterceptorClassOrNull();
    if (configuredInterceptor != null) {
      interceptorClass = configuredInterceptor;
    }
    return interceptorClass;
  }

  @SuppressWarnings("unchecked")
  private Class<? extends RemoteServiceProxy> getInterceptorClassOrNull() {
    Class<? extends RemoteServiceProxy> configuredInterceptorClass = null;
    try {
      PropertyOracle configurationProperties = generatorContext.getPropertyOracle();
      List<String> interceptorValue = configurationProperties.getConfigurationProperty(
          RemoteInterfaceProxyGenerator.INTERCEPTOR_PROPERTY_NAME).getValues();
      String forClass = interceptorValue.get(0);
      configuredInterceptorClass = (Class<? extends RemoteServiceProxy>) Class.forName(forClass);

    } catch (Exception e) {
      // nop
    }

    return configuredInterceptorClass;
  }

}
