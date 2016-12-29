package it.mate.onscommons.client.ui;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.onsen.OnsenUi;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class OnsHorizontalPanel extends HorizontalPanel {
  
  private boolean addDirect = false;
  
  private Element actualTableElement;
  
  private HorizontalAlignmentConstant horzAlign = ALIGN_DEFAULT;

  private VerticalAlignmentConstant vertAlign = ALIGN_TOP;
  
  public OnsHorizontalPanel() {
    super();
    OnsenUi.ensureId(getElement());
  }
  
  /**
   * NOTA BENE:
      la setAddDirect da uibinder non funziona (la chiama dopo la add!)
      occorre utilizzare OnsHorizontalPanelDirect
   */
  public void setAddDirect(boolean addDirect) {
    this.addDirect = addDirect;
    actualTableElement = getElement();
  }
  
  public boolean isAddDirect() {
    return addDirect;
  }
  
  @Override
  public void add(final Widget w) {
    if (addDirect || actualTableElement != null) {
      OnsHorizontalPanel.this.internalAdd(w);
    } else {
      GwtUtils.onAvailable(getElement().getId(), new Delegate<com.google.gwt.dom.client.Element>() {
        public void execute(com.google.gwt.dom.client.Element tableElement) {
          actualTableElement = (Element)tableElement;
          OnsHorizontalPanel.this.internalAdd(w);
        }
      });
    }
  }
  
  private void internalAdd(Widget w) {
    super.add(w);
    
    /** DEVO RIPETERE QUELLO CHE FA IL PARENT **/
    Element td = createAlignedTd();
    DOM.appendChild(getActualTableRow(), td);
    
    DOM.appendChild(td, w.getElement());
    
    if (addDirect) {
//    OnsenUi.compileElement(w.getElement());
    } else {
      String childId = w.getElement().getId();
      if (childId != null && !"".equals(childId)) {
        GwtUtils.onAvailable(childId, new Delegate<com.google.gwt.dom.client.Element>() {
          public void execute(com.google.gwt.dom.client.Element element) {
            OnsenUi.compileElement(element);
          }
        });
      }
    }

    
  }
  
  private Element getActualTableRow() {
    return (Element)actualTableElement.getElementsByTagName("tr").getItem(0);
  }

  @Override
  protected Element getBody() {
    if (actualTableElement != null) {
      return (Element)(actualTableElement.getElementsByTagName("tbody").getItem(0));
    } else {
      return super.getBody();
    }
  }
  
  @Override
  public void setWidth(final String width) {
    GwtUtils.onAvailable(getElement().getId(), new Delegate<com.google.gwt.dom.client.Element>() {
      public void execute(com.google.gwt.dom.client.Element containerElement) {
        GwtUtils.setJsPropertyString(containerElement.getStyle(), "width", width) ;
      }
    });
  }
  
  public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
    horzAlign = align;
    super.setHorizontalAlignment(align);
  }

  public void setVerticalAlignment(VerticalAlignmentConstant align) {
    vertAlign = align;
    super.setVerticalAlignment(align);
  }
  
  private Element createAlignedTd() {
    Element td = DOM.createTD();
    setCellHorizontalAlignment(td, horzAlign);
    setCellVerticalAlignment(td, vertAlign);
    return td;
  }
}
