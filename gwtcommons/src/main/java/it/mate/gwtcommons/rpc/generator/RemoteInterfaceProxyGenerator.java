package it.mate.gwtcommons.rpc.generator;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.rpc.ProxyCreator;
import com.google.gwt.user.rebind.rpc.ServiceInterfaceProxyGenerator;

public class RemoteInterfaceProxyGenerator extends ServiceInterfaceProxyGenerator {

  static String INTERCEPTOR_PROPERTY_NAME = "it.mate.gwtcommons.rpc.interceptor.class";

  protected GeneratorContext generatorContext;

  public RemoteInterfaceProxyGenerator() {
    super();
  }

  /* VER GWT 2.4 
  @Override
  public com.google.gwt.dev.javac.rebind.RebindResult generateIncrementally(TreeLogger logger, com.google.gwt.core.ext.GeneratorContextExt ctx, String requestedClass)
      throws UnableToCompleteException {
    cacheContext(ctx);
    TreeLogger loggerBranched = logger.branch(TreeLogger.DEBUG, "[*" + this.getClass().getSimpleName()
        + "*] Incremental Generator: Creating custom proxy for " + requestedClass, null);
    return super.generateIncrementally(loggerBranched, ctx, requestedClass);
  } 
  */
  
  /* VER GWT 2.5 */
  @Override
  public com.google.gwt.core.ext.RebindResult generateIncrementally(TreeLogger logger, GeneratorContext ctx, String requestedClass)
      throws UnableToCompleteException {
    cacheContext(ctx);
    TreeLogger loggerBranched = logger.branch(TreeLogger.DEBUG, "[*" + this.getClass().getSimpleName()
        + "*] Incremental Generator: Creating custom proxy for " + requestedClass, null);
    return super.generateIncrementally(logger, ctx, requestedClass);
  }

  private void cacheContext(GeneratorContext ctx) {
    if (this.generatorContext == null) {
      this.generatorContext = ctx;
    }
  }

  @Override
  protected ProxyCreator createProxyCreator(JClassType remoteService) {
    return new RemoteInterfaceProxyCreator(remoteService, this.generatorContext);
  }

}
