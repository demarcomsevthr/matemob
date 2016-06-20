package it.mate.testleaflet.client.view;

import it.mate.testleaflet.client.view.TimbriListView.Presenter;
import it.mate.testleaflet.shared.model.OrderItem;
import it.mate.testleaflet.shared.model.Timbro;
import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.event.TapEvent;
import it.mate.onscommons.client.event.TapHandler;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.ui.OnsCarousel;
import it.mate.onscommons.client.ui.OnsCarouselItem;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class TimbriListView extends AbstractBaseView<Presenter> {

  public interface Presenter extends BasePresenter {
    public void goToTimbroDetailView(Timbro timbro);
    public void addTimbroToCurrentOrder(Timbro timbro, Delegate<OrderItem> delegate);
  }

  public interface ViewUiBinder extends UiBinder<Widget, TimbriListView> { }

  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
  
  @UiField Panel wrapperPanel;
  @UiField OnsCarousel carousel;
  
  public TimbriListView() {
    initUI();
  }

  private void initProvidedElements() {

  }

  private void initUI() {
    initProvidedElements();
    initWidget(uiBinder.createAndBindUi(this));
    if (OnsenUi.isSlidingMenuPresent()) {
      OnsenUi.getSlidingMenu().setSwipeable(false);
    }
  }

  @Override
  public void onDetachView() {
    super.onDetachView();
    if (OnsenUi.isSlidingMenuPresent()) {
      OnsenUi.getSlidingMenu().setSwipeable(true);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public void setModel(Object model, String tag) {
    if (model instanceof List) {
      List<Timbro> timbri = (List<Timbro>)model;
      populateCarousel(timbri);
    } else if (model instanceof String) {
      final String titolo = (String)model;
      OnsenUi.onAvailableElement("onsUidToolbarCenterDiv", new Delegate<Element>() {
        public void execute(Element element) {
          element.setInnerText(titolo);
        }
      });
    }
  }
  
  private void populateCarousel(List<Timbro> timbri) {
    
    OnsenUi.suspendCompilations();
    
//  carousel.getElement().getStyle().setOpacity(0);
    
    OnsCarouselItem firstItem = null;
    for (Timbro timbro : timbri) {
      String html = "<div id='timbro"+timbro.getId()+"' class='app-carousel-item-inner'><p class='app-carousel-item-name'>" + timbro.getNome() +"</p>";
      html += "<img src='"+ timbro.getImageData() +"'/>";
      html += "<span class='app-carousel-item-price'>Prezzo "+ GwtUtils.formatCurrency(timbro.getPrezzo()) +" euro</span>";
      html += "</div>";
      OnsCarouselItem item = new OnsCarouselItem(html);
      item.setModel(timbro);
      item.addTapHandler(new TapHandler() {
        public void onTap(TapEvent event) {
          onBtnBuy(event);
        }
      });
      carousel.add(item);
      if (firstItem == null) {
        firstItem = item;
      }
    }
    
//  showImpl(carousel.getElement());
    
    OnsenUi.refreshCurrentPage();
    
  }
  
  protected static native void showImpl(Element elem) /*-{
    $wnd.animit(elem)
      .queue({
        css: {
          opacity: 0
        },
        duration: 0
      })
      .queue({
        css: {
          opacity: 1
        },
        duration: 1
      })
      .play();
  }-*/;

  @UiHandler("btnBuy")
  public void onBtnBuy(TapEvent event) {
    carousel.getActiveItem(new Delegate<OnsCarouselItem>() {
      public void execute(OnsCarouselItem item) {
        Timbro timbro = (Timbro)item.getModel();
        getPresenter().addTimbroToCurrentOrder(timbro, null);
      }
    });
  }
  
}
