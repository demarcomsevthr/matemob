package it.mate.testons.client.factories;

import it.mate.testons.client.activities.AdminActivity;
import it.mate.testons.client.activities.AdminActivityMapper;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;

public class AdminGinModule extends AbstractGinModule {
  
  private static AdminActivity mainActivity;

  @Override
  protected void configure() {
    
  }
  
  @Provides
  AdminActivityMapper getAdminActivityMapper() {
    return new AdminActivityMapper(AdminClientFactory.IMPL);
  }
  
  @Provides
  public AdminActivity getMainActivity() {
    if (mainActivity == null) {
      mainActivity = new AdminActivity(AdminClientFactory.IMPL);
    }
    return mainActivity;
  }
  
}
