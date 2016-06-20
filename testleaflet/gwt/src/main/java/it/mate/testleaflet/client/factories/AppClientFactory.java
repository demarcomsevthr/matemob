package it.mate.testleaflet.client.factories;

import it.mate.gwtcommons.client.factories.BaseClientFactory;
import it.mate.gwtcommons.client.history.BaseActivityMapper;
import it.mate.testleaflet.shared.service.RemoteFacadeAsync;

import com.google.gwt.user.client.ui.SimplePanel;
import com.googlecode.gwtphonegap.client.PhoneGap;

@SuppressWarnings("rawtypes")
public interface AppClientFactory extends BaseClientFactory<AppGinjector> {
  
  public static final AppClientFactory IMPL = Initializer.create();
  
  public static final String KEY_TRACE_ACTIVE = "traceActive";

  class Initializer {
    private static AppClientFactory create() {
      AppClientFactory clientFactory = new AppClientFactoryImpl();
      return clientFactory;
    }
  }
  
  public void initMvp(SimplePanel panel, BaseActivityMapper activityMapper);
  
  public com.google.web.bindery.event.shared.EventBus getBinderyEventBus();
  
  public PhoneGap getPhoneGap();
  
  public String getNativeProperty(String name, String defValue);
  
  public boolean getNativeProperty(String name, boolean defValue);
  
  public RemoteFacadeAsync getRemoteFacade();

}
