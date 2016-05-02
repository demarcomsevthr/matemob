package it.mate.phgcommons.client.utils;

import it.mate.gwtcommons.client.ui.Span;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.gwtcommons.client.utils.JQuery;
import it.mate.gwtcommons.client.utils.JQuery.StyleProperties;
import it.mate.phgcommons.client.ui.TouchAnchor;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.mvp.client.Animation;
import com.googlecode.mgwt.ui.client.dialog.PopinDialog;

public class PhgDialogUtils {
  
  public static final int BUTTONS_YESNOCANCEL = 1;
  public static final int BUTTONS_YESNO = 2;
  public static final int BUTTONS_OKCANCEL = 3;
  public static final int BUTTONS_OK = 4;
  
  private static PopinDialog messageDialog;
  
  public static void showMessageDialog(String message) {
    showMessageDialog(message, "Alert");
  }
  
  public static void showMessageDialog(String message, int buttonsType) {
    showMessageDialog(message, "Alert", buttonsType, null);
  }
  
  public static void showMessageDialog(String message, String title) {
    showMessageDialog(message, title, BUTTONS_OK, null);
  }
  
  public static void showMessageDialog(String message, String title, int buttonsType) {
    showMessageDialog(message, title, buttonsType, null);
  }
  
  public static void showMessageDialog(final String message, final String title, int buttonsType, final Delegate<Integer> delegate) {
    showMessageDialog(message, title, buttonsType, null, delegate);
  }
  
  public static void showMessageDialog(final String message, final String title, int buttonsType, Position position, final Delegate<Integer> delegate) {
    
    VerticalPanel dialogContainer = new VerticalPanel();
    dialogContainer.setSpacing(0);
    dialogContainer.setWidth("100%");
    dialogContainer.addStyleName("phg-PopinDialog-Container");
    
    SimplePanel row;
    
    row = new SimplePanel();
    row.addStyleName("phg-PopinDialog-MessageRow");
    dialogContainer.add(row);
    HTML messageHtml = new HTML(SafeHtmlUtils.fromTrustedString( encodeHtmlText(message) ));
    row.add(messageHtml);
    
    row = new SimplePanel();
    row.addStyleName("phg-PopinDialog-Separator");
    dialogContainer.add(row);
    
    row = new SimplePanel();
    row.addStyleName("phg-PopinDialog-ButtonsRow");
    dialogContainer.add(row);
    HorizontalPanel buttonsPanel = new HorizontalPanel();
    row.add(buttonsPanel);
    
    createButtonsAndShow(dialogContainer, buttonsPanel, message, title, buttonsType, position, delegate);
    
  }
  
  private static String encodeHtmlText(String text) {
    return text.replace("§", "<BR/>");
  }
  
  private static void createButtonsAndShow(final Widget dialogContainer, Panel buttonsPanel, String message, final String title, final int buttonsType, final Position position, final Delegate<Integer> delegate) {
    String buttonMsg = null;
    
    String lang = PhgUtils.getAppLocalLanguage();
    
    if (buttonsPanel != null) {
      if (buttonsType == BUTTONS_YESNO || buttonsType == BUTTONS_YESNOCANCEL) {
        buttonMsg = "it".equals(lang) ? "Si" : "Yes";
      } else {
        buttonMsg = "OK";
      }
      TouchAnchor btn = new TouchAnchor(SafeHtmlUtils.fromTrustedString(buttonMsg), new TapHandler() {
        public void onTap(TapEvent event) {
          hideMessageDialog();
          if (delegate != null)
            delegate.execute(1);
        }
      });
      buttonsPanel.add(btn);
    }
    
    if (buttonsType == BUTTONS_OKCANCEL || buttonsType == BUTTONS_YESNO || buttonsType == BUTTONS_YESNOCANCEL) {
      if (buttonsType == BUTTONS_OKCANCEL) {
        buttonMsg = "it".equals(lang) ? "Annulla" : "Cancel";
      } else {
        buttonMsg = "No";
      }
      TouchAnchor btn = new TouchAnchor(SafeHtmlUtils.fromTrustedString(buttonMsg), new TapHandler() {
        public void onTap(TapEvent event) {
          hideMessageDialog();
          if (delegate != null)
            delegate.execute(2);
        }
      });
      buttonsPanel.add(btn);
      btn.getElement().getParentElement().addClassName("phg-PopinDialog-ButtonsRow-2ndBtn");
    }
    
    if (buttonsType == BUTTONS_YESNOCANCEL) {
      buttonMsg = "it".equals(lang) ? "Annulla" : "Cancel";
      TouchAnchor btn = new TouchAnchor(SafeHtmlUtils.fromTrustedString(buttonMsg), new TapHandler() {
        public void onTap(TapEvent event) {
          hideMessageDialog();
          if (delegate != null)
            delegate.execute(3);
        }
      });
      buttonsPanel.add(btn);
      btn.getElement().getParentElement().addClassName("phg-PopinDialog-ButtonsRow-2ndBtn");
    }

    GwtUtils.deferredExecution(500, new Delegate<Void>() {
      public void execute(Void element) {
        if (messageDialog != null) {
          PhgUtils.log("message dialog is just opened!");
          return;
        }
        if (position != null) {
          messageDialog = MgwtDialogs.popin(title, dialogContainer, position.top, position.left);
        } else {
          messageDialog = MgwtDialogs.popin(title, dialogContainer);
        }
        
        StyleProperties prop = JQuery.createStyleProperties();
        if (buttonsType == BUTTONS_YESNOCANCEL) {
          prop.setWidth(33, Unit.PCT);
        } else if (buttonsType == BUTTONS_OK) {
          prop.setWidth(100, Unit.PCT);
        } else {
          prop.setWidth(50, Unit.PCT);
        }
        JQuery.select(".phg-PopinDialog-ButtonsRow td").css(prop);
        
      }
    });
  }
  
