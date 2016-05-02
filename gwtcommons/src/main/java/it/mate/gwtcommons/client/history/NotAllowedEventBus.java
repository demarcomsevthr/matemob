package it.mate.gwtcommons.client.history;

import it.mate.gwtcommons.client.utils.GwtUtils;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.web.bindery.event.shared.Event;

public class NotAllowedEventBus extends SimpleEventBus {

  public NotAllowedEventBus() {
    super();
    GwtUtils.log("ERROR: YOU ARE USING NOT ALLOWED EVENT BUS! (See also it.mate.gwtcommons.client.factories.CommonGinModule)");
    GwtUtils.log("ERROR: YOU ARE USING NOT ALLOWED EVENT BUS! (See also it.mate.gwtcommons.client.factories.CommonGinModule)");
    GwtUtils.log("ERROR: YOU ARE USING NOT ALLOWED EVENT BUS! (See also it.mate.gwtcommons.client.factories.CommonGinModule)");
    GwtUtils.log("ERROR: YOU ARE USING NOT ALLOWED EVENT BUS! (See also it.mate.gwtcommons.client.factories.CommonGinModule)");
  }

  @Override
  public void fireEvent(Event<?> event) {
    GwtUtils.log("ERROR: YOU ARE USING NOT ALLOWED EVENT BUS! (See also it.mate.gwtcommons.client.factories.CommonGinModule)");
  }
  

}
