package it.mate.onscommons.client.utils;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.ui.OnsDialog;
import it.mate.onscommons.client.ui.OnsIcon;
import it.mate.phgcommons.client.utils.OsDetectionUtils;
import it.mate.phgcommons.client.utils.PhgUtils;
import it.mate.phgcommons.client.utils.callbacks.JSOCallback;
import it.mate.phgcommons.client.utils.callbacks.JSOIntCallback;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class OnsDialogUtils {

  private static OnsDialog synchronizedWaitingDialog;
  
  private static boolean synchronizedAlert = false;
  
  private static OnsDialog synchronizedDialog = null;
  
  private static boolean synchronizedConfirm = false;
  
  private static int synchronizedAlertRetry = -1;
  
  public static void alert(String message) {
    alert(null, message);
  }
  
  public static void alertHtml(String title, String messageHtml) {
    alert(title, null, messageHtml);
  }
  
  public static void alert(String title, String message) {
    alert(title, message, null, null, null, null);
  }
  
  public static void alert(String title, String message, Delegate<Void> delegate) {
    alert(title, message, null, null, null, delegate);
  }
  
  public static void alert(String title, String message, String messageHtml) {
    alert(title, message, messageHtml, null, null, null);
  }
  
  public static void alert(String title, String message, String messageHtml, String buttonLabel) {
    alert(title, message, messageHtml, buttonLabel, null);
  }
  
  public static void alert(String title, String message, String messageHtml, Delegate<Void> delegate) {
    alert(title, message, messageHtml, null, null, delegate);
  }
  
  public static void alert(String title, String message, String messageHtml, String buttonLabel, String animation) {
    alert(title, message, messageHtml, buttonLabel, animation, null);
  }
  
  public static void alert(final String title, final String message, final String messageHtml, final String buttonLabel, final String animation, final Delegate<Void> delegate) {
    if (synchronizedAlert) {
      if (synchronizedAlertRetry < 10) {
        synchronizedAlertRetry ++;
        GwtUtils.deferredExecution(500, new Delegate<Void>() {
          public void execute(Void element) {
            alert(title, message, messageHtml, buttonLabel, animation, delegate);
          }
        });
      } else {
        if (delegate != null) {
          delegate.execute(null);
        }
      }
      return;
    }
    synchronizedAlert = true;
    synchronizedAlertRetry = -1;
    GwtUtils.deferredExecution(500, new Delegate<Void>() {
      public void execute(Void element) {
        alertImpl(Options.create().setTitle(title).setMessage(message).setMessageHtml(messageHtml)
            .setButtonLabel(buttonLabel).setAnimation(animation).setCallback(new JSOCallback() {
              public void handle(JavaScriptObject jso) {
                synchronizedAlert = false;
                synchronizedAlertRetry = -1;
                if (delegate != null) {
                  delegate.execute(null);
                }
              }
            }));
      }
    });
  }
  
  protected static native void alertImpl(JavaScriptObject options) /*-{
    $wnd.ons.notification.alert(options);
  }-*/;
  
  public static void confirm(String title, String message) {
    confirm(title, message, null);
  }
  
  public static void confirm(String title, String message, String messageHtml) {
    confirm(title, message, messageHtml, null);
  }
  
  public static void confirm(String title, String message, String messageHtml, String[] buttonLabels) {
    confirm(title, message, messageHtml, buttonLabels, null);
  }
  
  public static void confirm(String title, String message, String messageHtml, String[] buttonLabels, String animation) {
    confirm(title, message, messageHtml, buttonLabels, animation, null);
  }
  
  public static void confirm(String title, String message, String messageHtml, String[] buttonLabels, String animation, Boolean cancelable) {
    confirm(title, message, messageHtml, buttonLabels, animation, cancelable, null);
  }
  
  public static void confirm(String title, String message, String messageHtml, String[] buttonLabels, String animation, Boolean cancelable, final Delegate<Integer> delegate) {
    if (synchronizedConfirm) {
      return;
    }
    synchronizedConfirm = true;
    confirmImpl(Options.create().setTitle(title).setMessage(message).setMessageHtml(messageHtml)
      .setButtonLabels(buttonLabels).setAnimation(animation).setCancelable(cancelable != null ? cancelable : false)
      .setMaterial(OsDetectionUtils.isAndroid() || OsDetectionUtils.isDesktop())
      .setCallback(new JSOIntCallback() {
        public void handle(int index) {
          synchronizedConfirm = false;
          if (delegate != null) {
            delegate.execute(index);
          }
        }
      }));
  }
  
  protected static native void confirmImpl(JavaScriptObject options) /*-{
    $wnd.ons.notification.confirm(options);
  }-*/;
  
  protected static class Options extends JavaScriptObject {
    protected Options() { }
    protected static Options create() {
      return JavaScriptObject.createObject().cast();
    };
    protected final native Options setTitle(String value) /*-{
      if (value == null) {
        value = "Alert";
      }
      this['title'] = value;
      return this;    
    }-*/;
    protected final native Options setMessage(String value) /*-{
      if (value != null) {
        this['message'] = value;
      }
      return this;    
    }-*/;
    protected final native Options setMessageHtml(String value) /*-{
      if (value != null) {
        this['messageHTML'] = value;
      }
      return this;    
    }-*/;
    protected final native Options setButtonLabel(String value) /*-{
      if (value == null) {
        value = "OK";
      }
      this['buttonLabel'] = value;
      return this;    
    }-*/;
    protected final Options setButtonLabels(String[] values) {
      if (values == null) {
        values = new String[] {"OK"};
      }
      /* PER RETROCOMPATIBILITA CON GWT 2.5.1    
      JsArrayString jsValues = JsArrayString.createArray(values.length).cast(); 
      */
      JsArrayString jsValues = JsArrayString.createArray().cast();
      for (int it = 0; it < values.length; it++) {
        jsValues.set(it, values[it]);
      }
      GwtUtils.setJsPropertyJso(this, "buttonLabels", jsValues);
      return this;
    }
    protected final native Options setAnimation(String value) /*-{
      if (value != null) {
        this['animation'] = value;
      }
      return this;    
    }-*/;
    protected final native Options setCancelable(boolean value) /*-{
      this['cancelable'] = value;
      return this;    
    }-*/;
    protected final native Options setCallback(JSOCallback callback) /*-{
      if (callback != null) {
        var jsCallback = $entry(function() {
          callback.@it.mate.phgcommons.client.utils.callbacks.JSOCallback::handle(Lcom/google/gwt/core/client/JavaScriptObject;)();
        });
        this['callback'] = jsCallback;
      }
      return this;    
    }-*/;
    protected final native Options setCallback(JSOIntCallback callback) /*-{
      if (callback != null) {
        var jsCallback = $entry(function(index) {
          callback.@it.mate.phgcommons.client.utils.callbacks.JSOIntCallback::handle(I)(index);
        });
        this['callback'] = jsCallback;
      }
      return this;    
    }-*/;
    protected final native Options setMaterial(boolean flag) /*-{
      if (flag) {
        this['modifier'] = 'material';
      }
      return this;    
    }-*/;
  }
  
  public static OnsDialog createDialog(String html) {
    return createDialog(html, false);
  }
  public static OnsDialog createDialog(String html, boolean cancelable) {
    return createDialog(html, cancelable, null);
  }
  public static OnsDialog createDialog(String html, boolean cancelable, String animation) {
    return createDialog(html, cancelable, animation, null);
  }
  public static OnsDialog createDialog(String html, boolean cancelable, String animation, String stylename) {
    return createDialog(new HTML(html), cancelable, animation, stylename);
  }
  public static OnsDialog createDialog(Widget widget) {
    return createDialog(widget, false);
  }
  public static OnsDialog createDialog(Widget widget, boolean cancelable) {
    return createDialog(widget, cancelable, null);
  }
  public static OnsDialog createDialog(Widget widget, boolean cancelable, String animation) {
    return createDialog(widget, cancelable, animation, null);
  }
  public static OnsDialog createDialog(Widget widget, boolean cancelable, String animation, String stylename) {
    if (synchronizedDialog != null) {
      PhgUtils.log("SINGLETON DIALOG ALREADY SHOWN, SKIP CREATION OF NEW ONE");
    }
    synchronizedDialog = new OnsDialog();
    if (stylename != null) {
      synchronizedDialog.addStyleName(stylename);
    }
    synchronizedDialog.show(widget, Options.create().setAnimation(animation), cancelable);
    synchronizedDialog.addOnHideDelegate(new Delegate<JavaScriptObject>() {
      public void execute(JavaScriptObject element) {
        PhgUtils.log("CLOSING DIALOG " + synchronizedDialog);
        synchronizedDialog = null;
      }
    });
    return synchronizedDialog;
  }
  
  public static OnsDialog createDialogMulti(String html, boolean cancelable, String stylename) {
    Widget widget = new HTML(html);
    final OnsDialog dialog = new OnsDialog();
    if (stylename != null) {
      dialog.addStyleName(stylename);
    }
    dialog.show(widget, Options.create().setAnimation(null), cancelable);
    dialog.addOnHideDelegate(new Delegate<JavaScriptObject>() {
      public void execute(JavaScriptObject element) {
        PhgUtils.log("CLOSING DIALOG " + dialog);
      }
    });
    return dialog;
  }
  
  public static void showWaitingDialog() {
    showWaitingDialog("Attendere prego", 30000);
  }
  
  public static void showWaitingDialog(String message, Integer maxWait) {
    showWaitingDialog(message, maxWait, false);
  }
  
  public static void showWaitingDialog(String message, Integer maxWait, boolean cancelable) {
    if (synchronizedWaitingDialog == null) {
      HorizontalPanel panel = new HorizontalPanel();
      panel.getElement().getStyle().setPadding(5, Unit.PCT);
      panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
      OnsIcon icon = new OnsIcon();
      icon.setIcon("fa-spinner");
      icon.setSpin("true");
      final HTML html = new HTML(message);
      GwtUtils.ensureId(html.getElement());
      panel.add(icon);
      panel.add(html);
      synchronizedWaitingDialog = OnsDialogUtils.createDialog(panel, cancelable, null, "ons-waiting-dialog");
      if (maxWait != null) {
        GwtUtils.deferredExecution(maxWait, new Delegate<Void>() {
          public void execute(Void element) {
            hideWaitingDialog();
          }
        });
      }
      synchronizedWaitingDialog.getRealWidth(new Delegate<Integer>() {
        public void execute(final Integer width) {
          GwtUtils.onAvailable(html.getElement(), new Delegate<Element>() {
            public void execute(Element htmlElement) {
              htmlElement.getStyle().setWidth(width * 80 / 100, Unit.PX);
            }
          });
        }
      });
    }
  }
  
  public static void hideWaitingDialog() {
    if (synchronizedWaitingDialog != null) {
      try {
        PhgUtils.log("hinding synchronized waiting dialog...");
        synchronizedWaitingDialog.hide();
      } catch (Throwable th) {
        PhgUtils.log("hinding synchronized waiting dialog exception, proceed - " + th.getMessage());
      }
      synchronizedWaitingDialog = null;
    }
  }
  
  public static OnsDialog getSynchronizedWaitingDialog() {
    return synchronizedWaitingDialog;
  }
  
}
