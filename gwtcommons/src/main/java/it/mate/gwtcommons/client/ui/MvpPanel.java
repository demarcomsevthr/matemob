package it.mate.gwtcommons.client.ui;

import it.mate.gwtcommons.client.factories.BaseClientFactory;
import it.mate.gwtcommons.client.factories.CommonGinjector;
import it.mate.gwtcommons.client.history.BaseActivityMapper;
import it.mate.gwtcommons.client.utils.MvpUtils;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.SimplePanel;

public class MvpPanel extends SimplePanel {

  private String historyId;
  
  public MvpPanel() {
    super();
  }
  
  public void setHistoryId(String historyId) {
    this.historyId = historyId;
  }
  
  public String getHistoryId() {
    return historyId;
  }

  public void initMvp(BaseClientFactory<? extends CommonGinjector> clientFactory, BaseActivityMapper activityMapper) {
    MvpUtils.initMvp(clientFactory, this, historyId, activityMapper, null);
  }
  
  public void initMvp(BaseClientFactory<? extends CommonGinjector> clientFactory, BaseActivityMapper activityMapper, Place defaultPlace) {
    MvpUtils.initMvp(clientFactory, this, historyId, activityMapper, defaultPlace);
  }
  
}
