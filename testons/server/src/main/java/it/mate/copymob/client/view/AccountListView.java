package it.mate.testons.client.view;

import it.mate.testons.client.view.AccountListView.Presenter;
import it.mate.testons.shared.model.Account;
import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.gwtcommons.client.ui.MessageBox;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AccountListView extends AbstractBaseView<Presenter> {
  
  public interface Presenter extends BasePresenter {
    public void sendPushNotification(Account account, String message, String regId);
  }
  
  public interface ViewUiBinder extends UiBinder<Widget, AccountListView> { }
  
  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
  
  @UiField FlexTable resultsTable;

  public AccountListView() {
    initUI();
  }
  
  protected void initUI() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  @Override
  public void setModel(Object model, String tag) {
    if (model instanceof List) {
      List<Account> accounts = (List<Account>)model;
      populateTable(accounts);
    }
  }
  
  private void populateTable(List<Account> accounts) {
    
    int row = 0;
    
    for (final Account account : accounts) {
      
      Anchor aName = new Anchor(account.getName());
      resultsTable.setWidget(row, 0, aName);
        
      Anchor aEmail = new Anchor(account.getEmail() != null ? account.getEmail() : "");
      resultsTable.setWidget(row, 1, aEmail);
      
      Button btPush = new Button("Push");
      btPush.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          
          VerticalPanel dialogPanel = new VerticalPanel();
          
          dialogPanel.add(new Label("Reg Id"));
          final TextBox boxRegId = new TextBox();
          boxRegId.setWidth("10em");
          dialogPanel.add(boxRegId);
          
          dialogPanel.add(new Label("Messaggio"));
          final TextBox boxMessaggio = new TextBox();
          boxMessaggio.setWidth("10em");
          dialogPanel.add(boxMessaggio);
          
          MessageBox.create(new MessageBox.Configuration()
          .setCaptionText("Inserisci un messaggio")
          .setButtonType(MessageBox.BUTTONS_OKCANCEL)
          .setIconType(MessageBox.ICON_INFO)
          .setBodyWidget(dialogPanel)
          .setBodyWidth("400px")
          .setCallbacks(new MessageBox.Callbacks() {
            public void onOk() {

              String regId = boxRegId.getValue();
              String message = boxMessaggio.getValue();
              getPresenter().sendPushNotification(account, message, regId);
              
            }
          }));
        }
      });
      resultsTable.setWidget(row, 2, btPush);
        
      row++;
      
    }
    
  }

}
