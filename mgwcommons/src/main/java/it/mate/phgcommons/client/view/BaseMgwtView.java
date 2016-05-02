package it.mate.phgcommons.client.view;

import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.phgcommons.client.utils.IterationUtil;
import it.mate.phgcommons.client.utils.IterationUtil.ItemDelegate;
import it.mate.phgcommons.client.utils.IterationUtilSimple;
import it.mate.phgcommons.client.utils.PhgUtils;
import it.mate.phgcommons.client.utils.WebkitCssUtil;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.HeaderButton;
import com.googlecode.mgwt.ui.client.widget.HeaderPanel;
import com.googlecode.mgwt.ui.client.widget.LayoutPanel;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;
import com.googlecode.mgwt.ui.client.widget.impl.ScrollPanelImpl;

public abstract class BaseMgwtView <P extends BasePresenter> {
  
  protected LayoutPanel main;
  
  /* ATTENZIONE: MODIFICA DEL 13/12/2014 PER TESTARE ALTRI FRAMEWORK JS CHE ATTACCANO TOUCH HANDLER AL PANEL
   * (vedi sortable.js in protoph/testview)
  protected ScrollPanel scrollPanel;
  */
  protected HasWidgets scrollPanel;
  
  protected HeaderPanel headerPanel;
  protected HeaderButton headerBackButton;
  protected HeaderButton headerMainButton;
  protected HTML title;
  
  private P presenter;
  
  public BaseMgwtView() {
    initUI();
  }
  
  protected HasWidgets createScrollPanel() {
    return new MyScrollPanel();
  }

  private void initUI() {
    main = new LayoutPanel() {
      @Override
      protected void onUnload() {
        BaseMgwtView.this.onUnload();
        super.onUnload();
      }
    };
//  scrollPanel = new ScrollPanel();
    scrollPanel = createScrollPanel();
    headerPanel = new HeaderPanel();
    title = new HTML();
    headerPanel.setCenterWidget(title);

    headerBackButton = new HeaderButton();
    headerBackButton.setBackButton(true);
  
    // 21/02/2013
//  headerBackButton.setVisible(!MGWT.getOsDetection().isAndroid());

    headerMainButton = new HeaderButton();
    headerMainButton.setRoundButton(true);

    if (!MGWT.getOsDetection().isPhone()) {
      headerPanel.setLeftWidget(headerMainButton);
//    headerMainButton.addStyleName(MGWTStyle.getTheme().getMGWTClientBundle().getUtilCss().portraitonly());
    } else {
      headerPanel.setLeftWidget(headerBackButton);
    }

    main.add(headerPanel);
    main.add((Composite)scrollPanel);
    
    headerPanel.getElement().setId("mgwtHeaderPanel");
    
    main.addAttachHandler(new AttachEvent.Handler() {
      public void onAttachOrDetach(AttachEvent event) {
        onAttach(event);
      }
    });
    
  }
  
  public void onAttach(AttachEvent event) {

  }
  
  public void onUnload() {
    
  }
  
  public void setTitleHtml (String html) {
    getTitle().setHTML(html);
  }
  
  public Widget asWidget() {
    return main;
  }
  
  protected void initWidget(Widget widget) {
    if (scrollPanel instanceof ScrollPanel) {
      ((ScrollPanel)scrollPanel).setWidget(widget);
    } else if (scrollPanel instanceof BaseMgwtViewNoScrollPanel.MyScrollPanel) {
      ((BaseMgwtViewNoScrollPanel.MyScrollPanel)scrollPanel).setWidget(widget);
    }
  }
  
  public HTML getTitle() {
    return title;
  }
  
  protected ScrollPanel getScrollPanel() {
    return (ScrollPanel)scrollPanel;
  }
  
  protected ScrollPanelImpl getScrollPanelImpl() {
    if (scrollPanel instanceof BaseMgwtView.MyScrollPanel) {
      return ((MyScrollPanel)scrollPanel).getImpl();
    }
    return null;
  }
  
  public HeaderPanel getHeaderPanel() {
    return headerPanel;
  }
  
  protected HeaderButton getHeaderBackButton() {
    return headerBackButton;
  }
  
  protected P getPresenter() {
    return presenter;
  }

  public void setPresenter(P presenter) {
    this.presenter = presenter;
  }
  
  public void initHeaderBackButton(String text, final Delegate<TapEvent> delegate) {
    getHeaderBackButton().setText(text);
    getHeaderBackButton().addTapHandler(new TapHandler() {
      public void onTap(TapEvent event) {
        delegate.execute(event);
      }
    });
  }

  public void initHeaderBackButton(SafeHtml html, final Delegate<TapEvent> delegate) {
    headerBackButton = new HtmlHeaderButton();
//  headerBackButton.setBackButton(true);
    HtmlHeaderButton htmlHeaderButton = (HtmlHeaderButton)headerBackButton;
    htmlHeaderButton.setHtml(html);
    getHeaderBackButton().addTapHandler(new TapHandler() {
      public void onTap(TapEvent event) {
        delegate.execute(event);
      }
    });
    headerPanel.setLeftWidget(headerBackButton);
  }

  public abstract void setModel(Object model, String tag);  
  
  public static class HtmlHeaderButton extends HeaderButton {
    
    public void setHtml(SafeHtml html) {
      removeStyles();
      Element borderContainerElem = getElement().getFirstChildElement();
      borderContainerElem.setInnerSafeHtml(html);
      borderContainerElem.getStyle().setMarginTop(-3, Unit.PX);
      borderContainerElem.getStyle().setMarginLeft(-6, Unit.PX);
      try {
        Element pElem = getElement().getElementsByTagName("p").getItem(0);
        WebkitCssUtil.setStyleProperty(pElem.getStyle(), "border", "none");
        WebkitCssUtil.setStyleProperty(pElem.getStyle(), "background", "transparent");
      } catch (Throwable th) { }
    }
    
    
  }
  
  public class MyScrollPanel extends ScrollPanel {
    protected ScrollPanelImpl getImpl() {
      return impl;
    }
  }
  
  protected void refreshScrollPanel() {
    getScrollPanel().refresh();
  }
  
  protected void disableMainScrolling() {
    getScrollPanel().setScrollingEnabledX(false);
    getScrollPanel().setScrollingEnabledY(false);
  }
  
  protected void adjustInnerScrollPanelHeight(final ScrollPanel innerScrollPanel) {
    
    // In alcuni casi non setta bene le altezze al primo run, quindi uso l'escamotage di ripetere 3 volte l'operazione
    IterationUtilSimple.create(3, new ItemDelegate<Integer>() {
      public void handleItem(Integer item, final IterationUtil<Integer> iteration) {
        
        GwtUtils.deferredExecution(500, new Delegate<Void>() {
          public void execute(Void element) {
            try {
              innerScrollPanel.setScrollingEnabledY(true);
              int actualHeight = Window.getClientHeight() - innerScrollPanel.getAbsoluteTop();
              innerScrollPanel.getElement().getStyle().setHeight(actualHeight, Unit.PX);
              innerScrollPanel.refresh();
              iteration.next();
            } catch (Exception ex) {
              PhgUtils.log("Error " + ex.getClass().getName() + " - " + ex.getMessage());
            }
          }
        });
        
      }
    }, null);
    
  }
  
}
