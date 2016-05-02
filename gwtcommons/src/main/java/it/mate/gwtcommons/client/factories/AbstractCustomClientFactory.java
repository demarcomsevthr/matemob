package it.mate.gwtcommons.client.factories;

import java.io.Serializable;

import com.google.gwt.resources.client.CssResource;
import com.google.web.bindery.event.shared.EventBus;
//import com.google.gwt.event.shared.EventBus;

public interface AbstractCustomClientFactory extends Serializable {
  
  public void initEventBus(EventBus eventBus);
  
  public String getCustomName();
  
  public CssResource getCustomCss();

}
