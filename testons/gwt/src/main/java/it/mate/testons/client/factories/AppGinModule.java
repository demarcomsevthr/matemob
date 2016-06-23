package it.mate.testons.client.factories;

import it.mate.phgcommons.client.utils.OsDetectionUtils;
import it.mate.phgcommons.client.utils.PhgUtils;
import it.mate.testons.client.activities.mapper.MainActivityMapper;
import it.mate.testons.shared.service.RemoteFacade;
import it.mate.testons.shared.service.RemoteFacadeAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.inject.Provides;
import com.googlecode.gwtphonegap.client.util.PhonegapUtil;

public class AppGinModule extends AbstractGinModule {

  private final static String NAT_PROP_FACADE_MODULE_URL = "remoteFacadeModuleUrl";
  private final static String DEFAULT_FACADE_MODULE_URL = "https://testonssrv.appspot.com/main/";
  private final static String NAT_PROP_FACADE_RELATIVE_PATH = "remoteFacadeRelativePath";
  private final static String DEFAULT_FACADE_RELATIVE_PATH = ".remoteFacade";
  
  @Override
  protected void configure() {
//  bind(MainActivity.class).to(MainActivity.class).in(Singleton.class);
  }

  /*
  @Provides
  public MainDao getMainDao() {
    if (singletonDao == null) {
      singletonDao = new MainDao();
    }
    return singletonDao;
  }
  */
  
  @Provides
  MainActivityMapper getMainActivityMapper() {
    return new MainActivityMapper(AppClientFactory.IMPL);
  }
  
  @Provides
  public RemoteFacadeAsync getRemoteFacade() {
    PhgUtils.log("preparing remote facade...");
    RemoteFacadeAsync facade = GWT.create(RemoteFacade.class);
    ServiceDefTarget service = (ServiceDefTarget)facade;
    if (OsDetectionUtils.isDesktop()) {
      // do nothing
    } else {
      PhonegapUtil.prepareService(service, 
          AppClientFactory.IMPL.getNativeProperty(NAT_PROP_FACADE_MODULE_URL, DEFAULT_FACADE_MODULE_URL), 
          AppClientFactory.IMPL.getNativeProperty(NAT_PROP_FACADE_RELATIVE_PATH, DEFAULT_FACADE_RELATIVE_PATH));
    }
    PhgUtils.log("remote facade set on " + service.getServiceEntryPoint());
    return facade;
  }
  
}
