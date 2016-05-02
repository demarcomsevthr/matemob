package it.mate.gwtcommons.client.ui;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.client.SafeHtmlTemplates.Template;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;

public abstract class AnchorCell<C> extends AbstractCell<C> {

  interface Template extends SafeHtmlTemplates {
    @Template("<a href=\"{0}\" target=\"{1}\" title=\"{3}\">{2}</a>")
    SafeHtml a(SafeUri url, String target, String value, String title);
  }

  private static Template template;
  
  private Set<String> consumedEvents = new HashSet<String>();;
  
  private static final String DEFAULT_CELL_URL = "javascript:;";
  
  static {
    template = GWT.create(Template.class);
  }

  public AnchorCell() {
    this("click", "keydown");
  }
  
  public AnchorCell(String... consumedEvents) {
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
      sb.append(template.a(UriUtils.fromTrustedString(getCellUrl(model)), getCellTarget(model), getCellValue(model), getCellTitle(model)));
    }
  }
  
  protected String getCellUrl(C model) {
    return DEFAULT_CELL_URL;
  };
  
  protected String getCellValue(C model) {
    if (DEFAULT_CELL_URL.equals(getCellUrl(model))) {
      return "click me";
    } else {
      return getCellUrl(model);
    }
  }

  protected String getCellTarget(C model) {
    return "_self";
  }
  
  protected String getCellTitle(C model) {
    return getCellValue(model);
  }
  
  protected void onConsumedEvent(NativeEvent event, C value) {
    
  }

  public void onBrowserEvent(Context context, Element parent, C value, NativeEvent event, ValueUpdater<C> valueUpdater) {
    String eventType = event.getType();
    if (consumedEvents.contains(eventType)) {
      onConsumedEvent(event, value);
    }
  };
  
  
}
