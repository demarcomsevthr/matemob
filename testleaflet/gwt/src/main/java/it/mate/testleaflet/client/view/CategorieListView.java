package it.mate.testleaflet.client.view;

import it.mate.testleaflet.client.view.CategorieListView.Presenter;
import it.mate.testleaflet.shared.model.Categoria;
import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.onscommons.client.event.TapEvent;
import it.mate.onscommons.client.event.TapHandler;
import it.mate.onscommons.client.ui.OnsList;
import it.mate.onscommons.client.ui.OnsListItem;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class CategorieListView extends AbstractBaseView<Presenter> {

  public interface Presenter extends BasePresenter {
    public void goToTimbriListView(Categoria categoria);
  }

  public interface ViewUiBinder extends UiBinder<Widget, CategorieListView> { }

  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
  
  @UiField OnsList listCategorie;
  
  public CategorieListView() {
    initUI();
  }

  private void initProvidedElements() {

  }

  private void initUI() {
    initProvidedElements();
    initWidget(uiBinder.createAndBindUi(this));
  }

  @Override
  @SuppressWarnings("unchecked")
  public void setModel(Object model, String tag) {
    if (model instanceof List) {
      populateList((List<Categoria>)model);
    }
  }
  
  private void populateList(List<Categoria> categorie) {
    for (final Categoria categoria : categorie) {
      OnsListItem item = new OnsListItem(categoria.getDescrizione());
      item.setModifier("chevron");
      item.addTapHandler(new TapHandler() {
        public void onTap(TapEvent event) {
          getPresenter().goToTimbriListView(categoria);
        }
      });
      listCategorie.add(item);
    }
  }
  
}
