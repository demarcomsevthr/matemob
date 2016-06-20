package it.mate.testleaflet.client.view;

import it.mate.testleaflet.client.view.OrderItemEditView.Presenter;
import it.mate.testleaflet.shared.model.OrderItem;
import it.mate.testleaflet.shared.model.OrderItemRow;
import it.mate.testleaflet.shared.model.Timbro;
import it.mate.testleaflet.shared.utils.RenderUtils;
import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.event.TapEvent;
import it.mate.onscommons.client.event.TapHandler;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.ui.OnsButton;
import it.mate.onscommons.client.ui.OnsLabel;
import it.mate.onscommons.client.ui.OnsList;
import it.mate.onscommons.client.ui.OnsListItem;
import it.mate.phgcommons.client.utils.PhgUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class OrderItemEditView extends AbstractBaseView<Presenter> {

  public interface Presenter extends BasePresenter {
//  public void goToTimbroComposeView(Timbro timbro);
    public void goToTimbroComposeView(OrderItem orderItem);
    public void addOrderItemToCart(OrderItem orderItem);
    public void goToMessageListView(OrderItem orderItem);
    public void goToCartListView();
    public void goToOrderItemImageView(OrderItem orderItem);
  }

  public interface ViewUiBinder extends UiBinder<Widget, OrderItemEditView> { }

  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
  
  @UiField Panel wrapperPanel;
  @UiField HTML previewPanel;
  @UiField OnsLabel lbNome;
  @UiField OnsLabel lbDimensioni;
  @UiField OnsList list;
  
  OrderItem orderItem;
  
  List<OnsListItem> buttonItems = new ArrayList<>();
  
  public OrderItemEditView() {
    initUI();
  }

  private void initProvidedElements() {

  }

  private void initUI() {
    initProvidedElements();
    initWidget(uiBinder.createAndBindUi(this));
    OnsenUi.ensureId(previewPanel.getElement());
  }
  
  
  private void addBtn(String text, TapHandler handler) {
    OnsListItem listItem = new OnsListItem();
    listItem.addStyleName("ons-fadein");
    OnsButton btn = new OnsButton();
//  btn.setTextWhenAvailable(text);
    btn.setTextDirect(text);
    btn.addTapHandler(handler);
    btn.addStyleName("ons-fadein");
    listItem.add(btn);
//  list.add(listItem);
    buttonItems.add(listItem);
  }
  
  private void addBtnCompose(String text) {
    addBtn(text, new TapHandler() {
      public void onTap(TapEvent event) {
//      getPresenter().goToTimbroComposeView(orderItem.getTimbro());
        getPresenter().goToTimbroComposeView(orderItem);
      }
    });
  }
  
  private void addBtnCart(String text) {
    addBtn(text, new TapHandler() {
      public void onTap(TapEvent event) {
        getPresenter().addOrderItemToCart(orderItem);
      }
    });
  }
  
  private void addBtnMessages(String text) {
    addBtn(text, new TapHandler() {
      public void onTap(TapEvent event) {
        getPresenter().goToMessageListView(orderItem);
      }
    });
  }
  
  private void addBtnImage(String text) {
    addBtn(text, new TapHandler() {
      public void onTap(TapEvent event) {
        getPresenter().goToOrderItemImageView(orderItem);
      }
    });
  }
  
  private void addBtnBack(String text) {
    addBtn(text, new TapHandler() {
      public void onTap(TapEvent event) {
        if (orderItem.getRemoteId() == null) {
          getPresenter().goToCartListView();
        } else {
          //TODO: GO TO ORDER LIST VIEW
        }
      }
    });
  }
  
  @Override
  public void setModel(Object model, String tag) {
    
    if (model instanceof OrderItem) {
      this.orderItem = (OrderItem)model;
      
      PhgUtils.log("ORDER ITEM EDIT VIEW --> " + this.orderItem);
      
      OnsenUi.suspendCompilations();
      
      createButtons();
      iterateButtonsForRendering(buttonItems.iterator(), new Delegate<Void>() {
        public void execute(Void element) {
          composePreviewPanel(new Delegate<Void>() {
            public void execute(Void element) {
              OnsenUi.refreshCurrentPage();
            }
          });
        }
      });
      
    }
    
    
  }
  
  private void createButtons() {
    if (!orderItem.isInCart()) {
      addBtnCompose("Componi il timbro");
    } else {
      addBtnCompose("Modifica il timbro");
    }
    if (orderItem.getMessages() == null || orderItem.getMessages().size() == 0) {
      addBtnMessages("Aggiungi un messaggio");
    } else {
      addBtnMessages("Visualizza messaggi");
    }
    addBtnImage("Aggiungi un'immagine");
    if (!orderItem.isInCart()) {
      addBtnCart("Aggiungi al carrello");
    } else {
      if (orderItem.getRemoteId() == null) {
        addBtnBack("Torna al carrello");
      } else {
        addBtnBack("Indietro");
      }
    }
  }
  
  private void iterateButtonsForRendering(final Iterator<OnsListItem> it, final Delegate<Void> delegate) {
    if (it.hasNext()) {
      OnsListItem item = it.next();
      list.add(item, new Delegate<Element>() {
        public void execute(Element element) {
          iterateButtonsForRendering(it, delegate);
        }
      });
    } else {
      delegate.execute(null);
    }
  }
  
  private void composePreviewPanel(final Delegate<Void> delegate) {
    String previewId = previewPanel.getElement().getId();
    GwtUtils.onAvailable(previewId, new Delegate<Element>() {
      public void execute(Element previewElement) {
        
        Timbro timbro = orderItem.getTimbro();
        PhgUtils.log("PREVIEW " + timbro);
        
        int left = GwtUtils.getElementOffsetLeft(previewElement);
        int top = GwtUtils.getElementOffsetTop(previewElement);
        int width = GwtUtils.getElementOffsetWidth(previewElement);
        int height = GwtUtils.getElementOffsetHeight(previewElement);
        PhgUtils.log("PREVIEW ORIGINAL left = " + left + " top = " + top + " width = " + width + " height = " + height);
        
        double scaleFactor = width / timbro.getWidth();
        PhgUtils.log("SCALE FACTOR = " + scaleFactor);
        
        int newHeight = (int)(timbro.getHeight() * scaleFactor);
        PhgUtils.log("PREVIEW WIDTH = " + width);
        PhgUtils.log("PREVIEW HEIGHT = " + newHeight);
        previewElement.getStyle().setHeight(newHeight, Unit.PX);
        
        if (orderItem.getRows().size() > 0) {
          previewElement.removeAllChildren();
        }

        int rowTop = top;
        for (OrderItemRow row : orderItem.getRows()) {
//        Element span = RenderUtils.renderOrderItemAsGwtSpan(row, rowTop, left, 2.0);
          Element span = RenderUtils.renderOrderItemAsGwtSpan(row, rowTop, left, 4.0);
          rowTop += span.getPropertyInt("height");
          previewElement.appendChild(span);
        }
        
        lbNome.setText(timbro.getNome());
        lbDimensioni.setText("" + timbro.getWidth() + " x " + timbro.getHeight());
        
        delegate.execute(null);
        
      }
    });
  }

}
