package it.mate.testleaflet.shared.utils;

import it.mate.testleaflet.shared.model.OrderItemRow;
import it.mate.gwtcommons.client.utils.GwtUtils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;

public class RenderUtils {
  
  
  public static Element renderOrderItemAsGwtSpan(OrderItemRow row, int top, int left, double yFactor) {
    
    int actualSizePx = (int)(row.getSize() * yFactor);
    
    Element span = DOM.createSpan();
    span.setClassName("app-item-row-span");
    span.getStyle().setPosition(Position.FIXED);
    span.getStyle().setTop(top, Unit.PX);
    span.getStyle().setLeft(left, Unit.PX);
    span.getStyle().setWidth(96, Unit.PCT);
    span.setInnerHTML(row.getText());
    span.getStyle().setFontSize(actualSizePx, Unit.PX); 
    GwtUtils.setJsPropertyString(span.getStyle(), "fontFamily", row.getFontFamily()); 
    span.getStyle().setFontWeight(row.getBold() ? FontWeight.BOLD : FontWeight.NORMAL);
    if (row.getItalic()) {
      GwtUtils.setJsPropertyString(span.getStyle(), "fontStyle", "italic"); 
    }
    if (row.getUnderline()) {
      GwtUtils.setJsPropertyString(span.getStyle(), "textDecoration", "underline"); 
    }
    GwtUtils.setJsPropertyString(span.getStyle(), "textAlign", row.getAlign()); 
    
    int height = actualSizePx + 6;
    span.setPropertyInt("height", height);
    
    return span;
  }
  
  public static String imageTextToHtml(String imageText, String imageType) {
    if (imageText == null) {
      return null;
    }
    imageText = !imageText.startsWith("data:") ? ("data:image/"+ imageType +";base64," + imageText) : imageText;
    String html = "<img src='"+ imageText +"'/>";
    return html;
  }

}
