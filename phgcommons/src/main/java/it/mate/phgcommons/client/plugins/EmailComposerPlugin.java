package it.mate.phgcommons.client.plugins;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * 
 * SEE:
 * 
 * https://github.com/jcjee/email-composer
 *
 */

public class EmailComposerPlugin {
  
  public static void showEmailComposer(String subject, String body,
      String[] toRecipients, String ccRecipients, String bccRecipients, boolean bIsHTML, String attachments, String attachmentsData) {
    callPluginImpl("showEmailComposer", subject, body, toRecipients, ccRecipients, bccRecipients, bIsHTML, attachments, attachmentsData, 
        new JSOSuccess() {
          public void handleEvent(JavaScriptObject jso) {
            
          }
        }, new JSOFailure() {
          public void handleEvent(JavaScriptObject jso) {
            
          }
        });
  }
  
  private static native void callPluginImpl (String methodName, String subject, String body,
      String[] toRecipients, String ccRecipients, String bccRecipients, boolean bIsHTML, String attachments, String attachmentsData, 
      JSOSuccess success, JSOFailure failure) /*-{
    var jsSuccess = $entry(function(message) {
      success.@it.mate.phgcommons.client.plugins.EmailComposerPlugin.JSOSuccess::handleEvent(Lcom/google/gwt/core/client/JavaScriptObject;)(message);
    });
    var jsFailure = $entry(function(message) {
      failure.@it.mate.phgcommons.client.plugins.EmailComposerPlugin.JSOFailure::handleEvent(Lcom/google/gwt/core/client/JavaScriptObject;)(message);
    });
    $wnd.cordova.exec(jsSuccess, jsFailure, "EmailComposer", methodName, [{
      "toRecipients": toRecipients,
      "subject": subject,
      "body": body,
      "ccRecipients": ccRecipients,
      "bIsHTML": bIsHTML
    }])
  }-*/;

  protected static interface JSOCallback {
    public void handleEvent(JavaScriptObject jso);
  }

  protected static interface JSOSuccess extends JSOCallback { }

  protected static interface JSOFailure extends JSOCallback { }
  
}
