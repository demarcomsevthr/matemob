package it.mate.testons.client.places;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class AdminPlace extends Place /* implements ReversiblePlace */ {
  
  public static final String HOME = "home";
  
  public static final String ORDER_LIST = "orderList";
  
  public static final String ORDER_EDIT = "orderEdit";
  
  public static final String ACCOUNT_LIST = "accountList";
  
  private String token;
  
  private Object model;
  
  @Override
  public String toString() {
    return "MainPlace [token=" + token + "]";
  }

  public AdminPlace() {
    this.token = HOME;
  }

  public AdminPlace(String token) {
    this.token = token;
  }

  public AdminPlace(String token, Object model) {
    this.token = token;
    this.model = model;
  }
  
  public String getToken() {
    return token;
  }
  
  public Object getModel() {
    return model;
  }
  
  public Place getPreviousPlace() {
    return null;
  }

  public void setPreviousPlace(Place previousPlace) {
    
  }
  

  public static class Tokenizer implements PlaceTokenizer<AdminPlace> {

    @Override
    public String getToken(AdminPlace place) {
      return place.getToken();
    }

    @Override
    public AdminPlace getPlace(String token) {
      return new AdminPlace(token);
    }

  }


}
