package it.mate.testons.client.constants;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.Constants;

public interface AppProperties extends Constants {

  AppProperties IMPL = GWT.create(AppProperties.class);
  
  @DefaultStringValue("")
  String tabletAppName();
  
  @DefaultStringValue("")
  String phoneAppName();
  
  @DefaultBooleanValue(false)
  boolean extendedVersion();
  
  @DefaultStringValue("")
  String versionNumber();
  
  @DefaultStringValue("")
  String devName();
  
}
