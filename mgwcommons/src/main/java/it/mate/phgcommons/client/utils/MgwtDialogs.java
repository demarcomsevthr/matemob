package it.mate.phgcommons.client.utils;

import it.mate.gwtcommons.client.ui.SimpleContainer;
import it.mate.gwtcommons.client.utils.JQuery;
import it.mate.phgcommons.client.ui.ModalPopinDialog;

import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.mvp.client.Animation;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.dialog.AlertDialog;
import com.googlecode.mgwt.ui.client.dialog.ConfirmDialog;
import com.googlecode.mgwt.ui.client.dialog.ConfirmDialog.ConfirmCallback;
import com.googlecode.mgwt.ui.client.dialog.OptionsDialog;
import com.googlecode.mgwt.ui.client.dialog.PopinDialog;
import com.googlecode.mgwt.ui.client.widget.Button;

public class MgwtDialogs {

  public interface AlertCallback {
    public void onButtonPressed();
  }

  public static void alert(String title, String text, final AlertCallback callback) {
    AlertDialog alertDialog = new AlertDialog(MGWTStyle.getTheme().getMGWTClientBundle().getDialogCss(), title, text);
    alertDialog.addTapHandler(new TapHandler() {
      public void onTap(TapEvent event) {
        if (callback != null) {
          callback.onButtonPressed();
        }
      }
    });
    alertDialog.show();
  }

  public static void confirm(String title, String text, final ConfirmCallback callback) {
    ConfirmDialog confirmDialog = new ConfirmDialog(title, text, callback);
    confirmDialog.show();
  }
  
  public static PopinDialog popin (String title, Widget body) {
    return popin(title, body, false);
  }
  
  public static PopinDialog popin (String title, Widget body, boolean hideOnBackgroundClick) {
    return popin(title, body, hideOnBackgroundClick, true);
  }
  
  public static PopinDialog popin (String title, Widget body, boolean hideOnBackgroundClick, boolean centered) {
    return popin(title, body, hideOnBackgroundClick, centered, null, null);
  }
  
  public static PopinDialog popin (String title, Widget body, boolean hideOnBackgroundClick, boolean centered, Animation showAnimation, Animation hideAnimation) {
    PopinDialog dialog = createPopin(title, body, showAnimation, hideAnimation);
    dialog.setHideOnBackgroundClick(hideOnBackgroundClick);
    if (centered) {
      dialog.center();
    } else {
      dialog.show();
    }
    return dialog;
  }
  
  public static PopinDialog popin (String title, Widget body, Integer top, Integer left) {
    PopinDialog dialog = createPopin(title, body);
    dialog.show();
    Element animShadowElem = JQuery.selectFirstElement(".mgwt-DialogAnimationContainer-Shadow div");
    if (animShadowElem != null) {
      if (top != null) {
        animShadowElem.getStyle().setTop(top, Unit.PX);
      } else {
        Element animContainerElem = JQuery.selectFirstElement(".mgwt-DialogAnimationContainer");
        if (animContainerElem != null) {
          WebkitCssUtil.setStyleProperty(animContainerElem.getStyle(), "webkitBoxAlign", "center");
        }
      }
      if (left != null) {
        animShadowElem.getStyle().setLeft(left, Unit.PX);
      } else {
        Element animContainerElem = JQuery.selectFirstElement(".mgwt-DialogAnimationContainer");
        if (animContainerElem != null) {
          WebkitCssUtil.setStyleProperty(animContainerElem.getStyle(), "webkitBoxPack", "center");
        }
      }
    }
    return dialog;
  }
  
  private static PopinDialog createPopin (String title, Widget body) {
    return createPopin(title, body, null, null);
  }
  
