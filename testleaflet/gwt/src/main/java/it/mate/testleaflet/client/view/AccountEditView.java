package it.mate.testleaflet.client.view;

import it.mate.testleaflet.client.view.AccountEditView.Presenter;
import it.mate.testleaflet.shared.model.Account;
import it.mate.testleaflet.shared.model.impl.AccountTx;
import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.onscommons.client.event.TapEvent;
import it.mate.onscommons.client.ui.OnsTextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class AccountEditView extends AbstractBaseView<Presenter> {

  public interface Presenter extends BasePresenter {
    public void saveAccount(Account account, Delegate<Account> delegate);
  }

  public interface ViewUiBinder extends UiBinder<Widget, AccountEditView> { }

  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
  
  @UiField Panel wrapperPanel;
  
  @UiField OnsTextBox fdName;
  @UiField OnsTextBox fdEmail;
  
  private Account account;
  
  public AccountEditView() {
    initUI();
  }

  private void initProvidedElements() {

  }

  private void initUI() {
    initProvidedElements();
    initWidget(uiBinder.createAndBindUi(this));
  }

  @Override
  public void setModel(Object model, String tag) {
    if (model instanceof Account) {
      account = (Account)model;
      showModel(account);
    }
  }
  
  @UiHandler("btnSave")
  public void onBtnSave(TapEvent event) {
    getPresenter().saveAccount(flushModel(), new Delegate<Account>() {
      public void execute(Account element) {
        getPresenter().goToPrevious();
      }
    });
  }
  
  private void showModel(Account account) {
    fdName.setValue(account.getName());
    fdEmail.setValue(account.getEmail());
  }
  
  private Account flushModel() {
    if (account == null) {
      account = new AccountTx();
    }
    account.setName(fdName.getValue());
    account.setEmail(fdEmail.getValue());
    return account;
  }
  
}
