package it.mate.testleaflet.client;

import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.phgcommons.client.utils.PhgUtils;
import it.mate.testleaflet.client.constants.AppProperties;
import it.mate.testleaflet.client.factories.AppClientFactory;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;

public class MainEntryPoint implements EntryPoint {

  @Override
  public void onModuleLoad() {
    
    String traceActive = PhgUtils.getLocalStorageItem(AppClientFactory.KEY_TRACE_ACTIVE);
    if ("true".equals(traceActive)) {
      PhgUtils.log("***********    TRACE ENABLED   *************");
      PhgUtils.startTrace();
    }
    
    PhgUtils.log("***********    STARTING NEW APP INSTANCE   ***********");
    GwtUtils.logEnvironment(getClass(), "onModuleLoad");
    PhgUtils.logEnvironment();
    
    PhgUtils.log("AppProperties.extendedVersion = "+AppProperties.IMPL.extendedVersion());
    PhgUtils.log("AppConstants.versionNumber = "+AppProperties.IMPL.versionNumber());

    PhgUtils.setDefaultExceptionHandler(Logger.getLogger(getClass().getName()));
    startApp();
    
  }

  private void startApp() {
    AppClientFactory.IMPL.initModule(null);
  }
  
}