  private static PopinDialog createPopin (String title, Widget body, final Animation showAnimation, final Animation hideAnimation) {
    
    // 19/12/2013
//  PopinDialog popinDialog = new PopinDialog();
    
    PopinDialog popinDialog = new ModalPopinDialog() {
      protected Animation getShowAnimation() {
        if (showAnimation != null) {
          return showAnimation;
        } else {
          return super.getShowAnimation();
        }
      }
      protected Animation getHideAnimation() {
        if (hideAnimation != null) {
          return hideAnimation;
        } else {
          return super.getHideAnimation();
        }
      }
    };
    if (showAnimation != null) {
    } else {
      popinDialog = new ModalPopinDialog();
    }

    SimpleContainer dialogWrapper = new SimpleContainer();
    dialogWrapper.addStyleName("phg-PopinDialog-Wrapper");
    if (title != null) {
      Label titleWrapper = new Label(title);
      titleWrapper.addStyleName("phg-PopinDialog-TitleWrapper");
      dialogWrapper.add(titleWrapper);
    }
    SimplePanel bodyWrapper = new SimplePanel();
    bodyWrapper.addStyleName("phg-PopinDialog-BodyWrapper");
    bodyWrapper.add(body);
    dialogWrapper.add(bodyWrapper);
    
    popinDialog.add(dialogWrapper);
    
    return popinDialog;
  }

  private static class InternalTouchHandler implements TapHandler {
    private final int buttonCount;
    private final OptionCallback callback;
    private final OptionsDialog panel;
    public InternalTouchHandler(int buttonCount, OptionsDialog panel, OptionCallback callback) {
      this.buttonCount = buttonCount;
      this.panel = panel;

      this.callback = callback;
    }
    public void onTap(TapEvent event) {
      panel.hide();
      if (callback != null) {
        callback.onOptionSelected(buttonCount);
      }
    }
  }

  public static OptionsDialog options(List<OptionsDialogEntry> options, OptionCallback callback) {
    return options(options, callback, RootPanel.get());
  }

  public static OptionsDialog options(List<OptionsDialogEntry> options, OptionCallback callback, HasWidgets widgetToCover) {
    OptionsDialog optionsDialog = new OptionsDialog(MGWTStyle.getTheme().getMGWTClientBundle().getDialogCss());
    int count = 0;
    for (OptionsDialogEntry option : options) {
      if (option.text != null) {
        count++;
        Button button = new Button(option.text);
        switch (option.type) {
        case NORMAL:
          break;
        case IMPORTANT:
          button.setImportant(true);
          break;
        case CONFIRM:
          button.setConfirm(true);
          break;
        default:
          throw new RuntimeException("how did we get here?");
        }
        button.addTapHandler(new InternalTouchHandler(count, optionsDialog, callback));
        optionsDialog.add(button);
      } else if (option.html != null) {
        HTML html = new HTML(option.html);
        optionsDialog.add(html);
      } else if (option.widget != null) {
        optionsDialog.add(option.widget);
      }
    }
    optionsDialog.setPanelToOverlay(widgetToCover);
    optionsDialog.show();
    return optionsDialog;
  }

  public interface OptionCallback {
    public void onOptionSelected(int index);
  }

  public enum ButtonType {
    /**
     * normal button
     */
    NORMAL, /**
     * important button
     */
    IMPORTANT, /**
     * confirm button
     */
    CONFIRM
  };

  /**
   * Options for Options Dialog
   * 
   * @author Daniel Kurka
   * 
   */
  public static class OptionsDialogEntry {
    private String text;
    private ButtonType type;
    private SafeHtml html;
    private Widget widget;
    
    public OptionsDialogEntry setButtonText(String text) {
      this.text = text;
      return this;
    }
    
    public OptionsDialogEntry setButtonType(ButtonType type) {
      this.type = type;
      return this;
    }
    
    public OptionsDialogEntry setWidget(Widget widget) {
      this.widget = widget;
      return this;
    }
    
    public OptionsDialogEntry setHtml(SafeHtml html) {
      this.html = html;
      return this;
    }

  }

}
