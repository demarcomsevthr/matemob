package it.mate.testleaflet.client.view;

import it.mate.testleaflet.client.constants.AppProperties;
import it.mate.testleaflet.client.view.SettingsView.Presenter;
import it.mate.testleaflet.shared.model.OrderItem;
import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.event.TapEvent;
import it.mate.onscommons.client.ui.OnsLabel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class SettingsView extends AbstractBaseView<Presenter> {

  public interface Presenter extends BasePresenter {
    public void goToHomeView();
    public void resetDB();
    public void goToAccountEditView();
    public void testWaitingState(boolean flag);
    public void updateOrdersFromServer();
    public void registerPushNotifications();
    public void saveCustomerImageOnOrderItem(OrderItem orderItem, Delegate<OrderItem> delegate);
  }

  public interface ViewUiBinder extends UiBinder<Widget, SettingsView> { }

  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
  
  @UiField Label counterLbl;
  @UiField OnsLabel verLbl;
  
  boolean waiting = false;
  
  public SettingsView() {
    initUI();
  }

  private void initUI() {
    initWidget(uiBinder.createAndBindUi(this));
    verLbl.setText("Version " + AppProperties.IMPL.versionNumber());
  }
  
  @Override
  public void setModel(Object model, String tag) {
    if (model instanceof String) {
      counterLbl.setText((String)model);
    }
  }

  @UiHandler("btnReset")
  public void onBtnReset(TapEvent event) {
    GwtUtils.deferredExecution(200, new Delegate<Void>() {
      public void execute(Void element) {
        getPresenter().resetDB();
      }
    });
  }
  
  @UiHandler("btnAccount")
  public void onBtnAccount(TapEvent event) {
    getPresenter().goToAccountEditView();
  }

  /* 

  @UiHandler("btnDialog")
  public void onBtnDialog(TapEvent event) {
    OnsDialogUtils.createDialog("Szingleton dialog", true);
  }
  
  @UiHandler("btnWaiting")
  public void onBtnWaiting(TapEvent event) {
    waiting = !waiting;
    getPresenter().testWaitingState(waiting);
  }
  
  @UiHandler("btnUpdate")
  public void onBtnUpdate(TapEvent event) {
    getPresenter().updateOrdersFromServer();
  }
  
  @UiHandler("btnPush")
  public void onBtnPush(TapEvent event) {
    getPresenter().registerPushNotifications();
  }
  
  @UiHandler("btnPick")
  public void onBtnPick(TapEvent event) {
    if (ImagePickerPlugin.isInstalled()) {
      ImagePickerPlugin.getPictures(new ImagePickerPlugin.Options(), new Delegate<List<String>>() {
        public void execute(List<String> results) {
          if (results != null && results.size() > 0) {
            String url = results.get(0);
            String destFile = url.substring(url.lastIndexOf("/"));
            PhgUtils.log("destFile = " + destFile);
            FileSystemPlugin.readExternalFileAsEncodedData(url, destFile, new Delegate<String>() {
              public void execute(String fileContent) {
                PhgUtils.log("fileContent: " + fileContent);
              }
            });
          }
        }
      });
    } else {
      PhgUtils.log("ImagePickerPlugin NOT INSTALLED");
    }
  }
  
  @UiHandler("btnSdm")
  public void onBtnSdm(TapEvent event) {
    Window.Location.replace("http://10.0.2.2:8888/index.html");
  }
  
  */
  
}