  public static boolean isMessageDialogVisible() {
    return messageDialog != null;
  }
  
  private static void hideMessageDialog() {
    if (messageDialog != null) {
      messageDialog.hide();
      messageDialog = null;
    }
  }
  
  private static PopinDialog modalNotification = null;
  
  public static void showModalNotification(String message) {
    
    final VerticalPanel container = new VerticalPanel();
    container.setSpacing(0);
    container.setWidth("100%");
    container.addStyleName("phg-Notification-Container");
    
    Span span = new Span();
    span.setHTML(SafeHtmlUtils.fromTrustedString(message));
    container.add(span);
    
    GwtUtils.deferredExecution(500, new Delegate<Void>() {
      public void execute(Void element) {
        
        if (modalNotification != null) {
          PhgUtils.log("modal notification dialog just opened!");
          return;
        }
        
        modalNotification = MgwtDialogs.popin(null, container, false, false, Animation.DISSOLVE_REVERSE, Animation.DISSOLVE);
        
        StyleProperties prop = JQuery.createStyleProperties();
        prop.setBackgroundColor("transparent");
        JQuery.select(".mgwt-DialogAnimationContainer-Shadow").css(prop);
        
      }
    });
    
  }
  
  public static void hideModalNotification() {
    if (modalNotification != null) {
      modalNotification.hide();
      modalNotification = null;
    } else {
      GwtUtils.deferredExecution(1000, new Delegate<Void>() {
        public void execute(Void element) {
          if (modalNotification != null) {
            modalNotification.hide();
            modalNotification = null;
          }
        }
      });
    }
  }

  /*
  public static DialogBox create(Configuration config) {
    
    DialogBox dialog = new DialogBox();
    dialog.addStyleName("pgc-Dialog");
    
    dialog.setText(config.captionText);
    dialog.setAnimationEnabled(true);
    dialog.setGlassEnabled(true);
    
    VerticalPanel dialogPanel = new VerticalPanel();
    dialogPanel.addStyleName("pgc-DialogPanel");
    
    if (config.bodyText != null) {
      dialogPanel.add(new Label(config.bodyText));
    } else if (config.bodyHtml != null) {
      dialogPanel.add(new HTML(config.bodyHtml));
    } else if (config.bodyWidget != null) {
      dialogPanel.add(config.bodyWidget);
    }
    
    dialog.setWidget(dialogPanel);

    if (config.positionCallback != null) {
      dialog.setPopupPositionAndShow(config.positionCallback);
    } else if (config.objectRelativeTo != null) {
      dialog.showRelativeTo(config.objectRelativeTo);
    } else {
      dialog.show();
    }
    
    return dialog;
  }
  
  public static class Configuration {
    private String captionText = "it".equals(PhgUtils.getAppLocalLanguage()) ? "Attenzione" : "Alert";
    private String bodyText = null;
    private SafeHtml bodyHtml = null;
    private Widget bodyWidget = null;
    private PositionCallback positionCallback;
    private UIObject objectRelativeTo;
    public Configuration setCaptionText(String captionText) {
      this.captionText = captionText;
      return this;
    }
    public Configuration setBodyText(String bodyText) {
      this.bodyText = bodyText;
      return this;
    }
    public Configuration setBodyHtml(SafeHtml bodyHtml) {
      this.bodyHtml = bodyHtml;
      return this;
    }
    public Configuration setBodyWidget(Widget bodyWidget) {
      this.bodyWidget = bodyWidget;
      return this;
    }
    public Configuration setPositionCallback(PositionCallback positionCallback) {
      this.positionCallback = positionCallback;
      return this;
    }
    public Configuration setObjectRelativeTo(UIObject objectRelativeTo) {
      this.objectRelativeTo = objectRelativeTo;
      return this;
    }
  }
  
  */
  
  
  
}
