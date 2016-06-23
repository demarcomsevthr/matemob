package it.mate.testons.client.places;

import it.mate.gwtcommons.client.places.HasToken;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class MainPlace extends Place implements HasToken {
  
  public static final String MENU = "menu";
  
  public static final String HOME = "home";
  
  public static final String SETTINGS = "settings";
  
  public static final String SEARCH = "search";
  
  public static final String ABOUT = "about";
  
  public static final String CATEGORIE_LIST = "categorieList";
  
  public static final String TIMBRI_LIST = "timbriList";
  
  public static final String TIMBRO_DETAIL = "timbroDetail";
  
  public static final String ORDER_ITEM_EDIT = "ordItemEdit";
  
  public static final String ORDER_ITEM_COMPOSE = "ordItemCompose";
  
  public static final String ORDER_ITEM_IMAGE = "ordItemImage";
  
  public static final String MESSAGE_LIST = "messageList";
  
  public static final String ACCOUNT_EDIT = "accountEdit";
  
  public static final String CART_LIST = "cartList";
  
  public static final String CART_CONF = "cartConf";
  
  public static final String ORDER_LIST = "orderList";
  
  public static final String ORDER_EDIT = "orderEdit";
  
  private String token;
  
  private Object model;
  
  public MainPlace() {
    this.token = HOME;
  }
  
  public MainPlace(String token) {
    this(token, null);
  }
  
  @Override
  public String toString() {
    return "MainPlace [token=" + token + "]";
  }

  public MainPlace(String token, Object model) {
    this.token = token;
    this.model = model;
  }
  
  public String getToken() {
    return token;
  }
  
  public Object getModel() {
    return model;
  }
  
  
  public static class Tokenizer implements PlaceTokenizer<MainPlace> {

    @Override
    public String getToken(MainPlace place) {
      return place.getToken();
    }

    @Override
    public MainPlace getPlace(String token) {
      return new MainPlace(token);
    }

  }
  
}
