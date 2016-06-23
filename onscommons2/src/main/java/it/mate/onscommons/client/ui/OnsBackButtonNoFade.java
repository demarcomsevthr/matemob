package it.mate.onscommons.client.ui;

import com.google.gwt.user.client.DOM;

public class OnsBackButtonNoFade extends OnsBackButton {
  
  public OnsBackButtonNoFade() {
    super(DOM.createElement("ons-toolbar-button"), false);
  }
  
}
