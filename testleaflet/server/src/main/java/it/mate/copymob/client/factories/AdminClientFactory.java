package it.mate.testleaflet.client.factories;


import it.mate.testleaflet.client.activities.AdminActivityMapper;
import it.mate.gwtcommons.client.factories.BaseClientFactory;
import it.mate.gwtcommons.client.history.BaseActivityMapper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.SimplePanel;

public interface AdminClientFactory extends BaseClientFactory<AdminGinjector> {

  public static AdminClientFactory IMPL = GWT.create(AdminClientFactory.class);
  
  public void initMvp(SimplePanel panel, BaseActivityMapper activityMapper);
  
  public void initMvp(SimplePanel panel, BaseActivityMapper activityMapper, Place defaultPlace);
  
  public AdminActivityMapper getAdminActivityMapper();
  
}

