package it.mate.phgcommons.client.plugins;

import java.util.ArrayList;
import java.util.List;

public class Contact {
  
  private String id;
  
  private String displayName;
  
  private String givenName;
  
  private String middleName;
  
  private String familyName;
  
  private List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
  
  private List<Email> emails = new ArrayList<Email>();

  @Override
  public String toString() {
    return "Contact [id=" + id + ", displayName=" + displayName + ", givenName=" + givenName + ", middleName=" + middleName + ", familyName=" + familyName
        + ", phoneNumbers=" + phoneNumbers + ", emails=" + emails + "]";
  }

  public static class PhoneNumber {
    
    private String type;
    
    private String value;
    
    @Override
    public String toString() {
      return "PhoneNumber [type=" + type + ", value=" + value + "]";
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }
    
  }

  public static class Email {
    
    private String type;
    
    private String value;
    
    @Override
    public String toString() {
      return "Email [type=" + type + ", value=" + value + "]";
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }
    
  }
  
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDisplayName() {
    String result = displayName != null ? displayName : null;
    if (result == null) {
      result = concatDisplayName(result, givenName);
      result = concatDisplayName(result, familyName);
    }
    if (result == null) {
      result = "";
    }
    return result;
  }
  
  private String concatDisplayName(String result, String text) {
    if (text == null) {
      return result;
    }
    if (result == null) {
      result = text;
    } else {
      result += " " + text;
    }
    return result;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getGivenName() {
    return givenName;
  }

  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public String getFamilyName() {
    return familyName;
  }

  public void setFamilyName(String familyName) {
    this.familyName = familyName;
  }

  public List<PhoneNumber> getPhoneNumbers() {
    return phoneNumbers;
  }

  public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
    this.phoneNumbers = phoneNumbers;
  }

  public List<Email> getEmails() {
    return emails;
  }

  public void setEmails(List<Email> emails) {
    this.emails = emails;
  }

}
