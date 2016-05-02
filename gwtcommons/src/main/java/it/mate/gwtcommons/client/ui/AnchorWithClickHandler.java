package it.mate.gwtcommons.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.shared.DirectionEstimator;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Anchor;

public class AnchorWithClickHandler extends Anchor {
  
  public AnchorWithClickHandler(String text, ClickHandler handler) {
    super(text);
    addClickHandler(handler);
  }

  public AnchorWithClickHandler() {
    super();
  }

  public AnchorWithClickHandler(boolean useDefaultHref) {
    super(useDefaultHref);
  }

  public AnchorWithClickHandler(Element element) {
    super(element);
  }

  public AnchorWithClickHandler(SafeHtml html, Direction dir, String href) {
    super(html, dir, href);
  }

  public AnchorWithClickHandler(SafeHtml html, Direction dir) {
    super(html, dir);
  }

  public AnchorWithClickHandler(SafeHtml html, DirectionEstimator directionEstimator, String href) {
    super(html, directionEstimator, href);
  }

  public AnchorWithClickHandler(SafeHtml html, DirectionEstimator directionEstimator) {
    super(html, directionEstimator);
  }

  public AnchorWithClickHandler(SafeHtml html, String href, String target) {
    super(html, href, target);
  }

  public AnchorWithClickHandler(SafeHtml html, String href) {
    super(html, href);
  }

  public AnchorWithClickHandler(SafeHtml html) {
    super(html);
  }

  public AnchorWithClickHandler(String text, boolean asHtml, String href, String target) {
    super(text, asHtml, href, target);
  }

  public AnchorWithClickHandler(String text, boolean asHTML, String href) {
    super(text, asHTML, href);
  }

  public AnchorWithClickHandler(String text, boolean asHtml) {
    super(text, asHtml);
  }

  public AnchorWithClickHandler(String text, Direction dir, String href) {
    super(text, dir, href);
  }

  public AnchorWithClickHandler(String text, Direction dir) {
    super(text, dir);
  }

  public AnchorWithClickHandler(String text, DirectionEstimator directionEstimator, String href) {
    super(text, directionEstimator, href);
  }

  public AnchorWithClickHandler(String text, DirectionEstimator directionEstimator) {
    super(text, directionEstimator);
  }

  public AnchorWithClickHandler(String text, String href, String target) {
    super(text, href, target);
  }

  public AnchorWithClickHandler(String text, String href) {
    super(text, href);
  }

  public AnchorWithClickHandler(String text) {
    super(text);
  }

}
