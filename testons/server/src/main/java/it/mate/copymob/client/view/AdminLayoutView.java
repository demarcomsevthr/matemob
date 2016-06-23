package it.mate.testons.client.view;

import it.mate.testons.client.factories.AdminClientFactory;
import it.mate.testons.client.places.AdminPlace;
import it.mate.gwtcommons.client.ui.MvpPanel;
import it.mate.gwtcommons.client.utils.GwtUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class AdminLayoutView extends Composite /*implements PortalSessionStateChangeEvent.Handler*/ {
  
  public interface ViewUiBinder extends UiBinder<Widget, AdminLayoutView> { }
  
  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);

  @UiField Panel loggedStatePanel;
  @UiField Panel notloggedStatePanel;
  @UiField Panel roundedPanel;
  @UiField MenuBar menubar;
  @UiField MvpPanel adminMainMvp;
  @UiField Panel adminTabContainerPanel;
  @UiField Label loggedUserLabel;
  @UiField Anchor googleLoginAnchor;
  @UiField Anchor googleLogoutAnchor;
  @UiField Anchor forceGoogleLogoutAnchor;
  
  public AdminLayoutView() {
    initUI();
    GwtUtils.setBorderRadius(roundedPanel, "5px");
//  AppClientFactory.IMPL.getEventBus().addHandler(PortalSessionStateChangeEvent.TYPE, this);
  }
  
  private void setLoggedState (boolean logged) {
    loggedStatePanel.setVisible(logged);
    notloggedStatePanel.setVisible(!logged);
  }
  
  protected void initUI() {
    initWidget(uiBinder.createAndBindUi(this));

    addMenu(menubar, "Accounts", new AdminPlace(AdminPlace.ACCOUNT_LIST), null);
    
    addMenu(menubar, "Ordini", new AdminPlace(AdminPlace.ORDER_LIST), null);
    
    /*
    addMenu(menubar, "Generale", new GeneralPlace(), null);
    addMenu(menubar, "Pagine", new PortalPagePlace(PortalPagePlace.LIST).setHistoryName("Pagine"), null);
    addMenu(menubar, "Immagini", new ImagePlace(ImagePlace.LIST), null);
    addMenu(menubar, "Articoli", new ArticlePlace(ArticlePlace.FOLDER_LIST).setHistoryName("Articoli"), null);
    addMenu(menubar, "Calendario", new CalendarPlace(CalendarPlace.EVENT_LIST).setHistoryName("Calendario"), null);
    addMenu(menubar, "Documenti", new DocumentPlace(DocumentPlace.FOLDER_LIST).setHistoryName("Documenti"), null);
    addMenu(menubar, "Prodotti", new ProductPlace(ProductPlace.LIST).setHistoryName("Prodotti"), null);
    addMenu(menubar, "Produttori", new ProductPlace(ProductPlace.PRODUCER_LIST).setHistoryName("Produttori"), null);
    addMenu(menubar, "Utenti", new PortalUserPlace(PortalUserPlace.LIST).setHistoryName("Utenti"), null);
    breadcrumb.setClientFactory(AppClientFactory.IMPL);
    */

    adminMainMvp.initMvp(AdminClientFactory.IMPL, AdminClientFactory.IMPL.getGinjector().getAdminActivityMapper());

//  AdminClientUtils.applyDefaultResizePolicy(adminTabContainerPanel);
    
  }
  
  public MvpPanel getMvpPanel() {
    return adminMainMvp;
  }
  
  private class MenuItemInfo {
    String text;
    Place place;
    private MenuItemInfo(String text, Place place) {
      super();
      this.text = text;
      this.place = place;
    }
  }
  
  private void addMenu (MenuBar menubar, String title, Place place, MenuItemInfo[] items) {
    if (place != null) {
      menubar.addItem(title, createPlaceMenuCommand(place));
    } else {
      MenuBar folderMenu = new MenuBar(true);
      menubar.addItem(title, folderMenu);
      for (MenuItemInfo item : items) {
        folderMenu.addItem(item.text, createPlaceMenuCommand(item.place));
      }
    }
  }

  private <P extends Place> Command createPlaceMenuCommand(final Place place) {
    return new Command() {
      public void execute() {
        GwtUtils.log("MENU PRESSED");
        AdminClientFactory.IMPL.getPlaceController().goTo(place);
      }
    };
  }

}
