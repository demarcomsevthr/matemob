package it.mate.testons.client;



import it.mate.testons.client.factories.AdminClientFactory;
import it.mate.testons.client.view.AdminLayoutView;
import it.mate.gwtcommons.client.utils.GwtUtils;

import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class AdminEP implements EntryPoint {

  public void onModuleLoad() {
    GwtUtils.logEnvironment(getClass(), "onModuleLoad");

//  MainClientFactory.IMPL.setServerProperties(properties);
    
    AdminLayoutView mainView = new AdminLayoutView();
    RootPanel.get().add(mainView);
    AdminClientFactory.IMPL.initModule(mainView.getMvpPanel());
    
    
  }  
   
  private void startModule(Map<String, String> serverProperties) {

  }
  
}
