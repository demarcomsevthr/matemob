package it.mate.gwtcommons.client.ui;

import it.mate.gwtcommons.client.utils.Delegate;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class MessageBoxUtils {
  
  public static void popupOkCancel (String title, Widget bodyWidget, String width, final Delegate<MessageBox.Callbacks> delegate) {
    popupOkCancel(title, bodyWidget, width, delegate, null);
  }
  
  public static void popupOkCancel (String title, Widget bodyWidget, String width, final Delegate<MessageBox.Callbacks> delegate, final Delegate<DialogBox> onLoadDelegate) {
    MessageBox.create(new MessageBox.Configuration()
    .setCaptionText(title)
    .setButtonType(MessageBox.BUTTONS_OKCANCEL)
    .setIconType(MessageBox.ICON_INFO)
    .setBodyWidget(bodyWidget)
    .setBodyWidth(width)
    .setCallbacks(new MessageBox.Callbacks() {
      public void onOk() {
        delegate.execute(this);
      }
      public void onLoad(DialogBox dialog) {
        if (onLoadDelegate != null) {
          onLoadDelegate.execute(dialog);
        }
      }
    })
  );
  }
  
  public static void popupOk (String bodyContent) {
    popupOk(bodyContent, null);
  }
  
  public static void popupOk (String bodyContent, String width) {
    popupOk("Alert", bodyContent, width);
  }
  
  public static void popupOk (String title, String bodyContent, String width) {
    HTML body = new HTML(bodyContent);
    popupOk(title, body, width);
  }
  
  public static void popupOk (String title, Widget bodyWidget, String width) {
    MessageBox.create(new MessageBox.Configuration()
      .setCaptionText(title)
      .setButtonType(MessageBox.BUTTONS_OK)
      .setIconType(MessageBox.ICON_ALERT)
      .setBodyWidget(bodyWidget)
      .setBodyWidth(width)
    );
  }

}
