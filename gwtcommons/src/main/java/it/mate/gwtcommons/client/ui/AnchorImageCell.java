package it.mate.gwtcommons.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.client.SafeHtmlTemplates.Template;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;

public abstract class AnchorImageCell <C> extends AnchorCell<C> {

  interface Template extends SafeHtmlTemplates {
    @Template("<a href=\"{0}\" target=\"{1}\" title=\"{2}\"><img src=\"{3}\"/></a>")
    SafeHtml a(SafeUri url, String target, String title, SafeUri imageUrl);
  }

  private static Template template;
  
  static {
    template = GWT.create(Template.class);
  }

  public AnchorImageCell() {
    super("click");
  }

  public void render(Context context, C model, SafeHtmlBuilder sb) {
    if (model != null) {
      String url = getCellUrl(model);
      String target = getCellTarget(model);
      String title = getCellTitle(model);
      String imageUrl = GWT.getModuleBaseURL() + getCellImageUrl(model);
      sb.append(template.a(UriUtils.fromTrustedString(url), target, title, UriUtils.fromTrustedString(imageUrl)));
    }
  };
  
  protected String getCellImageUrl(C model) {
    return "";
  };
  
}
