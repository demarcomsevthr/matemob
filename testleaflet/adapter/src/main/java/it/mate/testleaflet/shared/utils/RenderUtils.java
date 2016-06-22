package it.mate.testleaflet.shared.utils;


public class RenderUtils {
  
  public static String imageTextToHtml(String imageText, String imageType) {
    if (imageText == null) {
      return null;
    }
    imageText = !imageText.startsWith("data:") ? ("data:image/"+ imageType +";base64," + imageText) : imageText;
    String html = "<img src='"+ imageText +"'/>";
    return html;
  }

}
