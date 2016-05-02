package it.mate.phgcommons.client.plugins;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.phgcommons.client.utils.JSONUtils;
import it.mate.phgcommons.client.utils.PhgUtils;
import it.mate.phgcommons.client.utils.callbacks.JSOStringCallback;
import it.mate.phgcommons.client.utils.callbacks.JSOSuccess;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class ContactsPlugin {

  public static native boolean isInstalled () /*-{
    return typeof ($wnd.navigator.contacts) != 'undefined';
  }-*/;
  
  public static void pickContact(final Delegate<Contact> delegate) {
    pickContactImpl(new JSOSuccess() {
      public void handle(JavaScriptObject jsContact) {
        PhgUtils.log("Success - " + JSONUtils.stringify(jsContact));
        Contact contact = parseJsContact(jsContact);
        delegate.execute(contact);
      }
    }, new JSOStringCallback() {
      public void handle(String message) {
        PhgUtils.log("Error - " + message);
      }
    });
  }
  
  protected static Contact parseJsContact(JavaScriptObject jsContact) {
    Contact contact = new Contact();
    int contactId = GwtUtils.getJsPropertyInt(jsContact, "id");
    if (contactId > -1) {
      contact.setId(""+contactId);
    }
    contact.setDisplayName(GwtUtils.getJsPropertyString(jsContact, "displayName"));
    
    JavaScriptObject jsName = GwtUtils.getJsPropertyJso(jsContact, "name");
    if (jsName != null) {
      contact.setGivenName(GwtUtils.getJsPropertyString(jsName, "givenName"));
      contact.setMiddleName(GwtUtils.getJsPropertyString(jsName, "middleName"));
      contact.setFamilyName(GwtUtils.getJsPropertyString(jsName, "familyName"));
    }
    
    JsArray<JavaScriptObject> jsPhoneNumbers = GwtUtils.getJsPropertyArray(jsContact, "phoneNumbers");
    if (jsPhoneNumbers != null) {
      for (int it = 0; it < jsPhoneNumbers.length(); it++) {
        JavaScriptObject jsPhoneNumber = jsPhoneNumbers.get(it);
        Contact.PhoneNumber phoneNumber = new Contact.PhoneNumber();
        phoneNumber.setType(GwtUtils.getJsPropertyString(jsPhoneNumber, "type"));
        phoneNumber.setValue(GwtUtils.getJsPropertyString(jsPhoneNumber, "value"));
        contact.getPhoneNumbers().add(phoneNumber);
      }
    }
    
    JsArray<JavaScriptObject> jsEmails = GwtUtils.getJsPropertyArray(jsContact, "emails");
    if (jsEmails != null) {
      for (int it = 0; it < jsEmails.length(); it++) {
        JavaScriptObject jsEmail = jsEmails.get(it);
        Contact.Email email = new Contact.Email();
        email.setType(GwtUtils.getJsPropertyString(jsEmail, "type"));
        email.setValue(GwtUtils.getJsPropertyString(jsEmail, "value"));
        contact.getEmails().add(email);
      }
    }
    
    return contact;
  }
  
  protected static native void pickContactImpl (JSOSuccess success, JSOStringCallback failure) /*-{
    var jsSuccess = $entry(function(contact) {
      success.@it.mate.phgcommons.client.utils.callbacks.JSOSuccess::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(contact);
    });
    var jsFailure = $entry(function(err) {
      failure.@it.mate.phgcommons.client.utils.callbacks.JSOStringCallback::handle(Ljava/lang/String;)(err);
    });
    $wnd.navigator.contacts.pickContact(jsSuccess, jsFailure);
  }-*/;
  
}
