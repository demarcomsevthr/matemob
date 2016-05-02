package it.mate.gwtcommons.client.ui;

import com.google.gwt.user.client.ui.Label;

public class Spacer extends Label {

  public Spacer() {
    super(" ");
  }

  public Spacer(String width) {
    this();
    this.setWidth(width);
  }

  public Spacer(String width, String height) {
    this();
    this.setWidth(width);
    this.setHeight(height);
  }
  
  public static Spacer horizontalSpacer(String width) {
    return new Spacer(width, "1px");
  }

  public static Spacer verticalSpacer(String height) {
    return new Spacer("1px", height);
  }

}
