package it.mate.onscommons.client.ui;

import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.phgcommons.client.utils.PhgUtils;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class OnsPage extends HTMLPanel implements AcceptsOneWidget {

  private final static String TAG_NAME = "ons-page";
  
  private static OnsPage lastPage;
  
  private static OnsPage prevPage;
  
  public OnsPage() {
    super(TAG_NAME, "");
    OnsenUi.ensureId(getElement());
    prevPage = lastPage;
    lastPage = this;
  }
  
  public OnsPage(String html) {
    super(TAG_NAME, html);
    OnsenUi.ensureId(getElement());
    getElement().addClassName("ons-page");
    lastPage = this;
    OnsenUi.setCurrentPageId(getElement().getId());
    
    PhgUtils.log(">>>>>>>> OnsPage::constructor - WITH html " + html);
    
  }
  
  @Override
  public void add(Widget widget) {
    
    if (widget != null) {
      PhgUtils.log(">>>>>>>> OnsPage::add - ADDING ELEMENT " + widget.getElement());
    }
    
    if (OnsenUi.isAddDirectWithPlainHtml()) {
      OnsenUi.appendInnerHtml(getElement(), OnsenUi.getPlainHtml(widget.getElement()));
    } else {
      super.add(widget, getElement());
      if (widget.getElement().getNodeName().toLowerCase().startsWith("ons")) {
        OnsenUi.compileElement(widget.getElement());
      }
    }
  }

  @Override
  public void setWidget(IsWidget w) {
    add(w);
  }
  
  public static OnsPage getLastCreatedPage() {
    return lastPage;
  }
  
  public static OnsPage getPrevPage() {
    return prevPage;
  }

}
