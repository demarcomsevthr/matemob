package it.mate.testons.client.view;

import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.onscommons.client.event.TapEvent;
import it.mate.onscommons.client.event.TapHandler;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.ui.OnsHorizontalPanel;
import it.mate.onscommons.client.ui.OnsLabel;
import it.mate.onscommons.client.ui.OnsList;
import it.mate.onscommons.client.ui.OnsListItem;
import it.mate.onscommons.client.utils.OnsDialogUtils;
import it.mate.testons.client.view.DynListView.Presenter;
import it.mate.testons.shared.model.Prestazione;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class DynListView extends AbstractBaseView<Presenter> {
  
  public interface Presenter extends BasePresenter {

  }

  public interface ViewUiBinder extends UiBinder<Widget, DynListView> { }

  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
  
  @UiField OnsList resultsList;
  
  public DynListView() {
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
    if (model instanceof List) {
      showResults((List<Prestazione>)model);
    }
  }
  
  private void showResults(final List<Prestazione> prestazioni) {
    OnsenUi.suspendCompilations();
    resultsList.clear(new Delegate<Element>() {
      public void execute(Element element) {
        Iterator<Prestazione> it = prestazioni.iterator();
        while (it.hasNext()) {
          final Prestazione prz = it.next();
          TapHandler rowTapHandler = new TapHandler() {
            public void onTap(TapEvent event) {
              OnsDialogUtils.alert("Pressed " + prz.getDescrizione());
            }
          };
          OnsListItem listItem = new OnsListItem();
          listItem.setModifier("chevron");
          listItem.addTapHandler(rowTapHandler);
          OnsHorizontalPanel rowPanel = new OnsHorizontalPanel();
          rowPanel.setAddDirect(true);
          String html = "<img src='main/images/prest-640.png' class='app-prz-item-img'/>";
          HTML imgHtml = new HTML(html);
          OnsenUi.addTapHandler(imgHtml, rowTapHandler);
          rowPanel.add(imgHtml);
          OnsLabel nameLbl = new OnsLabel(prz.getDescrizione());
          OnsenUi.addTapHandler(nameLbl, rowTapHandler);
          nameLbl.addStyleName("app-prz-item-name");
          rowPanel.add(nameLbl);
          listItem.add(rowPanel);
          resultsList.add(listItem);
          
        }
        OnsenUi.refreshCurrentPage();
      }
    });
  }
  
}
