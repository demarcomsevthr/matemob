package it.mate.gwtcommons.client.history;

import it.mate.gwtcommons.client.utils.GwtUtils;

import java.util.logging.Logger;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.History;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
//import com.google.gwt.event.shared.EventBus;
//import com.google.gwt.event.shared.HandlerRegistration;

public class MappedPlaceHistoryHandler {
  
  private static final Logger log = Logger.getLogger(MappedPlaceHistoryHandler.class.getName());

  public static class DefaultHistorian implements Historian {
    
    private String key = "default";
    
    public DefaultHistorian(String key) {
      this.key = key;
    }

    public HandlerRegistration addValueChangeHandler(
        ValueChangeHandler<String> valueChangeHandler) {
      return MappedHistory.addValueChangeHandler(key, valueChangeHandler);
    }

    public String getToken() {
      return MappedHistory.getToken(key);
    }

    public void newItem(String token, boolean issueEvent) {
      if (token != null) {
        MappedHistory.newItem(key, token, issueEvent);
      } else {
//      System.out.println("PlaceMappedHistoryHandler.DefaultHistorian: received token null >> do not change history");
      }
    }
  }

  /**
   * Optional delegate in charge of History related events. Provides nice
   * isolation for unit testing, and allows pre- or post-processing of tokens.
   * Methods correspond to the like named methods on {@link History}.
   */
  public interface Historian {
    /**
     * Adds a {@link com.google.gwt.event.logical.shared.ValueChangeEvent}
     * handler to be informed of changes to the browser's history stack.
     * 
     * @param handler the handler
     * @return the registration used to remove this value change handler
     */
    HandlerRegistration addValueChangeHandler(
        ValueChangeHandler<String> valueChangeHandler);

    /**
     * @return the current history token.
     */
    String getToken();

    /**
     * Adds a new browser history entry. Calling this method will cause
     * {@link ValueChangeHandler#onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)}
     * to be called as well.
     */
    void newItem(String token, boolean issueEvent);
  }

  private final Historian historian;

  private PlaceHistoryMapper mapper;

  private PlaceController placeController;

  private Place defaultPlace = Place.NOWHERE;
  
  @Override
  public String toString() {
    return "MappedPlaceHistoryHandler [mapper=" + mapper + ", placeController=" + placeController + ", defaultPlace="
        + defaultPlace + "]";
  }

  /**
   * Create a new PlaceHistoryHandler with a {@link DefaultHistorian}. The
   * DefaultHistorian is created via a call to GWT.create(), so an alternative
   * default implementation can be provided through &lt;replace-with&gt; rules
   * in a {@code gwt.xml} file.
   * 
   * @param mapper a {@link PlaceHistoryMapper} instance
   */
  public MappedPlaceHistoryHandler(String key, PlaceHistoryMapper mapper) {
//  this(mapper, (Historian) GWT.create(DefaultHistorian.class));
    this(mapper, (Historian) new DefaultHistorian(key));
  }

  /**
   * Create a new PlaceHistoryHandler.
   * 
   * @param mapper a {@link PlaceHistoryMapper} instance
   * @param historian a {@link Historian} instance
   */
  public MappedPlaceHistoryHandler(PlaceHistoryMapper mapper, Historian historian) {
    this.mapper = mapper;
    this.historian = historian;
  }
  
  public void setMapper(PlaceHistoryMapper mapper) {
    this.mapper = mapper;
  }

  /**
   * Handle the current history token. Typically called at application start, to
   * ensure bookmark launches work.
   */
  public void handleCurrentHistory() {
    handleHistoryToken(historian.getToken());
  }

  /**
   * Initialize this place history handler.
   * 
   * @return a registration object to de-register the handler
   */
  public HandlerRegistration register(PlaceController placeController,
      EventBus eventBus, Place defaultPlace) {
    this.placeController = placeController;
    this.defaultPlace = defaultPlace;

    final HandlerRegistration placeReg = eventBus.addHandler(
        PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
          public void onPlaceChange(PlaceChangeEvent event) {
            Place newPlace = event.getNewPlace();
            historian.newItem(tokenForPlace(newPlace), false);
          }
        });

    final HandlerRegistration historyReg = historian.addValueChangeHandler(new ValueChangeHandler<String>() {
      public void onValueChange(ValueChangeEvent<String> event) {
        String token = event.getValue();
        handleHistoryToken(token);
      }
    });

    return new HandlerRegistration() {
      public void removeHandler() {
        MappedPlaceHistoryHandler.this.defaultPlace = Place.NOWHERE;
        MappedPlaceHistoryHandler.this.placeController = null;
        placeReg.removeHandler();
        historyReg.removeHandler();
      }
    };
  }

  /**
   * Visible for testing.
   */
  Logger log() {
    return log;
  }

  private void handleHistoryToken(String token) {
    
    GwtUtils.log(getClass(), "handleHistoryToken", "token = " + token + " this = " + this);

    Place newPlace = null;

    if (token == null || "".equals(token)) {
      newPlace = defaultPlace;
    }

    if (newPlace == null) {
      newPlace = mapper.getPlace(token);
    }

    if (newPlace == null) {
      log().warning("Unrecognized history token: " + token);
      newPlace = defaultPlace;
    }

    GwtUtils.log(getClass(), "handleHistoryToken", "goto newPlace = " + newPlace);
    placeController.goTo(newPlace);
  }

  private String tokenForPlace(Place newPlace) {
    if (defaultPlace.equals(newPlace)) {
      return "";
    }

    String token = mapper.getToken(newPlace);
    if (token != null) {
      return token;
    }

    /*
    log().warning("Place not mapped to a token: " + newPlace);
    return "";
    */
//  System.out.println("PlaceMappedHistoryHandler.tokenForPlace: Place not mapped to a token "+newPlace);
    return null;
    
  }
  

}
