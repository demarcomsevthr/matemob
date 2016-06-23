package it.mate.onscommons.client.ui;

import it.mate.phgcommons.client.utils.PhgUtils;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

public class OnsCssPopover extends Composite {

  public OnsCssPopover(String html, String direction) {
    
    Element container = Document.get().createDivElement();
    
    Element mask = Document.get().createDivElement();
    mask.setClassName("popover-mask");
    container.appendChild(mask);
    
    Element popover = Document.get().createDivElement();
    popover.setClassName("popover popover--" + direction);
    container.appendChild(popover);
    
    Element arrow = Document.get().createDivElement();
    arrow.setClassName("popover__" + direction + "-arrow");
    popover.appendChild(arrow);
    
    Element content = Document.get().createDivElement();
    content.setClassName("popover__content");
    popover.appendChild(content);
    
    content.setInnerHTML(html);
    
    PhgUtils.log("wrapping " + container);
    
    String fullHtml = container.getInnerHTML();
    
    HTML widget = new HTML(fullHtml);
    
    initWidget(widget);
    
  }

}
