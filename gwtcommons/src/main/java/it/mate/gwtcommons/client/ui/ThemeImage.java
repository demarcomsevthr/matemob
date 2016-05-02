package it.mate.gwtcommons.client.ui;

import it.mate.gwtcommons.client.utils.UrlUtils;

import com.google.gwt.user.client.ui.Image;

public class ThemeImage extends Image {
  
  /**
   * Es: url = "images/common/add.png"
   */
  
  public ThemeImage(String url) {
    super(UrlUtils.getThemeResourceUrl(url));
  }

  @Override
  public void setUrl(String url) {
    super.setUrl(UrlUtils.getThemeResourceUrl(url));
  }
  
}
