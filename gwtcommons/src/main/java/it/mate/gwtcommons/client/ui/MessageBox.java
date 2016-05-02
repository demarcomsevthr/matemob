package it.mate.gwtcommons.client.ui;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MessageBox extends DialogBox {
  
  public static final int BUTTONS_YESNOCANCEL = 1;
  public static final int BUTTONS_YESNO = 2;
  public static final int BUTTONS_OKCANCEL = 3;
  public static final int BUTTONS_OK = 4;
  
  public static final int ICON_ALERT = 0;
  public static final int ICON_FORBIDDEN = 1;
  public static final int ICON_INFO = 2;
  public static final int ICON_QUESTION = 3;
  
  private static final String[] ICONS = {"alert.png", "forbidden.png", "info.png", "question.png"};
  
  public static class Callbacks {
    private boolean cancelClose = false;
    public void cancelClose() {
      this.cancelClose = true;
    }
    public void onCancel() { }
    public void onYes() { }
    public void onNo() { }
    public void onOk() { }
    public void onLoad(DialogBox dialog) { }
  }
  
  public static class Configuration {
    private String captionText = "Attenzione";
    private String bodyText = "";
    private String htmlText = "";
    private Widget bodyWidget;
    private int buttonType = BUTTONS_OK;
    private int iconType = ICON_INFO;
    private UIObject objectRelativeTo = null;
    private Callbacks callbacks = null;
    private int left = -1;
    private int top = -1;
    private String bodyWidth;
    public Configuration setCaptionText(String captionText) {
      this.captionText = captionText;
      return this;
    }
    public Configuration setBodyText(String bodyText) {
      this.bodyText = bodyText;
      return this;
    }
    public Configuration setButtonType(int buttonType) {
      this.buttonType = buttonType;
      return this;
    }
    public Configuration setIconType(int iconType) {
      this.iconType = iconType;
      return this;
    }
    public Configuration setObjectRelativeTo(UIObject objectRelativeTo) {
      this.objectRelativeTo = objectRelativeTo;
      return this;
    }
    public Configuration setCallbacks(Callbacks callbacks) {
      this.callbacks = callbacks;
      return this;
    }
    public Configuration setPosition(int left, int top) {
      this.left = left;
      this.top = top;
      return this;
    }
    public void setHtmlText(String htmlText) {
      this.htmlText = htmlText;
    }
    public Configuration setBodyWidget(Widget bodyWidget) {
      this.bodyWidget = bodyWidget;
      return this;
    }
    public Configuration setBodyWidth(String bodyWidth) {
      this.bodyWidth = bodyWidth;
      return this;
    }
  }
  
  private Callbacks callbacks;
  
  private Button defaultButton;
  
  public static MessageBox create(Configuration config) {
    return new MessageBox(config);
  }

  public static Configuration configuration() {
    return new Configuration();
  }
  
  public void setCallbacks(Callbacks callbacks) {
    this.callbacks = callbacks;
  }

  private MessageBox(final Configuration config) {
    this.callbacks = config.callbacks;
    initUI(config);
    if (config.left > -1 && config.top > -1) {
      this.setPopupPositionAndShow(new PositionCallback() {
        public void setPosition(int offsetWidth, int offsetHeight) {
          setPopupPosition(config.left, config.top);
        }
      });
    } else if (config.objectRelativeTo != null) {
      this.showRelativeTo(config.objectRelativeTo);
    } else {
      this.center();
    }
    /*
    GwtUtils.deferredExecution(500, new Delegate<Void>() {
      public void execute(Void value) {
        defaultButton.setFocus(true);
      }
    });
    */
  }
  
  private void initUI(Configuration config) {
    setText(config.captionText);
    setAnimationEnabled(true);
    setGlassEnabled(true);
    VerticalPanel bodyPanel = new VerticalPanel();
    bodyPanel.addStyleName("gwt-Portlet");
    if (config.bodyWidth != null) {
      bodyPanel.setWidth(config.bodyWidth);
    }
    HorizontalPanel hp = new HorizontalPanel();
    hp.add(new Image(GWT.getModuleBaseURL()+"images/messagebox/"+ICONS[config.iconType]));
    Widget bodyWidget;
    if (config.htmlText != null && !"".equals(config.htmlText)) {
      SafeHtml html = SafeHtmlUtils.fromSafeConstant(config.htmlText);
      bodyWidget = new HTML(html);
    } else if (config.bodyWidget != null) {
      bodyWidget = config.bodyWidget;
    } else if (config.bodyText.contains("§")) {
      SafeHtml html = SafeHtmlUtils.fromSafeConstant(config.bodyText.replace("§", "<BR/>"));
      bodyWidget = new HTML(html);
    } else {
      bodyWidget = new Label(config.bodyText);
    }
    GwtUtils.setStyleAttribute(bodyWidget, "lineHeight", "1.3em");
    hp.add(bodyWidget);
    hp.setCellVerticalAlignment(bodyWidget, HasVerticalAlignment.ALIGN_MIDDLE);
    GwtUtils.setStyleAttribute(bodyWidget, "paddingLeft", "10px");
    bodyPanel.add(hp);
    bodyPanel.add(new Spacer("0", "10px"));
    ArrayList<Button> buttons = new ArrayList<Button>();
    Button yesBtn = new Button("Si", new ClickHandler() {
      public void onClick(ClickEvent event) {
        if (callbacks != null) {
          callbacks.cancelClose = false;
          callbacks.onYes();
          if (callbacks.cancelClose)
            return;
        }
        hide();
      }
    });
    Button noBtn = new Button("No", new ClickHandler() {
      public void onClick(ClickEvent event) {
        if (callbacks != null) {
          callbacks.cancelClose = false;
          callbacks.onNo();
          if (callbacks.cancelClose)
            return;
        }
        hide();
      }
    });
    Button okBtn = new Button("Ok", new ClickHandler() {
      public void onClick(ClickEvent event) {
        if (callbacks != null) {
          callbacks.cancelClose = false;
          callbacks.onOk();
          if (callbacks.cancelClose)
            return;
        }
        hide();
      }
    });
    Button cancelBtn = new Button("Annulla", new ClickHandler() {
      public void onClick(ClickEvent event) {
        if (callbacks != null) {
          callbacks.cancelClose = false;
          callbacks.onCancel();
          if (callbacks.cancelClose)
            return;
        }
        hide();
      }
    });
    switch (config.buttonType) {
    case 1:
      buttons.add(cancelBtn);
    case 2:
      buttons.add(0, yesBtn);
      buttons.add(1, noBtn);
      break;
    case 3:
      buttons.add(cancelBtn);
    case 4:
      buttons.add(0, okBtn);
      break;
    }
    HorizontalPanel btPanel = new HorizontalPanel();
    for (Button button : buttons) {
      btPanel.add(button);
      GwtUtils.setStyleAttribute(button.getElement().getParentElement(), "paddingLeft", "5px");
      GwtUtils.setStyleAttribute(button, "outline", "invert none medium");
    }
    bodyPanel.add(btPanel);
    defaultButton = buttons.get(0);
    setWidget(bodyPanel);
    this.addAttachHandler(new AttachEvent.Handler() {
      public void onAttachOrDetach(AttachEvent event) {
        defaultButton.setFocus(true);
        if (callbacks != null) {
          GwtUtils.deferredExecution(new Delegate<Void>() {
            public void execute(Void element) {
              callbacks.onLoad(MessageBox.this);
            }
          });
        }
      }
    });
  }
  
}
