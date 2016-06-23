package it.mate.testons.client.view;

import it.mate.testons.client.view.OrderItemView.Presenter;
import it.mate.testons.shared.model.Order;
import it.mate.testons.shared.model.OrderItem;
import it.mate.testons.shared.utils.RenderUtils;
import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.gwtcommons.client.utils.GwtUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class OrderItemView extends AbstractBaseView<Presenter> {
  
  public interface Presenter extends BasePresenter {
    public void goToOrderEdit(String orderId);
  }
  
  public interface ViewUiBinder extends UiBinder<Widget, OrderItemView> { }
  
  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
  
  @UiField FormPanel formPanel;
  @UiField FileUpload fileupdBtn;
  @UiField Label filenameLabel;
  @UiField Hidden orderItemId;
  @UiField Button btnInvia;
  @UiField Label lblTimbroNome;
  @UiField HTML htmlAnteprima;
  
  private Order order;
  
  public OrderItemView() {
    initUI();
  }
  
  protected void initUI() {
    initWidget(uiBinder.createAndBindUi(this));
    
    formPanel.setAction(getUploadServletUrl());
    formPanel.setMethod(FormPanel.METHOD_POST);
    formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
    
    fileupdBtn.addChangeHandler(new ChangeHandler() {
      public void onChange(ChangeEvent event) {
        String filename = purgePath(purgePath(fileupdBtn.getFilename(), "\\"), "/");
        filenameLabel.setText(filename);
        btnInvia.setEnabled(true);
      }
    });
    
  }

  @Override
  public void setModel(Object model, String tag) {
    if (model instanceof OrderItem) {
      OrderItem orderItem = (OrderItem)model;
      lblTimbroNome.setText(orderItem.getTimbro().getNome());
      orderItemId.setValue(orderItem.getRemoteId());
      if (orderItem.getPreviewImage() != null) {
        htmlAnteprima.setHTML(RenderUtils.imageTextToHtml(orderItem.getPreviewImage(), "jpeg"));
      }
    }
  }

  private static String getUploadServletUrl() {
    return GWT.getHostPageBaseURL()+".uploadServlet";
  }
  
  private String purgePath(String text, String pathSeparator) {
    if (text.contains(pathSeparator)) {
      return text.substring(text.lastIndexOf(pathSeparator) + 1);
    }
    return text;
  }
  
  @UiHandler ("btnInvia")
  public void onBtnInvia (ClickEvent event) {
    formPanel.submit();
    formPanel.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
      public void onSubmitComplete(SubmitCompleteEvent event) {
        GwtUtils.log("UPLOAD EFFETTUATO");
        getPresenter().goToOrderEdit(order.getRemoteId());
      }
    });
  }
  
  public void setOrder(Order order) {
    this.order = order;
  }
  
}
