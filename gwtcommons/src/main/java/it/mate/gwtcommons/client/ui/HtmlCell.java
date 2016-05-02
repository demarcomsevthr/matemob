package it.mate.gwtcommons.client.ui;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

public abstract class HtmlCell<C> extends AbstractCell<C> {

  private Set<String> consumedEvents = new HashSet<String>();;
  
  public HtmlCell() {
    this("click", "keydown");
  }
  
  public HtmlCell(String... consumedEvents) {
    super(consumedEvents);
    if (consumedEvents != null && consumedEvents.length > 0) {
      for (String event : consumedEvents) {
        this.consumedEvents.add(event);
      }
    }
  }

  @Override
  public void render(Context context, C model, SafeHtmlBuilder sb) {
    if (model != null) {
      sb.append( getCellHtml(model) );
    }
  }
  
  protected SafeHtml getCellHtml(C model) {
    return SafeHtmlUtils.fromTrustedString("#nbsp;");
  };
  
  protected void onConsumedEvent(NativeEvent event, C value) {
    
  }

  public void onBrowserEvent(Context context, Element parent, C value, NativeEvent event, ValueUpdater<C> valueUpdater) {
    String eventType = event.getType();
    if (consumedEvents.contains(eventType)) {
      onConsumedEvent(event, value);
    }
  };
  
  
}
