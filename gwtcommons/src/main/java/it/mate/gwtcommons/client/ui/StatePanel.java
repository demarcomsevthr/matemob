package it.mate.gwtcommons.client.ui;

import com.google.gwt.user.client.ui.SimplePanel;

public class StatePanel extends SimplePanel {
  
  private String stateId;
  
  public StatePanel() {
    super();
    this.setVisible(false);
  }

  public StatePanel(String stateId) {
    super();
    this.stateId = stateId;
  }

  public String getStateId() {
    return stateId;
  }

  public void setStateId(String stateId) {
    this.stateId = stateId;
  }
  
  
}
