package it.mate.gwtcommons.client.utils;

import it.mate.gwtcommons.client.ui.StatePanel;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.ComplexPanel;

public class StatePanelUtil {
  
  private String currentState = "";
  
  private List<StatePanel> statePanels = new ArrayList<StatePanel>();

  public StatePanelUtil() {

  }
  
  public StatePanelUtil(String currentState) {
    this.currentState = currentState;
  }
  
  public void setCurrentState(String currentState) {
    this.currentState = currentState;
    for (StatePanel statePanel : statePanels) {
      boolean visible = currentState.equals(statePanel.getStateId());
      statePanel.setVisible(visible);
      if (statePanel.getWidget() instanceof ComplexPanel) {
        ComplexPanel complexPanel = (ComplexPanel)statePanel.getWidget();
        for (int it = 0; it < complexPanel.getWidgetCount(); it++) {
          complexPanel.getWidget(it).setVisible(visible);
        }
      }
    }
  }
  
  public void add(StatePanel statePanel) {
    statePanels.add(statePanel);
    setCurrentState(currentState);
  }
  
  

}
